package com.ytl.crm.service.ws.Impl.exec.exec;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ytl.crm.domain.entity.task.exec.MarketingTaskActionExecRecordEntity;
import com.ytl.crm.domain.enums.task.exec.TaskTriggerStatusEnum;
import com.ytl.crm.mapper.task.exec.MarketingTaskActionExecRecordMapper;
import com.ytl.crm.service.ws.define.exec.exec.IMarketingTaskActionExecRecordService;
import com.ytl.crm.service.ws.define.exec.exec.IMarketingTaskTriggerRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    public MarketingTaskActionExecRecordEntity queryByLogicCode(String logicCode) {
        LambdaQueryWrapper<MarketingTaskActionExecRecordEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(MarketingTaskActionExecRecordEntity::getLogicCode, logicCode)
                .last(" LIMIT 1");
        return getOne(wrapper);
    }

    @Override
    public List<MarketingTaskActionExecRecordEntity> queryByTriggerCode(String triggerCode) {
        LambdaQueryWrapper<MarketingTaskActionExecRecordEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(MarketingTaskActionExecRecordEntity::getTriggerCode, triggerCode);
        return list(wrapper);
    }

    @Override
    public List<MarketingTaskActionExecRecordEntity> queryByTriggerCodeAndStatus(String triggerCode, String execStatus) {
        LambdaQueryWrapper<MarketingTaskActionExecRecordEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(MarketingTaskActionExecRecordEntity::getTriggerCode, triggerCode);
        wrapper.eq(MarketingTaskActionExecRecordEntity::getActionExecStatus, execStatus);
        return list(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveActionExecRecord(List<MarketingTaskActionExecRecordEntity> recordList, String triggerCode) {
        boolean saveRet = saveBatch(recordList);
        boolean updateRet = false;
        if (saveRet) {
            updateRet = iMarketingTaskTriggerRecordService.updateTriggerStatus(triggerCode,
                    TaskTriggerStatusEnum.BIZ_DATA_PULLED.getCode(), TaskTriggerStatusEnum.WAIT_EXEC_ACTION.getCode());
        }
        if (!(saveRet && updateRet)) {
            throw new RuntimeException("保存动作执行记录异常");
        }
    }

    @Override
    public boolean updateActionRecordStatus(String logicCode, String fromStatus, String toStatus) {
        log.info("更新动作执行记录状态，logicCode={}，fromStatus={}，toStatus+{}", logicCode, fromStatus, toStatus);
        //fromStatus乐观锁，避免并发情况
        LambdaUpdateWrapper<MarketingTaskActionExecRecordEntity> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.set(MarketingTaskActionExecRecordEntity::getActionExecStatus, toStatus);
        updateWrapper.eq(MarketingTaskActionExecRecordEntity::getLogicCode, logicCode);
        updateWrapper.eq(MarketingTaskActionExecRecordEntity::getActionExecStatus, fromStatus);
        boolean updateRet = update(updateWrapper);
        log.info("更新动作执行记录状态，logicCode={}，updateRet={}", logicCode, updateRet);
        return updateRet;
    }

    @Override
    public List<MarketingTaskActionExecRecordEntity> queryByCompensateStatus(String triggerCode, String compensateStatus) {
        LambdaQueryWrapper<MarketingTaskActionExecRecordEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(MarketingTaskActionExecRecordEntity::getTriggerCode, triggerCode);
        wrapper.eq(MarketingTaskActionExecRecordEntity::getCompensateStatus, compensateStatus);
        return list(wrapper);
    }


    @Override
    public boolean updateCompensateStatus(String logicCode, String fromStatus, String toStatus) {
        //fromStatus乐观锁，避免并发情况
        LambdaUpdateWrapper<MarketingTaskActionExecRecordEntity> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.set(MarketingTaskActionExecRecordEntity::getCompensateStatus, toStatus);
        updateWrapper.eq(MarketingTaskActionExecRecordEntity::getLogicCode, logicCode);
        updateWrapper.eq(MarketingTaskActionExecRecordEntity::getCompensateStatus, fromStatus);
        return update(updateWrapper);
    }

}
