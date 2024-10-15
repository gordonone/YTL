package com.ytl.crm.mq.model.wechat.official;


import com.ytl.crm.event.wechat.model.WeChatEvent;

public interface WeChatEventMsgData {
    WeChatEvent toEvent();
}
