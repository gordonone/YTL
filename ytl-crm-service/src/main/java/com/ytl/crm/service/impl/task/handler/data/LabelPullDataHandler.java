package com.ytl.crm.service.impl.task.handler.data;

import com.ytl.crm.domain.entity.task.config.MarketingTaskEntity;
import com.ytl.crm.domain.entity.task.exec.MarketingTaskTriggerRecordEntity;
import com.ytl.crm.domain.enums.task.config.TaskTriggerTypeEnum;
import com.ytl.crm.domain.enums.task.exec.TaskTriggerStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class LabelPullDataHandler extends AbstractPullDataHandler {

    @Override
    public boolean support(TaskTriggerTypeEnum triggerTypeEnum) {
        return TaskTriggerTypeEnum.LABEL.equals(triggerTypeEnum);
    }

    @Override
    public void pullTaskData(MarketingTaskEntity taskEntity, MarketingTaskTriggerRecordEntity triggerRecord) {
        //分群id
        String conditionValue = taskEntity.getTriggerConditionValue();
        String triggerCode = triggerRecord.getLogicCode();
        log.info("拉取任务执行的业务数据，triggerCode={}，conditionValue={}", triggerCode, conditionValue);



        //todo 获取数据

        //更新状态
        boolean updateRet = iMarketingTaskTriggerRecordService.updateTriggerStatus(triggerCode,
                TaskTriggerStatusEnum.WAIT_PULL_DATA.getCode(), TaskTriggerStatusEnum.WAIT_CREATE_ACTION.getCode(), null);
        log.info("更新触发记录状态，triggerCode={}，updateRet={}", triggerCode, updateRet);
    }







}
