package com.ytl.crm.service.ws.Impl.exec.exec;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ytl.crm.domain.entity.common.TwoValueCountResult;
import com.ytl.crm.domain.entity.task.exec.MarketingTaskActionExecItemEntity;
import com.ytl.crm.domain.enums.common.YesOrNoEnum;
import com.ytl.crm.domain.enums.task.exec.ActionThirdTaskExecStatus;
import com.ytl.crm.domain.enums.task.exec.TaskActionCallBackStatusEnum;
import com.ytl.crm.domain.enums.task.exec.TaskActionItemExecStatusEnum;
import com.ytl.crm.domain.req.exec.TaskActionExecResultItemListReq;
import com.ytl.crm.domain.resp.common.PageResp;
import com.ytl.crm.domain.resp.task.exec.TaskActionExecResultItem;
import com.ytl.crm.mapper.task.exec.MarketingTaskActionExecItemMapper;
import com.ytl.crm.service.ws.define.exec.exec.IMarketingTaskActionExecItemService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 营销任务-动作执行记录-明细表 服务实现类
 * </p>
 *
 * @author hongj
 * @since 2024-08-12
 */
@Slf4j
@Service
public class MarketingTaskActionExecItemServiceImpl extends ServiceImpl<MarketingTaskActionExecItemMapper, MarketingTaskActionExecItemEntity> implements IMarketingTaskActionExecItemService {

    @Resource
    private MarketingTaskActionExecItemMapper marketingTaskActionExecItemMapper;
    @Resource
    private SqlSessionFactory sqlSessionFactory;

    @Override
    public boolean batchSaveItem(List<MarketingTaskActionExecItemEntity> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            return true;
        }
        if (entityList.size() == 1) {
            MarketingTaskActionExecItemEntity bizInfoEntity = entityList.get(0);
            return save(bizInfoEntity);
        }

        int result = 0;
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        try {
            MarketingTaskActionExecItemMapper mapper = sqlSession.getMapper(MarketingTaskActionExecItemMapper.class);
            //每1000条提交一次
            int batchCount = 1000;
            for (int index = 0; index < entityList.size(); index++) {
                MarketingTaskActionExecItemEntity bizInfoEntity = entityList.get(index);
                mapper.insert(bizInfoEntity);
                if (index != 0 && index % batchCount == 0) {
                    sqlSession.commit();
                }
                log.info("批量保存动作执行项数据进度，index={}", index);
            }
            sqlSession.commit();
            sqlSession.clearCache();
            result = entityList.size();
        } catch (Exception e) {
            sqlSession.rollback();
            result = -1;
            log.error("批量保存动作执行项数据异常回滚", e);
        } finally {
            if (sqlSession != null) {
                sqlSession.close();
            }
        }
        return result != entityList.size();

    }

    @Override
    public List<MarketingTaskActionExecItemEntity> queryByActionRecordCode(String actionRecordCode) {
        LambdaQueryWrapper<MarketingTaskActionExecItemEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(MarketingTaskActionExecItemEntity::getActionRecordCode, actionRecordCode);
        wrapper.eq(MarketingTaskActionExecItemEntity::getIsValid, YesOrNoEnum.YES.getCode());
        return list(wrapper);
    }

    @Override
    public List<MarketingTaskActionExecItemEntity> queryByBizInfoCode(String actionRecordCode, Collection<String> bizInfoCodeList) {
        LambdaQueryWrapper<MarketingTaskActionExecItemEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(MarketingTaskActionExecItemEntity::getActionRecordCode, actionRecordCode);
        wrapper.in(MarketingTaskActionExecItemEntity::getTaskBizCode, bizInfoCodeList);
        wrapper.eq(MarketingTaskActionExecItemEntity::getIsValid, YesOrNoEnum.YES.getCode());
        return list(wrapper);
    }

    @Override
    public List<MarketingTaskActionExecItemEntity> queryKeeperInitItem(String actionRecordCode, String keeperVirtualId) {
        LambdaQueryWrapper<MarketingTaskActionExecItemEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(MarketingTaskActionExecItemEntity::getActionRecordCode, actionRecordCode);
        wrapper.eq(MarketingTaskActionExecItemEntity::getVirtualKeeperId, keeperVirtualId);
        wrapper.eq(MarketingTaskActionExecItemEntity::getExecStatus, TaskActionItemExecStatusEnum.INIT.getCode());
        wrapper.eq(MarketingTaskActionExecItemEntity::getIsValid, YesOrNoEnum.YES.getCode());
        return list(wrapper);
    }

    @Override
    public List<MarketingTaskActionExecItemEntity> queryWaitCallBackItem(String actionRecordCode, String keeperVirtualId) {
        LambdaQueryWrapper<MarketingTaskActionExecItemEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(MarketingTaskActionExecItemEntity::getActionRecordCode, actionRecordCode);
        wrapper.eq(MarketingTaskActionExecItemEntity::getVirtualKeeperId, keeperVirtualId);
        wrapper.eq(MarketingTaskActionExecItemEntity::getExecStatus, TaskActionItemExecStatusEnum.SUCCESS.getCode());
        wrapper.eq(MarketingTaskActionExecItemEntity::getCallBackStatus, TaskActionCallBackStatusEnum.WAIT.getCode());
        wrapper.eq(MarketingTaskActionExecItemEntity::getIsValid, YesOrNoEnum.YES.getCode());
        return list(wrapper);
    }

    @Override
    public List<MarketingTaskActionExecItemEntity> queryWaitCompensateItem(String actionRecordCode, String keeperVirtualId) {
        LambdaQueryWrapper<MarketingTaskActionExecItemEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(MarketingTaskActionExecItemEntity::getActionRecordCode, actionRecordCode);
        wrapper.eq(MarketingTaskActionExecItemEntity::getVirtualKeeperId, keeperVirtualId);
        wrapper.eq(MarketingTaskActionExecItemEntity::getExecStatus, TaskActionItemExecStatusEnum.SUCCESS.getCode());
        wrapper.eq(MarketingTaskActionExecItemEntity::getCallBackStatus, TaskActionCallBackStatusEnum.SUCCESS.getCode());
        wrapper.eq(MarketingTaskActionExecItemEntity::getThirdTaskExecStatus, ActionThirdTaskExecStatus.NOT_EXEC.getCode());
        wrapper.eq(MarketingTaskActionExecItemEntity::getIsValid, YesOrNoEnum.YES.getCode());
        return list(wrapper);
    }

    @Override
    public boolean batchUpdateCallBackRet(List<Long> idList, String callBackStatus, String thirdExecStatus, Date thirdExecTime) {
        LambdaUpdateWrapper<MarketingTaskActionExecItemEntity> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.set(MarketingTaskActionExecItemEntity::getCallBackStatus, callBackStatus);
        updateWrapper.set(MarketingTaskActionExecItemEntity::getThirdTaskExecStatus, thirdExecStatus);
        updateWrapper.set(MarketingTaskActionExecItemEntity::getThirdTaskExecTime, thirdExecTime);
        //条件
        updateWrapper.in(MarketingTaskActionExecItemEntity::getId, idList);
        updateWrapper.eq(MarketingTaskActionExecItemEntity::getCallBackStatus, TaskActionCallBackStatusEnum.WAIT.getCode());
        return update(updateWrapper);
    }

    @Override
    public boolean updateItemAfterDoAction(String logicCode, String fromExecStatus, String toExecStatus,
                                           String thirdTaskId, String execMsg, String callBackStatus) {
        LambdaUpdateWrapper<MarketingTaskActionExecItemEntity> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.set(MarketingTaskActionExecItemEntity::getExecStatus, toExecStatus);
        updateWrapper.set(MarketingTaskActionExecItemEntity::getThirdTaskId, thirdTaskId);
        if (StringUtils.isNotBlank(execMsg)) {
            updateWrapper.set(MarketingTaskActionExecItemEntity::getExecMsg, execMsg);
        }
        if (StringUtils.isNotBlank(callBackStatus)) {
            updateWrapper.set(MarketingTaskActionExecItemEntity::getCallBackStatus, callBackStatus);
        }
        //条件
        updateWrapper.eq(MarketingTaskActionExecItemEntity::getLogicCode, logicCode);
        updateWrapper.eq(MarketingTaskActionExecItemEntity::getExecStatus, fromExecStatus);
        return update(updateWrapper);
    }

    @Override
    public boolean updateItemAfterDoAction(List<Long> idList, String fromExecStatus, String toExecStatus,
                                           String thirdTaskId, String execMsg, String callBackStatus) {
        LambdaUpdateWrapper<MarketingTaskActionExecItemEntity> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.set(MarketingTaskActionExecItemEntity::getExecStatus, toExecStatus)
                .set(MarketingTaskActionExecItemEntity::getThirdTaskId, thirdTaskId);
        if (StringUtils.isNotBlank(execMsg)) {
            updateWrapper.set(MarketingTaskActionExecItemEntity::getExecMsg, execMsg);
        }
        if (StringUtils.isNotBlank(callBackStatus)) {
            updateWrapper.set(MarketingTaskActionExecItemEntity::getCallBackStatus, callBackStatus);
        }
        //条件
        updateWrapper.in(MarketingTaskActionExecItemEntity::getId, idList)
                .eq(MarketingTaskActionExecItemEntity::getExecStatus, fromExecStatus);
        return update(updateWrapper);
    }

    @Override
    public boolean batchUpdateSendStatus(List<Long> idList, String thirdExecStatus, Date thirdExecTime) {
        LambdaUpdateWrapper<MarketingTaskActionExecItemEntity> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.set(MarketingTaskActionExecItemEntity::getThirdTaskExecStatus, thirdExecStatus);
        updateWrapper.set(MarketingTaskActionExecItemEntity::getThirdTaskExecTime, thirdExecTime);
        //条件
        updateWrapper.in(MarketingTaskActionExecItemEntity::getId, idList);
        return update(updateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchSaveCompensateItem(List<Long> oldItemIdList, List<MarketingTaskActionExecItemEntity> newItemList) {
        //1.更新老数据为无效
        LambdaUpdateWrapper<MarketingTaskActionExecItemEntity> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.set(MarketingTaskActionExecItemEntity::getIsValid, YesOrNoEnum.NO.getCode());
        updateWrapper.in(MarketingTaskActionExecItemEntity::getId, oldItemIdList);
        boolean updateRet = update(updateWrapper);
        log.info("更新老execItem状态为无效结果，updateRet={}", updateRet);

        //2.新增新数据
        boolean saveRet = saveBatch(newItemList);
        log.info("保存新的execItem数据，saveRet={}", saveRet);
    }

    @Override
    public List<MarketingTaskActionExecItemEntity> queryByThirdTaskId(String thirdTaskId) {
        LambdaQueryWrapper<MarketingTaskActionExecItemEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(MarketingTaskActionExecItemEntity::getThirdTaskId, thirdTaskId);
        wrapper.eq(MarketingTaskActionExecItemEntity::getIsValid, YesOrNoEnum.YES.getCode());
        return list(wrapper);
    }

    @Override
    public List<MarketingTaskActionExecItemEntity> queryByExecCodeAndBizCode(Collection<String> actionRecordCodes, String taskBizCode) {
        LambdaQueryWrapper<MarketingTaskActionExecItemEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.in(MarketingTaskActionExecItemEntity::getLogicCode, actionRecordCodes);
        wrapper.eq(MarketingTaskActionExecItemEntity::getTaskBizCode, taskBizCode);
        wrapper.eq(MarketingTaskActionExecItemEntity::getIsValid, YesOrNoEnum.YES.getCode());
        return list(wrapper);
    }

    @Override
    public List<TwoValueCountResult> countForBatchAction(Collection<String> actionCodes) {
        if (CollectionUtils.isEmpty(actionCodes)) {
            return Collections.emptyList();
        }
        return marketingTaskActionExecItemMapper.countForBatchAction(actionCodes);
    }

    @Override
    public List<TwoValueCountResult> countExecRet(Collection<String> actionCodes) {
        if (CollectionUtils.isEmpty(actionCodes)) {
            return Collections.emptyList();
        }
        return marketingTaskActionExecItemMapper.countExecRet(actionCodes);
    }

    @Override
    public PageResp<TaskActionExecResultItem> listBatchActionResultItem(TaskActionExecResultItemListReq listReq) {
        Integer total = marketingTaskActionExecItemMapper.countBatchActionResultItem(listReq);
        if (NumberUtils.INTEGER_ZERO.equals(total)) {
            return PageResp.emptyResp();
        }
        int pageSize = listReq.getPageSize();
        int pageNum = listReq.getPageNum();
        int offset = (pageNum - 1) * pageSize;
        List<TaskActionExecResultItem> dataList = marketingTaskActionExecItemMapper.listBatchActionResultItem(listReq, offset, pageSize);
        return PageResp.buildResp(total, dataList);
    }

    @Override
    public PageResp<TaskActionExecResultItem> listActionResultItem(TaskActionExecResultItemListReq listReq) {
        Integer total = marketingTaskActionExecItemMapper.countActionResultItem(listReq);
        if (NumberUtils.INTEGER_ZERO.equals(total)) {
            return PageResp.emptyResp();
        }
        int pageSize = listReq.getPageSize();
        int pageNum = listReq.getPageNum();
        int offset = (pageNum - 1) * pageSize;
        List<TaskActionExecResultItem> dataList = marketingTaskActionExecItemMapper.listActionResultItem(listReq, offset, pageSize);
        return PageResp.buildResp(total, dataList);
    }

}
