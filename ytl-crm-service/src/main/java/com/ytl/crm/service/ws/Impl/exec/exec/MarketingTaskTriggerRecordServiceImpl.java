package com.ytl.crm.service.ws.Impl.exec.exec;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ytl.crm.constants.CommonConstant;
import com.ytl.crm.domain.entity.task.config.MarketingTaskEntity;
import com.ytl.crm.domain.entity.task.exec.MarketingTaskTriggerRecordEntity;
import com.ytl.crm.domain.enums.common.YesOrNoEnum;
import com.ytl.crm.domain.enums.task.exec.TaskTriggerStatusEnum;
import com.ytl.crm.mapper.task.exec.MarketingTaskTriggerRecordMapper;
import com.ytl.crm.service.ws.define.exec.config.IMarketingTaskService;
import com.ytl.crm.service.ws.define.exec.exec.IMarketingTaskTriggerRecordService;
import com.ytl.crm.utils.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 营销任务-触发记录表 服务实现类
 * </p>
 *
 * @author hongj
 * @since 2024-08-12
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MarketingTaskTriggerRecordServiceImpl extends ServiceImpl<MarketingTaskTriggerRecordMapper, MarketingTaskTriggerRecordEntity> implements IMarketingTaskTriggerRecordService {

    private final IMarketingTaskService iMarketingTaskService;

    @Override
    public MarketingTaskTriggerRecordEntity queryByTaskCodeAndCreateTime(String taskCode, Date createTimeStart, Date createTimeEnd) {
        LambdaQueryWrapper<MarketingTaskTriggerRecordEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(MarketingTaskTriggerRecordEntity::getTaskCode, taskCode);
        wrapper.ge(MarketingTaskTriggerRecordEntity::getCreateTime, createTimeStart);
        wrapper.lt(MarketingTaskTriggerRecordEntity::getCreateTime, createTimeEnd);
        wrapper.last(" limit 1");
        return getOne(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveTriggerRecord(MarketingTaskEntity taskEntity) {
        // 1. 保存触发记录
        Date currentTime = DateTimeUtil.currentTime();
        String taskCode = taskEntity.getLogicCode();
        MarketingTaskTriggerRecordEntity triggerRecord = assemblyRecord(taskCode, currentTime);
        boolean saveRet = save(triggerRecord);
        boolean updateRet = false;
        if (saveRet) {
            Date triggerTimeLimit = DateTimeUtil.getTimeOfDayStart(currentTime);
            updateRet = iMarketingTaskService.updateTaskTriggerTime(taskCode, currentTime, triggerTimeLimit);
        }
        if (!(saveRet && updateRet)) {
            log.error("保存促发记录异常，taskCode={}", taskCode);
            throw new RuntimeException("保存促发记录异常，taskCode=" + taskCode);
        }
    }

    private MarketingTaskTriggerRecordEntity assemblyRecord(String taskCode, Date currentTime) {
        MarketingTaskTriggerRecordEntity triggerRecord = new MarketingTaskTriggerRecordEntity();
        triggerRecord.setTaskCode(taskCode);
        triggerRecord.setTriggerStatus(TaskTriggerStatusEnum.INIT.getCode());
        triggerRecord.setCreateTime(currentTime);
        triggerRecord.setCreateUserCode(CommonConstant.SYSTEM);
        triggerRecord.setCreateUserName(CommonConstant.SYSTEM_NAME);
        triggerRecord.setLastModifyTime(currentTime);
        triggerRecord.setModifyUserCode(CommonConstant.SYSTEM);
        triggerRecord.setModifyUserName(CommonConstant.SYSTEM_NAME);
        return triggerRecord;
    }


    @Override
    public List<MarketingTaskTriggerRecordEntity> queryWaitPullDataRecord() {
        Pair<Date, Date> todayStartToEnd = DateTimeUtil.getTodayStartToEnd(false);
        LambdaQueryWrapper<MarketingTaskTriggerRecordEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(MarketingTaskTriggerRecordEntity::getTriggerStatus, TaskTriggerStatusEnum.INIT.getCode());
        wrapper.ge(MarketingTaskTriggerRecordEntity::getCreateTime, todayStartToEnd.getLeft());
        wrapper.lt(MarketingTaskTriggerRecordEntity::getCreateTime, todayStartToEnd.getRight());
        return list(wrapper);
    }

    @Override
    public List<MarketingTaskTriggerRecordEntity> queryWaitCreateActionRecord() {
        Pair<Date, Date> todayStartToEnd = DateTimeUtil.getTodayStartToEnd(false);
        LambdaQueryWrapper<MarketingTaskTriggerRecordEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(MarketingTaskTriggerRecordEntity::getTriggerStatus, TaskTriggerStatusEnum.BIZ_DATA_PULLED.getCode());
        wrapper.ge(MarketingTaskTriggerRecordEntity::getCreateTime, todayStartToEnd.getLeft());
        wrapper.lt(MarketingTaskTriggerRecordEntity::getCreateTime, todayStartToEnd.getRight());
        return list(wrapper);
    }

    @Override
    public List<MarketingTaskTriggerRecordEntity> queryWaitExecActionRecord() {
        Pair<Date, Date> todayStartToEnd = DateTimeUtil.getTodayStartToEnd(false);
        LambdaQueryWrapper<MarketingTaskTriggerRecordEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(MarketingTaskTriggerRecordEntity::getTriggerStatus, TaskTriggerStatusEnum.WAIT_EXEC_ACTION.getCode());
        wrapper.ge(MarketingTaskTriggerRecordEntity::getCreateTime, todayStartToEnd.getLeft());
        wrapper.lt(MarketingTaskTriggerRecordEntity::getCreateTime, todayStartToEnd.getRight());
        return list(wrapper);
    }

    @Override
    public List<MarketingTaskTriggerRecordEntity> queryWaitCallBackRecord() {
        Pair<Date, Date> todayStartToEnd = DateTimeUtil.getTodayStartToEnd(false);
        LambdaQueryWrapper<MarketingTaskTriggerRecordEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(MarketingTaskTriggerRecordEntity::getTriggerStatus, TaskTriggerStatusEnum.TASK_FINISH.getCode());
        wrapper.ge(MarketingTaskTriggerRecordEntity::getCreateTime, todayStartToEnd.getLeft());
        wrapper.lt(MarketingTaskTriggerRecordEntity::getCreateTime, todayStartToEnd.getRight());
        return list(wrapper);
    }

    @Override
    public List<MarketingTaskTriggerRecordEntity> queryWaitCompensateRecord() {
        //取昨天的任务
        Date yesterday = DateTimeUtil.addDay(new Date(), -1);
        Pair<Date, Date> todayStartToEnd = DateTimeUtil.getDateStartToEnd(yesterday, false);
        LambdaQueryWrapper<MarketingTaskTriggerRecordEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(MarketingTaskTriggerRecordEntity::getTriggerStatus, TaskTriggerStatusEnum.TASK_FINISH.getCode());
        wrapper.ge(MarketingTaskTriggerRecordEntity::getHasCompensate, YesOrNoEnum.NO.getCode());
        wrapper.lt(MarketingTaskTriggerRecordEntity::getCreateTime, todayStartToEnd.getRight());
        wrapper.lt(MarketingTaskTriggerRecordEntity::getCreateTime, todayStartToEnd.getRight());
        return list(wrapper);
    }

    @Override
    public List<MarketingTaskTriggerRecordEntity> queryWaitCompensateCallbackRecord() {
        //取昨天的任务
        Date yesterday = DateTimeUtil.addDay(new Date(), -1);
        Pair<Date, Date> todayStartToEnd = DateTimeUtil.getDateStartToEnd(yesterday, false);
        LambdaQueryWrapper<MarketingTaskTriggerRecordEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(MarketingTaskTriggerRecordEntity::getTriggerStatus, TaskTriggerStatusEnum.TASK_FINISH.getCode());
        wrapper.ge(MarketingTaskTriggerRecordEntity::getHasCompensate, YesOrNoEnum.YES.getCode());
        wrapper.lt(MarketingTaskTriggerRecordEntity::getCreateTime, todayStartToEnd.getRight());
        wrapper.lt(MarketingTaskTriggerRecordEntity::getCreateTime, todayStartToEnd.getRight());
        return list(wrapper);
    }

    @Override
    public boolean updateTriggerStatus(String triggerCode, String fromStatus, String toStatus, String triggerDesc) {
        log.info("更新触发记录状态，triggerCode={}，fromStatus={}，toStatus+{}，triggerDesc={}", triggerCode, fromStatus, toStatus, triggerDesc);
        //fromStatus乐观锁，避免并发情况
        LambdaUpdateWrapper<MarketingTaskTriggerRecordEntity> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.set(MarketingTaskTriggerRecordEntity::getTriggerStatus, toStatus);
        if (StringUtils.isNotBlank(triggerDesc)) {
            updateWrapper.set(MarketingTaskTriggerRecordEntity::getTriggerDesc, triggerDesc);
        }
        updateWrapper.eq(MarketingTaskTriggerRecordEntity::getLogicCode, triggerCode);
        updateWrapper.eq(MarketingTaskTriggerRecordEntity::getTriggerStatus, fromStatus);
        boolean updateRet = update(updateWrapper);
        log.info("更新触发记录状态结果，triggerCode={}，updateRet={}", triggerCode, updateRet);
        return updateRet;
    }

    @Override
    public boolean updateTriggerStatus(String triggerCode, String fromStatus, String toStatus) {
        return updateTriggerStatus(triggerCode, fromStatus, toStatus, null);
    }

}
