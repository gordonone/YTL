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

    @Value("${weChat.official.callback.token:dNnDKuTUwWR}")
    private String officialCallBackToken;

    @Value("${weChat.official.callback.aseKeyBase64:VUdDLUNSTS1aSVJPT00tV0VDSEFULUFCQyFAIyRYWlk}")
    private String officialCallBackAesKeyBase6;

    @Value("${weChat.official.callback.officialSupportEventMap:{'change_external_contact':['add_external_contact','edit_external_contact','del_external_contact','del_follow_user']}}")
    private Map<String, List<String>> officialSupportEventMap;
    

}
