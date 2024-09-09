package com.ytl.crm.service.ws.define.friend;



import com.baomidou.mybatisplus.extension.service.IService;
import com.ytl.crm.domain.entity.friend.WechatFriendRelationEntity;
import com.ytl.crm.domain.req.ws.WsFriendChangeEvent;

/**
 * <p>
 * 员工和客户的微信好友关系表 服务类
 * </p>
 *
 * @author yanby
 * @since 2024-07-19
 */
public interface IWechatFriendRelationService extends IService<WechatFriendRelationEntity> {


    /**
     * 保存微信好友关系
     *
     * @param wechatFriendRelationEntity 申请活码记录
     * @param changeEvent          事件
     */
    void saveWechatFriendRelation(WechatFriendRelationEntity wechatFriendRelationEntity, WsFriendChangeEvent changeEvent);

    /**
     * 更新好友关系
     *
     * @param wechatFriendRelationEntity 实体
     * @param changeEvent                事件
     */
    void updateWechatFriend(WechatFriendRelationEntity wechatFriendRelationEntity, WsFriendChangeEvent changeEvent);
}
