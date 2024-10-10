package com.ytl.crm.consumer.ws;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * 微盛相关
 *
 * @author hongj
 * @version 1.0
 * @date 2024/7/28
 * @since JDK 8.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WsConsumerHelper {

//    private final WsConsumerConfig wsConsumerConfig;
//    private final WsConsumer wsConsumer;
//    private final RedisHelper redisHelper;

    /**
     * 获取token
     */
    public String acquireAccessToken() {
//        RedisKeyEnum cacheEnum = RedisKeyEnum.WS_ACCESS_TOKEN;
//        String cacheKey = cacheEnum.buildKey();
//        //从缓存获取
//        String accessTokenStr = acquireTokenFromCache(cacheKey);
//
//        if (StringUtils.isBlank(accessTokenStr)) {
//            //从接口获取
//            WsAccessToken wsAccessToken = acquireTokenFromWs();
//            if (wsAccessToken == null) {
//                throw new RuntimeException("获取微盛接口accessToken异常");
//            }
//            accessTokenStr = wsAccessToken.getAccess_token();
//            redisHelper.set(cacheKey, JSON.toJSONString(wsAccessToken), cacheEnum.genLiveTime(10));
//        }
//        return accessTokenStr;
        return null;
    }

//    private String acquireTokenFromCache(String cacheKey) {
//        String cacheValue = StringUtils.EMPTY;
//        try {
//            cacheValue = (String) redisHelper.get(cacheKey);
//        } catch (Exception e) {
//            log.error("从redis获取token异常，cacheKey={}", cacheKey, e);
//        }
//        //判断缓存时候有效
//        return checkAndGetToken(cacheValue);
//    }
//
//    private String checkAndGetToken(String tokenCache) {
//        if (StringUtils.isBlank(tokenCache)) {
//            return StringUtils.EMPTY;
//        }
//        WsAccessToken wsAccessToken = JSON.parseObject(tokenCache, WsAccessToken.class);
//        Long expiresMills = (wsAccessToken.getCreate_at() + wsAccessToken.getExpires_in()) * 1000;
//        Long currentTimeMillis = System.currentTimeMillis();
//        Long expireMillRange = wsConsumerConfig.getAccessTokenExpireMillRange();
//        boolean needRefresh = (expiresMills - currentTimeMillis) < expireMillRange;
//        return needRefresh ? StringUtils.EMPTY : wsAccessToken.getAccess_token();
//    }
//
//    public WsAccessToken acquireTokenFromWs() {
//        String appId = wsConsumerConfig.getAccessTokenAppId();
//        String appSecret = wsConsumerConfig.getAccessTokenAppSecret();
//        WsBaseResponse<WsAccessToken> resp = wsConsumer.acquireAccessToken(appId, appSecret);
//        if (resp != null && resp.isOk() && resp.getData() != null) {
//            return resp.getData();
//        }
//        return null;
//    }


}
