package com.ytl.crm.config.channel;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class ChannelQrCodeApolloConfig {

    @Value("${channel.qr.code.link.url:https://www.baidu.com}")
    private String channelQrCodeUrl;

}
