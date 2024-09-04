package com.ytl.crm.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Data
@Component
public class WsApolloConfig {
    /**
     * 获取accessToken的appId
     */
    @Value("${ws.accessToken.appId:1150333988}")
    private String accessTokenAppId;

    /**
     * 获取accessToken的appSecret
     */
    @Value("${ws.accessToken.appSecret:vkGa0ZZCuupKBE0BeC}")
    private String accessTokenAppSecret;

    /**
     * 过期时间误差，误差范围内也视为过期 1 * 60 * 1000
     */
    @Value("${ws.accessToken.expireMillRange:60000}")
    private Long accessTokenExpireMillRange;

}
