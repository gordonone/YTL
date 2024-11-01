package com.ytl.crm.logic.task.interfaces;

import com.ytl.crm.domain.bo.task.config.MarketingTaskMaterialContextBO;

import java.util.List;

public interface IMarketingTaskMaterialContentLogic {

    Boolean saveTaskMaterialContent(MarketingTaskMaterialContextBO marketingTaskMaterialContextBO);

    MarketingTaskMaterialContextBO getTaskMaterialContent(List<String> materialContextMediaList);
}
