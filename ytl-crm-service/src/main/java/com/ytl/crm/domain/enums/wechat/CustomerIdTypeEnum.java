package com.ytl.crm.domain.enums.wechat;

import com.ytl.crm.domain.enums.EnumWithCodeAndDesc;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 客户id类型
 */
@Getter
@AllArgsConstructor
public enum CustomerIdTypeEnum implements EnumWithCodeAndDesc<String> {

    UNKNOWN("UNKNOWN", "未知"),
    ZIROOM("ZIROOM", "自如客uid"),
    ;

    private final String code;
    private final String desc;

}
