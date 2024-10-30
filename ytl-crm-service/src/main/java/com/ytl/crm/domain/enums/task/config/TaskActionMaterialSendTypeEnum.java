package com.ytl.crm.domain.enums.task.config;


import com.ytl.crm.domain.enums.EnumWithCodeAndDesc;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Deprecated
public enum TaskActionMaterialSendTypeEnum implements EnumWithCodeAndDesc<String> {
    TRAIL("TRAIL", "轨迹形式", 1),
    NORMAL("NORMAL", "普通形式", 2),
    ;
    private final String code;
    private final String desc;
    private final Integer wsSendType;
}
