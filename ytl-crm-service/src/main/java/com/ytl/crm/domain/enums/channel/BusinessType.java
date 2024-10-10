package com.ytl.crm.domain.enums.channel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum BusinessType {
    CHANNEL("channel","渠道"),
    OTHER("other","其他");
    private String code;
    private String desc;
}
