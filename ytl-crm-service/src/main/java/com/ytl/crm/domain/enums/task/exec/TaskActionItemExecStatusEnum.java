package com.ytl.crm.domain.enums.task.exec;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TaskActionItemExecStatusEnum {

    INIT("INIT", "初始化"),
    FAIL("FAIL", "执行成功"),
    SUCCESS("SUCCESS", "执行失败"),
    ;

    private final String code;

    private final String desc;

}
