package com.ytl.crm.service.impl.task.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ytl.crm.domain.entity.task.config.MarketingTaskEntity;
import com.ytl.crm.domain.enums.task.config.TaskStatusEnum;
import com.ytl.crm.domain.resp.common.PageResp;
import com.ytl.crm.domain.bo.task.config.MarketingTaskQueryBO;
import com.ytl.crm.domain.bo.task.config.MarketingTaskStatusBO;
import com.ytl.crm.mapper.task.config.MarketingTaskMapper;
import com.ytl.crm.service.interfaces.task.config.IMarketingTaskService;
import com.ytl.crm.utils.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 营销任务表 服务实现类
 * </p>
 *
 * @author hongj
 * @since 2024-08-12
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MarketingTaskServiceImpl extends ServiceImpl<MarketingTaskMapper, MarketingTaskEntity> implements IMarketingTaskService {


    @Override
    public MarketingTaskEntity queryByTaskCode(String taskCode) {
        LambdaQueryWrapper<MarketingTaskEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MarketingTaskEntity::getLogicCode, taskCode);
        wrapper.last(" limit 1");
        return getOne(wrapper);
    }

    @Override
    public List<MarketingTaskEntity> queryWaitTriggerTask() {
        Date currentTime = DateTimeUtil.currentTime();
        //任务状态
        LambdaQueryWrapper<MarketingTaskEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MarketingTaskEntity::getTaskStatus, TaskStatusEnum.ENABLE.getCode());

        //任务有效时间
        Date validTimeLimit = currentTime;
        wrapper.le(MarketingTaskEntity::getValidTimeStart, validTimeLimit);
        wrapper.ge(MarketingTaskEntity::getValidTimeEnd, validTimeLimit);

        //任务动作执行时间段
        LocalTime actionTimeLimit = LocalTime.now();
        wrapper.le(MarketingTaskEntity::getActionTimeStart, actionTimeLimit);
        wrapper.ge(MarketingTaskEntity::getActionTimeEnd, actionTimeLimit);

        //今天没有触发过
        Date triggerTimeLimit = DateTimeUtil.getTimeOfDayStart(currentTime);
        wrapper.and(wq -> wq.isNull(MarketingTaskEntity::getLastTriggerTime).or().lt(MarketingTaskEntity::getLastTriggerTime, triggerTimeLimit));
        return list(wrapper);
    }

    @Override
    public boolean updateTaskTriggerTime(String taskCode, Date newTriggerTime, Date triggerTimeLimit) {
        LambdaUpdateWrapper<MarketingTaskEntity> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.set(MarketingTaskEntity::getLastTriggerTime, newTriggerTime);
        updateWrapper.eq(MarketingTaskEntity::getLogicCode, taskCode);
        // 这里相当于乐观锁
        updateWrapper.and(wq -> wq.isNull(MarketingTaskEntity::getLastTriggerTime).or().lt(MarketingTaskEntity::getLastTriggerTime, triggerTimeLimit));
        return update(updateWrapper);
    }

    @Override
    public boolean updateTaskStatus(MarketingTaskStatusBO marketingTaskStatusBO) {
        LambdaUpdateWrapper<MarketingTaskEntity> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.set(MarketingTaskEntity::getTaskStatus, marketingTaskStatusBO.getTaskStatus());
        updateWrapper.set(MarketingTaskEntity::getModifyUserCode, marketingTaskStatusBO.getModifyUserCode());
        updateWrapper.set(MarketingTaskEntity::getModifyUserName, marketingTaskStatusBO.getModifyUserName());
        updateWrapper.set(MarketingTaskEntity::getLastModifyTime, marketingTaskStatusBO.getLastModifyTime());
        updateWrapper.eq(MarketingTaskEntity::getLogicCode, marketingTaskStatusBO.getLogicCode());
        return update(updateWrapper);
    }

    @Override
    public PageResp<MarketingTaskEntity> queryTaskList(MarketingTaskQueryBO req) {

        LambdaQueryWrapper<MarketingTaskEntity> queryWrapper = Wrappers.lambdaQuery(MarketingTaskEntity.class).select().
                eq(StringUtils.isNotBlank(req.getProjectType()), MarketingTaskEntity::getProjectType, req.getProjectType()).
                eq(StringUtils.isNotBlank(req.getTaskStatus()), MarketingTaskEntity::getTaskStatus, req.getTaskStatus()).
                le(Objects.nonNull(req.getValidTimeStart()), MarketingTaskEntity::getValidTimeStart, req.getValidTimeStart()).
                ge(Objects.nonNull(req.getValidTimeEnd()), MarketingTaskEntity::getValidTimeEnd, req.getValidTimeEnd())
                .like(StringUtils.isNotBlank(req.getTaskName()), MarketingTaskEntity::getTaskName, req.getTaskName())
                .last(" order by field(task_status,'ENABLE','DISABLED'),create_time desc");

        Page<MarketingTaskEntity> page = new Page<>(req.getPageNum(), req.getPageSize());
        Page<MarketingTaskEntity> pageResult = page(page, queryWrapper);

        if (Objects.isNull(pageResult) || pageResult.getTotal() == 0) {
            return PageResp.emptyResp();
        }
        return PageResp.buildResp(Math.toIntExact(pageResult.getTotal()), pageResult.getRecords());
    }

    @Override
    public Integer countByTaskName(String taskName) {
        LambdaQueryWrapper<MarketingTaskEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MarketingTaskEntity::getTaskName, taskName);
        return count(wrapper);
    }

}
