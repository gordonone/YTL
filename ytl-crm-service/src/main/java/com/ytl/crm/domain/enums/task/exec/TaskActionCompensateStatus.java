package com.ytl.crm.domain.enums.task.exec;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TaskActionCompensateStatus {

    NONE("NONE", "无需补偿"),
    WAIT_EXEC("WAIT_EXEC", "等待执行"),
    WAIT_CALL_BACK("WAIT_CALL_BACK", "等待回调"),
    FINISH("FINISH", "执行完成"),
    ;

    private final String code;

    private final String desc;


}
