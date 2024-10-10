package com.ytl.crm.domain.enums.wechat;

import com.ziroom.ugc.crm.service.web.domain.enums.EnumWithCodeAndDesc;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WechatSourceEnum implements EnumWithCodeAndDesc<String> {
    OFFICIAL("OFFICIAL", "企微官方"),
    WEI_S("WEI_S", "微盛"),
    ;
    private final String code;
    private final String desc;

}
