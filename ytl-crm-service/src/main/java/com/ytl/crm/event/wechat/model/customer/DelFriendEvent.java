package com.ytl.crm.event.wechat.model.customer;

import com.ziroom.ugc.crm.service.web.domain.enums.wechat.CustomerDelType;
import com.ziroom.ugc.crm.service.web.domain.enums.wechat.WechatEventTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DelFriendEvent extends FriendEvent {

    private CustomerDelType delType;

    private WechatEventTypeEnum eventType = WechatEventTypeEnum.DEL_FRIEND;

}
