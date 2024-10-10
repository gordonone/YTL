package com.ytl.crm.event.wechat.model.customer;

import com.ytl.crm.domain.enums.wechat.WechatEventTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AddFriendEvent extends FriendEvent {

    /**
     * state用来串联
     */
    private String state;

    private WechatEventTypeEnum eventType = WechatEventTypeEnum.ADD_FRIEND;

}
