package com.ytl.crm.domain.req.wechat;


import lombok.Data;

import javax.validation.constraints.NotNull;


@Data
public class WechatFriendQueryByZrUidReq {

    @NotNull(message = "uid不能为空")
    private String uid;
}
