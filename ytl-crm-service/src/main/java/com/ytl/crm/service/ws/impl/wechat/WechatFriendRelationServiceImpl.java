package com.ytl.crm.service.ws.impl.wechat;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ytl.crm.domain.entity.wechat.WechatFriendRelationEntity;
import com.ytl.crm.domain.enums.wechat.WechatFriendStatusEnum;
import com.ytl.crm.mapper.wechat.WechatFriendRelationMapper;
import com.ytl.crm.service.ws.define.wechat.official.IWechatFriendRelationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 好友关系表 服务实现类
 * </p>
 *
 * @author hongj
 * @since 2024-10-01
 */
@Service
public class WechatFriendRelationServiceImpl extends ServiceImpl<WechatFriendRelationMapper, WechatFriendRelationEntity> implements IWechatFriendRelationService {

    @Override
    public WechatFriendRelationEntity queryByCustomerAndEmpId(String customerWxId, String empWxId) {
        if (StringUtils.isAnyBlank(customerWxId, empWxId)) {
            return null;
        }
        LambdaQueryWrapper<WechatFriendRelationEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WechatFriendRelationEntity::getCustomerWxId, customerWxId);
        wrapper.eq(WechatFriendRelationEntity::getEmpWxId, empWxId);
        wrapper.eq(WechatFriendRelationEntity::getStatus, WechatFriendStatusEnum.ADDED.getCode());
        wrapper.orderByDesc(WechatFriendRelationEntity::getId);
        wrapper.last(" LIMIT 1");
        return getOne(wrapper);
    }

    @Override
    public List<WechatFriendRelationEntity> listByApplyCode(Collection<String> applyCodes) {
        LambdaQueryWrapper<WechatFriendRelationEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.in(WechatFriendRelationEntity::getApplyCode, applyCodes);
        wrapper.eq(WechatFriendRelationEntity::getStatus, WechatFriendStatusEnum.ADDED.getCode());
        wrapper.orderByDesc(WechatFriendRelationEntity::getId);
        return list(wrapper);
    }

    @Override
    public boolean delRelation(String empWxId, String customerWxId) {
        LambdaUpdateWrapper<WechatFriendRelationEntity> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.set(WechatFriendRelationEntity::getStatus, WechatFriendStatusEnum.DELETED.getCode());
        updateWrapper.eq(WechatFriendRelationEntity::getCustomerWxId, customerWxId);
        updateWrapper.eq(WechatFriendRelationEntity::getEmpWxId, empWxId);
        updateWrapper.eq(WechatFriendRelationEntity::getStatus, WechatFriendStatusEnum.ADDED.getCode());
        return update(updateWrapper);
    }

    @Override
    public boolean updateRemark(String logicCode, String remarkName, String remarkPhone) {
        if (StringUtils.isAllBlank(remarkName, remarkPhone)) {
            return true;
        }
        LambdaUpdateWrapper<WechatFriendRelationEntity> updateWrapper = Wrappers.lambdaUpdate();
        if (StringUtils.isNotBlank(remarkName)) {
            updateWrapper.set(WechatFriendRelationEntity::getCustomerWxRemarkName, remarkName);
        }
        if (StringUtils.isNotBlank(remarkPhone)) {
            updateWrapper.set(WechatFriendRelationEntity::getCustomerWxRemarkPhone, remarkPhone);
        }
        updateWrapper.eq(WechatFriendRelationEntity::getLogicCode, logicCode);
        return update(updateWrapper);
    }
}
