package com.ytl.crm.service.interfaces.task.handler.data;


import com.ytl.crm.domain.entity.task.config.MarketingTaskEntity;
import com.ytl.crm.domain.entity.task.exec.MarketingTaskTriggerRecordEntity;
import com.ytl.crm.domain.enums.task.config.TaskTriggerTypeEnum;

public interface IMarketingTaskPullDataHandler {

    void pullTaskData(MarketingTaskEntity taskEntity, MarketingTaskTriggerRecordEntity triggerRecord);

    boolean support(TaskTriggerTypeEnum triggerTypeEnum);


}
