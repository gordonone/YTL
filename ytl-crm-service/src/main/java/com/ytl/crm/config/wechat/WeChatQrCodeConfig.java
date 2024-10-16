package com.ytl.crm.config.wechat;


import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Map;

@Data
@Component
public class WeChatQrCodeConfig {

    /**
     * 官方二维码过期时间
     */
    @Value("${weChat.official.qrCode.expireTime.dayInterval:5}")
    private Integer officialQrCodeExpireTimeDayInterval;

    //@ApolloJsonValue("${weChat.qrCode.applyTokenMap:{}}")
    private Map<String, String> weChatQrCodeApplyTokenMap;

    public boolean isValidToken(String scene, String token) {
        if (CollectionUtils.isEmpty(weChatQrCodeApplyTokenMap)) {
            return false;
        }
        String configToken = weChatQrCodeApplyTokenMap.get(scene);
        if (StringUtils.isBlank(configToken)) {
            return false;
        }
        return StringUtils.equalsIgnoreCase(configToken, token);
    }


}
