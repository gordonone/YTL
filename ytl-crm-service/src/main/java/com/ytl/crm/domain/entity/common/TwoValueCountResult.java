package com.ytl.crm.domain.entity.common;

import lombok.Data;

@Data
public class TwoValueCountResult {

    /**
     * count的关键字
     */
    private String countKey;

    /**
     * count的值1
     */
    private Long countValue1;

    /**
     * count的值2
     */
    private Long countValue2;

}
