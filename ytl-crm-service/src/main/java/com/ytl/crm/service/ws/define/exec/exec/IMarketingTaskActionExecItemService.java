package com.ytl.crm.service.ws.define.exec.exec;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ytl.crm.domain.entity.common.TwoValueCountResult;
import com.ytl.crm.domain.entity.task.exec.MarketingTaskActionExecItemEntity;
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

    boolean batchSaveItem(List<MarketingTaskActionExecItemEntity> itemEntityList);

    List<MarketingTaskActionExecItemEntity> queryByActionRecordCode(String actionRecordCode);

    List<MarketingTaskActionExecItemEntity> queryByBizInfoCode(String actionRecordCode, Collection<String> bizInfoCodeList);

    List<MarketingTaskActionExecItemEntity> queryKeeperInitItem(String actionRecordCode, String keeperCode);

    List<MarketingTaskActionExecItemEntity> queryWaitCallBackItem(String actionRecordCode, String keeperVirtualId);

    List<MarketingTaskActionExecItemEntity> queryWaitCompensateItem(String actionRecordCode, String keeperVirtualId);

    boolean batchUpdateCallBackRet(List<Long> idList, String callBackStatus, String thirdExecStatus, Date thirdExecTime);

    boolean updateItemAfterDoAction(String logicCode, String fromExecStatus, String toExecStatus, String thirdTaskId, String execMsg, String callBackStatus);

    boolean updateItemAfterDoAction(List<Long> idList, String fromExecStatus, String toExecStatus, String thirdTaskId, String execMsg, String callBackStatus);

    boolean batchUpdateSendStatus(List<Long> idList, String thirdExecStatus, Date thirdExecTime);

    /**
     * 针对补偿任务进行处理
     *
     * @param oldItemIdList 老的item的id
     * @param newItemList   老的item数据
     */
    void batchSaveCompensateItem(List<Long> oldItemIdList, List<MarketingTaskActionExecItemEntity> newItemList);

    /**
     * 更具三方任务id进行查询
     */
    List<MarketingTaskActionExecItemEntity> queryByThirdTaskId(String thirdTaskId);

    /**
     * 通过动作执行记录code和业务信息code查找
     *
     * @param execRecordCodes 执行记录code
     * @param taskBizCode     任务业务信息code
     */
    List<MarketingTaskActionExecItemEntity> queryByExecCodeAndBizCode(Collection<String> execRecordCodes, String taskBizCode);

    List<TwoValueCountResult> countForBatchAction(Collection<String> actionCodes);

    List<TwoValueCountResult> countExecRet(Collection<String> actionCodes);

    PageResp<TaskActionExecResultItem> listBatchActionResultItem(TaskActionExecResultItemListReq listReq);

    PageResp<TaskActionExecResultItem> listActionResultItem(TaskActionExecResultItemListReq listReq);

}
