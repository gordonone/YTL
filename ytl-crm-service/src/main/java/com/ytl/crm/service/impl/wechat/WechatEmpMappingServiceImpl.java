package com.ytl.crm.service.impl.wechat;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ytl.crm.domain.entity.wechat.WechatEmpMappingEntity;
import com.ytl.crm.domain.enums.common.YesOrNoEnum;
import com.ytl.crm.mapper.wechat.WechatEmpMappingMapper;
import com.ytl.crm.service.interfaces.wechat.official.IWechatEmpMappingService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 员工微信映射表 服务实现类
 * </p>
 *
 * @author hongj
 * @since 2024-10-01
 */
@Slf4j
@Service
public class WechatEmpMappingServiceImpl extends ServiceImpl<WechatEmpMappingMapper, WechatEmpMappingEntity> implements IWechatEmpMappingService {

    @Override
    public List<WechatEmpMappingEntity> listByThirdId(Collection<String> thirdWxIds, String thirdSource) {
        LambdaQueryWrapper<WechatEmpMappingEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WechatEmpMappingEntity::getIsDelete, YesOrNoEnum.NO.getCode());
        wrapper.eq(WechatEmpMappingEntity::getThirdSource, thirdSource);
        wrapper.in(WechatEmpMappingEntity::getEmpThirdWxId, thirdWxIds);
        return list(wrapper);
    }

    @Override
    public List<WechatEmpMappingEntity> listWaitFillEntity(String thirdSource, Long lastEndId, Integer size) {
        LambdaQueryWrapper<WechatEmpMappingEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WechatEmpMappingEntity::getIsDelete, YesOrNoEnum.NO.getCode());
        wrapper.eq(WechatEmpMappingEntity::getThirdSource, thirdSource);
        wrapper.and(wq -> wq.isNull(WechatEmpMappingEntity::getEmpWxId)
                .or().eq(WechatEmpMappingEntity::getEmpWxId, StringUtils.EMPTY));
        wrapper.gt(WechatEmpMappingEntity::getId, lastEndId);
        wrapper.orderByAsc(WechatEmpMappingEntity::getId);
        wrapper.last(" LIMIT " + size);
        return list(wrapper);
    }

    @Override
    public boolean updateEmpWxIdById(Long id, String empWxId) {
        LambdaUpdateWrapper<WechatEmpMappingEntity> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.set(WechatEmpMappingEntity::getEmpWxId, empWxId);
        updateWrapper.eq(WechatEmpMappingEntity::getId, id);
        return update(updateWrapper);
    }

    @Override
    public WechatEmpMappingEntity queryByThirdId(String empThirdWxId, String thirdSource) {
        LambdaQueryWrapper<WechatEmpMappingEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WechatEmpMappingEntity::getIsDelete, YesOrNoEnum.NO.getCode());
        wrapper.eq(WechatEmpMappingEntity::getEmpThirdWxId, empThirdWxId);
        wrapper.eq(WechatEmpMappingEntity::getThirdSource, thirdSource);
        wrapper.orderByAsc(WechatEmpMappingEntity::getId);
        wrapper.last(" LIMIT 1");
        return getOne(wrapper);
    }
}
