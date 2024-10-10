package com.ytl.crm.event.wechat.model;


import com.ytl.crm.domain.enums.wechat.WechatEventTypeEnum;
import com.ytl.crm.domain.enums.wechat.WechatSourceEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class WeChatEvent {

    /**
     * 原始消息，单独存储
     */
    private String originMsg;

    /**
     * 用于关联最开始的接口调用
     */
    private String traceId;

    /**
     * event创建时间
     */
    private Date createTime;

    /**
     * 事件来源
     */
    private WechatSourceEnum source;

    /**
     * 事件来源
     */
    private WechatEventTypeEnum eventType;

}
