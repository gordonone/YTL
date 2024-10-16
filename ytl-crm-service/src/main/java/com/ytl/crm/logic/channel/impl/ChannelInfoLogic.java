package com.ytl.crm.logic.channel.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ytl.crm.domain.common.BaseResponse;
import com.ytl.crm.domain.entity.channel.ChannelInfoEntity;
import com.ytl.crm.domain.entity.channel.ChannelInfoRelateEntity;
import com.ytl.crm.domain.entity.channel.DynamicColInfoEntity;
import com.ytl.crm.domain.enums.channel.DynamicColTypeEnum;
import com.ytl.crm.domain.enums.channel.RelateTypeEnum;
import com.ytl.crm.domain.enums.channel.StatusEnum;
import com.ytl.crm.domain.req.channel.ChannelInfoEditReq;
import com.ytl.crm.domain.req.channel.ChannelInfoSearchReq;
import com.ytl.crm.domain.resp.channel.ChannelInfoVo;
import com.ytl.crm.domain.resp.channel.ChannelInfosResp;
import com.ytl.crm.domain.resp.common.CommonDict;
import com.ytl.crm.service.interfaces.channel.IChannelCategoryTreeService;
import com.ytl.crm.service.interfaces.channel.IChannelInfoRelateService;
import com.ytl.crm.service.interfaces.channel.IChannelInfoService;
import com.ytl.crm.service.interfaces.channel.IDynamicColInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ChannelInfoLogic {
    @Resource
    private IChannelInfoService channelInfoService;

    @Resource
    private IChannelInfoRelateService channelInfoRelateService;

    @Resource
    private IDynamicColInfoService dynamicColInfoService;

    @Resource
    private IChannelCategoryTreeService categoryTreeService;

    public static final String dynamicTableName = "customer_datasource";

    public BaseResponse<ChannelInfosResp> search(ChannelInfoSearchReq req) {
        QueryWrapper<ChannelInfoEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().likeRight(ChannelInfoEntity::getName, req.getChannelName()).in(CollectionUtils.isNotEmpty(req.getStatusList()), ChannelInfoEntity::getStatus, req.getStatusList()).and(StringUtils.isNotBlank(req.getCategoryCode()), e -> e.eq(ChannelInfoEntity::getCategoryCode, req.getCategoryCode()).or().likeRight(ChannelInfoEntity::getCategoryCode, req.getCategoryCode() + "-")).orderByDesc(ChannelInfoEntity::getStatus).orderByDesc(ChannelInfoEntity::getCreateTime);
        Page<ChannelInfoEntity> page = new Page<>();
        page.setSize(req.getPageSize());
        page.setCurrent(req.getCurPage());
        Page<ChannelInfoEntity> pageResult = channelInfoService.page(page, queryWrapper);
        List<ChannelInfoVo> channelInfos = buildChannelInfoVos(pageResult);
        return BaseResponse.responseOk(ChannelInfosResp.builder().total(pageResult.getTotal()).channelInfos(channelInfos).dictList(getDictList()).build());
    }

    public BaseResponse<List<ChannelInfoEntity>> getByCategoryCode(String categoryCode) {
        QueryWrapper<ChannelInfoEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ChannelInfoEntity::getCategoryCode, categoryCode).orderByDesc(ChannelInfoEntity::getCreateTime);

        List<ChannelInfoEntity> list = channelInfoService.list(queryWrapper);
        return BaseResponse.responseOk(list);
    }

    private List<ChannelInfoVo> buildChannelInfoVos(Page<ChannelInfoEntity> pageResult) {
        List<ChannelInfoVo> channelInfos = new ArrayList<>();
        List<ChannelInfoEntity> records = pageResult.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return Collections.emptyList();
        }
        Set<String> categoryCodes = records.stream().map(ChannelInfoEntity::getCategoryCode).collect(Collectors.toSet());
        Map<String, String> fulLNameMapByCodes = categoryTreeService.getFulLNameMapByCodes(categoryCodes);
        for (ChannelInfoEntity record : records) {
            ChannelInfoVo channelInfoVo = new ChannelInfoVo();
            BeanUtils.copyProperties(record, channelInfoVo);
            channelInfoVo.setStatusShowText(StatusEnum.getDescByCode(record.getStatus()));
            channelInfoVo.setCategoryFullName(fulLNameMapByCodes.get(record.getCategoryCode()));
            channelInfos.add(channelInfoVo);
        }
        return channelInfos;
    }

    public BaseResponse setStatus(ChannelInfoEditReq req) {
        if (Objects.isNull(req.getId())) {
            return BaseResponse.responseFail("主键id 不能为空！");
        }
        if (Objects.isNull(req.getStatus())) {
            return BaseResponse.responseFail("状态不能为空！");
        }
        ChannelInfoEntity byId = channelInfoService.getById(req.getId());
        byId.setStatus(req.getStatus());
        boolean b = channelInfoService.updateById(byId);
        if (!b) {
            return BaseResponse.responseFail();
        }
        return BaseResponse.responseOk();
    }

    public BaseResponse saveOrUpdate(ChannelInfoEditReq req) {
        ChannelInfoEntity channelInfoEntity = new ChannelInfoEntity();
        BeanUtils.copyProperties(req, channelInfoEntity);
        if (Objects.isNull(req.getId())) {
            channelInfoEntity.setCreateUserCode(req.getUid());
            channelInfoEntity.setCreateUserName(req.getUserName());
        } else {
            channelInfoEntity.setModifyUserCode(req.getUid());
            channelInfoEntity.setModifyUserName(req.getUserName());
        }
        boolean b = channelInfoService.saveOrUpdate(channelInfoEntity);

        //处理渠道绑定的分配规则
        if (Objects.nonNull(req.getRuleIds())) {
            if (Objects.nonNull(req.getId())) {
                deleteRelate(req.getId());
            }
            //构造关联关系
            List<ChannelInfoRelateEntity> relates = req.getRuleIds().stream().map(ruleId -> {
                ChannelInfoRelateEntity channelInfoRelateEntity = new ChannelInfoRelateEntity();
                channelInfoRelateEntity.setChannelId(channelInfoEntity.getId());
                channelInfoRelateEntity.setRelateId(ruleId);
                channelInfoRelateEntity.setType(RelateTypeEnum.CUSTOMER_SOURCE_ALLOCATE_RULE.getCode());
                return channelInfoRelateEntity;
            }).collect(Collectors.toList());
            channelInfoRelateService.saveBatch(relates);
        }
        return BaseResponse.responseOk();
    }

    public BaseResponse<ChannelInfosResp> editSearch(ChannelInfoEditReq req) {
        ChannelInfoEntity byId = null;
        List<Long> ruleIds = null;
        if (Objects.nonNull(req.getId())) {
            byId = channelInfoService.getById(req.getId());
            ruleIds = channelInfoRelateService.list(new QueryWrapper<ChannelInfoRelateEntity>().lambda().eq(ChannelInfoRelateEntity::getChannelId, req.getId()).eq(ChannelInfoRelateEntity::getType, RelateTypeEnum.CUSTOMER_SOURCE_ALLOCATE_RULE.getCode())).stream().map(ChannelInfoRelateEntity::getRelateId).collect(Collectors.toList());
        }
        return BaseResponse.responseOk(ChannelInfosResp.builder().channelInfo(byId).ruleIds(ruleIds).dictList(getDictList()).build());
    }

    private List<CommonDict> getDictList() {
        CommonDict<Integer, String> statusDict = new CommonDict<>("status");
        for (StatusEnum value : StatusEnum.values()) {
            statusDict.put(value.getCode(), value.getDesc());
        }
        //查询渠道分配规则字典
        CommonDict<Long, String> ruleDict = new CommonDict<>("ruleIds");
        List<DynamicColInfoEntity> list = dynamicColInfoService.list();
        list.stream().filter(e -> Objects.equals(e.getTableName(), dynamicTableName)).filter(e -> Objects.equals(e.getColFormType(), DynamicColTypeEnum.CUSTOMER_SOURCE_ALLOCATE_RULE.getCode())).forEach(e -> ruleDict.put(e.getId(), e.getColShowNameForValue()));
        return Arrays.asList(statusDict, ruleDict);
    }

    private void deleteRelate(Long channelId) {
        QueryWrapper<ChannelInfoRelateEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ChannelInfoRelateEntity::getChannelId, channelId);
        channelInfoRelateService.remove(queryWrapper);
    }
}
