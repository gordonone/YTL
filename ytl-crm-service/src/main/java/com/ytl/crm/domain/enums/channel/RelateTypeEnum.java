package com.ytl.crm.domain.enums.channel;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RelateTypeEnum {
    CHANNEL_ALLOCATE_STRATEGY(1, "渠道分配策略"),
    CUSTOMER_SOURCE_ALLOCATE_RULE(2, "客源分配规则");

    private final Integer code;
    private final String desc;
}
