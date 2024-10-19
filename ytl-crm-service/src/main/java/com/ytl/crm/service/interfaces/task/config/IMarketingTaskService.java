package com.ytl.crm.service.interfaces.task.config;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ytl.crm.domain.entity.task.config.MarketingTaskEntity;
import com.ytl.crm.domain.resp.common.PageResp;
import com.ytl.crm.domain.bo.task.config.MarketingTaskQueryBO;
import com.ytl.crm.domain.bo.task.config.MarketingTaskStatusBO;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 营销任务表 服务类
 * </p>
 *
 * @author hongj
 * @since 2024-08-12
 */
public interface IMarketingTaskService extends IService<MarketingTaskEntity> {

    /**
     * 查询待任务信息
     */
    MarketingTaskEntity queryByTaskCode(String taskCode);

    /**
     * 获取待触发的任务
     */
    List<MarketingTaskEntity> queryWaitTriggerTask();

    /**
     * 更新任务上次触发时间
     *
     * @param taskCode         任务
     * @param triggerTimeLimit 上次触发时间限制
     */
    boolean updateTaskTriggerTime(String taskCode, Date newTriggerTime, Date triggerTimeLimit);

    /**
     * 修改任务状态
     */
    boolean updateTaskStatus(MarketingTaskStatusBO marketingTaskStatusBO);

    /**
     * 分页查询任务
     *
     * @param marketingTaskQueryBO
     * @return
     */
    PageResp<MarketingTaskEntity> queryTaskList(MarketingTaskQueryBO marketingTaskQueryBO);

    /**
     * 是否重名任务名称
     *
     * @param taskName
     * @return
     */
    Integer countByTaskName(String taskName);
}
