package com.ytl.crm.domain.enums.task.exec;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ActionResultEnum {
    EXEC_SUCCESS("EXEC_SUCCESS", "执行成功"),
    EXEC_FAIL("EXEC_FAIL", "执行失败"),
    ;
    private final String code;
    private final String desc;
}
