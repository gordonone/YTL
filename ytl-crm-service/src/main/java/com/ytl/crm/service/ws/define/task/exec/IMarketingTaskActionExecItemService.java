package com.ytl.crm.service.ws.define.task.exec;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ytl.crm.domain.bo.task.common.TwoValueCountResult;
import com.ytl.crm.domain.entity.task.exec.MarketingTaskActionExecItemEntity;
import com.ytl.crm.domain.entity.task.exec.MarketingTaskActionItemBizRelationEntity;
import com.ytl.crm.domain.enums.task.exec.TaskActionItemExecStatusEnum;
import com.ytl.crm.domain.enums.task.ret.TaskActionItemFinalRetEnum;
import com.ytl.crm.domain.req.exec.TaskActionExecResultItemListReq;
import com.ytl.crm.domain.resp.common.PageResp;
import com.ytl.crm.domain.resp.task.exec.TaskActionExecResultItem;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 营销任务-动作执行记录-明细表 服务类
 * </p>
 *
 * @author hongj
 * @since 2024-08-12
 */
public interface IMarketingTaskActionExecItemService extends IService<MarketingTaskActionExecItemEntity> {

    /**
     * 保存Item数据
     */
    boolean saveItemAndRelation(MarketingTaskActionExecItemEntity itemEntity, List<MarketingTaskActionItemBizRelationEntity> relationList);

    /**
     * 补偿时保存数据
     */
    boolean saveWhenCompensate(MarketingTaskActionExecItemEntity oldItem, MarketingTaskActionExecItemEntity newItem,
                               List<MarketingTaskActionItemBizRelationEntity> relationList);

    /**
     * 根据执行状态查询数据 - list
     */
    List<MarketingTaskActionExecItemEntity> listByExecStatus(String actionRecordCode, TaskActionItemExecStatusEnum execStatus, Integer isCompensate);

    /**
     * 根据执行状态查询数据 - getOne
     */
    MarketingTaskActionExecItemEntity getOneByExecStatus(String actionRecordCode, TaskActionItemExecStatusEnum execStatus, Integer isCompensate);

    /**
     * 根据logicCode更新Item状态 - 动作执行成功
     */
    boolean updateItemAfterExecSuccess(String logicCode, TaskActionItemExecStatusEnum fromExecStatus, TaskActionItemExecStatusEnum toExecStatus,
                                       String thirdTaskId, Date thirdTaskCreateTime, TaskActionItemFinalRetEnum finalRetEnum);

    /**
     * 根据logicCode更新Item状态 - 动作执行失败
     */
    boolean updateItemAfterExecFail(String logicCode, TaskActionItemExecStatusEnum fromExecStatus, TaskActionItemExecStatusEnum toExecStatus,
                                    String execMsg, TaskActionItemFinalRetEnum finalRetEnum);

    /**
     * 回调成功 - 指接口调用成功
     */
    boolean updateItemAfterCallBackSuccess(String logicCode, TaskActionItemExecStatusEnum fromExecStatus, TaskActionItemExecStatusEnum toExecStatus,
                                           String thirdTaskExecStatus, Date thirdTaskExecTime, TaskActionItemFinalRetEnum finalRetEnum);

    /**
     * 回调失败 - 指接口调用失败
     */
    boolean updateItemAfterCallBackFail(String logicCode, TaskActionItemExecStatusEnum fromExecStatus, TaskActionItemExecStatusEnum toExecStatus,
                                        String execMsg, TaskActionItemFinalRetEnum finalRetEnum);

    boolean updateExecStatusByTriggerCode(String triggerCode, TaskActionItemExecStatusEnum fromExecStatus,
                                          TaskActionItemExecStatusEnum toExecStatus, String execMsg);

    /**
     * 根据三方任务id进行查询
     */
    List<MarketingTaskActionExecItemEntity> queryByThirdTaskId(String thirdTaskId);

    /**
     * 根据最终执行结果进行统计
     */
    List<TwoValueCountResult> countByExecFinalRet(Collection<String> actionCodes);

    /**
     * 分页查询执行结果
     */
    PageResp<TaskActionExecResultItem> pageActionItemExecResult(TaskActionExecResultItemListReq listReq);

    /**
     * 根据执行记录code + 业务code
     */
    List<MarketingTaskActionExecItemEntity> queryByActionRecordCodeAndTaskBizCode(Collection<String> actionRecordCodes, String taskBizCode);

    /**
     * 根据状态查询对应的actionRecordCode
     */
    List<String> listActionRecordCodeByExecStatus(String triggerCode, TaskActionItemExecStatusEnum execStatusEnum);

    /**
     * 根据状态查询对应的triggerCode
     */
    List<String> listTriggerCodeByExecStatus(TaskActionItemExecStatusEnum execStatusEnum, Date startTime, Date endTime);

    /**
     * 根据原itemCode进行查询，用于补偿
     */
    MarketingTaskActionExecItemEntity getOneBySourceItemCode(String sourceItemCode);

}
