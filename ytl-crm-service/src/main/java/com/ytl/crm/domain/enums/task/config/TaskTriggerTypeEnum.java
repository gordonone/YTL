package com.ytl.crm.domain.enums.task.config;

import com.ytl.crm.domain.enums.EnumWithCodeAndDesc;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TaskTriggerTypeEnum implements EnumWithCodeAndDesc<String> {
    LABEL("LABEL", "标签"), MOCK("MOCK", "Mock数据"),
    ;
    private final String code;

    private final String desc;
}
