package com.ytl.crm.event.wechat.model.customer;

import com.ziroom.ugc.crm.service.web.domain.enums.wechat.WechatEventTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class EditFriendEvent extends FriendEvent {

    private WechatEventTypeEnum eventType = WechatEventTypeEnum.EDIT_FRIEND;

}
