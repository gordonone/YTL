package com.ytl.crm.service.ws.Impl.task.exec;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ytl.crm.domain.entity.task.exec.MarketingTaskTriggerRecordEntity;
import com.ytl.crm.domain.enums.task.exec.TaskActionItemExecStatusEnum;
import com.ytl.crm.domain.enums.task.exec.TaskTriggerStatusEnum;
import com.ytl.crm.mapper.task.exec.MarketingTaskTriggerRecordMapper;
import com.ytl.crm.service.ws.define.task.config.IMarketingTaskService;
import com.ytl.crm.service.ws.define.task.exec.IMarketingTaskActionExecItemService;
import com.ytl.crm.service.ws.define.task.exec.IMarketingTaskTriggerRecordService;
import com.ytl.crm.utils.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
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

    private final MarketingTaskTriggerRecordMapper marketingTaskTriggerRecordMapper;

    private final IMarketingTaskService iMarketingTaskService;
    private final IMarketingTaskActionExecItemService iMarketingTaskActionExecItemService;

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
    public void saveTriggerRecord(MarketingTaskTriggerRecordEntity triggerRecord) {
        //1.保存触发记录
        Date currentTime = DateTimeUtil.currentTime();
        String taskCode = triggerRecord.getTaskCode();
        triggerRecord.setCreateTime(currentTime);
        triggerRecord.setLastModifyTime(currentTime);
        boolean saveRet = save(triggerRecord);

        //2.更新触发时间
        boolean updateRet = false;
        if (saveRet) {
            Date triggerTimeLimit = DateTimeUtil.getTimeOfDayStart(currentTime);
            updateRet = iMarketingTaskService.updateTaskTriggerTime(taskCode, currentTime, triggerTimeLimit);
        }

        if (!(saveRet && updateRet)) {
            log.error("保存触发记录异常，taskCode={}", taskCode);
            throw new RuntimeException("保存触发记录异常，taskCode=" + taskCode);
        }
    }

    @Override
    public List<MarketingTaskTriggerRecordEntity> queryWaitPullDataRecord() {
        Pair<Date, Date> todayStartToEnd = DateTimeUtil.getTodayStartToEnd(false);
        LambdaQueryWrapper<MarketingTaskTriggerRecordEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(MarketingTaskTriggerRecordEntity::getTriggerStatus, TaskTriggerStatusEnum.WAIT_PULL_DATA.getCode());
        wrapper.ge(MarketingTaskTriggerRecordEntity::getCreateTime, todayStartToEnd.getLeft());
        wrapper.lt(MarketingTaskTriggerRecordEntity::getCreateTime, todayStartToEnd.getRight());
        return list(wrapper);
    }

    @Override
    public List<MarketingTaskTriggerRecordEntity> queryWaitCreateActionRecord() {
        Pair<Date, Date> todayStartToEnd = DateTimeUtil.getTodayStartToEnd(false);
        LambdaQueryWrapper<MarketingTaskTriggerRecordEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(MarketingTaskTriggerRecordEntity::getTriggerStatus, TaskTriggerStatusEnum.WAIT_CREATE_ACTION.getCode());
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
        List<String> logicCodes = iMarketingTaskActionExecItemService.listTriggerCodeByExecStatus(TaskActionItemExecStatusEnum.WAIT_CALL_BACK, null, null);
        if (CollectionUtils.isEmpty(logicCodes)) {
            return Collections.emptyList();
        }
        return queryByLogicCodes(logicCodes);
    }

    @Override
    public List<MarketingTaskTriggerRecordEntity> queryWaitCompensateRecord() {
        //创建时间是在昨天的item，且需要补偿的数据
        Date yesterday = DateTimeUtil.addDay(new Date(), -1);
        Pair<Date, Date> yesterdayStartToEnd = DateTimeUtil.getDateStartToEnd(yesterday, false);
        List<String> logicCodes = iMarketingTaskActionExecItemService.listTriggerCodeByExecStatus(TaskActionItemExecStatusEnum.WAIT_COMPENSATE, yesterdayStartToEnd.getLeft(), yesterdayStartToEnd.getRight());
        if (CollectionUtils.isEmpty(logicCodes)) {
            return Collections.emptyList();
        }
        return queryByLogicCodes(logicCodes);
    }

    @Override
    public boolean updateTriggerStatus(String triggerCode, String fromStatus, String toStatus, String triggerDesc) {
        log.info("更新触发记录状态，triggerCode={}，fromStatus={}，toStatus={}，triggerDesc={}", triggerCode, fromStatus, toStatus, triggerDesc);
        //fromStatus乐观锁，避免并发情况
        LambdaUpdateWrapper<MarketingTaskTriggerRecordEntity> updateWrapper = Wrappers.lambdaUpdate();
        if (StringUtils.isNotBlank(triggerDesc)) {
            updateWrapper.set(MarketingTaskTriggerRecordEntity::getTriggerDesc, triggerDesc);
        }
        updateWrapper.set(MarketingTaskTriggerRecordEntity::getTriggerStatus, toStatus);

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

    @Override
    public List<MarketingTaskTriggerRecordEntity> queryByLogicCodes(Collection<String> logicCodes) {
        LambdaQueryWrapper<MarketingTaskTriggerRecordEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.in(MarketingTaskTriggerRecordEntity::getLogicCode, logicCodes);
        return list(wrapper);
    }

}