package com.ytl.crm.domain.enums.task.exec;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TaskTriggerStatusEnum {

    WAIT_PULL_DATA("WAIT_PULL_DATA", "待拉取数据"),
    WAIT_CREATE_ACTION("WAIT_CREATE_ACTION", "待创建动作"),
    WAIT_EXEC_ACTION("WAIT_EXEC_ACTION", "待执行动作"),
    FINISH("FINISH", "触发完成"),
    FAIL("FAIL", "触发失败"),
    ;

    private final String code;

    private final String desc;

}
