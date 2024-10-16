package com.ytl.crm.domain.mq;


import com.ytl.crm.event.wechat.model.customer.FriendEvent;

public interface FriendEventMsgData<E extends FriendEvent> extends WeChatEventMsgData {

    String customerWxId();

    String empWxId();

    E toEvent();

}
