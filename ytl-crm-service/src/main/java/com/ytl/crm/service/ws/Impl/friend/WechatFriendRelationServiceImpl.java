package com.ytl.crm.service.ws.Impl.friend;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ytl.crm.domain.entity.friend.WechatFriendRelationEntity;
import com.ytl.crm.mapper.friend.WechatFriendRelationMapper;
import com.ytl.crm.service.ws.define.friend.IWechatFriendRelationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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


}
