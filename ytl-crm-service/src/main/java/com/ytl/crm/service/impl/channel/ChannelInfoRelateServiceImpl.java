package com.ytl.crm.service.impl.channel;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ytl.crm.domain.entity.channel.ChannelInfoRelateEntity;
import com.ytl.crm.mapper.channel.ChannelInfoRelateMapper;
import com.ytl.crm.service.interfaces.channel.IChannelInfoRelateService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ChannelInfoRelateServiceImpl extends ServiceImpl<ChannelInfoRelateMapper, ChannelInfoRelateEntity> implements IChannelInfoRelateService {

    @Override
    public List<ChannelInfoRelateEntity> getByChannelId(Long channelId) {
        QueryWrapper<ChannelInfoRelateEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ChannelInfoRelateEntity::getChannelId, channelId);
        return list(queryWrapper);
    }
}
