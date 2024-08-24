package com.ytl.crm.domain.enums.task.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TaskActionOneLevelTypeEnum {
    BATCH_SEND_MSG("BATCH_SEND_MSG", "群发"),
    CREATE_SR_ORDER("CREATE_SR_ORDER", "创建六类单"),
    ;
    private final String code;

    private final String desc;

    public boolean equalsCode(String code) {
        return this.code.equalsIgnoreCase(code);
    }

}
