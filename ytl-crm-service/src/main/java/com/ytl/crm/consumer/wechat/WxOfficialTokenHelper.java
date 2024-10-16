package com.ytl.crm.consumer.wechat;

import com.alibaba.fastjson.JSON;
import com.google.common.cache.Cache;
import com.ytl.crm.config.CacheConfig;
import com.ytl.crm.consumer.resp.wechat.WeChatAccessTokenCache;
import com.ytl.crm.consumer.resp.wechat.WeChatAccessTokenDTO;
import com.ytl.crm.domain.enums.common.RedisKeyEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WxOfficialTokenHelper {

    private final WxOfficialCustomerConfig wxOfficialCustomerConfig;
    private final Cache<String, String> wxCache;
    private final WxOfficialConsumer wxOfficialConsumer;

    /**
     * 获取token
     */
    public String acquireAccessToken() {
        RedisKeyEnum cacheEnum = RedisKeyEnum.WX_OFFICIAL_ACCESS_TOKEN;
        String cacheKey = cacheEnum.buildKey();
        //从缓存获取
        String accessTokenStr = acquireTokenFromCache(cacheKey);
        if (StringUtils.isBlank(accessTokenStr)) {
            //从接口获取
            WeChatAccessTokenDTO weChatAccessToken = acquireTokenFromWx();
            accessTokenStr = weChatAccessToken.getAccessToken();
            WeChatAccessTokenCache tokenCache = weChatAccessToken.buildCache();
            wxCache.put(cacheKey, JSON.toJSONString(tokenCache));
        }
        return accessTokenStr;
    }

    public void clearAccessToken() {
        RedisKeyEnum cacheEnum = RedisKeyEnum.WX_OFFICIAL_ACCESS_TOKEN;
        String cacheKey = cacheEnum.buildKey();
        wxCache.invalidate(cacheKey);
    }

    private String acquireTokenFromCache(String cacheKey) {
        String cacheValue = StringUtils.EMPTY;
        try {
            cacheValue = wxCache.getIfPresent(cacheKey);
        } catch (Exception e) {
            log.error("从redis获取token异常，cacheKey={}", cacheKey, e);
        }
        //判断缓存时候有效
        return checkAndGetToken(cacheValue);
    }

    private String checkAndGetToken(String cacheStr) {
        if (StringUtils.isBlank(cacheStr)) {
            return StringUtils.EMPTY;
        }
        WeChatAccessTokenCache tokenCache = JSON.parseObject(cacheStr, WeChatAccessTokenCache.class);
        Long expiresMills = (tokenCache.getCreateAt() + tokenCache.getExpiresIn()) * 1000;
        Long currentTimeMillis = System.currentTimeMillis();
        Long expireMillRange = wxOfficialCustomerConfig.getAccessTokenExpireMillRange();
        boolean needRefresh = (expiresMills - currentTimeMillis) < expireMillRange;
        return needRefresh ? StringUtils.EMPTY : tokenCache.getAccessToken();
    }

    private WeChatAccessTokenDTO acquireTokenFromWx() {
        String corpId = wxOfficialCustomerConfig.getAccessTokenCorpId();
        String corpSecret = wxOfficialCustomerConfig.getAccessTokenCorpSecret();
        WeChatAccessTokenDTO resp = wxOfficialConsumer.acquireAccessToken(corpId, corpSecret);
        WxOfficialRespCheckUtil.checkResp(resp);
        return resp;
    }
}
