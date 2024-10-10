package com.ytl.crm.service.ws.impl.friend.ws;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ytl.crm.domain.entity.friend.WechatEmpMappingEntity;
import com.ytl.crm.domain.enums.common.YesOrNoEnum;
import com.ytl.crm.mapper.friend.WechatEmpMappingMapper;
import com.ytl.crm.service.ws.define.friend.IWechatEmpMappingService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 员工标识映射表 服务实现类
 * </p>
 *
 * @author yanby
 * @since 2024-07-19
 */
@Service
public class WechatEmpMappingServiceImpl extends ServiceImpl<WechatEmpMappingMapper, WechatEmpMappingEntity> implements IWechatEmpMappingService {


    @Override
    public String getEmpIdByEmpThirdId(String empThirdId) {
        LambdaQueryWrapper<WechatEmpMappingEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WechatEmpMappingEntity::getEmpThirdId, empThirdId);
        return Optional.ofNullable(getOne(wrapper))
                .map(WechatEmpMappingEntity::getEmpId)
                .orElse(null);
    }

    @Override
    public String getEmpThirdIdByEmpId(String empId) {
        LambdaQueryWrapper<WechatEmpMappingEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WechatEmpMappingEntity::getEmpId, empId);
        return Optional.ofNullable(getOne(wrapper))
                .map(WechatEmpMappingEntity::getEmpThirdId)
                .orElse(null);
    }


    @Override
    public List<WechatEmpMappingEntity> getEmpByEmpThirdId(List<String> empThirdIds) {
        LambdaQueryWrapper<WechatEmpMappingEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.in(WechatEmpMappingEntity::getEmpThirdId, empThirdIds)
                .eq(WechatEmpMappingEntity::getIsDelete, YesOrNoEnum.NO.getCode());
        return list(wrapper);
    }
}
