package com.ytl.crm.config;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.ytl.crm.domain.enums.common.RedisKeyEnum;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {

    @Bean
    public Cache<String, String> wsCache() {
        return CacheBuilder.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(RedisKeyEnum.WS_ACCESS_TOKEN.genLiveTime(10), TimeUnit.SECONDS)
                .build();
    }


    @Bean
    public Cache<String, String> wxCache() {
        return CacheBuilder.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(RedisKeyEnum.WECHAT_ACCESS_TOKEN.genLiveTime(10), TimeUnit.SECONDS)
                .build();
    }
}
