package com.ytl.crm.domain.req.friend;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author 11911
 * @version 1.0
 * {@code @date} 2024/7/5 9:44
 */
@Data
public class WechatFriendRelationReq {

    /**
     * 合同
     */
    @NotNull(message = "合同编号不能为空")
    private String contractCode;

    /**
     * 渠道标识
     */
    @NotNull(message = "渠道标识不能为空")
    private String channelCode;

    /**
     * 合同类型，默认出房合同
     */
    private String contractType;

    /**
     * 用户标识，非必填
     */
    private String uid;

}
