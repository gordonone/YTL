package com.ytl.crm.service.ws.Impl.exec;


import cn.hutool.core.collection.CollectionUtil;
import com.ytl.crm.constants.CommonConstant;
import com.ytl.crm.domain.entity.task.config.MarketingTaskEntity;
import com.ytl.crm.domain.entity.task.exec.MarketingTaskActionExecRecordEntity;
import com.ytl.crm.domain.entity.task.exec.MarketingTaskTriggerRecordEntity;
import com.ytl.crm.domain.enums.common.YesOrNoEnum;
import com.ytl.crm.domain.enums.task.config.TaskActionDependencyEnum;
import com.ytl.crm.domain.enums.task.exec.TaskActionCompensateStatus;
import com.ytl.crm.domain.enums.task.exec.TaskActionExecStatusEnum;
import com.ytl.crm.domain.enums.task.exec.TaskTriggerStatusEnum;
import com.ytl.crm.domain.task.config.MarketingTaskActionBO;
import com.ytl.crm.domain.task.config.MarketingTaskConfigBO;
import com.ytl.crm.service.ws.Impl.exec.handler.MarketingTaskHandlerFactory;
import com.ytl.crm.service.ws.define.exec.IMarketTaskActionExecLogic;
import com.ytl.crm.service.ws.define.exec.IMarketingTaskConfigLogic;
import com.ytl.crm.service.ws.define.exec.exec.IMarketingTaskActionExecRecordService;
import com.ytl.crm.service.ws.define.exec.exec.IMarketingTaskTriggerRecordService;
import com.ytl.crm.service.ws.define.exec.handler.action.IMarketingTaskActionExecHandler;
import com.ytl.crm.utils.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 任务动作执行逻辑
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MarketTaskActionExecLogicImpl implements IMarketTaskActionExecLogic {

    private final IMarketingTaskConfigLogic iMarketingTaskConfigLogic;
    private final IMarketingTaskTriggerRecordService iMarketingTaskTriggerRecordService;
    private final IMarketingTaskActionExecRecordService iMarketingTaskActionExecRecordService;
    private final MarketingTaskHandlerFactory marketingTaskHandlerFactory;

    @Override
    public void createAction(MarketingTaskEntity taskEntity, MarketingTaskTriggerRecordEntity triggerRecord) {
        MarketingTaskConfigBO configBO = iMarketingTaskConfigLogic.acquireTaskConfig(taskEntity);
        String triggerCode = triggerRecord.getLogicCode();
        if (configBO == null || !configBO.isConfigOK()) {
            iMarketingTaskTriggerRecordService.updateTriggerStatus(triggerCode,
                    TaskTriggerStatusEnum.BIZ_DATA_PULLED.getCode(), TaskTriggerStatusEnum.TASK_FAIL.getCode(), "任务配置异常");
            return;
        }

        List<MarketingTaskActionBO> actionList = configBO.getTaskActionList();
        Date currentTime = DateTimeUtil.currentTime();
        List<MarketingTaskActionExecRecordEntity> actionRecordList = actionList.stream().map(item ->
                this.buildTaskActionRecord(item, triggerRecord, currentTime)).collect(Collectors.toList());

        //创建动作执行记录，并更新触发状态
        iMarketingTaskActionExecRecordService.saveActionExecRecord(actionRecordList, triggerCode);
    }

    private MarketingTaskActionExecRecordEntity buildTaskActionRecord(MarketingTaskActionBO actionBO,
                                                                      MarketingTaskTriggerRecordEntity triggerRecord,
                                                                      Date currentTime) {

        MarketingTaskActionExecRecordEntity actionRecord = new MarketingTaskActionExecRecordEntity();
        actionRecord.setTaskCode(actionBO.getTaskCode());
        actionRecord.setTriggerCode(triggerRecord.getLogicCode());
        actionRecord.setActionExecStatus(TaskActionExecStatusEnum.INIT.getCode());

        //补偿状态
//        TaskActionCompensateStatus compensateStatus = YesOrNoEnum.YES.equalsValue(actionBO.getIsTomorrowContinue()) ?
//                TaskActionCompensateStatus.WAIT : TaskActionCompensateStatus.NONE;
//        actionRecord.setCompensateStatus(compensateStatus.getCode());
        actionRecord.setCompensateStatus(TaskActionCompensateStatus.NONE.getCode());

        actionRecord.setActionCode(actionBO.getLogicCode());
        actionRecord.setActionOneLevelType(actionBO.getActionOneLevelType());
        actionRecord.setActionTwoLevelType(actionBO.getActionTwoLevelType());
        actionRecord.setActionOrder(actionRecord.getActionOrder());

        actionRecord.setCreateUserCode(CommonConstant.SYSTEM);
        actionRecord.setCreateUserName(CommonConstant.SYSTEM_NAME);
        actionRecord.setCreateTime(currentTime);
        actionRecord.setLastModifyTime(currentTime);
        actionRecord.setModifyUserCode(CommonConstant.SYSTEM);
        actionRecord.setModifyUserName(CommonConstant.SYSTEM_NAME);
        return actionRecord;
    }

    @Override
    public void execAction(MarketingTaskEntity taskEntity, MarketingTaskTriggerRecordEntity triggerRecord) {
        String triggerCode = triggerRecord.getLogicCode();

        List<MarketingTaskActionExecRecordEntity> actionRecordList = iMarketingTaskActionExecRecordService.queryByTriggerCode(triggerCode);
        List<MarketingTaskActionExecRecordEntity> needExecRecordList = actionRecordList.stream()
                .filter(item -> TaskActionExecStatusEnum.INIT.getCode().equals(item.getActionExecStatus()))
                .sorted(Comparator.comparing(MarketingTaskActionExecRecordEntity::getActionOrder))
                .collect(Collectors.toList());

        //没有需要执行的，直接完成任务
        if (CollectionUtils.isEmpty(needExecRecordList)) {
            iMarketingTaskTriggerRecordService.updateTriggerStatus(triggerCode,
                    TaskTriggerStatusEnum.WAIT_EXEC_ACTION.getCode(), TaskTriggerStatusEnum.TASK_FINISH.getCode());
            return;
        }

        MarketingTaskConfigBO configBO = iMarketingTaskConfigLogic.acquireTaskConfig(taskEntity);
        List<MarketingTaskActionBO> taskActionConfigList = configBO.getTaskActionList();

        Map<String, MarketingTaskActionBO> taskActionConfigMap = taskActionConfigList.stream()
                .collect(Collectors.toMap(MarketingTaskActionBO::getLogicCode, Function.identity(), (k1, k2) -> k1));

        int minActionOrder = needExecRecordList.get(0).getActionOrder();
        for (MarketingTaskActionExecRecordEntity execRecord : needExecRecordList) {
            String actionCode = execRecord.getActionCode();
            MarketingTaskActionBO actionBO = taskActionConfigMap.get(actionCode);
            boolean canExec = checkActionCanExec(execRecord, actionBO, minActionOrder);
            if (canExec) {
                //执行动作，可以改成异步执行
                IMarketingTaskActionExecHandler handler = marketingTaskHandlerFactory.acquireActionExecHandler(actionBO);
                if (handler == null) {
                    log.info("没有动作执行的handler的实现类，oneLevelType={}，twoLevelType={}", actionBO.getActionOneLevelType(),
                            actionBO.getActionTwoLevelType());
                    throw new RuntimeException("没有动作执行的handler的实现类");
                }
               // handler.execAction(actionBO, execRecord);
            }
        }

        //修改触发状态
        iMarketingTaskTriggerRecordService.updateTriggerStatus(triggerCode,
                TaskTriggerStatusEnum.WAIT_EXEC_ACTION.getCode(), TaskTriggerStatusEnum.TASK_FINISH.getCode());
    }

    private boolean checkActionCanExec(MarketingTaskActionExecRecordEntity execRecord, MarketingTaskActionBO actionBO, int minActionOrder) {
        Integer actionOrder = execRecord.getActionOrder();
        String actionDependency = actionBO.getActionDependency();
        //1.当前最小顺序
        if (actionOrder <= minActionOrder) {
            return true;
        }
        //2.不依赖前置
        return !TaskActionDependencyEnum.DEPEND_ON_BEFORE_ACTION.getCode().equals(actionDependency);
    }


    @Override
    public void actionCallBack(MarketingTaskEntity taskEntity, MarketingTaskTriggerRecordEntity triggerRecord) {
        String triggerCode = triggerRecord.getLogicCode();
        List<MarketingTaskActionExecRecordEntity> waitCallBackActionList = iMarketingTaskActionExecRecordService
                .queryByTriggerCodeAndStatus(triggerCode, TaskActionExecStatusEnum.WAIT_CALL_BACK.getCode());
        if (CollectionUtils.isEmpty(waitCallBackActionList)) {
            log.info("没有需要进行回调的数据，triggerCode={}", triggerCode);
            return;
        }
        MarketingTaskConfigBO configBO = iMarketingTaskConfigLogic.acquireTaskConfig(taskEntity);
        List<MarketingTaskActionBO> taskActionConfigList = configBO.getTaskActionList();

        Map<String, MarketingTaskActionBO> taskActionConfigMap = taskActionConfigList.stream()
                .collect(Collectors.toMap(MarketingTaskActionBO::getLogicCode, Function.identity(), (k1, k2) -> k1));

        for (MarketingTaskActionExecRecordEntity execRecord : waitCallBackActionList) {
            String actionCode = execRecord.getActionCode();
            MarketingTaskActionBO actionBO = taskActionConfigMap.get(actionCode);

            IMarketingTaskActionExecHandler handler = marketingTaskHandlerFactory.acquireActionExecHandler(actionBO);
            handler.callBackAction(actionBO, execRecord);

            //修改执行状态 WAIT_EXEC -> WAIT_CALL_BACK
            iMarketingTaskActionExecRecordService.updateActionRecordStatus(execRecord.getLogicCode(),
                    TaskActionExecStatusEnum.WAIT_CALL_BACK.getCode(), TaskActionExecStatusEnum.FINISH.getCode());
        }


    }

    @Override
    public void compensateAction(MarketingTaskEntity taskEntity, MarketingTaskTriggerRecordEntity triggerRecord) {
        String triggerCode = triggerRecord.getLogicCode();
        List<MarketingTaskActionExecRecordEntity> actionRecordList = iMarketingTaskActionExecRecordService.queryByCompensateStatus(triggerCode,
                TaskActionCompensateStatus.WAIT_EXEC.getCode());
        if (CollectionUtil.isEmpty(actionRecordList)) {
            return;
        }

        MarketingTaskConfigBO configBO = iMarketingTaskConfigLogic.acquireTaskConfig(taskEntity);
        List<MarketingTaskActionBO> taskActionConfigList = configBO.getTaskActionList();

        Map<String, MarketingTaskActionBO> taskActionConfigMap = taskActionConfigList.stream()
                .collect(Collectors.toMap(MarketingTaskActionBO::getLogicCode, Function.identity(), (k1, k2) -> k1));

        for (MarketingTaskActionExecRecordEntity actionExecRecord : actionRecordList) {

            String actionCode = actionExecRecord.getActionCode();
            MarketingTaskActionBO actionBO = taskActionConfigMap.get(actionCode);
            if (!YesOrNoEnum.YES.equalsValue(actionBO.getIsTomorrowContinue())) {
                continue;
            }
            IMarketingTaskActionExecHandler handler = marketingTaskHandlerFactory.acquireActionExecHandler(actionBO);
            handler.compensateAction(actionBO, actionExecRecord);

            //修改补偿状态 WAIT_EXEC -> WAIT_CALL_BACK
            iMarketingTaskActionExecRecordService.updateCompensateStatus(actionExecRecord.getLogicCode(),
                    TaskActionCompensateStatus.WAIT_EXEC.getCode(), TaskActionCompensateStatus.WAIT_CALL_BACK.getCode());
        }
    }

    @Override
    public void compensateCallBack(MarketingTaskEntity taskEntity, MarketingTaskTriggerRecordEntity triggerRecord) {
        //获取执行成功，但是没有回调结果的数据
        String triggerCode = triggerRecord.getLogicCode();
        List<MarketingTaskActionExecRecordEntity> waitCallBackActionList = iMarketingTaskActionExecRecordService.queryByCompensateStatus(triggerCode,
                TaskActionCompensateStatus.WAIT_CALL_BACK.getCode());
        if (CollectionUtils.isEmpty(waitCallBackActionList)) {
            log.info("没有需要进行回调的数据，triggerCode={}", triggerCode);
            return;
        }
        MarketingTaskConfigBO configBO = iMarketingTaskConfigLogic.acquireTaskConfig(taskEntity);
        List<MarketingTaskActionBO> taskActionConfigList = configBO.getTaskActionList();

        Map<String, MarketingTaskActionBO> taskActionConfigMap = taskActionConfigList.stream()
                .collect(Collectors.toMap(MarketingTaskActionBO::getLogicCode, Function.identity(), (k1, k2) -> k1));

        for (MarketingTaskActionExecRecordEntity execRecord : waitCallBackActionList) {
            String actionCode = execRecord.getActionCode();
            MarketingTaskActionBO actionBO = taskActionConfigMap.get(actionCode);
            IMarketingTaskActionExecHandler handler = marketingTaskHandlerFactory.acquireActionExecHandler(actionBO);
            handler.callBackAction(actionBO, execRecord);

            //修改补偿状态 WAIT_CALL_BACK -> FINISH
            iMarketingTaskActionExecRecordService.updateCompensateStatus(execRecord.getLogicCode(),
                    TaskActionCompensateStatus.WAIT_CALL_BACK.getCode(), TaskActionCompensateStatus.FINISH.getCode());
        }

    }


}
