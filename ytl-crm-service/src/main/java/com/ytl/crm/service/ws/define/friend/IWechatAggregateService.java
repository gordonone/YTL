package com.ytl.crm.service.ws.define.friend;


import com.ytl.crm.domain.entity.friend.WechatFriendRelationEntity;
import com.ytl.crm.domain.req.ws.WsFriendChangeEvent;

/**
 * @author 11911
 * @version 1.0
 * {@code @date} 2024/7/22 16:28
 */
public interface IWechatAggregateService {

    /**
     * 保存微信好友关系
     *
     * @param wechatFriendRelationEntity 申请活码记录
     * @param changeEvent          事件
     */
    void saveWechatFriendRelation(WechatFriendRelationEntity wechatFriendRelationEntity, WsFriendChangeEvent changeEvent);

    /**
     * 保存好友事件
     *
     * @param changeEvent 事件
     */
    void saveWsFriendChangeEvent(WsFriendChangeEvent changeEvent);

    /**
     * 更新好友关系
     *
     * @param wechatFriendRelationEntity 实体
     * @param changeEvent                事件
     */
    void updateWechatFriend(WechatFriendRelationEntity wechatFriendRelationEntity, WsFriendChangeEvent changeEvent);
}
