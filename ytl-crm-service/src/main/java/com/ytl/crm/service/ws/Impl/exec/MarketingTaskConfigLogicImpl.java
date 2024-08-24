package com.ytl.crm.service.ws.Impl.exec;

import com.google.common.collect.Lists;
import com.ytl.crm.config.MarketingTaskApolloConfig;
import com.ytl.crm.domain.entity.task.config.MarketingTaskActionEntity;
import com.ytl.crm.domain.entity.task.config.MarketingTaskActionMaterialEntity;
import com.ytl.crm.domain.entity.task.config.MarketingTaskEntity;
import com.ytl.crm.domain.enums.task.config.TaskActionDependencyEnum;
import com.ytl.crm.domain.enums.task.config.TaskActionOneLevelTypeEnum;
import com.ytl.crm.domain.enums.task.config.TaskActionTwoLevelTypeEnum;
import com.ytl.crm.domain.enums.task.config.TaskTriggerTypeEnum;
import com.ytl.crm.domain.resp.task.config.resp.TaskConfigConstantResp;
import com.ytl.crm.domain.resp.task.config.resp.TaskExecutionActionType;
import com.ytl.crm.domain.task.config.MarketingTaskActionBO;
import com.ytl.crm.domain.task.config.MarketingTaskBO;
import com.ytl.crm.domain.task.config.MarketingTaskConfigBO;
import com.ytl.crm.domain.task.config.MarketingTaskStatusBO;
import com.ytl.crm.service.ws.define.exec.IMarketingTaskConfigLogic;
import com.ytl.crm.service.ws.define.exec.config.IMarketingTaskActionMaterialService;
import com.ytl.crm.service.ws.define.exec.config.IMarketingTaskActionService;
import com.ytl.crm.service.ws.define.exec.config.IMarketingTaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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
    @Transactional(rollbackFor = Exception.class)
    public boolean saveTaskConfig(MarketingTaskConfigBO configBO) {

        MarketingTaskEntity marketingTaskEntity = new MarketingTaskEntity();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        marketingTaskEntity.setLogicCode(uuid);

        MarketingTaskBO taskBO = configBO.getTaskBaseInfo();
        BeanUtils.copyProperties(taskBO, marketingTaskEntity);

        iMarketingTaskService.save(marketingTaskEntity);

        if (Objects.nonNull(marketingTaskEntity.getId())) {

            List<MarketingTaskActionMaterialEntity> taskActionMaterialList = Lists.newArrayList();
            List<MarketingTaskActionEntity> taskActionList = Lists.newArrayList();

            if (!CollectionUtils.isEmpty(configBO.getTaskActionList())) {

                for (MarketingTaskActionBO x : configBO.getTaskActionList()) {

                    MarketingTaskActionEntity marketingTaskActionEntity = new MarketingTaskActionEntity();
                    marketingTaskActionEntity.setTaskCode(marketingTaskEntity.getLogicCode());
                    uuid = UUID.randomUUID().toString().replace("-", "");
                    marketingTaskActionEntity.setLogicCode(uuid);
                    BeanUtils.copyProperties(marketingTaskActionEntity, x);

                    MarketingTaskActionMaterialEntity marketingTaskActionMaterialEntity = new MarketingTaskActionMaterialEntity();
                    marketingTaskActionMaterialEntity.setActionCode(marketingTaskActionEntity.getLogicCode());
                    marketingTaskActionMaterialEntity.setTaskCode(marketingTaskEntity.getLogicCode());
                    uuid = UUID.randomUUID().toString().replace("-", "");
                    marketingTaskActionMaterialEntity.setLogicCode(uuid);

                    BeanUtils.copyProperties(marketingTaskActionMaterialEntity, x);

                    taskActionList.add(marketingTaskActionEntity);
                    taskActionMaterialList.add(marketingTaskActionMaterialEntity);
                }

                iMarketingTaskActionService.saveBatch(taskActionList);
                iMarketingTaskActionMaterialService.saveBatch(taskActionMaterialList);

                return true;
            }
        }
        return false;
    }

    @Override
    public boolean updateTaskConfig(MarketingTaskStatusBO marketingTaskStatusBO) {

        //编辑任务状态
        return iMarketingTaskService.updateTaskStatus(marketingTaskStatusBO.getLogicCode(), marketingTaskStatusBO.getTaskStatus());
    }

    @Override
    public TaskConfigConstantResp queryTaskConstant() {

        TaskConfigConstantResp taskConfigConstantResp = new TaskConfigConstantResp();
//        //执行动作枚举封装
//        List<TaskExecutionActionType> executionActionTypeList = assembleTaskExecutionAction();
//        taskConfigConstantResp.setExecutionActionTypeList(executionActionTypeList);
//        //组装依赖关系
//        Map<String, String> taskActionDependencyMap = Arrays.stream(TaskActionDependencyEnum.values())
//                .collect(Collectors.toMap(TaskActionDependencyEnum::getCode, TaskActionDependencyEnum::getDesc));
//        taskConfigConstantResp.setTaskActionDependencyEnumMap(taskActionDependencyMap);
//        //项目类别
//        taskConfigConstantResp.setTaskProjectTypeEnumMap(marketingTaskApolloConfig.getProjectTypeMap());
//        //触发条件
//        Map<String, String> taskTriggerTypeEnumMap = Arrays.stream(TaskTriggerTypeEnum.values())
//                .collect(Collectors.toMap(TaskTriggerTypeEnum::getCode, TaskTriggerTypeEnum::getDesc));
//        taskConfigConstantResp.setTaskTriggerTypeEnumMap(taskTriggerTypeEnumMap);

        return taskConfigConstantResp;
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
