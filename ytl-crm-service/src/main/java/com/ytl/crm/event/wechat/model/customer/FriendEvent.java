package com.ytl.crm.event.wechat.model.customer;


import com.ytl.crm.event.wechat.model.WeChatEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class FriendEvent extends WeChatEvent {

    /**
     * 好友事件表的logicCode
     */
    private String friendEventLogicCode;

    /**
     * 客户官方微信id
     */
    private String customerWxId;

    /**
     * 员工官方微信id
     */
    private String empWxId;


}
