package com.ytl.crm.mq.model.wechat.official;

import com.ziroom.ugc.crm.service.web.event.wechat.model.customer.FriendEvent;

public interface FriendEventMsgData<E extends FriendEvent> extends WeChatEventMsgData {

    String customerWxId();

    String empWxId();

    E toEvent();

}
