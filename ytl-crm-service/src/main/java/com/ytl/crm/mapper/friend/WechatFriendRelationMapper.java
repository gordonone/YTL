package com.ytl.crm.mapper.friend;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ytl.crm.domain.entity.friend.WechatFriendRelationEntity;
import com.ytl.crm.domain.entity.message.WechatMessageUserInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 员工和客户的微信好友关系表 Mapper 接口
 * </p>
 *
 * @author yanby
 * @since 2024-07-19
 */
public interface WechatFriendRelationMapper extends BaseMapper<WechatFriendRelationEntity> {

    /**
     * 根据员工或客户标识查询用户信息
     *
     * @param empIdsOrUserWechatIds 用户或员工标识列表
     * @return 员工列表
     */
    List<WechatMessageUserInfo> queryByEmpIdsOrUserWechatIds(@Param("empIdsOrUserWechatIds") List<String> empIdsOrUserWechatIds);
}
