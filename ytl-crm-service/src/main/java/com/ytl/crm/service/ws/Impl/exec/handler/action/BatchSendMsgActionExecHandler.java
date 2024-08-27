package com.ytl.crm.service.ws.Impl.exec.handler.action;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;

import com.ytl.crm.config.MarketingTaskApolloConfig;
import com.ytl.crm.consumer.WsConsumer;
import com.ytl.crm.domain.entity.task.exec.MarketingTaskActionExecItemEntity;
import com.ytl.crm.domain.entity.task.exec.MarketingTaskActionExecRecordEntity;
import com.ytl.crm.domain.entity.task.exec.MarketingTaskBizInfoEntity;
import com.ytl.crm.domain.enums.task.config.TaskActionMaterialSendTypeEnum;
import com.ytl.crm.domain.enums.task.config.TaskActionMaterialTypeEnum;
import com.ytl.crm.domain.enums.task.config.TaskActionOneLevelTypeEnum;
import com.ytl.crm.domain.enums.task.config.TaskActionTwoLevelTypeEnum;
import com.ytl.crm.domain.enums.task.exec.*;
import com.ytl.crm.domain.req.ws.WsCorpCreateMsgTaskReq;
import com.ytl.crm.domain.req.ws.WsMsgTaskExecDetailQueryReq;
import com.ytl.crm.domain.resp.ws.WsBaseResponse;
import com.ytl.crm.domain.resp.ws.WsCorpCreateMsgTaskResp;
import com.ytl.crm.domain.resp.ws.WsMsgTaskExecDetail;
import com.ytl.crm.domain.task.config.MarketingTaskActionBO;
import com.ytl.crm.domain.task.config.MarketingTaskActionMaterialBO;
import com.ytl.crm.domain.task.config.MarketingTaskBO;
import com.ytl.crm.domain.task.exec.TaskVirtualKeeperBO;
import com.ytl.crm.help.WsConsumerHelper;
import com.ytl.crm.utils.DateTimeUtil;
import com.ytl.crm.utils.RetryUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class BatchSendMsgActionExecHandler extends BaseActionExecHandler {

    @Resource
    private WsConsumer wsConsumer;
    @Resource
    private WsConsumerHelper wsConsumerHelper;
    @Resource
    private MarketingTaskApolloConfig marketingTaskApolloConfig;

    @Override
    public boolean support(MarketingTaskActionBO actionBO) {
        return TaskActionOneLevelTypeEnum.BATCH_SEND_MSG.getCode().equals(actionBO.getActionOneLevelType());
    }


    @Override
    public void execAction(MarketingTaskBO taskBaseInfo, MarketingTaskActionBO actionBO,
                           MarketingTaskActionExecRecordEntity actionExecRecord) {
        String triggerCode = actionExecRecord.getTriggerCode();
        List<String> virtuaKeeperlIdList = iMarketingTaskBizInfoService.queryBizVirtuaKeeperlIdList(triggerCode);
        if (CollectionUtils.isEmpty(virtuaKeeperlIdList)) {
            return;
        }

        //按照人员进行分组处理
        for (String virtualKeeperId : virtuaKeeperlIdList) {
            try {
                execOneKeeperAction(virtualKeeperId, actionBO, actionExecRecord);
            } catch (Exception e) {
                log.error("当前小木管家动作执行失败，virtualKeeperId={}", virtualKeeperId, e);
            }
        }

        //更新状态
        iMarketingTaskActionExecRecordService.updateActionRecordStatus(actionExecRecord.getLogicCode(),
                TaskActionExecStatusEnum.INIT.getCode(),
                TaskActionExecStatusEnum.WAIT_CALL_BACK.getCode());
    }

    private void execOneKeeperAction(String virtualKeeperId, MarketingTaskActionBO actionBO, MarketingTaskActionExecRecordEntity actionExecRecord) {
        String triggerCode = actionExecRecord.getTriggerCode();
        //获取业务数据
        List<MarketingTaskBizInfoEntity> validBizInfoList = iMarketingTaskBizInfoService.queryByVirtualKeeperId(triggerCode, virtualKeeperId);

        //初始化execItem
        super.initActionExecItem(actionExecRecord, validBizInfoList);

        //小木管家数据
        MarketingTaskBizInfoEntity oneBizInfo = validBizInfoList.get(0);
        TaskVirtualKeeperBO virtualKeeperBO = new TaskVirtualKeeperBO();
        virtualKeeperBO.setVirtualKeeperId(virtualKeeperId);
        virtualKeeperBO.setVirtualKeeperThirdId(oneBizInfo.getVirtualKeeperThirdId());
        virtualKeeperBO.setVirtualKeeperName(oneBizInfo.getVirtualKeeperName());

        //执行创建任务
        doExecCreateMsgTaskAction(actionExecRecord, virtualKeeperBO, actionBO, validBizInfoList);
    }

    private void doExecCreateMsgTaskAction(MarketingTaskActionExecRecordEntity actionExecRecord, TaskVirtualKeeperBO virtualKeeperBO,
                                           MarketingTaskActionBO actionBO, List<MarketingTaskBizInfoEntity> validBizInfoList) {
        String virtualKeeperId = virtualKeeperBO.getVirtualKeeperId();
        String actionRecordCode = actionExecRecord.getLogicCode();
        //获取init状态的Item
        List<MarketingTaskActionExecItemEntity> initItemList = iMarketingTaskActionExecItemService.queryKeeperInitItem(actionRecordCode, virtualKeeperId);
        if (CollectionUtils.isEmpty(initItemList)) {
            log.info("没有待执行的任务，直接返回，keeperVirtualId={}", virtualKeeperBO.getVirtualKeeperId());
            return;
        }

        //素材
        MarketingTaskActionMaterialBO materialBO = actionBO.getMaterialList().get(0);

        //需要发消息的业务数据
        Set<String> initItemBizCodeSet = initItemList.stream().map(MarketingTaskActionExecItemEntity::getTaskBizCode).collect(Collectors.toSet());
        List<MarketingTaskBizInfoEntity> bizInfoList = validBizInfoList.stream().filter(item -> initItemBizCodeSet.contains(item.getLogicCode()))
                .distinct().collect(Collectors.toList());

        //创建任务
        WsCorpCreateMsgTaskReq createMsgTaskReq = buildCreatMsgSendTaskReq(actionBO, virtualKeeperBO, materialBO, bizInfoList);
        Pair<String, String> createTaskRet = createMsgSendTask(createMsgTaskReq);

        //更新ExecItem的状态
        updateItemStatus(createTaskRet, initItemList);
    }

    private void updateItemStatus(Pair<String, String> createTaskRet, List<MarketingTaskActionExecItemEntity> initItemList) {
        String thirdTaskId = createTaskRet.getLeft();
        String execMsg = createTaskRet.getRight();
        boolean isSuccess = StringUtils.isNotBlank(thirdTaskId);
        //执行状态
        TaskActionItemExecStatusEnum execStatus = isSuccess ? TaskActionItemExecStatusEnum.SUCCESS : TaskActionItemExecStatusEnum.FAIL;
        //回调状态
        TaskActionCallBackStatusEnum callBackStatus = isSuccess ? TaskActionCallBackStatusEnum.WAIT : TaskActionCallBackStatusEnum.NONE;

        List<Long> idList = initItemList.stream().map(MarketingTaskActionExecItemEntity::getId).collect(Collectors.toList());
        boolean updateRet = iMarketingTaskActionExecItemService.updateItemAfterDoAction(idList, TaskActionItemExecStatusEnum.INIT.getCode(),
                execStatus.getCode(), thirdTaskId, execMsg, callBackStatus.getCode());
        log.info("更新item结果，logicCode={}，updateRet={}", JSON.toJSONString(idList), updateRet);
    }

    private WsCorpCreateMsgTaskReq buildCreatMsgSendTaskReq(MarketingTaskActionBO actionBO, TaskVirtualKeeperBO virtualKeeperBO,
                                                            MarketingTaskActionMaterialBO materialBO,
                                                            List<MarketingTaskBizInfoEntity> bizInfoList) {
        WsCorpCreateMsgTaskReq req = null;
        String actionTwoLevelType = actionBO.getActionTwoLevelType();
        if (TaskActionTwoLevelTypeEnum.MSG_TO_GROUP_WS.getCode().equalsIgnoreCase(actionTwoLevelType)) {
            List<String> groupThirdIdList = bizInfoList.stream().map(MarketingTaskBizInfoEntity::getGroupThirdCode)
                    .collect(Collectors.toList());
            req = buildCreatMsgSendTaskReqForGroup(virtualKeeperBO, materialBO, groupThirdIdList);
        } else if (TaskActionTwoLevelTypeEnum.MSG_TO_CUSTOMER_WS.getCode().equalsIgnoreCase(actionTwoLevelType)) {
            List<String> customerThirdIdList = bizInfoList.stream().map(MarketingTaskBizInfoEntity::getCustomerThirdId)
                    .collect(Collectors.toList());
            req = buildCreatMsgSendTaskReqForCustomer(virtualKeeperBO, materialBO, customerThirdIdList);
        }
        return req;
    }

    private WsCorpCreateMsgTaskReq buildCreatMsgSendTaskReqForCustomer(TaskVirtualKeeperBO virtualKeeperBO,
                                                                       MarketingTaskActionMaterialBO materialBO,
                                                                       List<String> customerThirdIdList) {
        WsCorpCreateMsgTaskReq createReq = new WsCorpCreateMsgTaskReq();
        createReq.setCreate_user_id(virtualKeeperBO.getVirtualKeeperThirdId());
        // 群发任务类型， 10:群发客户-员工一键发送（原企业群发客户）；
        createReq.setType(10);
        // 筛选条件扩展类型，1-员工与部门,2-员工与客户 或 员工与客户群.默认值:1
        createReq.setRange_filter_extra_type(2);
        // 自动提醒时间配置 (1代表15分钟，2代表30分钟，3代表1小时，4代表2小时。默认为2)
        createReq.setTime_config(2);
        createReq.setSend_self(1);
        //任务结束时间
        String sendMsgTimeLimit = marketingTaskApolloConfig.getSendMsgTimeLimit();
        Date todayTimeLimit = DateTimeUtil.getTodayTimeLimit(sendMsgTimeLimit, DateTimeUtil.DateTimeUtilFormat.HH_mm_ss.getFormat());
        createReq.setTask_end_time(todayTimeLimit.getTime() / 1000);

        //素材相关
        String materialType = materialBO.getMaterialType();
        if (TaskActionMaterialTypeEnum.TEXT.equalsCode(materialType)
                && StringUtils.isBlank(materialBO.getMaterialId())) {
            //文本 && 未设置素材id
            createReq.setText_content(materialBO.getMaterialContent());
        } else {
            TaskActionMaterialSendTypeEnum sendTypeEnum = TaskActionMaterialSendTypeEnum.valueOf(materialBO.getSendType());
            WsCorpCreateMsgTaskReq.MaterialSendDto materialSendDto = new WsCorpCreateMsgTaskReq.MaterialSendDto();
            materialSendDto.setMaterial_id(Long.parseLong(materialBO.getMaterialId()));
            materialSendDto.setSend_type(sendTypeEnum.getWsSendType());
            createReq.setMaterial_send_dto(Collections.singletonList(materialSendDto));
        }

        //extra
        WsCorpCreateMsgTaskReq.ExtraParam_10 extraParam = new WsCorpCreateMsgTaskReq.ExtraParam_10();
        extraParam.setUserid(virtualKeeperBO.getVirtualKeeperThirdId());
        extraParam.setExternal_userid(customerThirdIdList);
        WsCorpCreateMsgTaskReq.Extra<WsCorpCreateMsgTaskReq.ExtraParam_10> extraInfo = new WsCorpCreateMsgTaskReq.Extra<>();
        extraInfo.setParams(Collections.singletonList(extraParam));

        createReq.setExtra(JSON.toJSONString(extraInfo));

        return createReq;
    }

    private WsCorpCreateMsgTaskReq buildCreatMsgSendTaskReqForGroup(TaskVirtualKeeperBO virtualKeeperBO,
                                                                    MarketingTaskActionMaterialBO materialBO,
                                                                    List<String> groupThirdCodeList) {
        WsCorpCreateMsgTaskReq createReq = new WsCorpCreateMsgTaskReq();
        createReq.setCreate_user_id(virtualKeeperBO.getVirtualKeeperThirdId());
        // 群发任务类型，11:群发客户群-员工一键发送
        createReq.setType(11);
        // 筛选条件扩展类型，1-员工与部门,2-员工与客户 或 员工与客户群.默认值:1
        createReq.setRange_filter_extra_type(2);
        // 自动提醒时间配置 (1代表15分钟，2代表30分钟，3代表1小时，4代表2小时。默认为2)
        createReq.setTime_config(2);
        createReq.setSend_self(1);
        //任务结束时间
        String sendMsgTimeLimit = marketingTaskApolloConfig.getSendMsgTimeLimit();
        Date todayTimeLimit = DateTimeUtil.getTodayTimeLimit(sendMsgTimeLimit, DateTimeUtil.DateTimeUtilFormat.HH_mm_ss.getFormat());
        createReq.setTask_end_time(todayTimeLimit.getTime() / 1000);

        //素材相关
        String materialType = materialBO.getMaterialType();
        if (TaskActionMaterialTypeEnum.TEXT.equalsCode(materialType)
                && StringUtils.isBlank(materialBO.getMaterialId())) {
            //文本 && 未设置素材id - TODO 这里补充文案
            createReq.setText_content(materialBO.getMaterialContent());
        } else {
            TaskActionMaterialSendTypeEnum sendTypeEnum = TaskActionMaterialSendTypeEnum.valueOf(materialBO.getSendType());
            WsCorpCreateMsgTaskReq.MaterialSendDto materialSendDto = new WsCorpCreateMsgTaskReq.MaterialSendDto();
            materialSendDto.setMaterial_id(Long.parseLong(materialBO.getMaterialId()));
            materialSendDto.setSend_type(sendTypeEnum.getWsSendType());
            createReq.setMaterial_send_dto(Collections.singletonList(materialSendDto));
        }

        //extra
        WsCorpCreateMsgTaskReq.ExtraParam_11_2 extraParam = new WsCorpCreateMsgTaskReq.ExtraParam_11_2();
        extraParam.setUserid(virtualKeeperBO.getVirtualKeeperThirdId());
        extraParam.setChat_id(groupThirdCodeList);
        WsCorpCreateMsgTaskReq.Extra<WsCorpCreateMsgTaskReq.ExtraParam_11_2> extraInfo = new WsCorpCreateMsgTaskReq.Extra<>();
        extraInfo.setParams(Collections.singletonList(extraParam));

        createReq.setExtra(JSON.toJSONString(extraInfo));

        return createReq;
    }

    private Pair<String, String> createMsgSendTask(WsCorpCreateMsgTaskReq createReq) {
        //获取token
        String accessToken = wsConsumerHelper.acquireAccessToken();

        //调用创建任务接口
        String taskId = StringUtils.EMPTY;
        String failMsg = StringUtils.EMPTY;
        WsBaseResponse<WsCorpCreateMsgTaskResp> createResp = wsConsumer.corpCreateMsgTask(accessToken, createReq);
        if (createResp == null || !createResp.isOk() || createResp.getData() == null) {
            failMsg = createResp != null ? createResp.getMsg() : "调用微盛创建群发接口异常";
        } else {
            taskId = createResp.getData().getTaskId();
        }
        log.info("调用微盛创建群发接口结果，taskId={}，failMsg={}", taskId, failMsg);
        return Pair.of(taskId, failMsg);
    }

    /**
     * 主动回调微盛
     *
     * @param actionBO         动作bo
     * @param actionExecRecord 执行记录
     */
    @Override
    public void callBackAction(MarketingTaskActionBO actionBO, MarketingTaskActionExecRecordEntity actionExecRecord) {
        String triggerCode = actionExecRecord.getTriggerCode();
        List<String> virtuaKeeperlIdList = iMarketingTaskBizInfoService.queryBizVirtuaKeeperlIdList(triggerCode);
        if (CollectionUtils.isEmpty(virtuaKeeperlIdList)) {
            return;
        }

        //按照人员进行分组处理
        for (String virtualKeeperId : virtuaKeeperlIdList) {
            try {
                oneKeeperActionCallBack(actionExecRecord, virtualKeeperId, actionBO);
            } catch (Exception e) {
                log.error("当前小木管家动作执行失败，virtualKeeperId={}", virtualKeeperId);
            }
        }
    }

    private void oneKeeperActionCallBack(MarketingTaskActionExecRecordEntity actionExecRecord,
                                         String virtualKeeperId, MarketingTaskActionBO actionBO) {
        String actionRecordCode = actionExecRecord.getLogicCode();
        List<MarketingTaskActionExecItemEntity> waitCallBackItemList = iMarketingTaskActionExecItemService.queryWaitCallBackItem(actionRecordCode,
                virtualKeeperId);
        if (CollectionUtils.isEmpty(waitCallBackItemList)) {
            return;
        }

        //TODO 目前都是一个小木管家一个微盛的任务，所以取第一
        MarketingTaskActionExecItemEntity itemEntity = waitCallBackItemList.get(0);
        String taskId = itemEntity.getThirdTaskId();
        String virtualKeeperThirdId = itemEntity.getVirtualKeeperThirdId();

        WsMsgTaskExecDetail taskExecDetail = RetryUtil.execute("queryTaskExecDetail",
                () -> this.queryTaskExecDetail(taskId, virtualKeeperThirdId));
        String callBackStatus = TaskActionCallBackStatusEnum.FAIL.getCode();
        String thirdTaskStatus = ActionThirdTaskExecStatus.UNKNOWN.getCode();
        Date sendTime = null;
        if (taskExecDetail != null && !CollectionUtils.isEmpty(taskExecDetail.getRecords())) {
            callBackStatus = TaskActionCallBackStatusEnum.SUCCESS.getCode();
            List<WsMsgTaskExecDetail.WsMsgTaskExecRecord> records = taskExecDetail.getRecords();
            WsMsgTaskExecDetail.WsMsgTaskExecRecord wsMsgTaskExecRecord = records.get(0);
            thirdTaskStatus = ActionThirdTaskExecStatus.queryByWsStatus(wsMsgTaskExecRecord.getSend_status()).getCode();
            //更新状态和时间
            Integer sendTimeSeconds = wsMsgTaskExecRecord.getSend_time();
            if (sendTimeSeconds != null) {
                sendTime = new Date(sendTimeSeconds * 1000);
            }
        }
        List<Long> itemIdList = waitCallBackItemList.stream().map(MarketingTaskActionExecItemEntity::getId)
                .collect(Collectors.toList());
        boolean updateRet = iMarketingTaskActionExecItemService.batchUpdateCallBackRet(itemIdList, callBackStatus, thirdTaskStatus, sendTime);
        log.info("更新回调结果，actionRecordCode={}，updateRet={}", actionRecordCode, updateRet);
    }

    private WsMsgTaskExecDetail queryTaskExecDetail(String taskId, String virtualKeeperThirdId) {
        WsMsgTaskExecDetailQueryReq queryReq = buildQueryTaskExecDetailReq(taskId, virtualKeeperThirdId);
        String accessToken = wsConsumerHelper.acquireAccessToken();
        WsBaseResponse<WsMsgTaskExecDetail> queryResp = wsConsumer.queryTaskExecDetail(accessToken, queryReq);
        if (queryResp == null || !queryResp.isOk()) {
            return null;
        }
        return queryResp.getData();
    }

    private WsMsgTaskExecDetailQueryReq buildQueryTaskExecDetailReq(String taskId, String thirdKeeperVirtualId) {
        WsMsgTaskExecDetailQueryReq queryReq = new WsMsgTaskExecDetailQueryReq();
        queryReq.setQuery_user_id(thirdKeeperVirtualId);
        queryReq.setUser_id(thirdKeeperVirtualId);
        queryReq.setTask_id(taskId);
        return queryReq;
    }


    @Override
    public void compensateAction(MarketingTaskActionBO actionBO, MarketingTaskActionExecRecordEntity
            actionExecRecord) {
        //查找执行结果是未执行的数据
        String triggerCode = actionExecRecord.getTriggerCode();
        List<String> virtuaKeeperlIdList = iMarketingTaskBizInfoService.queryBizVirtuaKeeperlIdList(triggerCode);
        if (CollectionUtils.isEmpty(virtuaKeeperlIdList)) {
            return;
        }

        //按照人员进行分组处理
        for (String virtualKeeperId : virtuaKeeperlIdList) {
            try {
                oneKeeperActionCompensate(virtualKeeperId, actionBO, actionExecRecord);
            } catch (Exception e) {
                log.error("当前小木管家动作补偿，virtualKeeperId={}", virtualKeeperId);
            }
        }
        String actionRecordCode = actionExecRecord.getLogicCode();
        //更新任务补偿的状态
        iMarketingTaskActionExecRecordService.updateCompensateStatus(actionRecordCode, TaskActionCompensateStatus.WAIT_EXEC.getCode(),
                TaskActionCompensateStatus.WAIT_CALL_BACK.getCode());
    }

    private void oneKeeperActionCompensate(String virtualKeeperId, MarketingTaskActionBO actionBO,
                                           MarketingTaskActionExecRecordEntity actionExecRecord) {
        //查询需要重新执行的item
        String actionRecordCode = actionExecRecord.getLogicCode();
        List<MarketingTaskActionExecItemEntity> waitCompensateItemList = iMarketingTaskActionExecItemService
                .queryWaitCompensateItem(virtualKeeperId, actionRecordCode);
        if (CollectionUtils.isEmpty(waitCompensateItemList)) {
            log.info("没有需要补偿的数据，actionRecordCode={}，virtualKeeperId={}", actionRecordCode, virtualKeeperId);
            return;
        }

        //再查一遍结果，如果还是没执行，就重新发一个任务，并让原任务失效
        MarketingTaskActionExecItemEntity itemEntity = waitCompensateItemList.get(0);
        String taskId = itemEntity.getThirdTaskId();
        String virtualKeeperThirdId = itemEntity.getVirtualKeeperThirdId();

        WsMsgTaskExecDetail taskExecDetail = RetryUtil.execute("queryTaskExecDetail",
                () -> this.queryTaskExecDetail(taskId, virtualKeeperThirdId));
        boolean thirdTaskHasSend = checkThirdTaskHasSend(taskExecDetail);

        if (thirdTaskHasSend) {
            WsMsgTaskExecDetail.WsMsgTaskExecRecord taskExecRecord = taskExecDetail.getRecords().get(0);
            List<Long> itemIdList = waitCompensateItemList.stream().map(MarketingTaskActionExecItemEntity::getId)
                    .collect(Collectors.toList());
            String thirdTaskStatus = ActionThirdTaskExecStatus.EXEC_SUCCESS.getCode();
            Date sendTime = null;
            if (taskExecRecord.getSend_time() != null) {
                sendTime = new Date(taskExecRecord.getSend_time() * 1000);
            }
            boolean updateRet = iMarketingTaskActionExecItemService.batchUpdateSendStatus(itemIdList, thirdTaskStatus, sendTime);
            log.info("更新微盛任务执行结果，actionRecordCode={}，updateRet={}", actionRecordCode, updateRet);
        } else {
            //重新生成item
            List<MarketingTaskActionExecItemEntity> newItemList = copyFromOldItem(waitCompensateItemList);
            List<Long> oldItemIdList = waitCompensateItemList.stream().map(MarketingTaskActionExecItemEntity::getId)
                    .collect(Collectors.toList());
            iMarketingTaskActionExecItemService.batchSaveCompensateItem(oldItemIdList, newItemList);

            Set<String> bizCodeSet = waitCompensateItemList.stream().map(MarketingTaskActionExecItemEntity::getTaskBizCode).collect(Collectors.toSet());
            List<MarketingTaskBizInfoEntity> validBizInfoList = iMarketingTaskBizInfoService.batchQueryByLogicCode(bizCodeSet);

            //小木管家数据
            MarketingTaskBizInfoEntity oneBizInfo = validBizInfoList.get(0);
            TaskVirtualKeeperBO virtualKeeperBO = new TaskVirtualKeeperBO();
            virtualKeeperBO.setVirtualKeeperId(virtualKeeperId);
            virtualKeeperBO.setVirtualKeeperThirdId(oneBizInfo.getVirtualKeeperThirdId());
            virtualKeeperBO.setVirtualKeeperName(oneBizInfo.getVirtualKeeperName());

            //执行创建任务
            doExecCreateMsgTaskAction(actionExecRecord, virtualKeeperBO, actionBO, validBizInfoList);
        }
    }

    private List<MarketingTaskActionExecItemEntity> copyFromOldItem
            (List<MarketingTaskActionExecItemEntity> oldItemList) {
        List<MarketingTaskActionExecItemEntity> newItemList = Lists.newArrayListWithExpectedSize(oldItemList.size());
        for (MarketingTaskActionExecItemEntity oldItem : oldItemList) {
            MarketingTaskActionExecItemEntity itemEntity = new MarketingTaskActionExecItemEntity();
            itemEntity.setTaskCode(oldItem.getTaskCode());
            itemEntity.setTriggerCode(oldItem.getTriggerCode());
            itemEntity.setActionCode(oldItem.getActionCode());
            itemEntity.setActionRecordCode(oldItem.getActionRecordCode());

            itemEntity.setExecStatus(TaskActionItemExecStatusEnum.INIT.getCode());

            itemEntity.setTaskBizCode(oldItem.getTaskBizCode());
            itemEntity.setVirtualKeeperId(oldItem.getVirtualKeeperId());
            itemEntity.setVirtualKeeperThirdId(oldItem.getVirtualKeeperThirdId());
            itemEntity.setVirtualKeeperName(oldItem.getVirtualKeeperName());
            newItemList.add(itemEntity);
        }
        return newItemList;
    }

    private boolean checkThirdTaskHasSend(WsMsgTaskExecDetail taskExecDetail) {
        if (taskExecDetail == null || CollectionUtils.isEmpty(taskExecDetail.getRecords())) {
            return false;
        }
        WsMsgTaskExecDetail.WsMsgTaskExecRecord taskExecRecord = taskExecDetail.getRecords().get(0);
        return ActionThirdTaskExecStatus.EXEC_SUCCESS.getWsStatus().equals(taskExecRecord.getSend_status());
    }


}