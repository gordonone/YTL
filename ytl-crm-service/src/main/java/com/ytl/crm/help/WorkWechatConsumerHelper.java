package com.ytl.crm.help;

import com.alibaba.fastjson.JSON;
import com.google.common.cache.Cache;
import com.ytl.crm.consumer.WorkWeChatConsumer;
import com.ytl.crm.domain.enums.common.RedisKeyEnum;
import com.ytl.crm.domain.resp.work.WorkWechatTokenResp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author 11911
 * @version 1.0
 * {@code @date} 2024/7/29 16:30
 */
@Slf4j
@Component
public class WorkWechatConsumerHelper {

    @Resource
    private WorkWeChatConsumer weChatConsumer;

    @Resource
    private Cache<String, String> wxCache;

    /**
     * 企业ID
     */
    @Value("${wx.accessToken.appId:ww15316a4d063e467d}")
    private String accessTokenAppId;

    /**
     * 应用的凭证密钥
     */
    @Value("${wx.accessToken.appSecret:h-nB-SzzMxDL1a_GxpfiOuAb4YG2dnpiga2IiN0HBy4}")
    private String accessTokenAppSecret;

    /**
     * 过期时间误差，误差范围内也视为过期 1 * 60 * 1000
     */
    @Value("${wx.accessToken.expireSecondsRange:60000}")
    private Long accessTokenExpireMillRange;

    /**
     * 获取token
     */
    public String acquireAccessToken() {
        RedisKeyEnum cacheEnum = RedisKeyEnum.WECHAT_ACCESS_TOKEN;
        String cacheKey = cacheEnum.buildKey();
        String cacheValue = acquireTokenFromCache(cacheKey);
        String accessTokenStr = checkAndGetToken(cacheValue);
        if (StringUtils.isBlank(accessTokenStr)) {
            WorkWechatTokenResp wechatTokenResp = acquireTokenFromWx();
            if (wechatTokenResp == null) {
                throw new RuntimeException("获取企业微信接口accessToken异常");
            }
            wechatTokenResp.setCreateAt(System.currentTimeMillis());
            accessTokenStr = wechatTokenResp.getAccessToken();
            wxCache.put(cacheKey, JSON.toJSONString(wechatTokenResp));
        }
        return accessTokenStr;
    }

    private String checkAndGetToken(String tokenCache) {
        if (StringUtils.isBlank(tokenCache)) {
            return StringUtils.EMPTY;
        }
        WorkWechatTokenResp wechatTokenResp = JSON.parseObject(tokenCache, WorkWechatTokenResp.class);
        Long expiresMills = (wechatTokenResp.getCreateAt() + wechatTokenResp.getExpiresIn() * 1000);
        Long currentTimeMillis = System.currentTimeMillis();
        Long expireMillRange = accessTokenExpireMillRange;
        boolean needRefresh = (expiresMills - currentTimeMillis) < expireMillRange;
        return needRefresh ? StringUtils.EMPTY : wechatTokenResp.getAccessToken();
    }

    private String acquireTokenFromCache(String cacheKey) {
        String cacheValue = StringUtils.EMPTY;
        try {
            cacheValue = (String) wxCache.getIfPresent(cacheKey);
        } catch (Exception e) {
            log.error("从redis获取token异常，cacheKey={}", cacheKey, e);
        }
        return cacheValue;
    }


    public WorkWechatTokenResp acquireTokenFromWx() {
        WorkWechatTokenResp resp = weChatConsumer.getAccessToken(accessTokenAppId, accessTokenAppSecret);
        if (resp != null && resp.isOk() && StringUtils.isNotBlank(resp.getAccessToken())) {
            return resp;
        }
        return null;
    }

}
