package com.ytl.crm.service.ws.define.exec;


import com.ytl.crm.domain.entity.task.config.MarketingTaskEntity;
import com.ytl.crm.domain.resp.task.config.resp.TaskConfigConstantResp;
import com.ytl.crm.domain.task.config.MarketingTaskConfigBO;
import com.ytl.crm.domain.task.config.MarketingTaskStatusBO;

public interface IMarketingTaskConfigLogic {
    MarketingTaskConfigBO acquireTaskConfig(MarketingTaskEntity taskEntity);

    /**
     * 新建任务配置
     */
    boolean saveTaskConfig(MarketingTaskConfigBO marketingTaskConfigBO);

    /**
     * 修改任务配置
     */
    boolean updateTaskConfig(MarketingTaskStatusBO marketingTaskStatusBO);

    /**
     * 任务配置常量
     *
     * @return
     */
    TaskConfigConstantResp queryTaskConstant();

}
