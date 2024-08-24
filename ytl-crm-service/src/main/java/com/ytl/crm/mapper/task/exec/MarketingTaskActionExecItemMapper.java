package com.ytl.crm.mapper.task.exec;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ytl.crm.domain.entity.common.TwoValueCountResult;
import com.ytl.crm.domain.entity.task.exec.MarketingTaskActionExecItemEntity;
import com.ytl.crm.domain.req.exec.TaskActionExecResultItemListReq;
import com.ytl.crm.domain.resp.task.exec.TaskActionExecResultItem;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
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

    List<TwoValueCountResult> countForBatchAction(@Param("actionCodes") Collection<String> actionCodes);

    List<TwoValueCountResult> countExecRet(@Param("actionCodes") Collection<String> actionCodes);

    Integer countBatchActionResultItem(@Param("param") TaskActionExecResultItemListReq listReq);

    List<TaskActionExecResultItem> listBatchActionResultItem(@Param("param") TaskActionExecResultItemListReq listReq,
                                                             @Param("offset") Integer offset,
                                                             @Param("size") Integer size);

    Integer countActionResultItem(@Param("param") TaskActionExecResultItemListReq listReq);

    List<TaskActionExecResultItem> listActionResultItem(@Param("param") TaskActionExecResultItemListReq listReq,
                                                        @Param("offset") Integer offset,
                                                        @Param("size") Integer size);

}
