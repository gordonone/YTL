package com.ytl.crm.event.wechat.model.customer;


import com.ytl.crm.domain.enums.wechat.CustomerDelType;
import com.ytl.crm.domain.enums.wechat.WechatEventTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DelFriendEvent extends FriendEvent {

    private CustomerDelType delType;

    private WechatEventTypeEnum eventType = WechatEventTypeEnum.DEL_FRIEND;

}
