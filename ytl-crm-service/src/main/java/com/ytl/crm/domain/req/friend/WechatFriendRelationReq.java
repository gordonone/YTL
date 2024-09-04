package com.ytl.crm.domain.req.friend;

import lombok.Data;


/**
 * @author 11911
 * @version 1.0
 * {@code @date} 2024/7/5 9:44
 */
@Data
public class WechatFriendRelationReq {

    /**
     * 员工用户标识，必填
     */
    private String empUid;


    /**
     * 客户用户标识，必填
     */
    private String friendUid;

}
