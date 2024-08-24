package com.ytl.crm.service.ws.define.exec.config;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ytl.crm.domain.entity.task.config.MarketingTaskActionEntity;

import java.util.List;

/**
 * <p>
 * 营销任务-动作表 服务类
 * </p>
 *
 * @author hongj
 * @since 2024-08-12
 */
public interface IMarketingTaskActionService extends IService<MarketingTaskActionEntity> {

    List<MarketingTaskActionEntity> listByTaskCode(String taskCode);

    MarketingTaskActionEntity queryByLogicCode(String actionCode);

}
