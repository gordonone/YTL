package com.ytl.crm.service.ws.define.channel;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ytl.crm.domain.common.BaseResponse;
import com.ytl.crm.domain.entity.channel.ChannelCategoryTreeEntity;
import com.ytl.crm.domain.resp.channel.ChannelCategoryNode;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author cuiym
 * @since 2024-09-29
 */
public interface IChannelCategoryTreeService extends IService<ChannelCategoryTreeEntity> {
    Integer ROOT_LEVEL = 0;
    String SPILT = "-";
    BaseResponse<List<ChannelCategoryNode>> getAll();

    List<ChannelCategoryTreeEntity> getByLevel(Integer level);
    List<ChannelCategoryTreeEntity> getByParentId(Long parentId);

    Map<String, String> getFulLNameMapByCodes(Set<String> categoryCodes);
}
