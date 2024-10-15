package com.ytl.crm.service.interfaces.wechat.official;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ytl.crm.domain.entity.wechat.WechatFriendRelationEntity;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 好友关系表 服务类
 * </p>
 *
 * @author hongj
 * @since 2024-10-01
 */
public interface IWechatFriendRelationService extends IService<WechatFriendRelationEntity> {

    WechatFriendRelationEntity queryByCustomerAndEmpId(String customerWxId, String empWxId);

    List<WechatFriendRelationEntity> listByApplyCode(Collection<String> applyCodes);

    boolean delRelation(String empWxId, String customerWxId);

    boolean updateRemark(String logicId, String remarkName, String remarkPhone);

}
