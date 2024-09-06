package com.ytl.crm.service.ws.Impl.friend;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import com.ytl.crm.domain.constant.Constants;
import com.ytl.crm.domain.entity.friend.WechatFriendChangeEventEntity;
import com.ytl.crm.domain.entity.friend.WechatFriendRelationEntity;
import com.ytl.crm.domain.req.ws.WsFriendChangeEvent;
import com.ytl.crm.mapper.friend.WechatApplyQrcodeLogMapper;
import com.ytl.crm.mapper.friend.WechatFriendChangeEventMapper;
import com.ytl.crm.mapper.friend.WechatFriendRelationMapper;
import com.ytl.crm.service.ws.define.friend.IWechatAggregateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Objects;


/**
 * @author 11911
 * @version 1.0
 * {@code @date} 2024/7/22 16:29
 */
@Slf4j
@Service
public class WechatAggregateServiceImpl implements IWechatAggregateService {

    @Resource
    private WechatFriendRelationMapper wechatFriendRelationMapper;

    @Resource
    private WechatFriendChangeEventMapper wechatFriendChangeEventMapper;

    @Resource
    private WechatApplyQrcodeLogMapper wechatApplyQrcodeLogMapper;

    /**
     * 保存微信好友关系
     *
     * @param wechatFriendRelationEntity 申请活码记录
     * @param changeEvent                事件
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveWechatFriendRelation(WechatFriendRelationEntity wechatFriendRelationEntity, WsFriendChangeEvent changeEvent) {
        //保存好友关系
        if (Objects.isNull(wechatFriendRelationEntity.getId())) {
            wechatFriendRelationMapper.insert(wechatFriendRelationEntity);
        } else {
            wechatFriendRelationMapper.updateById(wechatFriendRelationEntity);
        }

        //保存事件
        saveWsFriendChangeEvent(changeEvent);
    }


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

    /**
     * 更新好友关系
     *
     * @param wechatFriendRelationEntity 实体
     * @param changeEvent                事件
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateWechatFriend(WechatFriendRelationEntity wechatFriendRelationEntity, WsFriendChangeEvent changeEvent) {
        wechatFriendRelationMapper.updateById(wechatFriendRelationEntity);

        saveWsFriendChangeEvent(changeEvent);
    }


}
