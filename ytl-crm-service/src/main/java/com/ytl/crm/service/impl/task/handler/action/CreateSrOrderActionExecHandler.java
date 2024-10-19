package com.ytl.crm.service.impl.task.handler.action;

import com.google.common.collect.Lists;
import com.ytl.crm.domain.bo.task.exec.CreateSrOrderConfig;
import com.ytl.crm.domain.bo.task.exec.MarketingTaskActionBO;
import com.ytl.crm.domain.bo.task.exec.MarketingTaskBO;
import com.ytl.crm.domain.bo.task.exec.MarketingTaskConfigBO;
import com.ytl.crm.domain.entity.task.exec.MarketingTaskActionExecItemEntity;
import com.ytl.crm.domain.entity.task.exec.MarketingTaskActionExecRecordEntity;
import com.ytl.crm.domain.entity.task.exec.MarketingTaskBizInfoEntity;
import com.ytl.crm.domain.enums.common.YesOrNoEnum;
import com.ytl.crm.domain.enums.task.config.TaskActionOneLevelTypeEnum;
import com.ytl.crm.domain.enums.task.config.TaskActionTwoLevelTypeEnum;
import com.ytl.crm.domain.enums.task.exec.TaskActionExecStatusEnum;
import com.ytl.crm.domain.enums.task.exec.TaskActionItemExecStatusEnum;
import com.ytl.crm.domain.enums.task.ret.TaskActionItemFinalRetEnum;
import com.ytl.crm.utils.EnumQueryUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class CreateSrOrderActionExecHandler extends BaseActionExecHandler {


    private final Pair<String, String> EXEC_ACTION_FAIL_RET = Pair.of("", "创建SR单异常");

    @Override
    public boolean support(MarketingTaskActionBO actionBO) {
        return TaskActionOneLevelTypeEnum.CREATE_SR_ORDER.getCode().equals(actionBO.getActionOneLevelType());
    }

    @Override
    public void execAction(MarketingTaskConfigBO configBO, MarketingTaskActionBO actionBO, MarketingTaskActionExecRecordEntity actionExecRecord) {
        //1.获取待执行数据
        String actionRecordCode = actionExecRecord.getLogicCode();
        List<MarketingTaskActionExecItemEntity> waitExecItemList = iMarketingTaskActionExecItemService.listByExecStatus(actionRecordCode, TaskActionItemExecStatusEnum.WAIT_EXEC, YesOrNoEnum.NO.getCode());

        //2.遍历执行
        if (!CollectionUtils.isEmpty(waitExecItemList)) {
            //立单相关信息
            MarketingTaskBO taskBaseInfo = configBO.getTaskBaseInfo();
            String conditionValue = taskBaseInfo.getTriggerConditionValue();
            Map<String, CreateSrOrderConfig> createSrOrderConfigMap = marketingTaskApolloConfig.getCondiftionValueToConfigMap();
            CreateSrOrderConfig createSrOrderConfig = createSrOrderConfigMap.get(conditionValue);
            if (createSrOrderConfig == null) {
                log.error("未设置分群id对应的立单条件，conditionValue={}", conditionValue);
                throw new RuntimeException("未设置分群id对应的立单条件，conditionValue=" + conditionValue);
            }

            //sendMsgType
            TaskActionTwoLevelTypeEnum sendMsgType = resolveTaskActionSendMsgType(configBO);
            for (MarketingTaskActionExecItemEntity itemEntity : waitExecItemList) {
                try {
                    execOneActionItem(itemEntity, createSrOrderConfig, sendMsgType);
                } catch (Exception e) {
                    log.error("执行动作异常，itemLogicCode={}", itemEntity.getLogicCode(), e);
                    updateItemStatusAfterExec(itemEntity, EXEC_ACTION_FAIL_RET);
                }
            }
        }

        //3.判断是否还有待执行的数据，如果没有，进入已完成
        MarketingTaskActionExecItemEntity itemEntity = iMarketingTaskActionExecItemService.getOneByExecStatus(actionRecordCode, TaskActionItemExecStatusEnum.WAIT_EXEC, YesOrNoEnum.NO.getCode());
        if (itemEntity == null) {
            boolean updateRet = iMarketingTaskActionExecRecordService.updateExecStatus(actionRecordCode, TaskActionExecStatusEnum.WAIT, TaskActionExecStatusEnum.FINISH);
            log.info("更新动作执行记录状态，actionRecordCode={}，updateRet={}", actionRecordCode, updateRet);
        }
    }

    private static final List<String> SEND_MSG_TWO_LEVEL_TYPE = Lists.newArrayList(TaskActionTwoLevelTypeEnum.MSG_TO_GROUP_WS.getCode(), TaskActionTwoLevelTypeEnum.MSG_TO_CUSTOMER_WS.getCode());

    private TaskActionTwoLevelTypeEnum resolveTaskActionSendMsgType(MarketingTaskConfigBO configBO) {
        List<MarketingTaskActionBO> taskActionList = configBO.getTaskActionList();
        //目前取的第一个
        Optional<MarketingTaskActionBO> sendMsgActionOpt = taskActionList.stream().filter(item -> SEND_MSG_TWO_LEVEL_TYPE.contains(item.getActionTwoLevelType())).collect(Collectors.toList()).stream().findFirst();
        if (sendMsgActionOpt.isPresent()) {
            MarketingTaskActionBO actionBO = sendMsgActionOpt.get();
            return EnumQueryUtil.of(TaskActionTwoLevelTypeEnum.class).getByCode(actionBO.getActionTwoLevelType());
        }
        return null;
    }

    private void execOneActionItem(MarketingTaskActionExecItemEntity itemEntity, CreateSrOrderConfig createSrOrderConfig, TaskActionTwoLevelTypeEnum sendMsgType) {
        String itemCode = itemEntity.getLogicCode();
        //查询关联的业务数据
        List<MarketingTaskBizInfoEntity> bizInfoList = iMarketingTaskBizInfoService.listByItemCode(Collections.singletonList(itemCode));
        MarketingTaskBizInfoEntity bizInfo = bizInfoList.get(0);

//        //创建SR单
//        Pair<String, String> createRetPair = createSrCase(itemEntity, bizInfo, createSrOrderConfig, sendMsgType);


        //更新Item的状态
        updateItemStatusAfterExec(itemEntity, null);
    }

    private void updateItemStatusAfterExec(MarketingTaskActionExecItemEntity itemEntity, Pair<String, String> createRetPair) {
        //更新item的数据
        String caseCode = createRetPair.getLeft();
        String execMsg = createRetPair.getRight();
        //这里需要兼容已经存在单据的情况，
        boolean isSuccess = StringUtils.isNotBlank(caseCode);
        //根据执行结果，更新状态
        String logicCode = itemEntity.getLogicCode();
        if (isSuccess) {
            Date currentTime = new Date();
            boolean updateSuccessRet = iMarketingTaskActionExecItemService.updateItemAfterExecSuccess(logicCode, TaskActionItemExecStatusEnum.WAIT_EXEC, TaskActionItemExecStatusEnum.FINISH, caseCode, currentTime, TaskActionItemFinalRetEnum.SUCCESS);
            log.info("更新动作执行成功item状态，logicCode={},ret={}", logicCode, updateSuccessRet);
        } else {
            boolean updateFailRet = iMarketingTaskActionExecItemService.updateItemAfterExecFail(logicCode, TaskActionItemExecStatusEnum.WAIT_EXEC, TaskActionItemExecStatusEnum.FAIL, execMsg, TaskActionItemFinalRetEnum.FAIL);
            log.info("更新动作执行失败item状态，logicCode={},ret={}", logicCode, updateFailRet);
        }
    }


}
