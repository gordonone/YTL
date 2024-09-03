package com.ytl.crm.domain.enums.task.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TaskStatusEnum {
    ENABLE("ENABLE", "已启用"),
    DISABLED("DISABLED", "已禁用");

    //todo 按有效时间处理
    //EXPIRED("EXPIRED", "已到期"),
    ;
    private final String code;

    private final String desc;

}
