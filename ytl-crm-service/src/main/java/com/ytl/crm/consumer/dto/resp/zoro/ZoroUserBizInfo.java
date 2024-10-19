package com.ytl.crm.consumer.dto.resp.zoro;

import lombok.Data;

@Data
public class ZoroUserBizInfo {

    /**
     * 客户uid
     */
    private String userId;

    /**
     * 合同号
     */
    private String contractCode;

    /**
     * 群号不一致无需执行
     */
    private String groupCode;


}
