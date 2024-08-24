package com.ytl.crm.domain.entity.common;

import lombok.Data;

/**
 * count结果
 *
 * @author hongj
 * @date 2/5/2023 下午4:11
 */
@Data
public class CountResult {

    /**
     * count的关键字
     */
    private String countKey;

    /**
     * count的值
     */
    private Long countValue;

}
