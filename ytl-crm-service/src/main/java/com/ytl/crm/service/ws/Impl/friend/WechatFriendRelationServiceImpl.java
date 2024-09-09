package com.ytl.crm.service.ws.Impl.friend;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ytl.crm.domain.constant.Constants;
import com.ytl.crm.domain.entity.friend.WechatFriendChangeEventEntity;
import com.ytl.crm.domain.entity.friend.WechatFriendRelationEntity;
import com.ytl.crm.domain.req.ws.WsFriendChangeEvent;
import com.ytl.crm.mapper.friend.WechatFriendRelationMapper;
import com.ytl.crm.service.ws.define.friend.IWechatFriendRelationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 员工和客户的微信好友关系表 服务实现类
 * </p>
 *
 * @author yanby
 * @since 2024-07-19
 */
@Service
public class WechatFriendRelationServiceImpl extends ServiceImpl<WechatFriendRelationMapper, WechatFriendRelationEntity> implements IWechatFriendRelationService {

    @Resource
    private WechatFriendRelationMapper wechatFriendRelationMapper;

    /**
     * 保存微信好友关系
     *
     * @param wechatFriendRelationEntity 申请活码记录
     * @param changeEvent                事件
     */
    @Override
    public void saveWechatFriendRelation(WechatFriendRelationEntity wechatFriendRelationEntity, WsFriendChangeEvent changeEvent) {
        //保存好友关系
        if (Objects.isNull(wechatFriendRelationEntity.getId())) {
            wechatFriendRelationMapper.insert(wechatFriendRelationEntity);
        } else {
            wechatFriendRelationMapper.updateById(wechatFriendRelationEntity);
        }
    }


    /**
     * 更新好友关系
     *
     * @param wechatFriendRelationEntity 实体
     * @param changeEvent                事件
     */
    @Override
    public void updateWechatFriend(WechatFriendRelationEntity wechatFriendRelationEntity, WsFriendChangeEvent changeEvent) {
        wechatFriendRelationMapper.updateById(wechatFriendRelationEntity);
    }


}
