package com.ytl.crm.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Random;

@Getter
@AllArgsConstructor
public enum RedisKeyEnum {

    /**
     * 微盛accessToken
     */
    WS_ACCESS_TOKEN("WS_ACCESS_TOKEN", 2 * 60 * 60),


    WS_ZIROOM_ACCESS_TOKEN("WS_ACCESS_ZIROOM_TOKEN", 2 * 60 * 60),

    WECHAT_ACCESS_TOKEN("WX_ACCESS_TOKEN", 2 * 60 * 60);

    private static final String COLON = ":";

    /**
     * redis key
     */
    private String key;
    /**
     * redis key存活的时间，单位：秒
     */
    private long liveTime;

    /**
     * 构建redisKey
     *
     * @param values 值
     * @return {@link String}
     * @date 24/8/2022 下午6:29
     * @author hongjie
     */
    public String buildKey(String... values) {
        if (values == null) {
            return this.getKey();
        }
        StringBuilder sb = new StringBuilder();
        sb.append(this.getKey());
        for (String value : values) {
            sb.append(COLON).append(value);
        }
        return sb.toString();
    }

    /**
     * 计算缓存过期时间
     *
     * @param bound 随机范围
     * @return java.lang.Long
     * @author hongj
     * @date 13/9/2023 上午11:50
     */
    public Long genLiveTime(int bound) {
        int random = new Random().nextInt(bound);
        return liveTime + random;
    }

}