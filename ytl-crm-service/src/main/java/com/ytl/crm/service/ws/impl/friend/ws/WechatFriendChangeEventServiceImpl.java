package com.ytl.crm.service.ws.impl.friend.ws;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ytl.crm.domain.constant.Constants;
import com.ytl.crm.domain.entity.friend.WechatFriendChangeEventEntity;
import com.ytl.crm.domain.req.ws.WsFriendChangeEvent;
import com.ytl.crm.mapper.friend.WechatFriendChangeEventMapper;
import com.ytl.crm.service.ws.define.friend.IWechatFriendChangeEventService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 添加好友事件表 服务实现类
 * </p>
 *
 * @author yanby
 * @since 2024-07-19
 */
@Service
public class WechatFriendChangeEventServiceImpl extends ServiceImpl<WechatFriendChangeEventMapper, WechatFriendChangeEventEntity> implements IWechatFriendChangeEventService {


    @Resource
    private WechatFriendChangeEventMapper wechatFriendChangeEventMapper;


    /**
     * 保存变更事件
     *
     * @param changeEvent 变更事件
     */
    @Override
    public void saveWsFriendChangeEvent(WsFriendChangeEvent changeEvent) {
        WechatFriendChangeEventEntity changeEventEntity = new WechatFriendChangeEventEntity();
        changeEventEntity.setChangeType(changeEvent.getChangeType());
        changeEventEntity.setLogicCode(String.valueOf(IdUtil.createSnowflake(1, 1).nextId()));
        changeEventEntity.setOriginalEvent(JSONObject.toJSONString(changeEvent));
        changeEventEntity.setMessageId(changeEvent.getMessageId());
        changeEventEntity.setExternalUserId(changeEvent.getExternalUserId());
        changeEventEntity.setState(changeEvent.getState());
        changeEventEntity.setVirtualEmpThirdId(changeEvent.getUserId());
        changeEventEntity.setCreateUserCode(Constants.SYSTEM_CODE);
        changeEventEntity.setCreateUserName(Constants.SYSTEM_NAME);
        changeEventEntity.setModifyUserCode(Constants.SYSTEM_CODE);
        changeEventEntity.setModifyUserName(Constants.SYSTEM_NAME);
        wechatFriendChangeEventMapper.insert(changeEventEntity);
    }

}
