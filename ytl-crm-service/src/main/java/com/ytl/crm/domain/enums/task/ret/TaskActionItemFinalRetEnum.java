package com.ytl.crm.domain.enums.task.ret;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TaskActionItemFinalRetEnum {
    INIT("INIT", "初始化"),
    SUCCESS("SUCCESS", "成功"),
    FAIL("FAIL", "失败"),
    ;
    private final String code;
    private final String desc;
}
