package com.ytl.crm.service.ws.impl.wechat;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ytl.crm.domain.entity.wechat.WechatCustomerMappingEntity;
import com.ytl.crm.domain.enums.common.YesOrNoEnum;
import com.ytl.crm.mapper.wechat.WechatCustomerMappingMapper;
import com.ytl.crm.service.ws.define.wechat.official.IWechatCustomerMappingService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 客户微信映射表 服务实现类
 * </p>
 *
 * @author hongj
 * @since 2024-10-01
 */
@Service
public class WechatCustomerMappingServiceImpl extends ServiceImpl<WechatCustomerMappingMapper, WechatCustomerMappingEntity> implements IWechatCustomerMappingService {

    @Override
    public WechatCustomerMappingEntity queryByThirdId(String customerThirdWxId, String thirdSource) {
        LambdaQueryWrapper<WechatCustomerMappingEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WechatCustomerMappingEntity::getCustomerWxId, customerThirdWxId);
        wrapper.eq(WechatCustomerMappingEntity::getThirdSource, thirdSource);
        wrapper.orderByAsc(WechatCustomerMappingEntity::getId);
        wrapper.eq(WechatCustomerMappingEntity::getIsDelete, YesOrNoEnum.NO.getCode());
        wrapper.last(" LIMIT 1");
        return getOne(wrapper);
    }

    @Override
    public boolean updateCustomerWxIdById(Long id, String officialWxId) {
        LambdaUpdateWrapper<WechatCustomerMappingEntity> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.set(WechatCustomerMappingEntity::getCustomerWxId, officialWxId);
        updateWrapper.eq(WechatCustomerMappingEntity::getId, id);
        return update(updateWrapper);
    }

}
