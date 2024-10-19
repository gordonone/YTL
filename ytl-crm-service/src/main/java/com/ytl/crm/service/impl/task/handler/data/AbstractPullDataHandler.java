package com.ytl.crm.service.impl.task.handler.data;

import cn.hutool.core.util.IdUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.ytl.crm.config.MarketingTaskApolloConfig;
import com.ytl.crm.constants.CommonConstant;
import com.ytl.crm.consumer.dto.resp.data.TaskBizDataDto;
import com.ytl.crm.domain.bo.task.exec.TaskBizInfoBO;
import com.ytl.crm.domain.entity.task.exec.MarketingTaskBizInfoEntity;
import com.ytl.crm.domain.entity.task.exec.MarketingTaskTriggerRecordEntity;
import com.ytl.crm.domain.enums.common.YesOrNoEnum;
import com.ytl.crm.domain.enums.task.exec.TaskRelatedBizInfoTypeEnum;
import com.ytl.crm.service.interfaces.task.exec.IMarketingTaskBizInfoService;
import com.ytl.crm.service.interfaces.task.exec.IMarketingTaskTriggerRecordService;
import com.ytl.crm.service.interfaces.task.exec.handler.data.IMarketingTaskPullDataHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public abstract class AbstractPullDataHandler implements IMarketingTaskPullDataHandler {

    private static final Integer GROUP_BROKE_UP = 0;
    private static final Integer GROUP_MEMBER_CUSTOMER_TYPE = 2;

    private static final Pair<Boolean, String> GROUP_OK = Pair.of(true, StringUtils.EMPTY);
    private static final Pair<Boolean, String> GROUP_CHANGE = Pair.of(false, "群不存在或已解散");
    private static final Pair<Boolean, String> GROUP_BIND_CHANGE = Pair.of(false, "群绑定合同发生变更");
    private static final Pair<Boolean, String> GROUP_MEMBER_CHANGE = Pair.of(false, "群成员发生变更");

    @Resource
    protected MarketingTaskApolloConfig marketingTaskApolloConfig;
    @Resource
    protected IMarketingTaskBizInfoService iMarketingTaskBizInfoService;
    @Resource
    protected IMarketingTaskTriggerRecordService iMarketingTaskTriggerRecordService;


    public void saveBizData(MarketingTaskTriggerRecordEntity triggerRecord, List<TaskBizDataDto> dataList) {
        if (CollectionUtils.isEmpty(dataList)) {
            return;
        }
        String triggerCode = triggerRecord.getLogicCode();
        //无效数据 - 异常数据
        List<TaskBizInfoBO> notValidDataList = Lists.newArrayList();

        //0.数据拍平
        List<TaskBizInfoBO> allDataList = flatDataList(dataList, notValidDataList);

        //1.过滤掉已经存在的数据
        Set<String> existContractCodes = existContractCode(triggerCode, allDataList);
        if (!CollectionUtils.isEmpty(existContractCodes)) {
            allDataList = allDataList.stream().filter(item -> !existContractCodes.contains(item.getContractCode())).collect(Collectors.toList());
        }

        //todo 改改改
        //2.群信息 + 小乐管家信息 & 过滤掉信息不全的数据
        List<TaskBizInfoBO> validDataList = filterAndFillDataInfo(allDataList, notValidDataList);

        //3.保存数据
       // saveBizData(triggerRecord, validDataList, notValidDataList);
    }

    private Set<String> existContractCode(String triggerCode, List<TaskBizInfoBO> allDataList) {
        Set<String> contractCodes = allDataList.stream().map(TaskBizInfoBO::getContractCode).collect(Collectors.toSet());
        List<String> existCodes = iMarketingTaskBizInfoService.queryExistBizCode(triggerCode, contractCodes);
        if (CollectionUtils.isEmpty(existCodes)) {
            return Collections.emptySet();
        }
        return Sets.newHashSet(existCodes);
    }

    public void saveBizData(MarketingTaskTriggerRecordEntity triggerRecord, List<TaskBizInfoBO> validDataList, List<TaskBizInfoBO> notValidDataList) {
        if (!CollectionUtils.isEmpty(validDataList)) {
            List<MarketingTaskBizInfoEntity> validEntityList = validDataList.stream().map(item -> convertToBizInfoEntity(triggerRecord, item)).collect(Collectors.toList());
            boolean saveValidRet = iMarketingTaskBizInfoService.batchSaveBizInfo(validEntityList);
        }

        if (!CollectionUtils.isEmpty(notValidDataList)) {
            List<MarketingTaskBizInfoEntity> notValidEntityList = notValidDataList.stream().map(item -> convertToBizInfoEntity(triggerRecord, item)).collect(Collectors.toList());
            boolean saveNotValidRet = iMarketingTaskBizInfoService.batchSaveBizInfo(notValidEntityList);
        }
    }

    private MarketingTaskBizInfoEntity convertToBizInfoEntity(MarketingTaskTriggerRecordEntity triggerRecord, TaskBizInfoBO bizInfoBO) {
        MarketingTaskBizInfoEntity bizInfoEntity = new MarketingTaskBizInfoEntity();
        BeanUtils.copyProperties(bizInfoBO, bizInfoEntity);
        bizInfoEntity.setLogicCode(String.valueOf(IdUtil.createSnowflake(1, 1).nextId()));
        bizInfoEntity.setTaskCode(triggerRecord.getTaskCode());
        bizInfoEntity.setTriggerRecordCode(triggerRecord.getLogicCode());

        //是否有效
        Integer isValid = Boolean.TRUE.equals(bizInfoBO.getIsValid()) ? YesOrNoEnum.YES.getCode() : YesOrNoEnum.NO.getCode();
        bizInfoEntity.setIsValid(isValid);
        bizInfoEntity.setNotValidMsg(bizInfoBO.getNotValidMsg());

        bizInfoEntity.setBizType(TaskRelatedBizInfoTypeEnum.RENT_CONTRACT.getCode());
        bizInfoEntity.setBizCode(bizInfoBO.getContractCode());
        bizInfoEntity.setCustomerId(bizInfoBO.getUid());
        bizInfoEntity.setCustomerThirdId(bizInfoBO.getCustomerThirdId());

        return bizInfoEntity;
    }

    private List<TaskBizInfoBO> flatDataList(List<TaskBizDataDto> dataList, List<TaskBizInfoBO> notValidDataList) {
        List<TaskBizInfoBO> newDataList = Lists.newArrayListWithExpectedSize(dataList.size());
        for (TaskBizDataDto bizInfo : dataList) {
            String uid = bizInfo.getUid();
            String groupCodeStr = bizInfo.getGroup_code();
            String contractCodeStr = bizInfo.getContract_num();
            List<String> groupCodeList = CommonConstant.COMMA_SPLITTER.trimResults().splitToList(groupCodeStr);
            List<String> contractCodeList = CommonConstant.COMMA_SPLITTER.trimResults().splitToList(contractCodeStr);
            if (groupCodeList.size() != contractCodeList.size()) {
                log.warn("数据异常，合同号和群号数量不匹配，uid={}", uid);
                notValidDataList.add(TaskBizInfoBO.notValidInfo(uid, "数据异常，合同号和群号数量不匹配"));
                continue;
            }
            for (int i = 0; i < groupCodeList.size(); i++) {
                TaskBizInfoBO bizInfoBO = new TaskBizInfoBO();
                bizInfoBO.setUid(uid);
                bizInfoBO.setGroupCode(groupCodeList.get(i));
                bizInfoBO.setContractCode(contractCodeList.get(i));
                newDataList.add(bizInfoBO);
            }
        }
        return newDataList;
    }

    private List<TaskBizInfoBO> filterAndFillDataInfo(List<TaskBizInfoBO> newDataList, List<TaskBizInfoBO> notValidDataList) {
        if (CollectionUtils.isEmpty(newDataList)) {
            return Collections.emptyList();
        }

        return Collections.emptyList();

    }

}
