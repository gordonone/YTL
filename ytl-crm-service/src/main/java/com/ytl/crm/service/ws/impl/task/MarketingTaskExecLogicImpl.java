package com.ytl.crm.service.ws.impl.task;


import cn.hutool.core.util.IdUtil;
import com.ytl.crm.domain.entity.task.config.MarketingTaskEntity;
import com.ytl.crm.domain.entity.task.exec.MarketingTaskTriggerRecordEntity;
import com.ytl.crm.domain.enums.task.exec.TaskTriggerStatusEnum;
import com.ytl.crm.domain.bo.task.exec.MarketingTaskBO;
import com.ytl.crm.domain.bo.task.exec.MarketingTaskConfigBO;
import com.ytl.crm.service.ws.impl.task.handler.MarketingTaskHandlerFactory;
import com.ytl.crm.service.ws.define.task.IMarketTaskActionExecLogic;
import com.ytl.crm.service.ws.define.task.IMarketingTaskConfigLogic;
import com.ytl.crm.service.ws.define.task.IMarketingTaskExecLogic;
import com.ytl.crm.service.ws.define.task.config.IMarketingTaskService;
import com.ytl.crm.service.ws.define.task.exec.IMarketingTaskTriggerRecordService;
import com.ytl.crm.service.ws.define.task.handler.data.IMarketingTaskPullDataHandler;
import com.ytl.crm.utils.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * 任务执行驱动
 * 1.触发任务
 * 2.获取业务数据
 * 3.创建动作
 * 4.执行动作
 * 5.执行动作回调（主动回调）
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MarketingTaskExecLogicImpl implements IMarketingTaskExecLogic {

//    private final RedissonClient redissonClient;

    private final MarketingTaskHandlerFactory marketingTaskHandlerFactory;
    private final IMarketingTaskService iMarketingTaskService;
    private final IMarketingTaskTriggerRecordService iMarketingTaskTriggerRecordService;
    private final IMarketTaskActionExecLogic iMarketTaskActionExecLogic;
    private final IMarketingTaskConfigLogic iMarketingTaskConfigLogic;

    @Override
    public void triggerTask() {
        //获取未触发的数据
        List<MarketingTaskEntity> waitTriggerTaskList = iMarketingTaskService.queryWaitTriggerTask();
        if (CollectionUtils.isEmpty(waitTriggerTaskList)) {
            log.warn("今日任务已全部触发无需执行");
            return;
        }
        Pair<Date, Date> todayStartToEnd = DateTimeUtil.getTodayStartToEnd(false);
        for (MarketingTaskEntity taskEntity : waitTriggerTaskList) {
            try {
                doTriggerTask(taskEntity, todayStartToEnd);
            } catch (Exception e) {
                log.error("触发任务异常，taskCode={}", taskEntity.getLogicCode(), e);
            }
        }
    }

    public void doTriggerTask(MarketingTaskEntity taskEntity, Pair<Date, Date> todayStartToEnd) {
        String taskCode = taskEntity.getLogicCode();
        String lockKey = "UGC_CRM_MARKETING_TASK:TRIGGER:" + taskCode;
        //     RLock lock = redissonClient.getLock(lockKey);
        try {
//            if (!lock.tryLock()) {
//                log.warn("当前任务正在触发，直接返回，taskCode={}", taskCode);
//                return;
//            }
            MarketingTaskTriggerRecordEntity oldTriggerRecord = iMarketingTaskTriggerRecordService.queryByTaskCodeAndCreateTime(taskCode, todayStartToEnd.getLeft(), todayStartToEnd.getRight());
            if (oldTriggerRecord != null) {
                String triggerCode = oldTriggerRecord.getLogicCode();
                log.warn("今日已触发任务，无需重新触发，taskCode={}，triggerCode={}", taskCode, triggerCode);
                return;
            }

            MarketingTaskConfigBO configBO = iMarketingTaskConfigLogic.acquireTaskConfig(taskEntity);
            MarketingTaskTriggerRecordEntity triggerRecord = assemblyRecord(configBO);

            iMarketingTaskTriggerRecordService.saveTriggerRecord(triggerRecord);

        } finally {
//            if (lock.isHeldByCurrentThread()) {
//                lock.unlock();
//            }
        }
    }

    private MarketingTaskTriggerRecordEntity assemblyRecord(MarketingTaskConfigBO configBO) {
        boolean configOK = configBO.isConfigOK();
        TaskTriggerStatusEnum triggerStatusEnum = configOK ? TaskTriggerStatusEnum.WAIT_PULL_DATA : TaskTriggerStatusEnum.FAIL;
        String triggerDesc = configOK ? StringUtils.EMPTY : "任务配置无效";
        MarketingTaskBO taskBaseInfo = configBO.getTaskBaseInfo();

        MarketingTaskTriggerRecordEntity triggerRecord = new MarketingTaskTriggerRecordEntity();
        String taskCode = taskBaseInfo.getLogicCode();
        triggerRecord.setLogicCode(String.valueOf(IdUtil.createSnowflake(1, 1).nextId()));
        triggerRecord.setTaskCode(taskCode);
        triggerRecord.setTriggerStatus(triggerStatusEnum.getCode());
        triggerRecord.setTriggerDesc(triggerDesc);
        return triggerRecord;
    }

    @Override
    public void pullData() {
        // 1. 获取init状态的触发记录
        List<MarketingTaskTriggerRecordEntity> triggerRecordList = iMarketingTaskTriggerRecordService.queryWaitPullDataRecord();
        if (CollectionUtils.isEmpty(triggerRecordList)) {
            log.warn("没有需要拉取业务数据的触发记录");
            return;
        }
        // 2. 逐一拉取数据
        for (MarketingTaskTriggerRecordEntity triggerRecord : triggerRecordList) {
            String triggerCode = triggerRecord.getLogicCode();
            try {
                doPullTaskData(triggerRecord);
            } catch (Exception e) {
                log.error("拉取业务数据异常，triggerCode={}", triggerCode, e);
            }
        }
    }

    public void doPullTaskData(MarketingTaskTriggerRecordEntity triggerRecord) {
        String taskCode = triggerRecord.getTaskCode();
        String lockKey = "UGC_CRM_MARKETING_TASK:PULL_DATA:" + taskCode;
        //    RLock lock = redissonClient.getLock(lockKey);
        try {
////            if (!lock.tryLock()) {
//                log.warn("当前任务正在拉取数据，无需重复执行，taskCode={}", taskCode);
//                return;
//            }
            MarketingTaskEntity taskEntity = iMarketingTaskService.queryByTaskCode(taskCode);
            IMarketingTaskPullDataHandler handler = marketingTaskHandlerFactory.acquirePullDataHandler(taskEntity);
            if (handler == null) {
                String triggerType = taskEntity.getTriggerType();
                log.warn("不支持的触发任务的类型，triggerType={}", triggerType);
                iMarketingTaskTriggerRecordService.updateTriggerStatus(triggerRecord.getLogicCode(), TaskTriggerStatusEnum.WAIT_PULL_DATA.getCode(), TaskTriggerStatusEnum.FAIL.getCode(), "不支持的触发类型" + triggerType);
                return;
            }
            handler.pullTaskData(taskEntity, triggerRecord);
        } finally {
//            if (lock.isHeldByCurrentThread()) {
//                lock.unlock();
//            }
        }
    }


    @Override
    public void createTaskAction() {
        // 1. 获取BIZ_DATA_PULLED状态的触发记录
        List<MarketingTaskTriggerRecordEntity> triggerRecordList = iMarketingTaskTriggerRecordService.queryWaitCreateActionRecord();
        if (CollectionUtils.isEmpty(triggerRecordList)) {
            log.warn("没有待创建动作的触发记录");
            return;
        }
        // 2. 逐一拉取数据
        for (MarketingTaskTriggerRecordEntity triggerRecord : triggerRecordList) {
            String triggerCode = triggerRecord.getLogicCode();
            try {
                doCreateAction(triggerRecord);
            } catch (Exception e) {
                log.error("创建任务动作异常，triggerCode={}", triggerCode, e);
            }
        }
    }

    public void doCreateAction(MarketingTaskTriggerRecordEntity triggerRecord) {
        String logicCode = triggerRecord.getLogicCode();
        String lockKey = "UGC_CRM_MARKETING_TASK:CREATE_ACTION:" + logicCode;
//        RLock lock = redissonClient.getLock(lockKey);
        try {
//            if (!lock.tryLock()) {
//                log.warn("当前任务正再创建动作执行记录，无需重复执行，taskCode={}", logicCode);
//                return;
//            }
            String taskCode = triggerRecord.getTaskCode();
            MarketingTaskEntity taskEntity = iMarketingTaskService.queryByTaskCode(taskCode);
            //创建actionRecord + actionItem
            iMarketTaskActionExecLogic.createAction(taskEntity, triggerRecord);
        } finally {
//            if (lock.isHeldByCurrentThread()) {
//                lock.unlock();
//            }
        }

    }


    @Override
    public void execTaskAction() {
        // 1. 获取WAIT_EXEC_ACTION状态的触发记录
        List<MarketingTaskTriggerRecordEntity> triggerRecordList = iMarketingTaskTriggerRecordService.queryWaitExecActionRecord();
        if (CollectionUtils.isEmpty(triggerRecordList)) {
            log.warn("没有待执行动作的触发记录");
            return;
        }
        // 2. 逐一执行动作
        for (MarketingTaskTriggerRecordEntity triggerRecord : triggerRecordList) {
            String triggerCode = triggerRecord.getLogicCode();
            try {
                doExecAction(triggerRecord);
            } catch (Exception e) {
                log.error("执行业务动作异常，triggerCode={}", triggerCode, e);
            }
        }
    }

    public void doExecAction(MarketingTaskTriggerRecordEntity triggerRecord) {
        String logicCode = triggerRecord.getLogicCode();
        String lockKey = "UGC_CRM_MARKETING_TASK:EXEC_ACTION:" + logicCode;
      //  RLock lock = redissonClient.getLock(lockKey);
        try {
//            if (!lock.tryLock()) {
//                log.warn("当前任务正在执行动作，无需重复执行，logicCode={}", logicCode);
//                return;
//            }
            String taskCode = triggerRecord.getTaskCode();
            MarketingTaskEntity taskEntity = iMarketingTaskService.queryByTaskCode(taskCode);
            iMarketTaskActionExecLogic.execAction(taskEntity, triggerRecord);
        } finally {
//            if (lock.isHeldByCurrentThread()) {
//                lock.unlock();
//            }
        }
    }

    @Override
    public void callBackTask() {
        // 1. 获取等待回调的数据，任务夜间执行
        List<MarketingTaskTriggerRecordEntity> triggerRecordList = iMarketingTaskTriggerRecordService.queryWaitCallBackRecord();
        if (CollectionUtils.isEmpty(triggerRecordList)) {
            log.warn("没有等待回调的触发记录");
            return;
        }

        // 2. 逐一回调
        for (MarketingTaskTriggerRecordEntity triggerRecord : triggerRecordList) {
            String triggerCode = triggerRecord.getLogicCode();
            try {
                doCallBackTask(triggerRecord);
            } catch (Exception e) {
                log.error("执行业务动作回调，triggerCode={}", triggerCode, e);
            }
        }
    }

    public void doCallBackTask(MarketingTaskTriggerRecordEntity triggerRecord) {
        String logicCode = triggerRecord.getLogicCode();
        String lockKey = "UGC_CRM_MARKETING_TASK:CALL_BACK:" + logicCode;
        //  RLock lock = redissonClient.getLock(lockKey);
        try {
//            if (!lock.tryLock()) {
//                log.warn("当前任务正在执行动作，无需重复执行，logicCode={}", logicCode);
//                return;
//            }
            String taskCode = triggerRecord.getTaskCode();
            MarketingTaskEntity taskEntity = iMarketingTaskService.queryByTaskCode(taskCode);
            iMarketTaskActionExecLogic.actionCallBack(taskEntity, triggerRecord);
        } finally {
//            if (lock.isHeldByCurrentThread()) {
//                lock.unlock();
//            }
        }
    }


    @Override
    public void compensateTask() {
        // 1. 获取待补偿的触发记录
        List<MarketingTaskTriggerRecordEntity> triggerRecordList = iMarketingTaskTriggerRecordService.queryWaitCompensateRecord();
        if (CollectionUtils.isEmpty(triggerRecordList)) {
            log.warn("没有等待补偿的触发记录");
            return;
        }

        // 2. 逐一补偿
        for (MarketingTaskTriggerRecordEntity triggerRecord : triggerRecordList) {
            String triggerCode = triggerRecord.getLogicCode();
            try {
                doCompensateTask(triggerRecord);
            } catch (Exception e) {
                log.error("执行业务动作补偿，triggerCode={}", triggerCode, e);
            }
        }
    }

    public void doCompensateTask(MarketingTaskTriggerRecordEntity triggerRecord) {
        //任务补偿，按task维度控制
        String taskCode = triggerRecord.getTaskCode();
        String lockKey = "UGC_CRM_MARKETING_TASK:COMPENSATE:" + taskCode;
        //    RLock lock = redissonClient.getLock(lockKey);
        try {
//            if (!lock.tryLock()) {
//                log.warn("当前任务正在执行动作，无需重复执行，taskCode={}", taskCode);
//                return;
//            }
            MarketingTaskEntity taskEntity = iMarketingTaskService.queryByTaskCode(taskCode);
            iMarketTaskActionExecLogic.compensateAction(taskEntity, triggerRecord);
        } finally {
//            if (lock.isHeldByCurrentThread()) {
//                lock.unlock();
//            }
        }
    }


}
