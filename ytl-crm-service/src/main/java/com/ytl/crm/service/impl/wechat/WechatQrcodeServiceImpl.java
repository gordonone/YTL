package com.ytl.crm.service.impl.wechat;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ytl.crm.domain.entity.wechat.WechatQrcodeEntity;
import com.ytl.crm.domain.enums.common.YesOrNoEnum;
import com.ytl.crm.mapper.wechat.WechatQrcodeMapper;
import com.ytl.crm.service.interfaces.wechat.official.IWechatQrcodeService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
public class WechatQrcodeServiceImpl extends ServiceImpl<WechatQrcodeMapper, WechatQrcodeEntity> implements IWechatQrcodeService {

    @Override
    public WechatQrcodeEntity queryByApplyCodeAndSource(String applyCode, String source) {
        LambdaQueryWrapper<WechatQrcodeEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WechatQrcodeEntity::getApplyCode, applyCode);
        wrapper.eq(WechatQrcodeEntity::getIsDelete, YesOrNoEnum.NO.getCode());
        wrapper.eq(WechatQrcodeEntity::getSource, source);
        wrapper.last(" LIMIT 1");
        return getOne(wrapper);
    }

    /**
     * 这期只会有官方的state
     */
    @Override
    public WechatQrcodeEntity queryByStateCode(String stateCode, String source) {
        LambdaQueryWrapper<WechatQrcodeEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WechatQrcodeEntity::getIsDelete, YesOrNoEnum.NO.getCode());
        wrapper.eq(WechatQrcodeEntity::getSource, source);
        wrapper.eq(WechatQrcodeEntity::getSourceState, stateCode);
        wrapper.last(" LIMIT 1");
        return getOne(wrapper);
    }

    @Override
    public List<WechatQrcodeEntity> listExpireQrCode(String source, Date expireTime, Long lastEndId, Integer size) {
        LambdaQueryWrapper<WechatQrcodeEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WechatQrcodeEntity::getIsDelete, YesOrNoEnum.NO.getCode());
        wrapper.eq(WechatQrcodeEntity::getSource, source);
        wrapper.lt(WechatQrcodeEntity::getQrcodeExpireTime, expireTime);
        wrapper.gt(WechatQrcodeEntity::getId, lastEndId);
        wrapper.orderByAsc(WechatQrcodeEntity::getId);
        wrapper.last(" LIMIT " + size);
        return list(wrapper);
    }

    @Override
    public boolean deleteEntityById(Long id) {
        LambdaUpdateWrapper<WechatQrcodeEntity> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.set(WechatQrcodeEntity::getIsDelete, YesOrNoEnum.YES.getCode());
        updateWrapper.eq(WechatQrcodeEntity::getId, id);
        return update(updateWrapper);
    }


}
