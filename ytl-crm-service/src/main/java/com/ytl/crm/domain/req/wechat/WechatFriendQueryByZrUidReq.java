package com.ytl.crm.domain.req.wechat;


import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class WechatFriendQueryByZrUidReq {
    @NotBlank(message = "uid不能为空")
    private String uid;
}
