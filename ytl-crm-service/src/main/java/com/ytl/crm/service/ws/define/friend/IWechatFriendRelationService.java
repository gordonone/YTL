package com.ytl.crm.service.ws.define.friend;



import com.baomidou.mybatisplus.extension.service.IService;
import com.ytl.crm.domain.entity.friend.WechatFriendRelationEntity;
import com.ytl.crm.domain.entity.message.WechatMessageUserInfo;

import java.util.List;

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
     * 根据员工或客户标识查询用户信息
     *
     * @param empIdsOrUserWechatIds 用户或员工标识列表
     * @return 员工列表
     */
    List<WechatMessageUserInfo> queryByEmpIdsOrUserWechatIds(List<String> empIdsOrUserWechatIds);
}
