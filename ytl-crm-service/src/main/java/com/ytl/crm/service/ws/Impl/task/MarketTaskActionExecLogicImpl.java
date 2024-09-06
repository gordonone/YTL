package com.ytl.crm.service.ws.Impl.task;


import cn.hutool.core.util.IdUtil;
import com.google.common.collect.Lists;
import com.ytl.crm.domain.entity.task.config.MarketingTaskEntity;
import com.ytl.crm.domain.entity.task.exec.*;
import com.ytl.crm.domain.enums.task.config.TaskActionDependencyEnum;
import com.ytl.crm.domain.enums.task.config.TaskStatusEnum;
import com.ytl.crm.domain.enums.task.exec.TaskActionExecStatusEnum;
import com.ytl.crm.domain.enums.task.exec.TaskActionItemExecStatusEnum;
import com.ytl.crm.domain.enums.task.exec.TaskTriggerStatusEnum;
import com.ytl.crm.domain.enums.task.exec.ThirdTaskExecStatusEnum;
import com.ytl.crm.domain.enums.task.ret.TaskActionItemFinalRetEnum;
import com.ytl.crm.domain.task.exec.MarketingTaskActionBO;
import com.ytl.crm.domain.task.exec.MarketingTaskBO;
import com.ytl.crm.domain.task.exec.MarketingTaskConfigBO;
import com.ytl.crm.service.ws.Impl.task.handler.MarketingTaskHandlerFactory;
import com.ytl.crm.service.ws.define.task.IMarketTaskActionExecLogic;
import com.ytl.crm.service.ws.define.task.IMarketingTaskConfigLogic;
import com.ytl.crm.service.ws.define.task.exec.*;
import com.ytl.crm.service.ws.define.task.handler.action.IMarketingTaskActionExecHandler;
import com.ytl.crm.utils.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * 任务动作执行逻辑
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MarketTaskActionExecLogicImpl implements IMarketTaskActionExecLogic {

    private final MarketingTaskHandlerFactory marketingTaskHandlerFactory;

    private final IMarketingTaskConfigLogic iMarketingTaskConfigLogic;
    private final IMarketingTaskTriggerRecordService iMarketingTaskTriggerRecordService;
    private final IMarketingTaskActionExecRecordService iMarketingTaskActionExecRecordService;
    private final IMarketingTaskBizInfoService iMarketingTaskBizInfoService;
    private final IMarketingTaskActionExecItemService iMarketingTaskActionExecItemService;
    private final IMarketingTaskActionItemBizRelationService iMarketingTaskActionItemBizRelationService;

    @Override
    public void createAction(MarketingTaskEntity taskEntity, MarketingTaskTriggerRecordEntity triggerRecord) {
        String triggerCode = triggerRecord.getLogicCode();
        //1.初始化actionRecord
        List<MarketingTaskActionExecRecordEntity> allActionRecordList = initActionRecord(taskEntity, triggerRecord);
        //2.初始化actionItem
        initAllActionExecItem(triggerRecord, allActionRecordList);
        //3.更新triggerRecord的状态 - 直接改成已完成
        boolean updateRet = iMarketingTaskTriggerRecordService.updateTriggerStatus(triggerCode,
                TaskTriggerStatusEnum.WAIT_CREATE_ACTION.getCode(), TaskTriggerStatusEnum.WAIT_EXEC_ACTION.getCode());
        log.info("更新触发记录状态，triggerCode={}，updateRet={}", triggerCode, updateRet);
    }

    private List<MarketingTaskActionExecRecordEntity> initActionRecord(MarketingTaskEntity taskEntity, MarketingTaskTriggerRecordEntity triggerRecord) {
        String triggerCode = triggerRecord.getLogicCode();
        MarketingTaskConfigBO configBO = iMarketingTaskConfigLogic.acquireTaskConfig(taskEntity);
        List<MarketingTaskActionBO> actionList = configBO.getTaskActionList();
        //这里在一个事务里，不可能存在部分成功，故有数据就说明已保存
        List<MarketingTaskActionExecRecordEntity> existActionList = iMarketingTaskActionExecRecordService.listByTriggerCode(triggerCode);
        if (!CollectionUtils.isEmpty(existActionList)) {
            return existActionList;
        }

        Date currentTime = DateTimeUtil.currentTime();
        actionList.sort(Comparator.comparing(MarketingTaskActionBO::getActionOrder));
        List<MarketingTaskActionExecRecordEntity> saveActionRecordList = Lists.newArrayListWithExpectedSize(actionList.size());
        for (MarketingTaskActionBO actionBO : actionList) {
            MarketingTaskActionExecRecordEntity actionExecRecord = this.buildTaskActionRecord(actionBO, triggerRecord, currentTime);
            saveActionRecordList.add(actionExecRecord);
        }

        boolean actionRecordSaveRet = iMarketingTaskActionExecRecordService.saveBatch(saveActionRecordList);
        log.info("保存动作记录结果，actionRecordSaveRet={}", actionRecordSaveRet);
        return saveActionRecordList;
    }

    private void initAllActionExecItem(MarketingTaskTriggerRecordEntity triggerRecord, List<MarketingTaskActionExecRecordEntity> allActionRecordList) {
        String triggerCode = triggerRecord.getLogicCode();
        List<MarketingTaskBizInfoEntity> bizInfoList = iMarketingTaskBizInfoService.queryValidEntity(triggerCode);
        if (CollectionUtils.isEmpty(bizInfoList)) {
            return;
        }

        for (MarketingTaskActionExecRecordEntity actionExecRecord : allActionRecordList) {
            initActionExecItem(actionExecRecord, bizInfoList);
        }
    }

    private void initActionExecItem(MarketingTaskActionExecRecordEntity actionExecRecord,
                                    List<MarketingTaskBizInfoEntity> bizInfoList) {
        //根据code查询已经保存的数据，没有保存的重新保存
        String actionRecordCode = actionExecRecord.getLogicCode();
        List<MarketingTaskBizInfoEntity> needSaveBizList = bizInfoList;
        Set<String> existBizCodeSet = iMarketingTaskActionItemBizRelationService.queryExistBizCode(actionRecordCode);
        if (!CollectionUtils.isEmpty(existBizCodeSet)) {
            needSaveBizList = bizInfoList.stream().filter(item -> !existBizCodeSet.contains(item.getLogicCode())).collect(Collectors.toList());
        }

        if (CollectionUtils.isEmpty(needSaveBizList)) {
            log.info("当前动作，没有需要保存的数据，actionRecordCode={}", actionExecRecord);
            return;
        }

        //按virtualKeeperThirdId分组
        Map<String, List<MarketingTaskBizInfoEntity>> keeperBizInfoMap = needSaveBizList.stream()
                .collect(Collectors.groupingBy(MarketingTaskBizInfoEntity::getVirtualKeeperThirdId));

        //构建item和itemBizRelation实体
        for (Map.Entry<String, List<MarketingTaskBizInfoEntity>> bizEntry : keeperBizInfoMap.entrySet()) {
            String virtualKeeperThirdId = bizEntry.getKey();
            List<MarketingTaskBizInfoEntity> bizList = bizEntry.getValue();
            //TODO 这里可能还需要进行拆分，目前按管家分组
            MarketingTaskBizInfoEntity bizInfoEntity = bizList.get(0);
            MarketingTaskActionExecItemEntity itemEntity = buildInitExecItem(actionExecRecord, bizInfoEntity);
            List<MarketingTaskActionItemBizRelationEntity> relationList = buildItemBizRelation(itemEntity, bizList);
            boolean saveRet = iMarketingTaskActionExecItemService.saveItemAndRelation(itemEntity, relationList);
            log.info("保存actionItem和itemBizRel结果，virtualKeeperThirdId={}，saveRet={}", virtualKeeperThirdId, saveRet);
        }
    }

    private MarketingTaskActionExecItemEntity buildInitExecItem(MarketingTaskActionExecRecordEntity actionExecRecord,
                                                                MarketingTaskBizInfoEntity bizInfoEntity) {
        MarketingTaskActionExecItemEntity itemEntity = new MarketingTaskActionExecItemEntity();
        itemEntity.setExecStatus(TaskActionItemExecStatusEnum.WAIT_EXEC.getCode());
        itemEntity.setFinalExecRet(TaskActionItemFinalRetEnum.INIT.getCode());
        itemEntity.setThirdTaskExecStatus(ThirdTaskExecStatusEnum.INIT.getCode());
        itemEntity.setLogicCode(String.valueOf(IdUtil.createSnowflake(1, 1).nextId()));
        itemEntity.setTaskCode(actionExecRecord.getTaskCode());
        itemEntity.setTriggerCode(actionExecRecord.getTriggerCode());
        itemEntity.setActionCode(actionExecRecord.getActionCode());
        itemEntity.setActionRecordCode(actionExecRecord.getLogicCode());
        itemEntity.setVirtualKeeperId(bizInfoEntity.getVirtualKeeperId());
        itemEntity.setVirtualKeeperThirdId(bizInfoEntity.getVirtualKeeperThirdId());
        itemEntity.setVirtualKeeperName(bizInfoEntity.getVirtualKeeperName());
        return itemEntity;
    }

    private List<MarketingTaskActionItemBizRelationEntity> buildItemBizRelation(MarketingTaskActionExecItemEntity itemEntity,
                                                                                List<MarketingTaskBizInfoEntity> bizList) {
        String itemCode = itemEntity.getLogicCode();
        String actionRecordCode = itemEntity.getActionRecordCode();
        List<MarketingTaskActionItemBizRelationEntity> relationList = Lists.newArrayListWithExpectedSize(bizList.size());
        for (MarketingTaskBizInfoEntity bizInfo : bizList) {
            MarketingTaskActionItemBizRelationEntity relation = new MarketingTaskActionItemBizRelationEntity();
            relation.setLogicCode(String.valueOf(IdUtil.createSnowflake(1, 1).nextId()));
            relation.setActionItemCode(itemCode);
            relation.setTaskBizCode(bizInfo.getLogicCode());
            relation.setActionRecordCode(actionRecordCode);
            relationList.add(relation);
        }
        return relationList;
    }

    private MarketingTaskActionExecRecordEntity buildTaskActionRecord(MarketingTaskActionBO actionBO,
                                                                      MarketingTaskTriggerRecordEntity triggerRecord,
                                                                      Date currentTime) {

        MarketingTaskActionExecRecordEntity actionRecord = new MarketingTaskActionExecRecordEntity();
        actionRecord.setLogicCode(String.valueOf(IdUtil.createSnowflake(1, 1).nextId()));
        actionRecord.setTaskCode(actionBO.getTaskCode());
        actionRecord.setTriggerCode(triggerRecord.getLogicCode());
        actionRecord.setActionExecStatus(TaskActionExecStatusEnum.WAIT.getCode());

        actionRecord.setActionCode(actionBO.getLogicCode());
        actionRecord.setActionOneLevelType(actionBO.getActionOneLevelType());
        actionRecord.setActionTwoLevelType(actionBO.getActionTwoLevelType());
        actionRecord.setActionOrder(actionBO.getActionOrder());

        actionRecord.setCreateTime(currentTime);
        actionRecord.setLastModifyTime(currentTime);
        return actionRecord;
    }

    @Override
    public void execAction(MarketingTaskEntity taskEntity, MarketingTaskTriggerRecordEntity triggerRecord) {
        String triggerCode = triggerRecord.getLogicCode();

        MarketingTaskConfigBO configBO = iMarketingTaskConfigLogic.acquireTaskConfig(taskEntity);
        List<MarketingTaskActionBO> taskActionConfigList = configBO.getTaskActionList();
        Map<String, MarketingTaskActionBO> taskActionConfigMap = taskActionConfigList.stream()
                .collect(Collectors.toMap(MarketingTaskActionBO::getLogicCode, Function.identity(), (k1, k2) -> k1));

        //取待执行的任务 多条可以并行执行
        List<MarketingTaskActionExecRecordEntity> waitExecActionList = iMarketingTaskActionExecRecordService
                .listByTriggerCodeAndStatus(triggerCode, TaskActionExecStatusEnum.WAIT);

        if (!CollectionUtils.isEmpty(waitExecActionList)) {
            waitExecActionList.sort(Comparator.comparing(MarketingTaskActionExecRecordEntity::getActionOrder));
            int minActionOrder = waitExecActionList.get(0).getActionOrder();
            for (MarketingTaskActionExecRecordEntity execRecord : waitExecActionList) {
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
                    handler.execAction(configBO, actionBO, execRecord);
                }
            }
        }
        MarketingTaskActionExecRecordEntity waitExecAction = iMarketingTaskActionExecRecordService.getOneByTriggerCodeAndStatus(triggerCode, TaskActionExecStatusEnum.WAIT);
        if (waitExecAction == null) {
            //修改触发状态，判断是否还有待执行和触发的动作，如果没有，执行更新
            iMarketingTaskTriggerRecordService.updateTriggerStatus(triggerCode,
                    TaskTriggerStatusEnum.WAIT_EXEC_ACTION.getCode(), TaskTriggerStatusEnum.FINISH.getCode());
        }
    }

    private boolean checkActionCanExec(MarketingTaskActionExecRecordEntity execRecord, MarketingTaskActionBO actionBO, Integer minActionOrder) {
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
        //查询需要回调的actionRecord
        String triggerCode = triggerRecord.getLogicCode();
        List<MarketingTaskActionExecRecordEntity> waitCallBackActionList = iMarketingTaskActionExecRecordService.listWaitCallbackAction(triggerCode);
        if (CollectionUtils.isEmpty(waitCallBackActionList)) {
            log.info("没有待回调的动作");
            return;
        }

        MarketingTaskConfigBO configBO = iMarketingTaskConfigLogic.acquireTaskConfig(taskEntity);
        MarketingTaskBO taskBO = configBO.getTaskBaseInfo();

        List<MarketingTaskActionBO> taskActionConfigList = configBO.getTaskActionList();
        Map<String, MarketingTaskActionBO> taskActionConfigMap = taskActionConfigList.stream()
                .collect(Collectors.toMap(MarketingTaskActionBO::getLogicCode, Function.identity(), (k1, k2) -> k1));

        for (MarketingTaskActionExecRecordEntity execRecord : waitCallBackActionList) {
            String actionCode = execRecord.getActionCode();
            MarketingTaskActionBO actionBO = taskActionConfigMap.get(actionCode);
            IMarketingTaskActionExecHandler handler = marketingTaskHandlerFactory.acquireActionExecHandler(actionBO);
            handler.callBackAction(taskBO, actionBO, execRecord);
        }

    }

    @Override
    public void compensateAction(MarketingTaskEntity taskEntity, MarketingTaskTriggerRecordEntity triggerRecord) {
        String triggerCode = triggerRecord.getLogicCode();
        List<MarketingTaskActionExecRecordEntity> waitCompensateActionList = iMarketingTaskActionExecRecordService.listWaitCompensateAction(triggerCode);
        if (CollectionUtils.isEmpty(waitCompensateActionList)) {
            log.info("没有待补偿的动作，triggerCode={}", triggerCode);
            return;
        }

        //根据triggerCode更新状态
        if (!checkTaskCanCompensate(taskEntity)) {
            boolean updateRet = iMarketingTaskActionExecItemService.updateExecStatusByTriggerCode(triggerCode, TaskActionItemExecStatusEnum.WAIT_COMPENSATE,
                    TaskActionItemExecStatusEnum.FINISH, "任务已失效，无需补偿");
            log.info("任务已失效，无需补偿，更新item状态，triggerCode={}，updateRet={}", triggerCode, updateRet);
            return;
        }

        MarketingTaskConfigBO configBO = iMarketingTaskConfigLogic.acquireTaskConfig(taskEntity);
        List<MarketingTaskActionBO> taskActionConfigList = configBO.getTaskActionList();
        Map<String, MarketingTaskActionBO> taskActionConfigMap = taskActionConfigList.stream()
                .collect(Collectors.toMap(MarketingTaskActionBO::getLogicCode, Function.identity(), (k1, k2) -> k1));

        for (MarketingTaskActionExecRecordEntity actionExecRecord : waitCompensateActionList) {
            String actionCode = actionExecRecord.getActionCode();
            MarketingTaskActionBO actionBO = taskActionConfigMap.get(actionCode);
            IMarketingTaskActionExecHandler handler = marketingTaskHandlerFactory.acquireActionExecHandler(actionBO);
            handler.compensateAction(configBO, actionBO, actionExecRecord);
        }
    }

    private boolean checkTaskCanCompensate(MarketingTaskEntity taskEntity) {
        if (!TaskStatusEnum.ENABLE.getCode().equals(taskEntity.getTaskStatus())) {
            return false;
        }
        Date currentTime = DateTimeUtil.currentTime();
        return DateTimeUtil.isTimeInRange(currentTime, taskEntity.getValidTimeStart(), taskEntity.getValidTimeEnd());
    }

}
