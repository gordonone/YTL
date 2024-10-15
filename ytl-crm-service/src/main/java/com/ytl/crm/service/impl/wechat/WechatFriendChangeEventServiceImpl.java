package com.ytl.crm.service.impl.wechat;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ytl.crm.domain.entity.wechat.WechatFriendChangeEventEntity;
import com.ytl.crm.mapper.wechat.WechatFriendChangeEventMapper;
import com.ytl.crm.service.interfaces.wechat.official.IWechatFriendChangeEventService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 好友相关事件 服务实现类
 * </p>
 *
 * @author hongj
 * @since 2024-10-02
 */
@Service
public class WechatFriendChangeEventServiceImpl extends ServiceImpl<WechatFriendChangeEventMapper, WechatFriendChangeEventEntity> implements IWechatFriendChangeEventService {

    @Override
    public boolean updateEventStatus(String logicCode, String fromStatus, String toStatus) {
        LambdaUpdateWrapper<WechatFriendChangeEventEntity> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.set(WechatFriendChangeEventEntity::getEventStatus, toStatus);
        updateWrapper.eq(WechatFriendChangeEventEntity::getLogicCode, logicCode);
        updateWrapper.eq(WechatFriendChangeEventEntity::getEventStatus, fromStatus);
        return update(updateWrapper);
    }
}
