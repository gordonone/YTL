package com.ytl.crm.service.ws.Impl.exec;


import com.ytl.crm.domain.entity.task.config.MarketingTaskEntity;
import com.ytl.crm.domain.entity.task.exec.MarketingTaskTriggerRecordEntity;
import com.ytl.crm.domain.enums.task.exec.TaskTriggerStatusEnum;
import com.ytl.crm.service.ws.Impl.exec.handler.MarketingTaskHandlerFactory;
import com.ytl.crm.service.ws.define.exec.IMarketTaskActionExecLogic;
import com.ytl.crm.service.ws.define.exec.IMarketingTaskExecLogic;
import com.ytl.crm.service.ws.define.exec.config.IMarketingTaskService;
import com.ytl.crm.service.ws.define.exec.exec.IMarketingTaskTriggerRecordService;
import com.ytl.crm.service.ws.define.exec.handler.data.IMarketingTaskPullDataHandler;
import com.ytl.crm.utils.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    private final IMarketingTaskService iMarketingTaskService;
    private final IMarketingTaskTriggerRecordService iMarketingTaskTriggerRecordService;
    private final IMarketTaskActionExecLogic iMarketTaskActionExecLogic;

    private final MarketingTaskHandlerFactory marketingTaskHandlerFactory;

    @Override
    public void triggerTask() {
        //获取未触发的数据
        List<MarketingTaskEntity> waitTriggerTaskList = iMarketingTaskService.queryWaitTriggerTask();
        if (CollectionUtils.isEmpty(waitTriggerTaskList)) {
            log.warn("今日任务已全部促发无需执行");
            return;
        }
        Pair<Date, Date> todayStartToEnd = DateTimeUtil.getTodayStartToEnd(false);
        for (MarketingTaskEntity taskEntity : waitTriggerTaskList) {
            try {
                doTriggerTask(taskEntity, todayStartToEnd);
            } catch (Exception e) {
                log.error("促发任务异常，taskCode={}", taskEntity.getLogicCode(), e);
            }
        }
    }

    public void doTriggerTask(MarketingTaskEntity taskEntity, Pair<Date, Date> todayStartToEnd) {
        String taskCode = taskEntity.getLogicCode();
        String lockKey = "UGC_CRM_MARKETING_TASK:TRIGGER:" + taskCode;
//        RLock lock = redissonClient.getLock(lockKey);
//        try {
//            if (!lock.tryLock()) {
//                log.warn("当前任务正在触发，直接返回，taskCode={}", taskCode);
//                return;
//            }
//            MarketingTaskTriggerRecordEntity triggerRecord = iMarketingTaskTriggerRecordService.queryByTaskCodeAndCreateTime(taskCode,
//                    todayStartToEnd.getLeft(), todayStartToEnd.getRight());
//            if (triggerRecord != null) {
//                String triggerCode = triggerRecord.getLogicCode();
//                log.warn("今日已触发任务，无需重新触发，taskCode={}，triggerCode={}", taskCode, triggerCode);
//                return;
//            }
//
//            iMarketingTaskTriggerRecordService.saveTriggerRecord(taskEntity);
//
//        } finally {
//            if (lock.isHeldByCurrentThread()) {
//                lock.unlock();
//            }
//        }
    }

    @Override
    public void acquireTaskBizData() {
        // 1. 获取init状态的促发记录
        List<MarketingTaskTriggerRecordEntity> triggerRecordList = iMarketingTaskTriggerRecordService.queryWaitPullDataRecord();
        if (CollectionUtils.isEmpty(triggerRecordList)) {
            log.warn("没有需要拉取业务数据的促发记录");
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
//        String lockKey = "UGC_CRM_MARKETING_TASK:PULL_DATA:" + taskCode;
//        RLock lock = redissonClient.getLock(lockKey);
//        try {
//            if (!lock.tryLock()) {
//                log.warn("当前任务正在拉取数据，无需重复执行，taskCode={}", taskCode);
//                return;
//            }
//            MarketingTaskEntity taskEntity = iMarketingTaskService.queryByTaskCode(taskCode);
//            IMarketingTaskPullDataHandler handler = marketingTaskHandlerFactory.acquirePullDataHandler(taskEntity);
//            if (handler == null) {
//                String triggerType = taskEntity.getTriggerType();
//                log.warn("不支持的触发任务的类型，triggerType={}", triggerType);
//                iMarketingTaskTriggerRecordService.updateTriggerStatus(triggerRecord.getLogicCode(),
//                        TaskTriggerStatusEnum.INIT.getCode(), TaskTriggerStatusEnum.TASK_FAIL.getCode(), "不支持的触发类型" + triggerType);
//                return;
//            }
//            handler.pullTaskData(taskEntity, triggerRecord);
//        } finally {
//            if (lock.isHeldByCurrentThread()) {
//                lock.unlock();
//            }
//        }
    }


    @Override
    public void createAction() {
        // 1. 获取BIZ_DATA_PULLED状态的促发记录
        List<MarketingTaskTriggerRecordEntity> triggerRecordList = iMarketingTaskTriggerRecordService.queryWaitCreateActionRecord();
        if (CollectionUtils.isEmpty(triggerRecordList)) {
            log.warn("没有待执行动作的触发记录");
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
//        String taskCode = triggerRecord.getTaskCode();
//        String lockKey = "UGC_CRM_MARKETING_TASK:CREATE_ACTION:" + taskCode;
//        RLock lock = redissonClient.getLock(lockKey);
//        try {
//            if (!lock.tryLock()) {
//                log.warn("当前任务正再创建动作执行记录，无需重复执行，taskCode={}", taskCode);
//                return;
//            }
//            MarketingTaskEntity taskEntity = iMarketingTaskService.queryByTaskCode(taskCode);
//            //创建actionRecord
//            iMarketTaskActionExecLogic.createAction(taskEntity, triggerRecord);
//        } finally {
//            if (lock.isHeldByCurrentThread()) {
//                lock.unlock();
//            }
//        }

    }


    @Override
    public void execTaskAction() {
        // 1. 获取WAIT_EXEC_ACTION状态的促发记录
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
//        String taskCode = triggerRecord.getTaskCode();
//        String lockKey = "UGC_CRM_MARKETING_TASK:EXEC_ACTION:" + taskCode;
//        RLock lock = redissonClient.getLock(lockKey);
//        try {
//            if (!lock.tryLock()) {
//                log.warn("当前任务正在执行动作，无需重复执行，taskCode={}", taskCode);
//                return;
//            }
//            MarketingTaskEntity taskEntity = iMarketingTaskService.queryByTaskCode(taskCode);
//            iMarketTaskActionExecLogic.execAction(taskEntity, triggerRecord);
//        } finally {
//            if (lock.isHeldByCurrentThread()) {
//                lock.unlock();
//            }
//        }
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
        String taskCode = triggerRecord.getTaskCode();
        String lockKey = "UGC_CRM_MARKETING_TASK:CALL_BACK:" + taskCode;
//        RLock lock = redissonClient.getLock(lockKey);
//        try {
//            if (!lock.tryLock()) {
//                log.warn("当前任务正在执行动作，无需重复执行，taskCode={}", taskCode);
//                return;
//            }
//            MarketingTaskEntity taskEntity = iMarketingTaskService.queryByTaskCode(taskCode);
//            iMarketTaskActionExecLogic.actionCallBack(taskEntity, triggerRecord);
//        } finally {
//            if (lock.isHeldByCurrentThread()) {
//                lock.unlock();
//            }
//        }
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
//        String taskCode = triggerRecord.getTaskCode();
//        String lockKey = "UGC_CRM_MARKETING_TASK:COMPENSATE:" + taskCode;
//        RLock lock = redissonClient.getLock(lockKey);
//        try {
//            if (!lock.tryLock()) {
//                log.warn("当前任务正在执行动作，无需重复执行，taskCode={}", taskCode);
//                return;
//            }
//            MarketingTaskEntity taskEntity = iMarketingTaskService.queryByTaskCode(taskCode);
//            iMarketTaskActionExecLogic.compensateAction(taskEntity, triggerRecord);
//        } finally {
//            if (lock.isHeldByCurrentThread()) {
//                lock.unlock();
//            }
//        }
    }

    @Override
    public void compensateCallBack() {
        // 1. 获取等待回调的任务数据，任务夜间执行
        List<MarketingTaskTriggerRecordEntity> triggerRecordList = iMarketingTaskTriggerRecordService.queryWaitCompensateCallbackRecord();
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


}
