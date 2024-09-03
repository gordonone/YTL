package com.ytl.crm.service.ws.define.task;


import com.ytl.crm.domain.entity.task.config.MarketingTaskEntity;
import com.ytl.crm.domain.entity.task.exec.MarketingTaskTriggerRecordEntity;

public interface IMarketTaskActionExecLogic {

    /**
     * 创建动作执行明细 - ActionRecord和ActionItem
     *
     * @param taskEntity    任务信息
     * @param triggerRecord 触发记录
     */
    void createAction(MarketingTaskEntity taskEntity, MarketingTaskTriggerRecordEntity triggerRecord);

    /**
     * 执行动作
     *
     * @param taskEntity    任务信息
     * @param triggerRecord 触发记录
     */
    void execAction(MarketingTaskEntity taskEntity, MarketingTaskTriggerRecordEntity triggerRecord);

    /**
     * 动作回调
     *
     * @param taskEntity    任务信息
     * @param triggerRecord 触发记录
     */
    void actionCallBack(MarketingTaskEntity taskEntity, MarketingTaskTriggerRecordEntity triggerRecord);

    /**
     * 动作补偿
     *
     * @param taskEntity    任务信息
     * @param triggerRecord 触发记录
     */
    void compensateAction(MarketingTaskEntity taskEntity, MarketingTaskTriggerRecordEntity triggerRecord);

}
