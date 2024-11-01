package com.ytl.crm.service.interfaces.customer;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ytl.crm.domain.entity.customer.UserPlatformAccountEntity;

/**
 * <p>
 * 客户基本信息表 服务类
 * </p>
 *
 * @author hongj
 * @since 2024-10-15
 */
public interface IUserPlatformAccountService extends IService<UserPlatformAccountEntity> {

    UserPlatformAccountEntity queryByCustomerAndEmpId(String customerWxId, String empWxId);

}
