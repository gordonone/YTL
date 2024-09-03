package com.ytl.crm.service.ws.define.exec.exec;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ytl.crm.domain.entity.task.exec.MarketingTaskActionItemBizRelationEntity;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 营销任务-动作执行明细和业务信息关联表 服务类
 * </p>
 *
 * @author hongj
 * @since 2024-08-29
 */
public interface IMarketingTaskActionItemBizRelationService extends IService<MarketingTaskActionItemBizRelationEntity> {

    Set<String> queryExistBizCode(String actionRecordCode);

    List<MarketingTaskActionItemBizRelationEntity> listByItemCode(String itemCode);

    MarketingTaskActionItemBizRelationEntity getOneByItemCode(String itemCode);

    List<MarketingTaskActionItemBizRelationEntity> queryByActionRecordCodeAndTaskBizCode(Collection<String> actionRecordCodes, String taskBizCode);


}
