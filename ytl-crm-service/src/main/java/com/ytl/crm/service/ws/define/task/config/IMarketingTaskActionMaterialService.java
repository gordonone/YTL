package com.ytl.crm.service.ws.define.task.config;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ytl.crm.domain.entity.task.config.MarketingTaskActionMaterialEntity;

import java.util.List;

/**
 * <p>
 * 营销任务-动作素材表 服务类
 * </p>
 *
 * @author hongj
 * @since 2024-08-12
 */
public interface IMarketingTaskActionMaterialService extends IService<MarketingTaskActionMaterialEntity> {

    List<MarketingTaskActionMaterialEntity> listByTaskCode(String taskCode);

}