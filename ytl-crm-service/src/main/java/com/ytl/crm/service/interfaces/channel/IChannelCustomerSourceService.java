package com.ytl.crm.service.interfaces.channel;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ytl.crm.domain.entity.channel.ChannelCustomerSourceEntity;
import com.ytl.crm.domain.req.channel.dynamic.DynamicSearchReq;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author cuiym
 * @since 2024-09-29
 */
public interface IChannelCustomerSourceService extends IService<ChannelCustomerSourceEntity> {
     Page<ChannelCustomerSourceEntity> getChannelCustomerSourceEntityPage(DynamicSearchReq req);
     List<ChannelCustomerSourceEntity> queryByChannelInfoId(Long channelInfoId);
     List<ChannelCustomerSourceEntity> queryByChannelInfoLogicCode(String channelInfoLogicCode);
}
