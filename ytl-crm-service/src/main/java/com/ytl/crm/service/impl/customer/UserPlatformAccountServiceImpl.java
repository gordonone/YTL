package com.ytl.crm.service.impl.customer;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ytl.crm.domain.entity.customer.UserPlatformAccountEntity;
import com.ytl.crm.mapper.customer.UserPlatformAccountMapper;
import com.ytl.crm.service.interfaces.customer.IUserPlatformAccountService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 客户基本信息表 服务实现类
 * </p>
 *
 * @author hongj
 * @since 2024-10-15
 */
@Service
public class UserPlatformAccountServiceImpl extends ServiceImpl<UserPlatformAccountMapper, UserPlatformAccountEntity> implements IUserPlatformAccountService {

    @Override
    public UserPlatformAccountEntity queryByCustomerAndEmpId(String customerWxId, String empWxId) {
        if (StringUtils.isAnyBlank(customerWxId, empWxId)) {
            return null;
        }
        LambdaQueryWrapper<UserPlatformAccountEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(UserPlatformAccountEntity::getCustomerWxId, customerWxId);
        wrapper.eq(UserPlatformAccountEntity::getEmpWxId, empWxId);
     //   wrapper.eq(UserPlatformAccountEntity::getStatus, WechatFriendStatusEnum.ADDED.getCode());
        wrapper.orderByDesc(UserPlatformAccountEntity::getId);
        wrapper.last(" LIMIT 1");
        return getOne(wrapper);
    }
}
