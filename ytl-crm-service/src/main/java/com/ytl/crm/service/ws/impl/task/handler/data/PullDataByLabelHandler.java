package com.ytl.crm.service.ws.impl.task.handler.data;

import com.ytl.crm.domain.entity.task.config.MarketingTaskEntity;
import com.ytl.crm.domain.entity.task.exec.MarketingTaskTriggerRecordEntity;
import com.ytl.crm.domain.enums.task.config.TaskTriggerTypeEnum;
import com.ytl.crm.service.ws.define.task.handler.data.IMarketingTaskPullDataHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PullDataByLabelHandler implements IMarketingTaskPullDataHandler {


    @Override
    public boolean support(TaskTriggerTypeEnum triggerTypeEnum) {
        return TaskTriggerTypeEnum.LABEL.equals(triggerTypeEnum);
    }

    @Override
    public void pullTaskData(MarketingTaskEntity taskEntity, MarketingTaskTriggerRecordEntity triggerRecord) {

    }


}
