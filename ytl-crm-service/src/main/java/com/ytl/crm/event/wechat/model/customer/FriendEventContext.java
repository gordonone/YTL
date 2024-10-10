package com.ytl.crm.event.wechat.model.customer;


import com.ytl.crm.domain.entity.wechat.WechatFriendRelationEntity;
import com.ytl.crm.domain.entity.wechat.WechatQrcodeApplyLogEntity;
import com.ytl.crm.domain.entity.wechat.WechatQrcodeEntity;
import lombok.Data;

@Data
public class FriendEventContext<E extends FriendEvent> {

    /**
     * 事件，除当前字段不为空，其他按需查询
     */
    private E event;

    /**
     * 申请记录
     */
    private WechatQrcodeApplyLogEntity applyLogEntity;

    /**
     * 二维码记录
     */
    private WechatQrcodeEntity qrcodeEntity;

    /**
     * 之前存在的relation（适用于删除和编辑事件）
     */
    private WechatFriendRelationEntity oldFriendRelation;

    public static <E extends FriendEvent> FriendEventContext<E> of(E event) {
        FriendEventContext<E> context = new FriendEventContext<>();
        context.setEvent(event);
        return context;
    }

}
