package com.ytl.crm.service.ws.Impl.exec;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.ytl.crm.constants.CommonConstant;
import com.ytl.crm.domain.entity.common.TwoValueCountResult;
import com.ytl.crm.domain.entity.task.config.MarketingTaskActionEntity;
import com.ytl.crm.domain.entity.task.exec.MarketingTaskActionExecItemEntity;
import com.ytl.crm.domain.entity.task.exec.MarketingTaskActionExecRecordEntity;
import com.ytl.crm.domain.enums.task.config.TaskActionOneLevelTypeEnum;
import com.ytl.crm.domain.enums.task.config.TaskActionTwoLevelTypeEnum;
import com.ytl.crm.domain.enums.task.exec.ActionThirdTaskExecStatus;
import com.ytl.crm.domain.req.exec.TaskActionExecResultItemListReq;
import com.ytl.crm.domain.req.exec.TaskActionExecResultSummaryQueryReq;
import com.ytl.crm.domain.resp.common.PageResp;
import com.ytl.crm.domain.resp.task.exec.SrCaseRelatedMsgActionInfo;
import com.ytl.crm.domain.resp.task.exec.TaskActionExecResultItem;
import com.ytl.crm.domain.resp.task.exec.TaskActionExecResultSummary;
import com.ytl.crm.domain.resp.task.exec.TaskActionExecResultSummaryPageInit;
import com.ytl.crm.service.ws.define.exec.IMarketingTaskExecResultLogic;
import com.ytl.crm.service.ws.define.exec.config.IMarketingTaskActionService;
import com.ytl.crm.service.ws.define.exec.exec.IMarketingTaskActionExecItemService;
import com.ytl.crm.service.ws.define.exec.exec.IMarketingTaskActionExecRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MarketingTaskExecResultLogicImpl implements IMarketingTaskExecResultLogic {

    private final IMarketingTaskActionExecItemService iMarketingTaskActionExecItemService;
    private final IMarketingTaskActionExecRecordService iMarketingTaskActionExecRecordService;
    private final IMarketingTaskActionService iMarketingTaskActionService;

    @Override
    public SrCaseRelatedMsgActionInfo queryCaseRelatedMsgAction(String caseCode) {
        List<MarketingTaskActionExecItemEntity> itemList = iMarketingTaskActionExecItemService.queryByThirdTaskId(caseCode);
        if (CollectionUtils.isEmpty(itemList)) {
            return null;
        }
        MarketingTaskActionExecItemEntity createCaseItem = itemList.get(0);
        String triggerCode = createCaseItem.getTriggerCode();

        List<MarketingTaskActionExecRecordEntity> actionExecRecordList = iMarketingTaskActionExecRecordService.queryByTriggerCode(triggerCode);
        if (CollectionUtils.isEmpty(actionExecRecordList)) {
            return null;
        }
        List<String> sendMsgActionRecordCodeList = actionExecRecordList.stream()
                .filter(item -> TaskActionOneLevelTypeEnum.BATCH_SEND_MSG.getCode().equals(item.getActionTwoLevelType()))
                .map(MarketingTaskActionExecRecordEntity::getLogicCode).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(sendMsgActionRecordCodeList)) {
            return null;
        }
        String taskBizCode = createCaseItem.getTaskBizCode();
        List<MarketingTaskActionExecItemEntity> msgSendItemList = iMarketingTaskActionExecItemService.queryByExecCodeAndBizCode(sendMsgActionRecordCodeList, taskBizCode);
        if (CollectionUtils.isEmpty(msgSendItemList)) {
            return null;
        }
        MarketingTaskActionExecItemEntity msgSendItem = msgSendItemList.stream()
                .filter(item -> ActionThirdTaskExecStatus.EXEC_SUCCESS.getCode().equals(item.getThirdTaskExecStatus()))
                .findFirst().orElse(msgSendItemList.get(0));

        return SrCaseRelatedMsgActionInfo.builder()
                .thirdTaskId(msgSendItem.getThirdTaskId())
                .thirdTaskExecStatus(msgSendItem.getThirdTaskExecStatus())
                .thirdTaskExecTime(msgSendItem.getThirdTaskExecTime())
                .thirdTaskCreateTime(msgSendItem.getThirdTaskCreateTime())
                .build();
    }

    @Override
    public TaskActionExecResultSummaryPageInit summaryPageInit(String taskCode) {
        List<MarketingTaskActionEntity> actionEntityList = iMarketingTaskActionService.listByTaskCode(taskCode);
        TaskActionExecResultSummaryPageInit pageInit = new TaskActionExecResultSummaryPageInit();
        if (!CollectionUtils.isEmpty(actionEntityList)) {
            List<TaskActionExecResultSummaryPageInit.ActionSimpleInfo> actionList = Lists.newArrayList();
            pageInit.setActionList(actionList);
            for (MarketingTaskActionEntity actionEntity : actionEntityList) {
                TaskActionExecResultSummaryPageInit.ActionSimpleInfo actionSimpleInfo = new TaskActionExecResultSummaryPageInit.ActionSimpleInfo();
                actionSimpleInfo.setActionCode(actionEntity.getLogicCode());
                actionSimpleInfo.setActionName(parseActionName(actionEntity));
                actionSimpleInfo.setActionOrder(actionEntity.getActionOrder());
            }
        }
        return pageInit;
    }

    private String parseActionName(MarketingTaskActionEntity actionEntity) {
        TaskActionOneLevelTypeEnum oneLevelType = TaskActionOneLevelTypeEnum.valueOf(actionEntity.getActionOneLevelType());
        TaskActionTwoLevelTypeEnum twoLevelType = TaskActionTwoLevelTypeEnum.valueOf(actionEntity.getActionTwoLevelType());
        return CommonConstant.LINE_JOINER.join(oneLevelType.getDesc(), twoLevelType.getDesc());
    }


    @Override
    public List<TaskActionExecResultSummary> queryTaskActionExecSummary(TaskActionExecResultSummaryQueryReq queryReq) {
        String taskCode = queryReq.getTaskCode();
        List<MarketingTaskActionEntity> actionConfigList = iMarketingTaskActionService.listByTaskCode(taskCode);
        if (CollectionUtils.isEmpty(actionConfigList)) {
            return Collections.emptyList();
        }

        //分组 + 单条
        Set<String> batchActionCodes = Sets.newHashSet();
        Set<String> singleActionCodes = Sets.newHashSet();
        for (MarketingTaskActionEntity actionConfig : actionConfigList) {
            if (TaskActionOneLevelTypeEnum.BATCH_SEND_MSG.equalsCode(actionConfig.getActionOneLevelType())) {
                batchActionCodes.add(actionConfig.getLogicCode());
            } else {
                singleActionCodes.add(actionConfig.getLogicCode());
            }
        }

        List<TwoValueCountResult> batchActionCountList = iMarketingTaskActionExecItemService.countForBatchAction(batchActionCodes);
        List<TwoValueCountResult> singleCountList = iMarketingTaskActionExecItemService.countExecRet(singleActionCodes);
        Map<String, TwoValueCountResult> groupCountMap = CollectionUtils.isEmpty(batchActionCountList) ? Collections.emptyMap() :
                batchActionCountList.stream().collect(Collectors.toMap(TwoValueCountResult::getCountKey, Function.identity(), (k1, k2) -> k1));
        Map<String, TwoValueCountResult> singleCountMap = CollectionUtils.isEmpty(singleCountList) ? Collections.emptyMap() :
                singleCountList.stream().collect(Collectors.toMap(TwoValueCountResult::getCountKey, Function.identity(), (k1, k2) -> k1));

        List<TaskActionExecResultSummary> retList = Lists.newArrayListWithExpectedSize(actionConfigList.size());
        for (MarketingTaskActionEntity actionEntity : actionConfigList) {
            String logicCode = actionEntity.getLogicCode();
            TwoValueCountResult oneCountResult = batchActionCodes.contains(logicCode)
                    ? groupCountMap.get(logicCode) : singleCountMap.get(logicCode);

            TaskActionExecResultSummary summary = new TaskActionExecResultSummary();
            summary.setActionCode(actionEntity.getLogicCode());
            summary.setActionOrder(actionEntity.getActionOrder());
            summary.setActionName(parseActionName(actionEntity));
            Long successCount = oneCountResult == null ? 0 : oneCountResult.getCountValue1();
            Long failCount = oneCountResult == null ? 0 : oneCountResult.getCountValue2();
            summary.setSuccessCount(successCount);
            summary.setFailCount(failCount);

            retList.add(summary);
        }
        return retList;
    }

    @Override
    public PageResp<TaskActionExecResultItem> listActionItemExecResult(TaskActionExecResultItemListReq listReq) {
        String actionCode = listReq.getActionCode();
        MarketingTaskActionEntity taskAction = iMarketingTaskActionService.queryByLogicCode(actionCode);
        if (taskAction == null) {
            return PageResp.emptyResp();
        }

        //如果属于群发消息的类型 - 目前仅有微盛
        PageResp<TaskActionExecResultItem> resp = null;
        if (TaskActionOneLevelTypeEnum.BATCH_SEND_MSG.equalsCode(taskAction.getActionOneLevelType())) {
            resp = iMarketingTaskActionExecItemService.listBatchActionResultItem(listReq);
        } else {
            resp = iMarketingTaskActionExecItemService.listActionResultItem(listReq);
        }
        return resp;
    }
}
