package com.ytl.crm.logic.task.impl;

import com.alibaba.fastjson.JSON;
import com.ytl.crm.domain.bo.task.config.MarketingTaskMaterialContextBO;
import com.ytl.crm.domain.entity.task.config.MarketingTaskMaterialContentEntity;
import com.ytl.crm.domain.enums.task.config.TaskActionWxMaterialTypeEnum;
import com.ytl.crm.logic.task.interfaces.IMarketingTaskMaterialContentLogic;
import com.ytl.crm.service.interfaces.task.config.MarketingTaskMaterialContentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


@Slf4j
@Service
@RequiredArgsConstructor
public class IMarketingTaskMaterialContentLogicImpl implements IMarketingTaskMaterialContentLogic {

    private final MarketingTaskMaterialContentService marketingTaskMaterialContentService;

    @Transactional
    @Override
    public Boolean saveTaskMaterialContent(MarketingTaskMaterialContextBO marketingTaskMaterialContextBO) {

        for (MarketingTaskMaterialContextBO.MsgContext msgContext : marketingTaskMaterialContextBO.getAttachments()) {
            MarketingTaskMaterialContentEntity marketingTaskMaterialContentEntity = new MarketingTaskMaterialContentEntity();

            if (msgContext.image != null) {
                marketingTaskMaterialContentEntity.setAttachmentsType(TaskActionWxMaterialTypeEnum.image.getCode());
                marketingTaskMaterialContentEntity.setMaterialRemark(JSON.toJSONString(msgContext.image));
                marketingTaskMaterialContentEntity.setMaterialType("0");
            }

            if (msgContext.video != null) {
                marketingTaskMaterialContentEntity.setAttachmentsType(TaskActionWxMaterialTypeEnum.video.getCode());
                marketingTaskMaterialContentEntity.setMaterialRemark(JSON.toJSONString(msgContext.video));
                marketingTaskMaterialContentEntity.setMaterialType("0");
            }

            if (msgContext.link != null) {
                marketingTaskMaterialContentEntity.setAttachmentsType("link");
                marketingTaskMaterialContentEntity.setMaterialRemark(JSON.toJSONString(msgContext.link));
                marketingTaskMaterialContentEntity.setMaterialType("1");
            }

            marketingTaskMaterialContentEntity.setCreateTime(new Date());
            marketingTaskMaterialContentEntity.setLastModifyTime(new Date());
            marketingTaskMaterialContentService.save(marketingTaskMaterialContentEntity);
        }


        return false;
    }
}
