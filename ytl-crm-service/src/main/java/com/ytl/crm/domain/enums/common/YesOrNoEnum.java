package com.ytl.crm.domain.enums.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum YesOrNoEnum {
    NO(0, "否"),
    YES(1, "是"),
    ;
    private final Integer code;

    private final String desc;

    public boolean equalsValue(Integer value) {
        return this.code.equals(value);
    }

}
