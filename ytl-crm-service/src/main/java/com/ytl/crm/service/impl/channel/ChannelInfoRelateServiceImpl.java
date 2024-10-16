package com.ytl.crm.service.impl.channel;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ytl.crm.domain.entity.channel.ChannelInfoRelateEntity;
import com.ytl.crm.mapper.channel.ChannelInfoRelateMapper;
import com.ytl.crm.service.interfaces.channel.IChannelInfoRelateService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 渠道信息关联表（渠道分配策略和客源分配规则关联） 服务实现类
 * </p>
 *
 * @author cuiym
 * @since 2024-09-29
 */
@Service
public class ChannelInfoRelateServiceImpl extends ServiceImpl<ChannelInfoRelateMapper, ChannelInfoRelateEntity> implements IChannelInfoRelateService {

    @Override
    public List<ChannelInfoRelateEntity> getByChannelId(Long channelId) {
        QueryWrapper<ChannelInfoRelateEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ChannelInfoRelateEntity::getChannelId, channelId);
        return list(queryWrapper);
    }
}
