package com.ytl.crm.service.ws.define.exec.exec;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ytl.crm.domain.entity.task.exec.MarketingTaskActionExecRecordEntity;

import java.util.List;

/**
 * <p>
 * 营销任务-动作执行记录表 服务类
 * </p>
 *
 * @author hongj
 * @since 2024-08-12
 */
public interface IMarketingTaskActionExecRecordService extends IService<MarketingTaskActionExecRecordEntity> {

    MarketingTaskActionExecRecordEntity queryByLogicCode(String logicCode);

    List<MarketingTaskActionExecRecordEntity> queryByTriggerCode(String triggerCode);

    List<MarketingTaskActionExecRecordEntity> queryByTriggerCodeAndStatus(String triggerCode, String execStatus);

    void saveActionExecRecord(List<MarketingTaskActionExecRecordEntity> recordList, String triggerCode);

    boolean updateActionRecordStatus(String logicCode, String fromStatus, String toStatus);

    List<MarketingTaskActionExecRecordEntity> queryByCompensateStatus(String triggerCode, String compensateStatus);

    boolean updateCompensateStatus(String logicCode, String fromStatus, String toStatus);

}
