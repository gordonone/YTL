package com.ytl.crm.service.ws.help;


import com.alibaba.fastjson.JSON;
import com.google.common.cache.Cache;
import com.ytl.crm.config.WsApolloConfig;
import com.ytl.crm.service.ws.consumer.WsConsumer;
import com.ytl.crm.domain.enums.RedisKeyEnum;
import com.ytl.crm.domain.resp.ws.WsAccessToken;
import com.ytl.crm.domain.resp.ws.WsBaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class WsConsumerHelper {

    @Autowired
    private WsApolloConfig wsApolloConfig;
    @Autowired
    private Cache<String, String> wsCache;
    @Autowired
    private WsConsumer wsConsumer;

    /**
     * 获取token
     */
    public String acquireAccessToken() {
        RedisKeyEnum cacheEnum = RedisKeyEnum.WS_ACCESS_TOKEN;
        String cacheKey = cacheEnum.buildKey();
        String cacheValue = acquireTokenFromCache(cacheKey);
        String accessTokenStr = checkAndGetToken(cacheValue);
        if (StringUtils.isBlank(accessTokenStr)) {
            WsAccessToken wsAccessToken = acquireTokenFromWs();
            if (wsAccessToken == null) {
                throw new RuntimeException("获取微盛接口accessToken异常");
            }
            accessTokenStr = wsAccessToken.getAccess_token();
            wsCache.put(cacheKey, JSON.toJSONString(wsAccessToken));
        }
        return accessTokenStr;
    }


    private String checkAndGetToken(String tokenCache) {
        if (StringUtils.isBlank(tokenCache)) {
            return StringUtils.EMPTY;
        }
        WsAccessToken wsAccessToken = JSON.parseObject(tokenCache, WsAccessToken.class);
        Long expiresMills = (wsAccessToken.getCreate_at() + wsAccessToken.getExpires_in()) * 1000;
        Long currentTimeMillis = System.currentTimeMillis();
        Long expireMillRange = wsApolloConfig.getAccessTokenExpireMillRange();
        boolean needRefresh = (expiresMills - currentTimeMillis) < expireMillRange;
        return needRefresh ? StringUtils.EMPTY : wsAccessToken.getAccess_token();
    }

    private String acquireTokenFromCache(String cacheKey) {
        String cacheValue = StringUtils.EMPTY;
        try {
            cacheValue = wsCache.getIfPresent(cacheKey);
        } catch (Exception e) {
            log.error("从redis获取token异常，cacheKey={}", cacheKey, e);
        }
        return cacheValue;
    }


    public WsAccessToken acquireTokenFromWs() {
        String appId = wsApolloConfig.getAccessTokenAppId();
        String appSecret = wsApolloConfig.getAccessTokenAppSecret();
        WsBaseResponse<WsAccessToken> resp = wsConsumer.acquireAccessToken(appId, appSecret);
        if (resp != null && resp.isOk() && resp.getData() != null) {
            return resp.getData();
        }
        return null;
    }

}
