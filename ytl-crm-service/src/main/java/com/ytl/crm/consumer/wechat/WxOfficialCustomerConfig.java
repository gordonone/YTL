package com.ytl.crm.consumer.wechat;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class WxOfficialCustomerConfig {

    /**
     * 获取accessToken的corpId
     */
    @Value("${wxOfficial.accessToken.corpId:ww15316a4d063e467d}")
    private String accessTokenCorpId;

    /**
     * 获取accessToken的corpSecret
     */
    @Value("${wxOfficial.accessToken.corpSecret:aRL5gO8j45LYk5kHLoljPs3xmT--8b37SkogdQzI8DI}")
    private String accessTokenCorpSecret;

    /**
     * token过期时间误差，10min（改成负的可以让token提前失效）
     */
    @Value("${wxOfficial.accessToken.expireMillRange:60000}")
    private Long accessTokenExpireMillRange;

}
