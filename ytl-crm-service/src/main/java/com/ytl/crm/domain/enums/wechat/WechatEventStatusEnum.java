package com.ytl.crm.domain.enums.wechat;

import com.ytl.crm.domain.enums.EnumWithCodeAndDesc;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WechatEventStatusEnum implements EnumWithCodeAndDesc<String> {
    INIT("INIT", "初始化"),
    IGNORE("IGNORE", "无需处理"),
    FAIL("FAIL", "处理失败"),
    SUCCESS("SUCCESS", "处理成功"),
    ;
    private final String code;
    private final String desc;
}
