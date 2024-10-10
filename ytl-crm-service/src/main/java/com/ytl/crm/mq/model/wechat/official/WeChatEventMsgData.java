package com.ytl.crm.mq.model.wechat.official;

import com.ziroom.ugc.crm.service.web.event.wechat.model.WeChatEvent;

public interface WeChatEventMsgData {
    WeChatEvent toEvent();
}
