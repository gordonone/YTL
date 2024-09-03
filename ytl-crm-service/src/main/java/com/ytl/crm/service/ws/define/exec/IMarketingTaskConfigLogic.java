package com.ytl.crm.service.ws.define.exec;


import com.ytl.crm.domain.entity.task.config.MarketingTaskEntity;
import com.ytl.crm.domain.resp.task.config.resp.TaskConfigConstantResp;
import com.ytl.crm.domain.resp.ws.WsMaterialMediaResp;
import com.ytl.crm.domain.task.config.MarketingTaskConfigAddBO;
import com.ytl.crm.domain.task.config.MarketingTaskMediaBO;
import com.ytl.crm.domain.task.config.MarketingTaskStatusBO;
import com.ytl.crm.domain.task.exec.MarketingTaskConfigBO;

public interface IMarketingTaskConfigLogic {

    /**
     * 获取任务配置信息
     */
    MarketingTaskConfigBO acquireTaskConfig(String taskCode);

    MarketingTaskConfigBO acquireTaskConfig(MarketingTaskEntity taskEntity);

    /**
     * 任务详情
     *
     * @param taskCode
     * @return
     */
    MarketingTaskConfigBO queryTaskDetail(String taskCode);

    /**
     * 新建任务配置
     */
    Boolean saveTaskConfig(MarketingTaskConfigAddBO marketingTaskConfigBO);

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

    /**
     * 校验素材是否正确
     */
    WsMaterialMediaResp.Media previewWsMaterialMedia(MarketingTaskMediaBO marketingTaskMediaBO);

}