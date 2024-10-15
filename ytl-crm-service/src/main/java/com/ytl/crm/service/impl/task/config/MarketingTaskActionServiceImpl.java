package com.ytl.crm.service.impl.task.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ytl.crm.domain.entity.task.config.MarketingTaskActionEntity;
import com.ytl.crm.mapper.task.config.MarketingTaskActionMapper;
import com.ytl.crm.service.interfaces.task.config.IMarketingTaskActionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 营销任务-动作表 服务实现类
 * </p>
 *
 * @author hongj
 * @since 2024-08-12
 */
@Slf4j
@Service
public class MarketingTaskActionServiceImpl extends ServiceImpl<MarketingTaskActionMapper, MarketingTaskActionEntity> implements IMarketingTaskActionService {

    @Override
    public List<MarketingTaskActionEntity> listByTaskCode(String taskCode) {
        LambdaQueryWrapper<MarketingTaskActionEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(MarketingTaskActionEntity::getTaskCode, taskCode);
        return list(wrapper);
    }

    @Override
    public MarketingTaskActionEntity queryByLogicCode(String actionCode) {
        LambdaQueryWrapper<MarketingTaskActionEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(MarketingTaskActionEntity::getLogicCode, actionCode);
        wrapper.last(" LIMIT 1");
        return getOne(wrapper);
    }
}
