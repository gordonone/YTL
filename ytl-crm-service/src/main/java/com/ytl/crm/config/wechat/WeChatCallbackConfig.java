package com.ytl.crm.config.wechat;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Data
@Component
public class WeChatCallbackConfig {

    @Value("${weChat.official.callback.corpId:}")
    private String officialCallBackCorpId;

    @Value("${weChat.official.callback.token:}")
    private String officialCallBackToken;

    @Value("${weChat.official.callback.aseKeyBase64:}")
    private String officialCallBackAesKeyBase6;

    //@ApolloJsonValue("${weChat.official.callback.officialSupportEventMap:{}}")
    private Map<String, List<String>> officialSupportEventMap;

}
