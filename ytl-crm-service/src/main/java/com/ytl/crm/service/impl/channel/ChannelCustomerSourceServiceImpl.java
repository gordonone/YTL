package com.ytl.crm.service.impl.channel;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ytl.crm.domain.entity.channel.ChannelCustomerSourceEntity;
import com.ytl.crm.domain.enums.channel.StatusEnum;
import com.ytl.crm.domain.req.channel.dynamic.DynamicSearchReq;
import com.ytl.crm.mapper.channel.ChannelCustomerSourceMapper;
import com.ytl.crm.service.interfaces.channel.IChannelCustomerSourceService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author cuiym
 * @since 2024-09-29
 */
@Service
public class ChannelCustomerSourceServiceImpl extends ServiceImpl<ChannelCustomerSourceMapper, ChannelCustomerSourceEntity> implements IChannelCustomerSourceService {
    @Override
    public Page<ChannelCustomerSourceEntity> getChannelCustomerSourceEntityPage(DynamicSearchReq req) {
        QueryWrapper<ChannelCustomerSourceEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().likeRight(ChannelCustomerSourceEntity::getEmpName, req.getEmpName())
                .eq(ChannelCustomerSourceEntity::getChannelInfoId, req.getChannelInfoId())
                .orderByDesc(ChannelCustomerSourceEntity::getCreateTime);
        Page<ChannelCustomerSourceEntity> page = new Page<>();
        page.setCurrent(req.getCurPage());
        page.setSize(req.getPageSize());
        Page<ChannelCustomerSourceEntity> pageResult = page(page, queryWrapper);
        return pageResult;
    }

    @Override
    public List<ChannelCustomerSourceEntity> queryByChannelInfoId(Long channelInfoId) {
        QueryWrapper<ChannelCustomerSourceEntity> queryWrapper=new QueryWrapper<>();
        queryWrapper.lambda().eq(ChannelCustomerSourceEntity::getChannelInfoId, channelInfoId)
                .eq(ChannelCustomerSourceEntity::getStatus, StatusEnum.ENABLE.getCode());
        return list(queryWrapper);
    }
    @Override
    public List<ChannelCustomerSourceEntity> queryByChannelInfoLogicCode(String channelInfoLogicCode) {
        QueryWrapper<ChannelCustomerSourceEntity> queryWrapper=new QueryWrapper<>();
        queryWrapper.lambda().eq(ChannelCustomerSourceEntity::getChannelInfoLogicCode, channelInfoLogicCode)
                .eq(ChannelCustomerSourceEntity::getStatus, StatusEnum.ENABLE.getCode());
        return list(queryWrapper);
    }
}
