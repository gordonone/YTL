package com.ytl.crm.domain.task.common;

import lombok.Data;

/**
 * 两个key的统计
 *
 * @author hongj
 * @date 3/7/2023 下午10:50
 */
@Data
public class TwoKeyCountResult<K1, K2> {

    /**
     * count的关键字1
     */
    private K1 countKey1;

    /**
     * count的关键字2
     */
    private K2 countKey2;

    /**
     * count的值
     */
    private Integer countValue;

}
