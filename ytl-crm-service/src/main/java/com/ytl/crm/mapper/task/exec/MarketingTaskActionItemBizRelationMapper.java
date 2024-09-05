package com.ytl.crm.mapper.task.exec;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ytl.crm.domain.entity.task.exec.MarketingTaskActionItemBizRelationEntity;

import java.util.List;

/**
 * <p>
 * 营销任务-动作执行明细和业务信息关联表 Mapper 接口
 * </p>
 *
 * @author hongj
 * @since 2024-08-29
 */
public interface MarketingTaskActionItemBizRelationMapper extends BaseMapper<MarketingTaskActionItemBizRelationEntity> {

    List<String> selectBizCodeByActionRecordCode(String actionRecordCode);

}
