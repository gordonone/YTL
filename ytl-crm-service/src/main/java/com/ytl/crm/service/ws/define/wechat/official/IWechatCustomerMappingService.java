package com.ytl.crm.service.ws.define.wechat.official;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ytl.crm.domain.entity.wechat.WechatCustomerMappingEntity;

/**
 * <p>
 * 客户微信映射表 服务类
 * </p>
 *
 * @author hongj
 * @since 2024-10-01
 */
public interface IWechatCustomerMappingService extends IService<WechatCustomerMappingEntity> {

    WechatCustomerMappingEntity queryByThirdId(String customerThirdWxId, String code);

    boolean updateCustomerWxIdById(Long id, String officialWxId);

}
