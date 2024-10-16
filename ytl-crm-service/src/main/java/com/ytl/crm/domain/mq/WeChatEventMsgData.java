package com.ytl.crm.domain.mq;


import com.ytl.crm.event.wechat.model.WeChatEvent;

public interface WeChatEventMsgData {
    WeChatEvent toEvent();
}
