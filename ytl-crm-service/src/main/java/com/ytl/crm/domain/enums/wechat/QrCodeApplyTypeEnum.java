package com.ytl.crm.domain.enums.wechat;

import com.ytl.crm.domain.enums.EnumWithCodeAndDesc;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 二维码申请类型
 */
@Getter
@AllArgsConstructor
public enum QrCodeApplyTypeEnum implements EnumWithCodeAndDesc<String> {
    CHANNEL("CHANNEL", "渠道码"),
    CUSTOMER("CUSTOMER", "客户码"),
    ;
    private final String code;
    private final String desc;

}
