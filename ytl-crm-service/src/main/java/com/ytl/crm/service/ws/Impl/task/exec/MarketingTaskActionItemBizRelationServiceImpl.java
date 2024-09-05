package com.ytl.crm.service.ws.Impl.task.exec;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Sets;
import com.ytl.crm.domain.entity.task.exec.MarketingTaskActionItemBizRelationEntity;
import com.ytl.crm.mapper.task.exec.MarketingTaskActionItemBizRelationMapper;
import com.ytl.crm.service.ws.define.task.exec.IMarketingTaskActionItemBizRelationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 营销任务-动作执行明细和业务信息关联表 服务实现类
 * </p>
 *
 * @author hongj
 * @since 2024-08-29
 */
@Slf4j
@Service
public class MarketingTaskActionItemBizRelationServiceImpl extends ServiceImpl<MarketingTaskActionItemBizRelationMapper, MarketingTaskActionItemBizRelationEntity> implements IMarketingTaskActionItemBizRelationService {
    @Resource
    private MarketingTaskActionItemBizRelationMapper marketingTaskActionItemBizRelationMapper;

    @Override
    public Set<String> queryExistBizCode(String actionRecordCode) {
        if (StringUtils.isBlank(actionRecordCode)) {
            return Collections.emptySet();
        }
        List<String> bizCodeList = marketingTaskActionItemBizRelationMapper.selectBizCodeByActionRecordCode(actionRecordCode);
        if (CollectionUtils.isEmpty(bizCodeList)) {
            return Collections.emptySet();
        }
        return Sets.newHashSet(bizCodeList);
    }

    @Override
    public List<MarketingTaskActionItemBizRelationEntity> listByItemCode(String actionItemCode) {
        if (StringUtils.isBlank(actionItemCode)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<MarketingTaskActionItemBizRelationEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(MarketingTaskActionItemBizRelationEntity::getActionItemCode, actionItemCode);
        return list(wrapper);
    }

    @Override
    public MarketingTaskActionItemBizRelationEntity getOneByItemCode(String actionItemCode) {
        if (StringUtils.isBlank(actionItemCode)) {
            return null;
        }
        LambdaQueryWrapper<MarketingTaskActionItemBizRelationEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(MarketingTaskActionItemBizRelationEntity::getActionItemCode, actionItemCode);
        wrapper.last(" LIMIT 1");
        return getOne(wrapper);
    }

    @Override
    public List<MarketingTaskActionItemBizRelationEntity> queryByActionRecordCodeAndTaskBizCode(Collection<String> actionRecordCodes, String taskBizCode) {
        if (StringUtils.isBlank(taskBizCode) || CollectionUtils.isEmpty(actionRecordCodes)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<MarketingTaskActionItemBizRelationEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.in(MarketingTaskActionItemBizRelationEntity::getActionRecordCode, actionRecordCodes);
        wrapper.eq(MarketingTaskActionItemBizRelationEntity::getTaskBizCode, taskBizCode);
        return list(wrapper);
    }

}
