package com.ytl.crm.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GenderEnum {
    UN_KNOWN(-1, "未知", "先生/女士"),
    MALE(1, "女", "女士"),
    FEMALE(2, "男", "先生"),
    ;

    /**
     * 这里code使用passport中的code
     */
    private final Integer code;

    private final String desc;

    private final String greetingDesc;

    public static GenderEnum getByCode(Integer code) {
        for (GenderEnum aEnum : GenderEnum.values()) {
            if (aEnum.getCode().equals(code)) {
                return aEnum;
            }
        }
        return UN_KNOWN;
    }
}
