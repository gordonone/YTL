package com.ytl.crm.domain.enums.task.exec;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TaskActionCallBackStatusEnum {

    NONE("NONE", "无需回调"),
    WAIT("WAIT", "等待回调"),
    FAIL("FAIL", "回调失败"),
    SUCCESS("SUCCESS", "回调成功"),
    ;

    private final String code;

    private final String desc;
}
