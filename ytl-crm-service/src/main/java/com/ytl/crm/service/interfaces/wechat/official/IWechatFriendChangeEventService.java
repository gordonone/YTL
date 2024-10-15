package com.ytl.crm.service.interfaces.wechat.official;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ytl.crm.domain.entity.wechat.WechatFriendChangeEventEntity;

/**
 * <p>
 * 好友相关事件 服务类
 * </p>
 *
 * @author hongj
 * @since 2024-10-02
 */
public interface IWechatFriendChangeEventService extends IService<WechatFriendChangeEventEntity> {

    boolean updateEventStatus(String logicCode, String fromStatus, String toStatus);

}
