package com.ytl.crm.domain.enums.task.exec;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TaskTriggerStatusEnum {

    INIT("INIT", "初始化"),
    BIZ_DATA_PULLED("BIZ_DATA_PULLED", "业务数据拉取完成"),
    WAIT_EXEC_ACTION("WAIT_EXEC_ACTION", "动作待执行"),
    TASK_FINISH("TASK_FINISH", "任务完成"),
    TASK_FAIL("TASK_FAIL", "触发失败"),
    ;

    private final String code;

    private final String desc;

}
