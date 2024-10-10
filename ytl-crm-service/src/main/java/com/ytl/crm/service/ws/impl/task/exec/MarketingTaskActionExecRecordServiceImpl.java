package com.ytl.crm.service.ws.impl.task.exec;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ytl.crm.domain.entity.task.exec.MarketingTaskActionExecRecordEntity;
import com.ytl.crm.domain.enums.task.config.TaskActionOneLevelTypeEnum;
import com.ytl.crm.domain.enums.task.exec.TaskActionExecStatusEnum;
import com.ytl.crm.mapper.task.exec.MarketingTaskActionExecRecordMapper;
import com.ytl.crm.service.ws.define.task.exec.IMarketingTaskActionExecItemService;
import com.ytl.crm.service.ws.define.task.exec.IMarketingTaskActionExecRecordService;
import com.ytl.crm.service.ws.define.task.exec.IMarketingTaskTriggerRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.ytl.crm.domain.enums.task.exec.TaskActionItemExecStatusEnum;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 营销任务-动作执行记录表 服务实现类
 * </p>
 *
 * @author hongj
 * @since 2024-08-12
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MarketingTaskActionExecRecordServiceImpl extends ServiceImpl<MarketingTaskActionExecRecordMapper, MarketingTaskActionExecRecordEntity> implements IMarketingTaskActionExecRecordService {

    private final IMarketingTaskTriggerRecordService iMarketingTaskTriggerRecordService;
    private final IMarketingTaskActionExecItemService iMarketingTaskActionExecItemService;

    @Override
    public MarketingTaskActionExecRecordEntity queryByLogicCode(String logicCode) {
        LambdaQueryWrapper<MarketingTaskActionExecRecordEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(MarketingTaskActionExecRecordEntity::getLogicCode, logicCode)
                .last(" LIMIT 1");
        return getOne(wrapper);
    }

    @Override
    public List<MarketingTaskActionExecRecordEntity> listByTriggerCode(String triggerCode) {
        LambdaQueryWrapper<MarketingTaskActionExecRecordEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(MarketingTaskActionExecRecordEntity::getTriggerCode, triggerCode);
        return list(wrapper);
    }

    @Override
    public List<MarketingTaskActionExecRecordEntity> listByTriggerCodeAndStatus(String triggerCode, TaskActionExecStatusEnum execStatusEnum) {
        LambdaQueryWrapper<MarketingTaskActionExecRecordEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(MarketingTaskActionExecRecordEntity::getTriggerCode, triggerCode);
        wrapper.eq(MarketingTaskActionExecRecordEntity::getActionExecStatus, execStatusEnum.getCode());
        return list(wrapper);
    }

    @Override
    public MarketingTaskActionExecRecordEntity getOneByTriggerCodeAndStatus(String triggerCode, TaskActionExecStatusEnum execStatusEnum) {
        LambdaQueryWrapper<MarketingTaskActionExecRecordEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(MarketingTaskActionExecRecordEntity::getTriggerCode, triggerCode);
        wrapper.eq(MarketingTaskActionExecRecordEntity::getActionExecStatus, execStatusEnum.getCode());
        wrapper.last(" limit 1");
        return getOne(wrapper);
    }

    @Override
    public boolean updateExecStatus(String logicCode, TaskActionExecStatusEnum fromStatus, TaskActionExecStatusEnum toStatus) {
        LambdaUpdateWrapper<MarketingTaskActionExecRecordEntity> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.set(MarketingTaskActionExecRecordEntity::getActionExecStatus, toStatus.getCode());

        updateWrapper.eq(MarketingTaskActionExecRecordEntity::getActionExecStatus, fromStatus.getCode());
        updateWrapper.eq(MarketingTaskActionExecRecordEntity::getLogicCode, logicCode);
        return update(updateWrapper);
    }

    @Override
    public List<MarketingTaskActionExecRecordEntity> listWaitCallbackAction(String triggerCode) {
        return listByItemStatus(triggerCode, TaskActionItemExecStatusEnum.WAIT_CALL_BACK);
    }

    @Override
    public List<MarketingTaskActionExecRecordEntity> listWaitCompensateAction(String triggerCode) {
        return listByItemStatus(triggerCode, TaskActionItemExecStatusEnum.WAIT_COMPENSATE);
    }

    private List<MarketingTaskActionExecRecordEntity> listByItemStatus(String triggerCode, TaskActionItemExecStatusEnum itemExecStatusEnum) {
        List<String> logicCodes = iMarketingTaskActionExecItemService.listActionRecordCodeByExecStatus(triggerCode, itemExecStatusEnum);
        if (CollectionUtils.isEmpty(logicCodes)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<MarketingTaskActionExecRecordEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.in(MarketingTaskActionExecRecordEntity::getLogicCode, logicCodes);
        return list(wrapper);
    }

    @Override
    public List<MarketingTaskActionExecRecordEntity> listByActionOneType(String triggerCode, TaskActionOneLevelTypeEnum oneLevelTypeEnum) {
        LambdaQueryWrapper<MarketingTaskActionExecRecordEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(MarketingTaskActionExecRecordEntity::getActionOneLevelType, oneLevelTypeEnum.getCode());
        wrapper.eq(MarketingTaskActionExecRecordEntity::getTriggerCode, triggerCode);
        return list(wrapper);
    }


}