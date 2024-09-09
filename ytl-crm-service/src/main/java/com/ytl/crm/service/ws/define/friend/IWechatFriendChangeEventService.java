package com.ytl.crm.service.ws.define.friend;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ytl.crm.domain.entity.friend.WechatFriendChangeEventEntity;
import com.ytl.crm.domain.req.ws.WsFriendChangeEvent;

/**
 * <p>
 * 添加好友事件表 服务类
 * </p>
 *
 * @author yanby
 * @since 2024-07-19
 */
public interface IWechatFriendChangeEventService extends IService<WechatFriendChangeEventEntity> {

    /**
     * 保存好友事件
     *
     * @param changeEvent 事件
     */
    void saveWsFriendChangeEvent(WsFriendChangeEvent changeEvent);
}
