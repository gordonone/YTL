package com.ytl.crm.mapper.task.exec;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ytl.crm.domain.entity.task.exec.MarketingTaskTriggerRecordEntity;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 营销任务-触发记录表 Mapper 接口
 * </p>
 *
 * @author hongj
 * @since 2024-08-12
 */
public interface MarketingTaskTriggerRecordMapper extends BaseMapper<MarketingTaskTriggerRecordEntity> {

    List<String> selectWaitCompensateRecordCode(@Param("createTimeStart") Date createTimeStart,
                                                @Param("createTimeEnd") Date createTimeEnd);
}
