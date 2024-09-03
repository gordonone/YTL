package com.ytl.crm.service.ws.Impl.exec;

import com.google.common.collect.Lists;
import com.ytl.crm.config.MarketingTaskApolloConfig;
import com.ytl.crm.consumer.WsConsumer;
import com.ytl.crm.domain.entity.task.config.MarketingTaskActionEntity;
import com.ytl.crm.domain.entity.task.config.MarketingTaskActionMaterialEntity;
import com.ytl.crm.domain.entity.task.config.MarketingTaskEntity;
import com.ytl.crm.domain.enums.task.config.*;
import com.ytl.crm.domain.req.ws.WsMaterialReq;
import com.ytl.crm.domain.resp.task.config.resp.TaskConfigConstantResp;
import com.ytl.crm.domain.resp.task.config.resp.TaskExecutionActionType;
import com.ytl.crm.domain.resp.ws.WsBaseResponse;
import com.ytl.crm.domain.resp.ws.WsMaterialMediaResp;
import com.ytl.crm.domain.task.config.*;
import com.ytl.crm.domain.task.exec.MarketingTaskConfigBO;
import com.ytl.crm.help.WsConsumerHelper;
import com.ytl.crm.service.ws.define.exec.IMarketingTaskConfigLogic;
import com.ytl.crm.service.ws.define.exec.config.IMarketingTaskActionMaterialService;
import com.ytl.crm.service.ws.define.exec.config.IMarketingTaskActionService;
import com.ytl.crm.service.ws.define.exec.config.IMarketingTaskService;
import com.ytl.crm.utils.DateTimeUtil;
import com.ytl.crm.utils.EnumQueryUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MarketingTaskConfigLogicImpl implements IMarketingTaskConfigLogic {

    private final IMarketingTaskService iMarketingTaskService;
    private final IMarketingTaskActionService iMarketingTaskActionService;
    private final IMarketingTaskActionMaterialService iMarketingTaskActionMaterialService;
    private final MarketingTaskApolloConfig marketingTaskApolloConfig;
    private final WsConsumer wsConsumer;
    private final WsConsumerHelper wsConsumerHelper;

    @Override
    public MarketingTaskConfigBO acquireTaskConfig(String taskCode) {
        if (StringUtils.isBlank(taskCode)) {
            return null;
        }
        MarketingTaskEntity taskEntity = iMarketingTaskService.queryByTaskCode(taskCode);
        return acquireTaskConfig(taskEntity);
    }

    @Override
    public MarketingTaskConfigBO acquireTaskConfig(MarketingTaskEntity taskEntity) {
        if (taskEntity == null) {
            return null;
        }
        String taskCode = taskEntity.getLogicCode();
        List<MarketingTaskActionEntity> actionList = iMarketingTaskActionService.listByTaskCode(taskCode);
        List<MarketingTaskActionMaterialEntity> materialList = iMarketingTaskActionMaterialService.listByTaskCode(taskCode);
        return MarketingTaskConfigBO.assemblyBO(taskEntity, actionList, materialList);
    }

    @Override
    public MarketingTaskConfigBO queryTaskDetail(String taskCode) {

        MarketingTaskEntity marketingTaskEntity = iMarketingTaskService.queryByTaskCode(taskCode);
        if (marketingTaskEntity == null) {
            return null;
        }
        List<MarketingTaskActionEntity> actionList = iMarketingTaskActionService.listByTaskCode(taskCode);
        List<MarketingTaskActionMaterialEntity> materialList = iMarketingTaskActionMaterialService.listByTaskCode(taskCode);
        return MarketingTaskConfigBO.assemblyBO(marketingTaskEntity, actionList, materialList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveTaskConfig(MarketingTaskConfigAddBO configBO) {


        MarketingTaskEntity marketingTaskEntity = new MarketingTaskEntity();
        MarketingTaskAddBO taskBO = configBO.getTaskBaseInfo();
        BeanUtils.copyProperties(taskBO, marketingTaskEntity);

        String uuid = UUID.randomUUID().toString().replace("-", "");
        marketingTaskEntity.setLogicCode(uuid);
        marketingTaskEntity.setActionTimeStart(LocalTime.parse(taskBO.getActionTimeStartStr(), DateTimeFormatter.ofPattern("HH:mm:ss")));
        marketingTaskEntity.setActionTimeEnd(LocalTime.parse(taskBO.getActionTimeEndStr(), DateTimeFormatter.ofPattern("HH:mm:ss")));
        marketingTaskEntity.setTaskStatus(TaskStatusEnum.ENABLE.getCode());
        marketingTaskEntity.setCreateTime(DateTimeUtil.currentTime());

        marketingTaskEntity.setModifyUserCode(configBO.getTaskBaseInfo().getCreateUserCode());
        marketingTaskEntity.setModifyUserName(configBO.getTaskBaseInfo().getCreateUserName());
        marketingTaskEntity.setLastModifyTime(DateTimeUtil.currentTime());

        //查询重复
        if (iMarketingTaskService.countByTaskName(marketingTaskEntity.getTaskName()) >= 1) {
            return false;
        }

        iMarketingTaskService.save(marketingTaskEntity);

        if (Objects.nonNull(marketingTaskEntity.getId())) {

            List<MarketingTaskActionMaterialEntity> taskActionMaterialList = Lists.newArrayList();
            List<MarketingTaskActionEntity> taskActionList = Lists.newArrayList();


            for (MarketingTaskActionAddBO x : configBO.getTaskActionList()) {

                MarketingTaskActionEntity marketingTaskActionEntity = new MarketingTaskActionEntity();
                BeanUtils.copyProperties(x, marketingTaskActionEntity);
                marketingTaskActionEntity.setTaskCode(marketingTaskEntity.getLogicCode());
                uuid = UUID.randomUUID().toString().replace("-", "");
                marketingTaskActionEntity.setLogicCode(uuid);
                marketingTaskActionEntity.setCreateTime(marketingTaskEntity.getCreateTime());
                marketingTaskActionEntity.setCreateUserCode(marketingTaskEntity.getCreateUserCode());
                marketingTaskActionEntity.setCreateUserName(marketingTaskEntity.getCreateUserName());

                List<MarketingTaskActionMaterialAddBO> materialList = x.getMaterialList();
                if (!CollectionUtils.isEmpty(materialList)) {
                    for (MarketingTaskActionMaterialAddBO materialBO : materialList) {
                        MarketingTaskActionMaterialEntity marketingTaskActionMaterialEntity = new MarketingTaskActionMaterialEntity();
                        BeanUtils.copyProperties(materialBO, marketingTaskActionMaterialEntity);
                        marketingTaskActionMaterialEntity.setActionCode(marketingTaskActionEntity.getLogicCode());
                        marketingTaskActionMaterialEntity.setTaskCode(marketingTaskEntity.getLogicCode());
                        uuid = UUID.randomUUID().toString().replace("-", "");
                        marketingTaskActionMaterialEntity.setLogicCode(uuid);
                        marketingTaskActionMaterialEntity.setCreateTime(marketingTaskEntity.getCreateTime());
                        marketingTaskActionMaterialEntity.setCreateUserCode(marketingTaskEntity.getCreateUserCode());
                        marketingTaskActionMaterialEntity.setCreateUserName(marketingTaskEntity.getCreateUserName());
                        taskActionMaterialList.add(marketingTaskActionMaterialEntity);
                    }
                }
                taskActionList.add(marketingTaskActionEntity);
            }

            iMarketingTaskActionService.saveBatch(taskActionList);
            iMarketingTaskActionMaterialService.saveBatch(taskActionMaterialList);

            return true;
        }

        return false;
    }

    @Override
    public boolean updateTaskConfig(MarketingTaskStatusBO marketingTaskStatusBO) {

        //编辑任务状态
        return iMarketingTaskService.updateTaskStatus(marketingTaskStatusBO);
    }

    @Override
    public TaskConfigConstantResp queryTaskConstant() {

        TaskConfigConstantResp taskConfigConstantResp = new TaskConfigConstantResp();
        //执行动作枚举封装
        List<TaskExecutionActionType> executionActionTypeList = assembleTaskExecutionAction();
        taskConfigConstantResp.setExecutionActionTypeList(executionActionTypeList);
        //组装依赖关系
        Map<String, String> taskActionDependencyMap = Arrays.stream(TaskActionDependencyEnum.values()).collect(Collectors.toMap(TaskActionDependencyEnum::getCode, TaskActionDependencyEnum::getDesc));
        taskConfigConstantResp.setTaskActionDependencyEnumMap(taskActionDependencyMap);
        //项目类别
        // taskConfigConstantResp.setTaskProjectTypeEnumMap(marketingTaskApolloConfig.getProjectTypeMap());
        //触发条件
        Map<String, String> taskTriggerTypeEnumMap = Arrays.stream(TaskTriggerTypeEnum.values()).collect(Collectors.toMap(TaskTriggerTypeEnum::getCode, TaskTriggerTypeEnum::getDesc));
        taskConfigConstantResp.setTaskTriggerTypeEnumMap(taskTriggerTypeEnumMap);

        //素材类型
        Map<String, String> taskActionMaterialTypeMap = Arrays.stream(TaskActionMaterialTypeEnum.values()).collect(Collectors.toMap(TaskActionMaterialTypeEnum::getCode, TaskActionMaterialTypeEnum::getDesc));
        taskConfigConstantResp.setTaskActionMaterialTypeMap(taskActionMaterialTypeMap);

        //素材发送方式
        Map<String, String> taskActionMaterialSendTypeMap = Arrays.stream(TaskActionMaterialSendTypeEnum.values()).collect(Collectors.toMap(TaskActionMaterialSendTypeEnum::getCode, TaskActionMaterialSendTypeEnum::getDesc));
        taskConfigConstantResp.setTaskActionMaterialSendTypeMap(taskActionMaterialSendTypeMap);

        //任务状态
        Map<String, String> taskStatusMap = Arrays.stream(TaskStatusEnum.values()).collect(Collectors.toMap(TaskStatusEnum::getCode, TaskStatusEnum::getDesc));
        taskConfigConstantResp.setTaskStatusMap(taskStatusMap);

        return taskConfigConstantResp;
    }

    @Override
    public WsMaterialMediaResp.Media previewWsMaterialMedia(MarketingTaskMediaBO marketingTaskMediaBO) {

        //校验素材类型
        TaskActionMaterialTypeEnum materialTypeEnum = EnumQueryUtil.of(TaskActionMaterialTypeEnum.class).getByCode(marketingTaskMediaBO.getMaterialType());
        if (Objects.isNull(materialTypeEnum)) {
            return null;
        }

        if (TaskActionMaterialTypeEnum.TEXT.equalsCode(marketingTaskMediaBO.getMaterialType())) {
            return null;
        } else {
            //判断字符串是否数字
            boolean isMatch = marketingTaskMediaBO.getMaterialId().matches("\\d+");
            if (isMatch) {
                //获取token
                String accessToken = wsConsumerHelper.acquireAccessToken();
                WsMaterialReq wsMaterialReq = new WsMaterialReq();
                wsMaterialReq.setType(materialTypeEnum.getWsType());
                try {
                    wsMaterialReq.setMaterial_id(Long.parseLong(marketingTaskMediaBO.getMaterialId()));
                    wsMaterialReq.setCurrent_index(1);
                    wsMaterialReq.setPage_size(10);
                    WsBaseResponse<WsMaterialMediaResp> pageResult = wsConsumer.queryMediaList(accessToken, wsMaterialReq);
                    log.info("previewWsMaterialMedia,req,resp:{},{}", wsMaterialReq, pageResult);
                    if (Objects.nonNull(pageResult) && pageResult.isOk() && Objects.nonNull(pageResult.getData()) && pageResult.getData().getTotal() == 1) {
                        return pageResult.getData().getRecords().get(0);
                    }
                } catch (NumberFormatException e) {
                    log.error("素材编码格式异常：" + e.getMessage());
                    return null;
                }
            }
        }
        return null;
    }


    /**
     * 组装执行动作
     *
     * @return
     */
    public List<TaskExecutionActionType> assembleTaskExecutionAction() {

        List<TaskExecutionActionType> executionActionTypeList = new ArrayList<>();
        //遍历枚举
        for (TaskActionOneLevelTypeEnum taskActionOneLevelType : TaskActionOneLevelTypeEnum.values()) {

            TaskExecutionActionType taskExecutionActionType = new TaskExecutionActionType(taskActionOneLevelType.getCode(), taskActionOneLevelType.getDesc());

            List<TaskExecutionActionType> childrenCatalog = Lists.newArrayList();

            for (TaskActionTwoLevelTypeEnum taskActionTwoLevelType : TaskActionTwoLevelTypeEnum.values()) {

                if (taskActionTwoLevelType.getParentType().equals(taskActionOneLevelType)) {
                    TaskExecutionActionType taskChildExecutionActionType = new TaskExecutionActionType(taskActionTwoLevelType.getCode(), taskActionTwoLevelType.getDesc());
                    childrenCatalog.add(taskChildExecutionActionType);
                }
            }

            taskExecutionActionType.setChildrenCatalog(childrenCatalog);
            executionActionTypeList.add(taskExecutionActionType);
        }
        return executionActionTypeList;
    }


}