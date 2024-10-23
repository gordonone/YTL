package com.ytl.crm.service.impl.channel;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ytl.crm.domain.entity.channel.DynamicRelateDataEntity;
import com.ytl.crm.mapper.channel.DynamicRelateDataMapper;
import com.ytl.crm.service.interfaces.channel.IDynamicRelateDataService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 动态列关联数据表 服务实现类
 * </p>
 *
 * @author cuiym
 * @since 2024-09-29
 */
@Service
public class DynamicRelateDataServiceImpl extends ServiceImpl<DynamicRelateDataMapper, DynamicRelateDataEntity> implements IDynamicRelateDataService {

    @Override
    public boolean removeByDataId(Long dataId, String tableName) {
        QueryWrapper<DynamicRelateDataEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(DynamicRelateDataEntity::getDataId, dataId);
        return remove(queryWrapper);
    }
}
