package com.ytl.crm.service.ws.impl.task;

import com.google.common.collect.Lists;
import com.ytl.crm.constants.CommonConstant;
import com.ytl.crm.domain.bo.task.common.TwoValueCountResult;
import com.ytl.crm.domain.entity.task.config.MarketingTaskActionEntity;
import com.ytl.crm.domain.entity.task.exec.MarketingTaskActionExecItemEntity;
import com.ytl.crm.domain.entity.task.exec.MarketingTaskActionExecRecordEntity;
import com.ytl.crm.domain.entity.task.exec.MarketingTaskActionItemBizRelationEntity;
import com.ytl.crm.domain.enums.task.config.TaskActionOneLevelTypeEnum;
import com.ytl.crm.domain.enums.task.config.TaskActionTwoLevelTypeEnum;
import com.ytl.crm.domain.enums.task.exec.ThirdTaskExecStatusEnum;
import com.ytl.crm.domain.req.exec.TaskActionExecResultItemListReq;
import com.ytl.crm.domain.req.exec.TaskActionExecResultSummaryQueryReq;
import com.ytl.crm.domain.resp.common.PageResp;
import com.ytl.crm.domain.resp.task.exec.SrCaseRelatedMsgActionInfo;
import com.ytl.crm.domain.resp.task.exec.TaskActionExecResultItem;
import com.ytl.crm.domain.resp.task.exec.TaskActionExecResultSummary;
import com.ytl.crm.domain.resp.task.exec.TaskActionExecResultSummaryPageInit;
import com.ytl.crm.service.ws.define.task.IMarketingTaskExecResultLogic;
import com.ytl.crm.service.ws.define.task.config.IMarketingTaskActionService;
import com.ytl.crm.service.ws.define.task.exec.IMarketingTaskActionExecItemService;
import com.ytl.crm.service.ws.define.task.exec.IMarketingTaskActionExecRecordService;
import com.ytl.crm.service.ws.define.task.exec.IMarketingTaskActionItemBizRelationService;
import com.ytl.crm.utils.EnumQueryUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MarketingTaskExecResultLogicImpl implements IMarketingTaskExecResultLogic {

    private final IMarketingTaskActionService iMarketingTaskActionService;
    private final IMarketingTaskActionExecRecordService iMarketingTaskActionExecRecordService;
    private final IMarketingTaskActionExecItemService iMarketingTaskActionExecItemService;
    private final IMarketingTaskActionItemBizRelationService iMarketingTaskActionItemBizRelationService;

    @Override
    public SrCaseRelatedMsgActionInfo queryCaseRelatedMsgAction(String caseCode) {
        List<MarketingTaskActionExecItemEntity> itemList = iMarketingTaskActionExecItemService.queryByThirdTaskId(caseCode);
        if (CollectionUtils.isEmpty(itemList)) {
            return null;
        }
        MarketingTaskActionExecItemEntity createCaseItem = itemList.get(0);
        String triggerCode = createCaseItem.getTriggerCode();

        //触发记录找发消息记录
        List<MarketingTaskActionExecRecordEntity> sendMsgActionRecordList = iMarketingTaskActionExecRecordService.listByActionOneType(triggerCode, TaskActionOneLevelTypeEnum.BATCH_SEND_MSG);
        if (CollectionUtils.isEmpty(sendMsgActionRecordList)) {
            return null;
        }

        //查询SR单的taskBizCode
        MarketingTaskActionItemBizRelationEntity createCaseRelation = iMarketingTaskActionItemBizRelationService.getOneByItemCode(createCaseItem.getLogicCode());
        String taskBizCode = createCaseRelation.getTaskBizCode();

        List<String> sendMsgActionRecordCodes = sendMsgActionRecordList.stream().map(MarketingTaskActionExecRecordEntity::getLogicCode).collect(Collectors.toList());
        List<MarketingTaskActionExecItemEntity> sendMsgItemList = iMarketingTaskActionExecItemService.queryByActionRecordCodeAndTaskBizCode(sendMsgActionRecordCodes, taskBizCode);
        if (CollectionUtils.isEmpty(sendMsgItemList)) {
            return null;
        }

        //优先找手动执行成功的数据
        MarketingTaskActionExecItemEntity sendMsgItem = sendMsgItemList.stream().filter(item -> ThirdTaskExecStatusEnum.EXEC_SUCCESS.getCode().equals(item.getThirdTaskExecStatus())).findFirst().orElse(sendMsgItemList.get(0));

        return SrCaseRelatedMsgActionInfo.builder().thirdTaskId(sendMsgItem.getThirdTaskId()).thirdTaskExecStatus(sendMsgItem.getThirdTaskExecStatus()).thirdTaskExecTime(sendMsgItem.getThirdTaskExecTime()).thirdTaskCreateTime(sendMsgItem.getThirdTaskCreateTime()).build();
    }

    @Override
    public TaskActionExecResultSummaryPageInit summaryPageInit(String taskCode) {
        List<MarketingTaskActionEntity> actionEntityList = iMarketingTaskActionService.listByTaskCode(taskCode);
        TaskActionExecResultSummaryPageInit pageInit = new TaskActionExecResultSummaryPageInit();
        if (!CollectionUtils.isEmpty(actionEntityList)) {
            List<TaskActionExecResultSummaryPageInit.ActionSimpleInfo> actionList = Lists.newArrayList();
            for (MarketingTaskActionEntity actionEntity : actionEntityList) {
                TaskActionExecResultSummaryPageInit.ActionSimpleInfo actionSimpleInfo = new TaskActionExecResultSummaryPageInit.ActionSimpleInfo();
                actionSimpleInfo.setActionCode(actionEntity.getLogicCode());
                actionSimpleInfo.setActionName(parseActionName(actionEntity));
                actionSimpleInfo.setActionOrder(actionEntity.getActionOrder());
                actionList.add(actionSimpleInfo);
            }
            actionList.sort(Comparator.comparing(TaskActionExecResultSummaryPageInit.ActionSimpleInfo::getActionOrder));
            pageInit.setActionList(actionList);
        }
        return pageInit;
    }

    private String parseActionName(MarketingTaskActionEntity actionEntity) {
        TaskActionOneLevelTypeEnum oneLevelType = EnumQueryUtil.of(TaskActionOneLevelTypeEnum.class).getByCode(actionEntity.getActionOneLevelType());
        TaskActionTwoLevelTypeEnum twoLevelType = EnumQueryUtil.of(TaskActionTwoLevelTypeEnum.class).getByCode(actionEntity.getActionTwoLevelType());
        return CommonConstant.LINE_JOINER.join(oneLevelType.getDesc(), twoLevelType.getDesc());
    }

    @Override
    public List<TaskActionExecResultSummary> queryTaskActionExecSummary(TaskActionExecResultSummaryQueryReq queryReq) {
        String taskCode = queryReq.getTaskCode();
        List<MarketingTaskActionEntity> actionConfigList = iMarketingTaskActionService.listByTaskCode(taskCode);
        if (CollectionUtils.isEmpty(actionConfigList)) {
            return Collections.emptyList();
        }

        List<String> actionCodes = actionConfigList.stream().map(MarketingTaskActionEntity::getLogicCode).collect(Collectors.toList());
        List<TwoValueCountResult> countRetList = iMarketingTaskActionExecItemService.countByExecFinalRet(actionCodes);
        Map<String, TwoValueCountResult> countRetMap = CollectionUtils.isEmpty(countRetList) ? Collections.emptyMap() : countRetList.stream().collect(Collectors.toMap(TwoValueCountResult::getCountKey, Function.identity(), (k1, k2) -> k1));
        List<TaskActionExecResultSummary> summaryListList = Lists.newArrayListWithExpectedSize(actionConfigList.size());
        for (MarketingTaskActionEntity actionEntity : actionConfigList) {
            String logicCode = actionEntity.getLogicCode();
            TwoValueCountResult oneCountResult = countRetMap.get(logicCode);
            Long successCount = oneCountResult == null ? 0 : oneCountResult.getCountValue1();
            Long failCount = oneCountResult == null ? 0 : oneCountResult.getCountValue2();

            TaskActionExecResultSummary summary = new TaskActionExecResultSummary();
            summary.setActionCode(actionEntity.getLogicCode());
            summary.setActionOrder(actionEntity.getActionOrder());
            summary.setActionName(parseActionName(actionEntity));
            summary.setSuccessCount(successCount);
            summary.setFailCount(failCount);

            summaryListList.add(summary);
        }
        return summaryListList;
    }

    @Override
    public PageResp<TaskActionExecResultItem> listActionItemExecResult(TaskActionExecResultItemListReq listReq) {
        return iMarketingTaskActionExecItemService.pageActionItemExecResult(listReq);
    }
}
