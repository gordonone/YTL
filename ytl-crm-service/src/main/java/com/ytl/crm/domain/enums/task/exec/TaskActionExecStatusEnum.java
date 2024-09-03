package com.ytl.crm.domain.enums.task.exec;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TaskActionExecStatusEnum {

    WAIT("WAIT", "待执行"),
    FINISH("FINISH", "执行结束"),
    ;

    private final String code;

    private final String desc;

}
