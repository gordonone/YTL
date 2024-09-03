package com.ytl.crm.domain.enums.task.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TaskTriggerTypeEnum {
    LABEL("LABEL", "标签"),
    MOCK("MOCK", "Mock数据"),
    ;
    private final String code;

    private final String desc;
}
