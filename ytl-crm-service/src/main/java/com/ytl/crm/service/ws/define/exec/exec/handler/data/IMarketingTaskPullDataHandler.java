package com.ytl.crm.service.ws.define.exec.exec.handler.data;

import com.ziroom.ugc.crm.service.web.domain.entity.task.config.MarketingTaskEntity;
import com.ziroom.ugc.crm.service.web.domain.entity.task.exec.MarketingTaskTriggerRecordEntity;
import com.ziroom.ugc.crm.service.web.domain.enums.task.config.TaskTriggerTypeEnum;

public interface IMarketingTaskPullDataHandler {

    void pullTaskData(MarketingTaskEntity taskEntity, MarketingTaskTriggerRecordEntity triggerRecord);

    boolean support(TaskTriggerTypeEnum triggerTypeEnum);


}
