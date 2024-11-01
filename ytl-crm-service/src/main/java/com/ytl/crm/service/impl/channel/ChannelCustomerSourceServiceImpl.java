package com.ytl.crm.service.impl.channel;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ziroom.ugc.crm.service.web.domain.dto.req.channel.dynamic.DynamicSearchReq;
import com.ziroom.ugc.crm.service.web.domain.entity.channel.ChannelCustomerSourceEntity;
import com.ziroom.ugc.crm.service.web.domain.enums.channel.StatusEnum;
import com.ziroom.ugc.crm.service.web.mapper.channel.ChannelCustomerSourceMapper;
import com.ziroom.ugc.crm.service.web.service.interfaces.channel.IChannelCustomerSourceService;
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

    @Override
    public void updateWxInfo(String oldWxId, String newWxId, String empName) {
        QueryWrapper<ChannelCustomerSourceEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ChannelCustomerSourceEntity::getEmpWxId, oldWxId);
        List<ChannelCustomerSourceEntity> list = list(queryWrapper);
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        for (ChannelCustomerSourceEntity channelCustomerSourceEntity : list) {
            channelCustomerSourceEntity.setEmpWxId(newWxId);
            channelCustomerSourceEntity.setEmpName(empName);
        }
        updateBatchById(list);
    }
}
