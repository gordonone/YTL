package com.ytl.crm.logic.task.impl;

import com.ytl.crm.domain.bo.task.config.MarketingTaskMaterialContextBO;
import com.ytl.crm.domain.entity.task.config.MarketingTaskMaterialContentEntity;
import com.ytl.crm.logic.task.interfaces.IMarketingTaskMaterialContentLogic;
import com.ytl.crm.service.interfaces.task.config.MarketingTaskMaterialContentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class IMarketingTaskMaterialContentLogicImpl implements IMarketingTaskMaterialContentLogic {

    private final MarketingTaskMaterialContentService marketingTaskMaterialContentService;

    @Override
    public Boolean saveTaskMaterialContent(MarketingTaskMaterialContextBO marketingTaskMaterialContextBO) {

        MarketingTaskMaterialContentEntity marketingTaskMaterialContentEntity = new MarketingTaskMaterialContentEntity();

        marketingTaskMaterialContentService.save(marketingTaskMaterialContentEntity);
        return false;
    }
}
