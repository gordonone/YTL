package com.ytl.crm.service.ws.define.exec.exec;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ytl.crm.domain.entity.task.config.MarketingTaskEntity;
import com.ytl.crm.domain.entity.task.exec.MarketingTaskTriggerRecordEntity;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 营销任务-触发记录表 服务类
 * </p>
 *
 * @author hongj
 * @since 2024-08-12
 */
public interface IMarketingTaskTriggerRecordService extends IService<MarketingTaskTriggerRecordEntity> {

    MarketingTaskTriggerRecordEntity queryByTaskCodeAndCreateTime(String taskCode,
                                                                  Date createTimeStart,
                                                                  Date createTimeEnd);

    void saveTriggerRecord(MarketingTaskEntity taskEntity);

    List<MarketingTaskTriggerRecordEntity> queryWaitPullDataRecord();

    List<MarketingTaskTriggerRecordEntity> queryWaitCreateActionRecord();

    List<MarketingTaskTriggerRecordEntity> queryWaitExecActionRecord();

    List<MarketingTaskTriggerRecordEntity> queryWaitCallBackRecord();

    List<MarketingTaskTriggerRecordEntity> queryWaitCompensateRecord();

    List<MarketingTaskTriggerRecordEntity> queryWaitCompensateCallbackRecord();

    boolean updateTriggerStatus(String triggerCode, String fromStatus, String toStatus, String triggerDesc);

    boolean updateTriggerStatus(String triggerCode, String fromStatus, String toStatus);

}
