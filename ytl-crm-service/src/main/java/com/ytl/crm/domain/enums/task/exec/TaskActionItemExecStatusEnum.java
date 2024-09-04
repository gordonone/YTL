package com.ytl.crm.domain.enums.task.exec;

import com.ytl.crm.domain.enums.EnumWithCodeAndDesc;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TaskActionItemExecStatusEnum implements EnumWithCodeAndDesc<String> {

    INIT("INIT", "初始化"),
    WAIT_EXEC("WAIT_EXEC", "待执行"),
    WAIT_CALL_BACK("WAIT_CALL_BACK", "待回调"),
    WAIT_COMPENSATE("WAIT_COMPENSATE", "待补偿"),
    FINISH("FINISH", "执行完成"),
    FAIL("FAIL", "执行失败"),
    ;

    private final String code;

    private final String desc;

}
