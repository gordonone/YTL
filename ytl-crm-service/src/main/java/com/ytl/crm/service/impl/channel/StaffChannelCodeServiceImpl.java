package com.ytl.crm.service.impl.channel;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ytl.crm.domain.bo.channel.StaffChannelLiveBo;
import com.ytl.crm.domain.entity.channel.StaffChannelCodeEntity;
import com.ytl.crm.mapper.channel.StaffChannelCodeMapper;
import com.ytl.crm.service.interfaces.channel.IStaffChannelCodeService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author hongj
 * @since 2024-09-26
 */
@Service
public class StaffChannelCodeServiceImpl extends ServiceImpl<StaffChannelCodeMapper, StaffChannelCodeEntity> implements IStaffChannelCodeService {

    @Override
    public List<StaffChannelCodeEntity> getStaffChannelLiveCode(Long staffId) {
        LambdaQueryWrapper<StaffChannelCodeEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(StaffChannelCodeEntity::getStaffId, staffId);
        return list(wrapper);
    }

    @Override
    public StaffChannelCodeEntity getLiveCode(String logicCode) {
        LambdaQueryWrapper<StaffChannelCodeEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(StaffChannelCodeEntity::getLogicCode, logicCode);
        return getOne(wrapper);
    }

    @Override
    public StaffChannelCodeEntity getStaffChannelLiveCode(StaffChannelLiveBo staffChannelLiveBo) {
        LambdaQueryWrapper<StaffChannelCodeEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(StaffChannelCodeEntity::getExternalId, staffChannelLiveBo.getExternalUserId());
        wrapper.eq(StaffChannelCodeEntity::getChannelCode, staffChannelLiveBo.getChannelCode());
        wrapper.last(" LIMIT 1");
        return getOne(wrapper);
    }

    @Override
    public boolean saveStaffApplyChannelCode(StaffChannelCodeEntity staffChannelCodeEntity) {

        LambdaUpdateWrapper<StaffChannelCodeEntity> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.set(StaffChannelCodeEntity::getApplyQrCode, staffChannelCodeEntity.getApplyQrCode());
        updateWrapper.eq(StaffChannelCodeEntity::getId, staffChannelCodeEntity.getId());
        return update(updateWrapper);
    }

    @Override
    public boolean saveStaffQrChannelCode(StaffChannelCodeEntity staffChannelCodeEntity) {

        LambdaUpdateWrapper<StaffChannelCodeEntity> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.set(StaffChannelCodeEntity::getQrCodeUrl, staffChannelCodeEntity.getQrCodeUrl());
        updateWrapper.eq(StaffChannelCodeEntity::getId, staffChannelCodeEntity.getId());
        return update(updateWrapper);
    }
}
