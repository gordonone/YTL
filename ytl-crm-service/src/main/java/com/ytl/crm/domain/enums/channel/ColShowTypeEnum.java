package com.ytl.crm.domain.enums.channel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ColShowTypeEnum {
    // 1 只展示列名称  2 展示列名称，字典code名称
    ONLY_NAME(1, "只展示列名称"),
    NAME_CODE(2, "展示列名称，字典code名称");

    private Integer code;
    private String desc;
}
