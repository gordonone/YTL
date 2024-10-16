package com.ytl.crm.config;


import com.ytl.crm.config.wechat.WeChatCallbackConfig;
import com.ytl.crm.utils.wechat.crypt.AesException;
import com.ytl.crm.utils.wechat.crypt.WeChatBizMsgCryptHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WeChatConfig {

    @Bean("weChatBizMsgCryptHandler")
    public WeChatBizMsgCryptHandler cryptHandler(WeChatCallbackConfig config) throws AesException {
        return new WeChatBizMsgCryptHandler(
                config.getOfficialCallBackToken(),
                config.getOfficialCallBackAesKeyBase6(),
                config.getOfficialCallBackCorpId());
    }

}
