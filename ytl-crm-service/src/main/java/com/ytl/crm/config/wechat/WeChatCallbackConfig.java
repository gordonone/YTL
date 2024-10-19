package com.ytl.crm.config.wechat;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Data
@Component
public class WeChatCallbackConfig {

    @Value("${weChat.official.callback.corpId:wwc787a2ba27d7e130}")
    private String officialCallBackCorpId;

    //todo 需修改
    @Value("${weChat.official.callback.token:dNnDKuTUwWR}")
    private String officialCallBackToken;

    @Value("${weChat.official.callback.aseKeyBase64:VUdDLUNSTS1aSVJPT00tV0VDSEFULUFCQyFAIyRYWlk}")
    private String officialCallBackAesKeyBase6;

}
