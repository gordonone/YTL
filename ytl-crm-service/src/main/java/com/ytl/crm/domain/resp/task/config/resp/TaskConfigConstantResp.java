package com.ytl.crm.domain.resp.task.config.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskConfigConstantResp {

    /**
     * 执行动作类别
     */
    public List<TaskExecutionActionType> executionActionTypeList;

    /**
     * 依赖动作前置
     */
    public Map<String, String> taskActionDependencyEnumMap;

    /**
     * TaskTriggerTypeEnum 触发条件
     */
    public Map<String, String> taskTriggerTypeEnumMap;


    /**
     * 项目类型
     */
    public Map<String, String> taskProjectTypeEnumMap;

}
