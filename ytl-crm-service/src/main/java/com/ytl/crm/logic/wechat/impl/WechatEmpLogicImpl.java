package com.ytl.crm.logic.wechat.impl;

import com.google.common.collect.Lists;
import com.ytl.crm.config.WeChatThirdSourceConfig;
import com.ytl.crm.consumer.wechat.WxOfficialConsumerHelper;
import com.ytl.crm.consumer.ws.WsConsumer;
import com.ytl.crm.consumer.ws.WsConsumerHelper;
import com.ytl.crm.domain.entity.wechat.WechatEmpMappingEntity;
import com.ytl.crm.domain.enums.common.YesOrNoEnum;
import com.ytl.crm.domain.enums.wechat.WechatSourceEnum;
import com.ytl.crm.domain.resp.ws.WsBaseResponse;
import com.ytl.crm.domain.resp.ws.WsEmpListResp;
import com.ytl.crm.service.interfaces.channel.IStaffPlatformAccountService;
import com.ytl.crm.service.interfaces.wechat.official.IWechatEmpMappingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class WechatEmpLogicImpl implements IWechatEmpLogic {

    private final WeChatThirdSourceConfig weChatThirdSourceConfig;
    private final IWechatEmpMappingService iWechatEmpMappingService;
    private final IStaffPlatformAccountService staffPlatformAccountService;

    private final WsConsumer wsConsumer;
    private final WsConsumerHelper wsConsumerHelper;
    private final WxOfficialConsumerHelper wxOfficialConsumerHelper;

    @Override
    public void syncWsEmpList() {
        //查询员工列表
        int currentPage = 1;
        int pageSize = 100;
        boolean flag = true;
        while (flag) {
            WsEmpListResp wsEmpListResp = queryWsEmpList(currentPage, pageSize);
            if (wsEmpListResp == null) {
                return;
            }
            List<WsEmpListResp.WsUserDetail> records = wsEmpListResp.getRecords();
            handleWsEmpRecord(records);
            //未查到数据或者分页数据不足
            currentPage++;
            flag = !CollectionUtils.isEmpty(records) && records.size() >= pageSize;
        }
    }

    private WsEmpListResp queryWsEmpList(int currentPage, int pageSize) {
        WsBaseResponse<WsEmpListResp> resp = wsConsumer.queryEmpList(wsConsumerHelper.acquireAccessToken(), currentPage, pageSize);
        if (resp != null && resp.isOk()) {
            return resp.getData();
        }
        return null;
    }

    private void handleWsEmpRecord(List<WsEmpListResp.WsUserDetail> records) {
        if (CollectionUtils.isEmpty(records)) {
            return;
        }
        WechatSourceEnum sourceEnum = WechatSourceEnum.WEI_S;
        Set<String> thirdIdSet = records.stream().map(WsEmpListResp.WsUserDetail::getUserId).collect(Collectors.toSet());
        //1.先查库，判断有没有
        List<WechatEmpMappingEntity> empMappingList = iWechatEmpMappingService.listByThirdId(thirdIdSet, sourceEnum.getCode());
        Set<String> existThirdIdSet = CollectionUtils.isEmpty(empMappingList) ? Collections.emptySet() :
                empMappingList.stream().map(WechatEmpMappingEntity::getEmpThirdWxId).collect(Collectors.toSet());
        List<WsEmpListResp.WsUserDetail> needSaveRecords = records.stream().filter(item ->
                !existThirdIdSet.contains(item.getUserId())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(needSaveRecords)) {
            return;
        }
        List<WechatEmpMappingEntity> needSaveEntityList = needSaveRecords.stream().map(item ->
                buildEmpMappingEntity(item.getUserId(), sourceEnum)).collect(Collectors.toList());
        boolean saveRet = iWechatEmpMappingService.saveBatch(needSaveEntityList);
        log.info("保存微盛员工列表ret，saveRet={}", saveRet);
    }

    private WechatEmpMappingEntity buildEmpMappingEntity(String thirdWxId, WechatSourceEnum sourceEnum) {
        WechatEmpMappingEntity entity = new WechatEmpMappingEntity();
        entity.setIsDelete(YesOrNoEnum.NO.getCode());
        entity.setEmpThirdWxId(thirdWxId);
        entity.setThirdSource(sourceEnum.getCode());
        return entity;
    }

    @Override
    public void syncOfficialEmpId(WechatSourceEnum sourceEnum) {
        String agentId = weChatThirdSourceConfig.acquireThirdSourceAgentId(sourceEnum);
        if (StringUtils.isBlank(agentId)) {
            log.warn("未配置对应微信来源的agentId，source={}", sourceEnum.getCode());
            return;
        }

        //查询员工列表
        Long lastEndId = 0L;
        Integer pageSize = 1000;
        List<WechatEmpMappingEntity> waitFillList = iWechatEmpMappingService.listWaitFillEntity(sourceEnum.getCode(), lastEndId, pageSize);
        if (CollectionUtils.isEmpty(waitFillList)) {
            log.info("没有需要填充官方微信id的数据，source={}", sourceEnum.getCode());
            return;
        }

        while (!CollectionUtils.isEmpty(waitFillList)) {
            Long newEndId = waitFillList.get(waitFillList.size() - 1).getId();
            log.info("当前填充企微官方id进度，lastEndId={}，newEndId={}", lastEndId, newEndId);
            handleFillOfficialId(waitFillList, agentId);
            lastEndId = newEndId;
            waitFillList = iWechatEmpMappingService.listWaitFillEntity(sourceEnum.getCode(), lastEndId, pageSize);
        }
        log.info("当前填充企微官方id完成，source={}", sourceEnum.getCode());
    }


    private void handleFillOfficialId(List<WechatEmpMappingEntity> waitFillList, String agentId) {
        //最多1000每组
        List<List<WechatEmpMappingEntity>> waitFillEntityPartitionList = Lists.partition(waitFillList, 500);
        for (List<WechatEmpMappingEntity> entityList : waitFillEntityPartitionList) {
            List<String> thirdIdList = entityList.stream().map(WechatEmpMappingEntity::getEmpThirdWxId).collect(Collectors.toList());
            Map<String, String> officialIdMap = wxOfficialConsumerHelper.batchQueryEmpOfficialId(thirdIdList, agentId);
            for (WechatEmpMappingEntity entity : entityList) {
                String empThirdWxId = entity.getEmpThirdWxId();
                String officialEmpId = officialIdMap.get(empThirdWxId);
                if (StringUtils.isNotBlank(officialEmpId)) {
                    boolean updateRet = iWechatEmpMappingService.updateEmpWxIdById(entity.getId(), officialEmpId);
                    log.info("刷新官方微信id，结果updateRet={}，empThirdWxId={}，officialEmpId={}", updateRet, empThirdWxId, officialEmpId);
                }
            }
        }
    }


    @Override
    public boolean isCareAboutEmpWxId(String empWxId) {
        if (StringUtils.isBlank(empWxId)) {
            return false;
        }
        return Objects.nonNull(staffPlatformAccountService.queryByWxId(empWxId));
    }

    @Override
    public String transferToOfficialId(String empThirdWxId, WechatSourceEnum sourceEnum) {
        WechatEmpMappingEntity empMappingEntity = iWechatEmpMappingService.queryByThirdId(empThirdWxId, sourceEnum.getCode());
        if (empMappingEntity != null && StringUtils.isNotBlank(empMappingEntity.getEmpWxId())) {
            return empMappingEntity.getEmpWxId();
        }
        String agentId = weChatThirdSourceConfig.acquireThirdSourceAgentId(sourceEnum);
        if (StringUtils.isBlank(agentId)) {
            log.warn("未配置对应微信来源的agentId，source={}", sourceEnum.getCode());
            return null;
        }
        String officialWxId = wxOfficialConsumerHelper.queryEmpOfficialId(empThirdWxId, agentId);
        //处理转换后置逻辑，可以异步或者try-catch，不影响主流程
        logicAfterTransfer(empMappingEntity, empThirdWxId, officialWxId, sourceEnum);
        return officialWxId;
    }

    private void logicAfterTransfer(WechatEmpMappingEntity oldEntity,
                                    String empThirdWxId, String officialWxId,
                                    WechatSourceEnum sourceEnum) {
        if (StringUtils.isBlank(officialWxId)) {
            return;
        }
        if (oldEntity != null) {
            boolean updateRet = iWechatEmpMappingService.updateEmpWxIdById(oldEntity.getId(), officialWxId);
            log.info("更新官方微信id，结果updateRet={}，empThirdWxId={}，officialEmpId={}", updateRet, empThirdWxId, officialWxId);
        } else {
            WechatEmpMappingEntity newEntity = buildEmpMappingEntity(empThirdWxId, officialWxId, sourceEnum);
            boolean saveRet = iWechatEmpMappingService.save(newEntity);
            log.info("保存官方微信id映射，结果saveRet={}，empThirdWxId={}，officialEmpId={}", saveRet, empThirdWxId, officialWxId);
        }
    }

    private WechatEmpMappingEntity buildEmpMappingEntity(String thirdWxId, String officialWxId, WechatSourceEnum sourceEnum) {
        WechatEmpMappingEntity entity = new WechatEmpMappingEntity();
        entity.setIsDelete(YesOrNoEnum.NO.getCode());
        entity.setEmpWxId(officialWxId);
        entity.setEmpThirdWxId(thirdWxId);
        entity.setThirdSource(sourceEnum.getCode());
        return entity;
    }

}
