package com.ytl.crm.domain.enums.task.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TaskStatusEnum {
    PROGRESS("PROGRESS", "进行中"),
    DISABLED("DISABLED", "已禁用"),
    EXPIRED("EXPIRED", "已到期"),
    ;
    private final String code;

    private final String desc;

}
