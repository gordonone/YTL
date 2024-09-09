package com.ytl.crm.service.ws.Impl.friend;

import com.ytl.crm.domain.entity.friend.WechatFriendRelationEntity;
import com.ytl.crm.domain.req.ws.WsFriendChangeEvent;
import com.ytl.crm.service.ws.define.friend.IWechatAggregateService;
import com.ytl.crm.service.ws.define.friend.IWechatFriendChangeEventService;
import com.ytl.crm.service.ws.define.friend.IWechatFriendRelationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * @author 11911
 * @version 1.0
 * {@code @date} 2024/7/22 16:29
 */
@Slf4j
@Service
public class WechatAggregateServiceImpl implements IWechatAggregateService {

    @Resource
    private IWechatFriendRelationService wechatFriendRelationService;

    @Resource
    private IWechatFriendChangeEventService wechatFriendChangeEventService;

    /**
     * 保存微信好友关系
     *
     * @param wechatFriendRelationEntity 申请活码记录
     * @param changeEvent                事件
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveWechatFriendRelation(WechatFriendRelationEntity wechatFriendRelationEntity, WsFriendChangeEvent changeEvent) {

        wechatFriendRelationService.saveWechatFriendRelation(wechatFriendRelationEntity,changeEvent);
        //保存事件
        wechatFriendChangeEventService.saveWsFriendChangeEvent(changeEvent);
    }

    @Override
    public void saveWsFriendChangeEvent(WsFriendChangeEvent changeEvent) {
        wechatFriendChangeEventService.saveWsFriendChangeEvent(changeEvent);
    }


    /**
     * 更新好友关系
     *
     * @param wechatFriendRelationEntity 实体
     * @param changeEvent                事件
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateWechatFriend(WechatFriendRelationEntity wechatFriendRelationEntity, WsFriendChangeEvent changeEvent) {
        wechatFriendRelationService.updateById(wechatFriendRelationEntity);
        wechatFriendChangeEventService.saveWsFriendChangeEvent(changeEvent);
    }


}
