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

//    /**
//     * 欢迎语模版 - 加好友成功
//     * key对应ApplyCodeChannelEnum的code字段
//     * {@link com.ziroom.wechat.service.domain.enumerate.ApplyCodeChannelEnum}
//     */
//    @ApolloJsonValue("${ws.addFriend.success.greetingMsgMap:{}}")
//    private Map<String, List<GreetingMsgConfig>> addFriendGreetingMsgMap;
//
//    /**
//     * 欢迎语模版 - 加群成功
//     * key对应ApplyCodeChannelEnum的code字段
//     * {@link com.ziroom.wechat.service.domain.enumerate.ApplyCodeChannelEnum}
//     */
//    @ApolloJsonValue("${ws.createGroup.success.greetingMsgMap:{}}")
//    private Map<String, List<GreetingMsgConfig>> createGroupSuccessMsgMap;
//
//    @Value("${ws.greeting.bakKeeperDefaultName:小木助手}")
//    private String bakKeeperDefaultName;

}
