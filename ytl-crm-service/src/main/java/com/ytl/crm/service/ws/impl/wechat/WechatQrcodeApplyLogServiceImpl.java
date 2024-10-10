package com.ytl.crm.service.ws.impl.wechat;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ytl.crm.domain.entity.wechat.WechatQrcodeApplyLogEntity;
import com.ytl.crm.domain.enums.common.YesOrNoEnum;
import com.ytl.crm.mapper.wechat.WechatQrcodeApplyLogMapper;
import com.ytl.crm.service.ws.define.wechat.official.IWechatQrcodeApplyLogService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 申请活码记录 服务实现类
 * </p>
 *
 * @author hongj
 * @since 2024-09-27
 */
@Service
public class WechatQrcodeApplyLogServiceImpl extends ServiceImpl<WechatQrcodeApplyLogMapper, WechatQrcodeApplyLogEntity> implements IWechatQrcodeApplyLogService {

    @Override
    public WechatQrcodeApplyLogEntity queryByLogicCode(String logicCode) {
        LambdaQueryWrapper<WechatQrcodeApplyLogEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WechatQrcodeApplyLogEntity::getLogicCode, logicCode);
        wrapper.last(" LIMIT 1");
        return getOne(wrapper);
    }

    @Override
    public List<WechatQrcodeApplyLogEntity> queryApplyCodeByCustomerId(String customerIdType, String customerId) {
        LambdaQueryWrapper<WechatQrcodeApplyLogEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WechatQrcodeApplyLogEntity::getCustomerIdType, customerIdType);
        wrapper.eq(WechatQrcodeApplyLogEntity::getCustomerId, customerId);
        return list(wrapper);
    }

    @Override
    public List<WechatQrcodeApplyLogEntity> queryExistApplyLog(String channelCode, String uniqueKey, String applyType) {
        LambdaQueryWrapper<WechatQrcodeApplyLogEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WechatQrcodeApplyLogEntity::getChannelCode, channelCode);
        wrapper.eq(WechatQrcodeApplyLogEntity::getUniqueKey, uniqueKey);
        wrapper.eq(WechatQrcodeApplyLogEntity::getApplyType, applyType);
        wrapper.eq(WechatQrcodeApplyLogEntity::getIsDelete, YesOrNoEnum.NO.getCode());
        return list(wrapper);
    }

}
