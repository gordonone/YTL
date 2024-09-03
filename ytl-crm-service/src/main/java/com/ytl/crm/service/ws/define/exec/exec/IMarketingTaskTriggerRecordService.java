package com.ytl.crm.service.ws.define.exec.exec;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ytl.crm.domain.entity.task.exec.MarketingTaskTriggerRecordEntity;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface IMarketingTaskTriggerRecordService extends IService<MarketingTaskTriggerRecordEntity> {

    MarketingTaskTriggerRecordEntity queryByTaskCodeAndCreateTime(String taskCode, Date createTimeStart, Date createTimeEnd);

    /**
     * 保存触发记录
     */
    void saveTriggerRecord(MarketingTaskTriggerRecordEntity triggerRecord);

    /**
     * 查询待拉取数据的triggerRecord
     */
    List<MarketingTaskTriggerRecordEntity> queryWaitPullDataRecord();

    /**
     * 查询待创建任务动作的triggerRecord
     */
    List<MarketingTaskTriggerRecordEntity> queryWaitCreateActionRecord();

    /**
     * 查询待执行任务动作的triggerRecord
     */
    List<MarketingTaskTriggerRecordEntity> queryWaitExecActionRecord();

    /**
     * 查询待回调的triggerRecord
     */
    List<MarketingTaskTriggerRecordEntity> queryWaitCallBackRecord();

    /**
     * 查询待补偿的triggerRecord
     */
    List<MarketingTaskTriggerRecordEntity> queryWaitCompensateRecord();

    /**
     * 更新触发状态 + 描述
     */
    boolean updateTriggerStatus(String triggerCode, String fromStatus, String toStatus, String triggerDesc);

    /**
     * 更新触发状态
     */
    boolean updateTriggerStatus(String triggerCode, String fromStatus, String toStatus);

    /**
     * 批量更新
     */
    List<MarketingTaskTriggerRecordEntity> queryByLogicCodes(Collection<String> logicCodes);

}