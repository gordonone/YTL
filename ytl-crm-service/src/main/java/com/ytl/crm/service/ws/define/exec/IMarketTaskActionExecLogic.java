package com.ytl.crm.service.ws.define.exec;


import com.ytl.crm.domain.entity.task.config.MarketingTaskEntity;
import com.ytl.crm.domain.entity.task.exec.MarketingTaskTriggerRecordEntity;

public interface IMarketTaskActionExecLogic {

    void createAction(MarketingTaskEntity taskEntity, MarketingTaskTriggerRecordEntity triggerRecord);

    void execAction(MarketingTaskEntity taskEntity, MarketingTaskTriggerRecordEntity triggerRecord);

    void actionCallBack(MarketingTaskEntity taskEntity, MarketingTaskTriggerRecordEntity triggerRecord);

    /**
     * 动作补偿
     */
    void compensateAction(MarketingTaskEntity taskEntity, MarketingTaskTriggerRecordEntity triggerRecord);

    /**
     * 动作补偿 - 回调
     */
    void compensateCallBack(MarketingTaskEntity taskEntity, MarketingTaskTriggerRecordEntity triggerRecord);

}
