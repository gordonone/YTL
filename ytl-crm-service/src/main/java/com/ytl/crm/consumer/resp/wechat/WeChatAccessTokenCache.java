package com.ytl.crm.consumer.resp.wechat;

import lombok.Data;

@Data
public class WeChatAccessTokenCache {

    /**
     * 获取到的凭证，最长为512字节
     */
    private String accessToken;

    /**
     * 凭证的有效时间（秒）
     */
    private Long expiresIn;

    /**
     * 创建时间(时间戳)-秒
     */
    private Long createAt;

}
