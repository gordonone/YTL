package com.ytl.crm.domain.enums.task.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TaskActionDependencyEnum {

    NONE("NONE", "无"),
    DEPEND_ON_BEFORE_ACTION("DEPEND_ON_BEFORE_ACTION", "依赖前序任务"),
    ;
    private final String code;

    private final String desc;


}
