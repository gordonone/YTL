package com.ytl.crm.service.ws.define.channel;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ytl.crm.domain.entity.channel.ChannelInfoRelateEntity;

import java.util.List;

/**
 * <p>
 * 渠道信息关联表（渠道分配策略和客源分配规则关联） 服务类
 * </p>
 *
 * @author cuiym
 * @since 2024-09-29
 */
public interface IChannelInfoRelateService extends IService<ChannelInfoRelateEntity> {
    List<ChannelInfoRelateEntity> getByChannelId(Long channelId);
}
