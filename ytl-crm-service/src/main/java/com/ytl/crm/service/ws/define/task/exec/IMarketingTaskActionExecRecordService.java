package com.ytl.crm.service.ws.define.task.exec;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ytl.crm.domain.entity.task.exec.MarketingTaskActionExecRecordEntity;
import com.ytl.crm.domain.enums.task.config.TaskActionOneLevelTypeEnum;
import com.ytl.crm.domain.enums.task.exec.TaskActionExecStatusEnum;

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

    List<MarketingTaskActionExecRecordEntity> listByTriggerCode(String triggerCode);

    List<MarketingTaskActionExecRecordEntity> listByTriggerCodeAndStatus(String triggerCode, TaskActionExecStatusEnum execStatusEnum);

    MarketingTaskActionExecRecordEntity getOneByTriggerCodeAndStatus(String triggerCode, TaskActionExecStatusEnum execStatusEnum);

    boolean updateExecStatus(String logicCode, TaskActionExecStatusEnum fromStatus, TaskActionExecStatusEnum toStatus);

    List<MarketingTaskActionExecRecordEntity> listWaitCallbackAction(String logicCode);

    List<MarketingTaskActionExecRecordEntity> listWaitCompensateAction(String triggerCode);

    List<MarketingTaskActionExecRecordEntity> listByActionOneType(String triggerCode, TaskActionOneLevelTypeEnum oneLevelTypeEnum);

}
