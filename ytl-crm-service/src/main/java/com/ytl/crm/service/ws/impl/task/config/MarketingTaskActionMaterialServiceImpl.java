package com.ytl.crm.service.ws.impl.task.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ytl.crm.domain.entity.task.config.MarketingTaskActionMaterialEntity;
import com.ytl.crm.mapper.task.config.MarketingTaskActionMaterialMapper;
import com.ytl.crm.service.ws.define.task.config.IMarketingTaskActionMaterialService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 营销任务-动作素材表 服务实现类
 * </p>
 *
 * @author hongj
 * @since 2024-08-12
 */
@Service
public class MarketingTaskActionMaterialServiceImpl extends ServiceImpl<MarketingTaskActionMaterialMapper, MarketingTaskActionMaterialEntity> implements IMarketingTaskActionMaterialService {

    @Override
    public List<MarketingTaskActionMaterialEntity> listByTaskCode(String taskCode) {
        LambdaQueryWrapper<MarketingTaskActionMaterialEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(MarketingTaskActionMaterialEntity::getTaskCode, taskCode);
        return list(wrapper);
    }
}
