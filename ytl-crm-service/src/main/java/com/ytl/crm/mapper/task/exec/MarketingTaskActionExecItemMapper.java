package com.ytl.crm.mapper.task.exec;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ytl.crm.domain.entity.common.TwoValueCountResult;
import com.ytl.crm.domain.entity.task.exec.MarketingTaskActionExecItemEntity;
import com.ytl.crm.domain.req.exec.TaskActionExecResultItemListReq;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 营销任务-动作执行记录-明细表 Mapper 接口
 * </p>
 *
 * @author hongj
 * @since 2024-08-12
 */
public interface MarketingTaskActionExecItemMapper extends BaseMapper<MarketingTaskActionExecItemEntity> {

    List<TwoValueCountResult> countByExecFinalRet(@Param("actionCodes") Collection<String> actionCodes);

    Integer countActionItem(@Param("param") TaskActionExecResultItemListReq listReq);

    List<MarketingTaskActionExecItemEntity> listActionItem(@Param("param") TaskActionExecResultItemListReq listReq,
                                                           @Param("offset") Integer offset,
                                                           @Param("size") Integer size);

    List<String> listActionRecordCodeByExecStatus(@Param("triggerCode") String triggerCode,
                                                  @Param("execStatus") String execStatus);

    List<String> listTriggerCodeByExecStatus(@Param("execStatus") String execStatus,
                                             @Param("startTime") Date startTime,
                                             @Param("endTime") Date endTime);

}
