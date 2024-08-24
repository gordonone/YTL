package com.ytl.crm.domain.enums.task.exec;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TaskActionExecStatusEnum {

    INIT("INIT", "初始化"),
    WAIT_CALL_BACK("WAIT_CALL_BACK", "等待回调"),
    FINISH("FINISH", "执行结束"),
    ;

    private final String code;

    private final String desc;

}
