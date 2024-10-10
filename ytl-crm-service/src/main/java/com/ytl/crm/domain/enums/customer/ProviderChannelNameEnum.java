package com.ytl.crm.domain.enums.customer;


import com.ytl.crm.domain.enums.EnumWithCodeAndDesc;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProviderChannelNameEnum implements EnumWithCodeAndDesc<String> {

    GENERAL("GENERAL", "企微官方"),
    WS("WS", "微盛"),
    JUZI("JUZI", "句子互动");

    private final String code;
    private final String desc;
}
