package com.ytl.crm.domain.enums.channel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum StatusEnum {
    NO_ENABLE(0,"未启用"),
    ENABLE(1,"已启用"),
    DISABLE(2,"已禁用");
    private Integer code;
    private String desc;
    public static String getDescByCode(Integer code){
        return Arrays.stream(values()).filter(item->item.code.equals(code)).map(item->item.desc).findFirst().orElse(null);
    }
}
