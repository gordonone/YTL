package com.ytl.crm.consumer.dto.resp.wechat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class WeChatAccessTokenDTO extends WeChatBaseResp {

    /**
     * 获取到的凭证，最长为512字节
     */
    @JsonProperty(value = "access_token")
    private String accessToken;

    /**
     * 凭证的有效时间（秒）
     */
    @JsonProperty(value = "expires_in")
    private Long expiresIn;

    public WeChatAccessTokenCache buildCache() {
        WeChatAccessTokenCache cache = new WeChatAccessTokenCache();
        cache.setAccessToken(this.accessToken);
        cache.setExpiresIn(this.expiresIn);
        //考虑误差等
        long createMills = System.currentTimeMillis() - 5000;
        cache.setCreateAt(createMills / 1000);
        return cache;
    }
}
