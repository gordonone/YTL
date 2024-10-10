package com.ytl.crm.domain.enums.wechat;

import com.ytl.crm.domain.enums.EnumWithCodeAndDesc;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum CustomerDelType implements EnumWithCodeAndDesc<String> {
    EMP_FIRST("EMP_FIRST", "员工删除"),
    CUSTOMER_FIRST("CUSTOMER_FIRST", "客户主动删除"),
    ;
    private final String code;
    private final String desc;
}
