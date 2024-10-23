package com.ytl.crm.service.interfaces.channel;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ytl.crm.domain.entity.channel.DynamicRelateDataEntity;

/**
 * <p>
 * 动态列关联数据表 服务类
 * </p>
 *
 * @author cuiym
 * @since 2024-09-29
 */
public interface IDynamicRelateDataService extends IService<DynamicRelateDataEntity> {
    boolean removeByDataId(Long dataId, String tableName);
}
