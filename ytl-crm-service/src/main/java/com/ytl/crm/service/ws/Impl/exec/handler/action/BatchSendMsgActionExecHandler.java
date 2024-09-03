package com.ytl.crm.service.ws.Impl.exec.handler.action;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;

import com.ytl.crm.common.exception.UgcCrmServiceException;
import com.ytl.crm.config.MarketingTaskApolloConfig;
import com.ytl.crm.consumer.WsConsumer;
import com.ytl.crm.domain.entity.task.exec.MarketingTaskActionExecItemEntity;
import com.ytl.crm.domain.entity.task.exec.MarketingTaskActionExecRecordEntity;
import com.ytl.crm.domain.entity.task.exec.MarketingTaskActionItemBizRelationEntity;
import com.ytl.crm.domain.entity.task.exec.MarketingTaskBizInfoEntity;
import com.ytl.crm.domain.enums.common.YesOrNoEnum;
import com.ytl.crm.domain.enums.task.config.*;
import com.ytl.crm.domain.enums.task.exec.*;
import com.ytl.crm.domain.enums.task.ret.TaskActionItemFinalRetEnum;
import com.ytl.crm.domain.req.ws.WsCorpCreateMsgTaskReq;
import com.ytl.crm.domain.req.ws.WsMsgTaskExecDetailQueryReq;
import com.ytl.crm.domain.resp.ws.WsBaseResponse;
import com.ytl.crm.domain.resp.ws.WsCorpCreateMsgTaskResp;
import com.ytl.crm.domain.resp.ws.WsMsgTaskExecDetail;
import com.ytl.crm.domain.task.exec.MarketingTaskActionBO;
import com.ytl.crm.domain.task.exec.MarketingTaskActionMaterialBO;
import com.ytl.crm.domain.task.exec.MarketingTaskBO;
import com.ytl.crm.domain.task.exec.MarketingTaskConfigBO;
import com.ytl.crm.help.WsConsumerHelper;
import com.ytl.crm.utils.DateTimeUtil;
import com.ytl.crm.utils.EnumQueryUtil;
import com.ytl.crm.utils.RetryUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
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

    private final Pair<String, String> EXEC_ACTION_FAIL_RET = Pair.of("", "创建群发任务异常");

    @Override
    public boolean support(MarketingTaskActionBO actionBO) {
        return TaskActionOneLevelTypeEnum.BATCH_SEND_MSG.getCode().equals(actionBO.getActionOneLevelType());
    }

    @Override
    public void execAction(MarketingTaskConfigBO configBO, MarketingTaskActionBO actionBO,
                           MarketingTaskActionExecRecordEntity actionExecRecord) {
        //1.获取待执行数据 - 非待补偿数据
        String actionRecordCode = actionExecRecord.getLogicCode();
        List<MarketingTaskActionExecItemEntity> waitExecItemList = iMarketingTaskActionExecItemService.listByExecStatus(actionRecordCode,
                TaskActionItemExecStatusEnum.WAIT_EXEC, YesOrNoEnum.NO.getCode());

        //2.遍历执行
        if (!CollectionUtils.isEmpty(waitExecItemList)) {
            for (MarketingTaskActionExecItemEntity itemEntity : waitExecItemList) {
                try {
                    execOneActionItem(configBO, actionBO, itemEntity);
                } catch (Exception e) {
                    log.error("执行动作异常，itemLogicCode={}", itemEntity.getLogicCode(), e);
                    updateItemStatusAfterExec(itemEntity, EXEC_ACTION_FAIL_RET);
                }
            }
        }

        //3.判断该动作是否还有待执行的数据，如果没有，任务状态进入待回调
        MarketingTaskActionExecItemEntity itemEntity = iMarketingTaskActionExecItemService.getOneByExecStatus(actionRecordCode,
                TaskActionItemExecStatusEnum.WAIT_EXEC, YesOrNoEnum.NO.getCode());
        if (itemEntity == null) {
            boolean updateRet = iMarketingTaskActionExecRecordService.updateExecStatus(actionRecordCode,
                    TaskActionExecStatusEnum.WAIT, TaskActionExecStatusEnum.FINISH);
            log.info("更新动作记录状态，actionRecordCode={}，updateRet={}", actionRecordCode, updateRet);
        }
    }

    private void execOneActionItem(MarketingTaskConfigBO configBO, MarketingTaskActionBO actionBO,
                                   MarketingTaskActionExecItemEntity itemEntity) {
        //查询关联的业务数据
        String itemCode = itemEntity.getLogicCode();
        List<MarketingTaskBizInfoEntity> bizInfoList = iMarketingTaskBizInfoService.listByItemCode(Collections.singletonList(itemCode));

        //创建群发任务
        MarketingTaskBO taskBO = configBO.getTaskBaseInfo();
        WsCorpCreateMsgTaskReq createTaskReq = buildCreatMsgSendTaskReq(taskBO, actionBO, itemEntity, bizInfoList);
        Pair<String, String> createTaskRet = createMsgSendTask(createTaskReq);

        //更新item的状态
        updateItemStatusAfterExec(itemEntity, createTaskRet);
    }

    private void updateItemStatusAfterExec(MarketingTaskActionExecItemEntity itemEntity, Pair<String, String> createTaskRet) {
        //判断是否执行成功
        String thirdTaskId = createTaskRet.getLeft();
        String execMsg = createTaskRet.getRight();
        boolean isSuccess = StringUtils.isNotBlank(thirdTaskId);

        //根据执行结果，更新状态
        String logicCode = itemEntity.getLogicCode();
        if (isSuccess) {
            Date currentTime = new Date();
            boolean updateSuccessRet = iMarketingTaskActionExecItemService.updateItemAfterExecSuccess(logicCode, TaskActionItemExecStatusEnum.WAIT_EXEC,
                    TaskActionItemExecStatusEnum.WAIT_CALL_BACK, thirdTaskId, currentTime, null);
            log.info("更新动作执行成功item状态，logicCode={},ret={}", logicCode, updateSuccessRet);
        } else {
            boolean updateFailRet = iMarketingTaskActionExecItemService.updateItemAfterExecFail(logicCode, TaskActionItemExecStatusEnum.WAIT_EXEC,
                    TaskActionItemExecStatusEnum.FAIL, execMsg, TaskActionItemFinalRetEnum.FAIL);
            log.info("更新动作执行失败item状态，logicCode={},ret={}", logicCode, updateFailRet);
        }
    }

    private WsCorpCreateMsgTaskReq buildCreatMsgSendTaskReq(MarketingTaskBO taskBO, MarketingTaskActionBO actionBO,
                                                            MarketingTaskActionExecItemEntity itemEntity,
                                                            List<MarketingTaskBizInfoEntity> bizInfoList) {
        String taskName = taskBO.getTaskName();
        //判断是发给客户群还是客户
        String actionTwoLevelType = actionBO.getActionTwoLevelType();
        String virtualKeeperThirdId = itemEntity.getVirtualKeeperThirdId();
        WsCorpCreateMsgTaskReq createReq = null;
        if (TaskActionTwoLevelTypeEnum.MSG_TO_GROUP_WS.getCode().equalsIgnoreCase(actionTwoLevelType)) {
            createReq = initGroupReq(virtualKeeperThirdId, bizInfoList);
        } else if (TaskActionTwoLevelTypeEnum.MSG_TO_CUSTOMER_WS.getCode().equalsIgnoreCase(actionTwoLevelType)) {
            createReq = initCustomerReq(virtualKeeperThirdId, bizInfoList);
        } else {
            log.info("不支持的动作二级类型，actionTwoLevelType={}", actionTwoLevelType);
            throw new UgcCrmServiceException("不支持的动作二级类型" + actionTwoLevelType);
        }

        //公共参数
        createReq.setTask_name(taskName);
        createReq.setCreate_user_id(virtualKeeperThirdId);
        Date todayTimeLimit = confirmSendTimeEnd(taskBO);
        createReq.setTask_end_time(todayTimeLimit.getTime() / 1000);
        createReq.setTime_config(2);

        //素材相关
        List<MarketingTaskActionMaterialBO> materialList = actionBO.getMaterialList();
        List<WsCorpCreateMsgTaskReq.MaterialSendDto> sendDtoList = Lists.newArrayListWithExpectedSize(materialList.size());
        for (MarketingTaskActionMaterialBO materialBO : materialList) {
            String materialType = materialBO.getMaterialType();
            if (TaskActionMaterialTypeEnum.TEXT.equalsCode(materialType)
                    && StringUtils.isBlank(materialBO.getMaterialId())) {
                //文本 && 未设置素材id - TODO 这里补充文案
                createReq.setText_content(materialBO.getMaterialContent());
            } else {
                TaskActionMaterialSendTypeEnum sendTypeEnum = EnumQueryUtil.of(TaskActionMaterialSendTypeEnum.class).getByCode(materialBO.getSendType());
                WsCorpCreateMsgTaskReq.MaterialSendDto materialSendDto = new WsCorpCreateMsgTaskReq.MaterialSendDto();
                materialSendDto.setMaterial_id(Long.parseLong(materialBO.getMaterialId()));
                materialSendDto.setSend_type(sendTypeEnum.getWsSendType());
                sendDtoList.add(materialSendDto);
            }
        }
        createReq.setMaterial_send_dto(sendDtoList);
        return createReq;
    }

    private Date confirmSendTimeEnd(MarketingTaskBO taskBO) {
        LocalTime actionTimeEnd = taskBO.getActionTimeEnd();
        if (actionTimeEnd != null) {
            LocalDate today = LocalDate.now();
            LocalDateTime sentTimeEnd = LocalDateTime.of(today, actionTimeEnd);
            return Date.from(sentTimeEnd.atZone(ZoneId.of("Asia/Shanghai")).toInstant());
        }

        String sendMsgTimeLimit = marketingTaskApolloConfig.getSendMsgTimeLimit();
        return DateTimeUtil.getTodayTimeLimit(sendMsgTimeLimit, DateTimeUtil.DateTimeUtilFormat.HH_mm_ss.getFormat());
    }

    private WsCorpCreateMsgTaskReq initGroupReq(String virtualKeeperThirdId, List<MarketingTaskBizInfoEntity> bizInfoList) {
        WsCorpCreateMsgTaskReq createReq = new WsCorpCreateMsgTaskReq();
        createReq.setType(11);
        createReq.setRange_filter_extra_type(2);
        //extra
        List<String> groupThirdIdList = bizInfoList.stream().map(MarketingTaskBizInfoEntity::getGroupThirdCode)
                .collect(Collectors.toList());
        WsCorpCreateMsgTaskReq.ExtraParam_11_2 extraParam = new WsCorpCreateMsgTaskReq.ExtraParam_11_2();
        extraParam.setUserid(virtualKeeperThirdId);
        extraParam.setChat_id(groupThirdIdList);

        WsCorpCreateMsgTaskReq.Extra<WsCorpCreateMsgTaskReq.ExtraParam_11_2> extraInfo = new WsCorpCreateMsgTaskReq.Extra<>();
        extraInfo.setParams(Collections.singletonList(extraParam));

        createReq.setExtra(JSON.toJSONString(extraInfo));
        return createReq;
    }

    private WsCorpCreateMsgTaskReq initCustomerReq(String virtualKeeperThirdId, List<MarketingTaskBizInfoEntity> bizInfoList) {
        WsCorpCreateMsgTaskReq createReq = new WsCorpCreateMsgTaskReq();
        createReq.setType(10);
        createReq.setRange_filter_extra_type(2);
        createReq.setTime_config(2);
        createReq.setSend_self(1);

        //extra
        List<String> customerThirdIdList = bizInfoList.stream().map(MarketingTaskBizInfoEntity::getCustomerThirdId)
                .collect(Collectors.toList());
        WsCorpCreateMsgTaskReq.ExtraParam_10 extraParam = new WsCorpCreateMsgTaskReq.ExtraParam_10();
        extraParam.setUserid(virtualKeeperThirdId);
        extraParam.setExternal_userid(customerThirdIdList);

        WsCorpCreateMsgTaskReq.Extra<WsCorpCreateMsgTaskReq.ExtraParam_10> extraInfo = new WsCorpCreateMsgTaskReq.Extra<>();
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
            taskId = createResp.getData().getTask_id();
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
    public void callBackAction(MarketingTaskBO taskBO, MarketingTaskActionBO actionBO, MarketingTaskActionExecRecordEntity actionExecRecord) {
        //找到待回调的数据
        String actionRecordCode = actionExecRecord.getLogicCode();
        //回调不区分是补偿数据还是非补偿数据
        List<MarketingTaskActionExecItemEntity> waitCallBackItemList = iMarketingTaskActionExecItemService
                .listByExecStatus(actionRecordCode, TaskActionItemExecStatusEnum.WAIT_CALL_BACK, null);
        if (CollectionUtils.isEmpty(waitCallBackItemList)) {
            return;
        }

        boolean taskCanCompensate = taskCanCompensate(taskBO);

        //执行回调
        for (MarketingTaskActionExecItemEntity itemEntity : waitCallBackItemList) {
            String logicCode = itemEntity.getLogicCode();
            try {
                doOneItemCallback(actionBO, itemEntity, taskCanCompensate);
            } catch (Exception e) {
                log.error("动作item回调异常，logicCode={}", logicCode, e);
                boolean updateRet = iMarketingTaskActionExecItemService.updateItemAfterCallBackFail(logicCode, TaskActionItemExecStatusEnum.WAIT_CALL_BACK,
                        TaskActionItemExecStatusEnum.FAIL, "回调异常", TaskActionItemFinalRetEnum.FAIL);
                log.info("更新动作回到异常结果，logicCode={},updateRet={}", logicCode, updateRet);
            }
        }
    }

    private boolean taskCanCompensate(MarketingTaskBO taskBO) {
        return TaskStatusEnum.ENABLE.getCode().equals(taskBO.getTaskStatus());
    }

    private void doOneItemCallback(MarketingTaskActionBO actionBO, MarketingTaskActionExecItemEntity itemEntity, boolean taskCanCompensate) {
        String thirdTaskId = itemEntity.getThirdTaskId();
        String virtualKeeperThirdId = itemEntity.getVirtualKeeperThirdId();
        //查询三方任务执行结果
        Pair<String, Date> execPair = queryThirdTaskExecDetail(thirdTaskId, virtualKeeperThirdId);

        String thirdTaskStatus = execPair.getLeft();
        Date sendTime = execPair.getRight();
        boolean thirdTaskSuccess = ThirdTaskExecStatusEnum.EXEC_SUCCESS.getCode().equalsIgnoreCase(thirdTaskStatus);

        //判断是否需要补偿
        TaskActionItemExecStatusEnum toExecStatus = TaskActionItemExecStatusEnum.FINISH;
        if (!thirdTaskSuccess && taskCanCompensate && checkNeedCompensate(itemEntity, actionBO)) {
            toExecStatus = TaskActionItemExecStatusEnum.WAIT_COMPENSATE;
        }

        //最终结果
        TaskActionItemFinalRetEnum finalRetEnum = thirdTaskSuccess ? TaskActionItemFinalRetEnum.SUCCESS : TaskActionItemFinalRetEnum.FAIL;
        String logicCode = itemEntity.getLogicCode();
        boolean updateRet = iMarketingTaskActionExecItemService.updateItemAfterCallBackSuccess(logicCode,
                TaskActionItemExecStatusEnum.WAIT_CALL_BACK, toExecStatus, thirdTaskStatus, sendTime, finalRetEnum);
        log.info("更新回调结果，logicCode={}，updateRet={}", logicCode, updateRet);
    }

    private boolean checkNeedCompensate(MarketingTaskActionExecItemEntity itemEntity,
                                        MarketingTaskActionBO actionBO) {
        //任务可补偿-明天能执行 && 三方未成功 && 且需要第二天执行 && 不是补偿任务时进行补偿，更新为WAIT_COMPENSATE，否则为FINISH
        return YesOrNoEnum.YES.equalsValue(actionBO.getIsTomorrowContinue())
                && checkItemNeedCompensate(itemEntity);
    }

    private boolean checkItemNeedCompensate(MarketingTaskActionExecItemEntity itemEntity) {
        //如果已经是补偿的任务，不进行补偿
        if (YesOrNoEnum.YES.equalsValue(itemEntity.getIsCompensate())) {
            return false;
        }
        //item的创建时间不能早于昨天，兼容回调执行超过当天的数据
        Date createTime = itemEntity.getCreateTime();
        Date yesterdayStart = DateTimeUtil.getTimeOfDayStart(DateTimeUtil.addDay(new Date(), -1));
        return createTime.after(yesterdayStart);
    }

    private Pair<String, Date> queryThirdTaskExecDetail(String thirdTaskId, String virtualKeeperThirdId) {
        WsMsgTaskExecDetail taskExecDetail = RetryUtil.execute("queryTaskExecDetail",
                () -> this.queryTaskExecDetail(thirdTaskId, virtualKeeperThirdId));
        String thirdTaskStatus = ThirdTaskExecStatusEnum.UNKNOWN.getCode();
        Date sendTime = null;
        if (taskExecDetail != null && !CollectionUtils.isEmpty(taskExecDetail.getRecords())) {
            List<WsMsgTaskExecDetail.WsMsgTaskExecRecord> records = taskExecDetail.getRecords();
            WsMsgTaskExecDetail.WsMsgTaskExecRecord wsMsgTaskExecRecord = records.get(0);
            thirdTaskStatus = ThirdTaskExecStatusEnum.queryByWsStatus(wsMsgTaskExecRecord.getSend_status()).getCode();
            //更新状态和时间
            Integer sendTimeSeconds = wsMsgTaskExecRecord.getSend_time();
            if (sendTimeSeconds != null) {
                sendTime = new Date(sendTimeSeconds * 1000L);
            }
        }
        return Pair.of(thirdTaskStatus, sendTime);
    }

    private WsMsgTaskExecDetail queryTaskExecDetail(String taskId, String virtualKeeperThirdId) {
        WsMsgTaskExecDetailQueryReq queryReq = new WsMsgTaskExecDetailQueryReq();
        queryReq.setQuery_user_id(virtualKeeperThirdId);
        queryReq.setUser_id(virtualKeeperThirdId);
        queryReq.setTask_id(taskId);
        String accessToken = wsConsumerHelper.acquireAccessToken();
        WsBaseResponse<WsMsgTaskExecDetail> queryResp = wsConsumer.queryTaskExecDetail(accessToken, queryReq);
        if (queryResp == null || !queryResp.isOk()) {
            return null;
        }
        return queryResp.getData();
    }

    @Override
    public void compensateAction(MarketingTaskConfigBO configBO, MarketingTaskActionBO actionBO,
                                 MarketingTaskActionExecRecordEntity actionExecRecord) {
        //找到待补偿的数据
        String actionRecordCode = actionExecRecord.getLogicCode();
        List<MarketingTaskActionExecItemEntity> waitCompensateItemlist = iMarketingTaskActionExecItemService
                .listByExecStatus(actionRecordCode, TaskActionItemExecStatusEnum.WAIT_COMPENSATE, YesOrNoEnum.NO.getCode());
        if (CollectionUtils.isEmpty(waitCompensateItemlist)) {
            return;
        }

        //过滤掉不需要补偿的数据
        for (MarketingTaskActionExecItemEntity itemEntity : waitCompensateItemlist) {
            compensateOneItem(configBO, itemEntity, actionBO);
        }
    }

    private void compensateOneItem(MarketingTaskConfigBO configBO, MarketingTaskActionExecItemEntity oldItemEntity, MarketingTaskActionBO actionBO) {
        //如果已存在，直接执行
        MarketingTaskActionExecItemEntity waitCompensateItem = iMarketingTaskActionExecItemService
                .getOneBySourceItemCode(oldItemEntity.getLogicCode());
        if (waitCompensateItem != null) {
            if (!TaskActionItemExecStatusEnum.WAIT_EXEC.getCode().equals(waitCompensateItem.getExecStatus())) {
                log.info("回调任务状态不是已执行，无需处理，itemCode={}", waitCompensateItem.getLogicCode());
                return;
            }
            //直接执行
            execOneActionItem(configBO, actionBO, waitCompensateItem);
        } else {
            //执行 & 保存
            compensateAndSaveItem(configBO, actionBO, oldItemEntity);
        }
    }

    private void compensateAndSaveItem(MarketingTaskConfigBO configBO, MarketingTaskActionBO actionBO,
                                       MarketingTaskActionExecItemEntity oldItemEntity) {
        //查询再查一下任务的执行状态
        String thirdTaskId = oldItemEntity.getThirdTaskId();
        String virtualKeeperThirdId = oldItemEntity.getVirtualKeeperThirdId();
        WsMsgTaskExecDetail taskExecDetail = RetryUtil.execute("queryTaskExecDetail",
                () -> this.queryTaskExecDetail(thirdTaskId, virtualKeeperThirdId));
        boolean thirdTaskHasExec = checkThirdTaskHasExec(taskExecDetail);
        //再查一遍结果，如果还是没执行，就重新生成一个任务，并让原任务失效
        String oldItemLogicCode = oldItemEntity.getLogicCode();
        if (thirdTaskHasExec) {
            WsMsgTaskExecDetail.WsMsgTaskExecRecord taskExecRecord = taskExecDetail.getRecords().get(0);
            String thirdTaskStatus = ThirdTaskExecStatusEnum.EXEC_SUCCESS.getCode();
            Date sendTime = null;
            if (taskExecRecord.getSend_time() != null) {
                sendTime = new Date(taskExecRecord.getSend_time() * 1000);
            }
            //相当于回调成功
            boolean updateRet = iMarketingTaskActionExecItemService.updateItemAfterCallBackSuccess(oldItemLogicCode,
                    TaskActionItemExecStatusEnum.WAIT_COMPENSATE, TaskActionItemExecStatusEnum.FINISH,
                    thirdTaskStatus, sendTime, TaskActionItemFinalRetEnum.SUCCESS);
            log.info("更新微盛任务执行结果，logicCode={}，updateRet={}", oldItemLogicCode, updateRet);
        } else {
            //1.创建新的
            MarketingTaskActionExecItemEntity newItemEntity = copyItemWhenCompensate(oldItemEntity);
            List<MarketingTaskActionItemBizRelationEntity> relationList = copyRelationWhenCompensate(oldItemLogicCode, newItemEntity.getLogicCode());
            boolean saveRet = iMarketingTaskActionExecItemService.saveWhenCompensate(oldItemEntity, newItemEntity, relationList);
            if (saveRet) {
                //2.执行任务动作
                execOneActionItem(configBO, actionBO, newItemEntity);
            }
        }
    }

    private List<MarketingTaskActionItemBizRelationEntity> copyRelationWhenCompensate(String oldItemCode, String newItemCode) {
        List<MarketingTaskActionItemBizRelationEntity> oldRelationList = actionItemBizRelationService.listByItemCode(oldItemCode);
        List<MarketingTaskActionItemBizRelationEntity> newRelationList = Lists.newArrayListWithExpectedSize(oldRelationList.size());
        for (MarketingTaskActionItemBizRelationEntity oldRelation : oldRelationList) {
            MarketingTaskActionItemBizRelationEntity newRelation = new MarketingTaskActionItemBizRelationEntity();
            String uuid = UUID.randomUUID().toString().replace("-", "");
            newRelation.setLogicCode(uuid);
            newRelation.setActionItemCode(newItemCode);
            newRelation.setTaskBizCode(oldRelation.getTaskBizCode());
            newRelation.setActionRecordCode(oldRelation.getActionRecordCode());
            newRelationList.add(newRelation);
        }
        return newRelationList;
    }

    private MarketingTaskActionExecItemEntity copyItemWhenCompensate(MarketingTaskActionExecItemEntity oldItem) {
        MarketingTaskActionExecItemEntity itemEntity = new MarketingTaskActionExecItemEntity();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        itemEntity.setLogicCode(uuid);
        itemEntity.setSourceItemCode(oldItem.getLogicCode());
        itemEntity.setIsCompensate(YesOrNoEnum.YES.getCode());
        itemEntity.setTaskCode(oldItem.getTaskCode());
        itemEntity.setTriggerCode(oldItem.getTriggerCode());
        itemEntity.setActionCode(oldItem.getActionCode());
        itemEntity.setActionRecordCode(oldItem.getActionRecordCode());
        itemEntity.setExecStatus(TaskActionItemExecStatusEnum.WAIT_EXEC.getCode());
        itemEntity.setFinalExecRet(TaskActionItemFinalRetEnum.INIT.getCode());
        itemEntity.setThirdTaskExecStatus(ThirdTaskExecStatusEnum.INIT.getCode());
        itemEntity.setVirtualKeeperId(oldItem.getVirtualKeeperId());
        itemEntity.setVirtualKeeperThirdId(oldItem.getVirtualKeeperThirdId());
        itemEntity.setVirtualKeeperName(oldItem.getVirtualKeeperName());
        return itemEntity;
    }

    private boolean checkThirdTaskHasExec(WsMsgTaskExecDetail taskExecDetail) {
        if (taskExecDetail == null || CollectionUtils.isEmpty(taskExecDetail.getRecords())) {
            return false;
        }
        WsMsgTaskExecDetail.WsMsgTaskExecRecord taskExecRecord = taskExecDetail.getRecords().get(0);
        return ThirdTaskExecStatusEnum.EXEC_SUCCESS.getWsStatus().equals(taskExecRecord.getSend_status());
    }


}