package com.ytl.crm.service.ws.impl.task.exec;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.ytl.crm.domain.bo.task.common.TwoValueCountResult;
import com.ytl.crm.domain.entity.task.config.MarketingTaskActionEntity;
import com.ytl.crm.domain.entity.task.exec.MarketingTaskActionExecItemEntity;
import com.ytl.crm.domain.entity.task.exec.MarketingTaskActionItemBizRelationEntity;
import com.ytl.crm.domain.enums.common.YesOrNoEnum;
import com.ytl.crm.domain.enums.task.exec.TaskActionItemExecStatusEnum;
import com.ytl.crm.domain.enums.task.ret.TaskActionItemFinalRetEnum;
import com.ytl.crm.domain.req.exec.TaskActionExecResultItemListReq;
import com.ytl.crm.domain.resp.common.PageResp;
import com.ytl.crm.domain.resp.task.exec.TaskActionExecResultItem;
import com.ytl.crm.mapper.task.exec.MarketingTaskActionExecItemMapper;
import com.ytl.crm.service.ws.define.task.config.IMarketingTaskActionService;
import com.ytl.crm.service.ws.define.task.exec.IMarketingTaskActionExecItemService;
import com.ytl.crm.service.ws.define.task.exec.IMarketingTaskActionItemBizRelationService;
import com.ytl.crm.utils.EnumQueryUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    private IMarketingTaskActionItemBizRelationService iMarketingTaskActionItemBizRelationService;
    @Resource
    private IMarketingTaskActionService iMarketingTaskActionService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveItemAndRelation(MarketingTaskActionExecItemEntity itemEntity, List<MarketingTaskActionItemBizRelationEntity> relationList) {
        //1.保存item
        boolean itemSaveRet = save(itemEntity);
        log.info("保存新的execItem数据，saveRet={}", itemSaveRet);

        //2.保存关系
        boolean relSaveRet = iMarketingTaskActionItemBizRelationService.saveBatch(relationList);
        log.info("保存新的关系数据，saveRet={}", itemSaveRet);
        return itemSaveRet && relSaveRet;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveWhenCompensate(MarketingTaskActionExecItemEntity oldItem, MarketingTaskActionExecItemEntity newItem,
                                      List<MarketingTaskActionItemBizRelationEntity> relationList) {
        //1.保存item
        boolean itemSaveRet = save(newItem);
        log.info("保存新的execItem数据，saveRet={}", itemSaveRet);

        //2.保存关系
        boolean relSaveRet = iMarketingTaskActionItemBizRelationService.saveBatch(relationList);
        log.info("保存新的关系数据，saveRet={}", itemSaveRet);

        //3.更新老数据为无效
        LambdaUpdateWrapper<MarketingTaskActionExecItemEntity> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.set(MarketingTaskActionExecItemEntity::getIsValid, YesOrNoEnum.NO.getCode());
        updateWrapper.set(MarketingTaskActionExecItemEntity::getExecStatus, TaskActionItemExecStatusEnum.FINISH.getCode());
        updateWrapper.set(MarketingTaskActionExecItemEntity::getExecMsg, "已发起补偿");

        updateWrapper.eq(MarketingTaskActionExecItemEntity::getExecStatus, TaskActionItemExecStatusEnum.WAIT_COMPENSATE.getCode());
        updateWrapper.eq(MarketingTaskActionExecItemEntity::getId, oldItem.getId());
        boolean updateRet = update(updateWrapper);
        log.info("更新老execItem状态为无效结果，updateRet={}", updateRet);
        return itemSaveRet && relSaveRet && updateRet;
    }

    @Override
    public List<MarketingTaskActionExecItemEntity> listByExecStatus(String actionRecordCode, TaskActionItemExecStatusEnum execStatusEnum, Integer isCompensate) {
        LambdaQueryWrapper<MarketingTaskActionExecItemEntity> wrapper = buildByExecStatusWrapper(actionRecordCode, execStatusEnum, isCompensate);
        return list(wrapper);
    }

    @Override
    public MarketingTaskActionExecItemEntity getOneByExecStatus(String actionRecordCode, TaskActionItemExecStatusEnum execStatusEnum, Integer isCompensate) {
        LambdaQueryWrapper<MarketingTaskActionExecItemEntity> wrapper = buildByExecStatusWrapper(actionRecordCode, execStatusEnum, isCompensate);
        wrapper.last(" LIMIT 1");
        return getOne(wrapper);
    }

    private LambdaQueryWrapper<MarketingTaskActionExecItemEntity> buildByExecStatusWrapper(String actionRecordCode,
                                                                                           TaskActionItemExecStatusEnum execStatusEnum,
                                                                                           Integer isCompensate) {
        LambdaQueryWrapper<MarketingTaskActionExecItemEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(MarketingTaskActionExecItemEntity::getActionRecordCode, actionRecordCode);
        wrapper.eq(MarketingTaskActionExecItemEntity::getExecStatus, execStatusEnum.getCode());
        wrapper.eq(MarketingTaskActionExecItemEntity::getIsValid, YesOrNoEnum.YES.getCode());
        if (isCompensate != null) {
            wrapper.eq(MarketingTaskActionExecItemEntity::getIsCompensate, isCompensate);
        }
        return wrapper;
    }

    @Override
    public boolean updateItemAfterExecSuccess(String logicCode, TaskActionItemExecStatusEnum fromExecStatus, TaskActionItemExecStatusEnum toExecStatus,
                                              String thirdTaskId, Date thirdTaskCreateTime, TaskActionItemFinalRetEnum finalRetEnum) {
        LambdaUpdateWrapper<MarketingTaskActionExecItemEntity> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.set(MarketingTaskActionExecItemEntity::getExecStatus, toExecStatus.getCode());
        updateWrapper.set(MarketingTaskActionExecItemEntity::getThirdTaskCreateTime, thirdTaskCreateTime);
        updateWrapper.set(MarketingTaskActionExecItemEntity::getThirdTaskId, thirdTaskId);
        if (finalRetEnum != null) {
            updateWrapper.set(MarketingTaskActionExecItemEntity::getFinalExecRet, finalRetEnum.getCode());
        }

        //条件
        updateWrapper.eq(MarketingTaskActionExecItemEntity::getLogicCode, logicCode);
        updateWrapper.eq(MarketingTaskActionExecItemEntity::getExecStatus, TaskActionItemExecStatusEnum.WAIT_EXEC.getCode());
        return update(updateWrapper);
    }

    @Override
    public boolean updateItemAfterExecFail(String logicCode, TaskActionItemExecStatusEnum fromExecStatus, TaskActionItemExecStatusEnum toExecStatus,
                                           String execMsg, TaskActionItemFinalRetEnum finalRetEnum) {
        LambdaUpdateWrapper<MarketingTaskActionExecItemEntity> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.set(MarketingTaskActionExecItemEntity::getExecStatus, toExecStatus.getCode());
        updateWrapper.set(MarketingTaskActionExecItemEntity::getFinalExecRet, finalRetEnum.getCode());
        if (StringUtils.isNotBlank(execMsg)) {
            updateWrapper.set(MarketingTaskActionExecItemEntity::getExecMsg, execMsg);
        }
        //条件
        updateWrapper.eq(MarketingTaskActionExecItemEntity::getLogicCode, logicCode);
        updateWrapper.eq(MarketingTaskActionExecItemEntity::getExecStatus, fromExecStatus.getCode());
        return update(updateWrapper);
    }

    @Override
    public boolean updateItemAfterCallBackSuccess(String logicCode, TaskActionItemExecStatusEnum fromExecStatus, TaskActionItemExecStatusEnum toExecStatus,
                                                  String thirdTaskExecStatus, Date thirdTaskExecTime, TaskActionItemFinalRetEnum finalRetEnum) {
        LambdaUpdateWrapper<MarketingTaskActionExecItemEntity> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.set(MarketingTaskActionExecItemEntity::getExecStatus, toExecStatus.getCode());
        updateWrapper.set(MarketingTaskActionExecItemEntity::getThirdTaskExecStatus, thirdTaskExecStatus);
        updateWrapper.set(MarketingTaskActionExecItemEntity::getFinalExecRet, finalRetEnum.getCode());
        if (thirdTaskExecTime != null) {
            updateWrapper.set(MarketingTaskActionExecItemEntity::getThirdTaskExecTime, thirdTaskExecTime);
        }
        //条件
        updateWrapper.eq(MarketingTaskActionExecItemEntity::getLogicCode, logicCode);
        updateWrapper.eq(MarketingTaskActionExecItemEntity::getExecStatus, fromExecStatus.getCode());
        return update(updateWrapper);
    }

    @Override
    public boolean updateItemAfterCallBackFail(String logicCode, TaskActionItemExecStatusEnum fromExecStatus, TaskActionItemExecStatusEnum toExecStatus,
                                               String execMsg, TaskActionItemFinalRetEnum finalRetEnum) {
        LambdaUpdateWrapper<MarketingTaskActionExecItemEntity> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.set(MarketingTaskActionExecItemEntity::getExecStatus, toExecStatus.getCode());
        if (StringUtils.isNotBlank(execMsg)) {
            updateWrapper.set(MarketingTaskActionExecItemEntity::getExecMsg, execMsg);
        }
        updateWrapper.set(MarketingTaskActionExecItemEntity::getFinalExecRet, finalRetEnum.getCode());
        //条件
        updateWrapper.eq(MarketingTaskActionExecItemEntity::getLogicCode, logicCode);
        updateWrapper.eq(MarketingTaskActionExecItemEntity::getExecStatus, fromExecStatus.getCode());
        return update(updateWrapper);
    }

    @Override
    public boolean updateExecStatusByTriggerCode(String triggerCode, TaskActionItemExecStatusEnum fromExecStatus, TaskActionItemExecStatusEnum toExecStatus, String execMsg) {
        LambdaUpdateWrapper<MarketingTaskActionExecItemEntity> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.set(MarketingTaskActionExecItemEntity::getExecStatus, toExecStatus.getCode());
        if (StringUtils.isNotBlank(execMsg)) {
            updateWrapper.set(MarketingTaskActionExecItemEntity::getExecMsg, execMsg);
        }
        //条件
        updateWrapper.eq(MarketingTaskActionExecItemEntity::getTriggerCode, triggerCode);
        updateWrapper.eq(MarketingTaskActionExecItemEntity::getExecStatus, fromExecStatus.getCode());
        return update(updateWrapper);
    }

    @Override
    public List<MarketingTaskActionExecItemEntity> queryByThirdTaskId(String thirdTaskId) {
        LambdaQueryWrapper<MarketingTaskActionExecItemEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(MarketingTaskActionExecItemEntity::getThirdTaskId, thirdTaskId);
        wrapper.eq(MarketingTaskActionExecItemEntity::getIsValid, YesOrNoEnum.YES.getCode());
        return list(wrapper);
    }

    @Override
    public List<TwoValueCountResult> countByExecFinalRet(Collection<String> actionCodes) {
        return marketingTaskActionExecItemMapper.countByExecFinalRet(actionCodes);
    }

    @Override
    public PageResp<TaskActionExecResultItem> pageActionItemExecResult(TaskActionExecResultItemListReq listReq) {
        Integer total = marketingTaskActionExecItemMapper.countActionItem(listReq);
        if (NumberUtils.INTEGER_ZERO.equals(total)) {
            return PageResp.emptyResp();
        }
        int pageSize = listReq.getPageSize();
        int pageNum = listReq.getPageNum();
        int offset = (pageNum - 1) * pageSize;

        List<MarketingTaskActionExecItemEntity> itemList = marketingTaskActionExecItemMapper.listActionItem(listReq, offset, pageSize);
        if (CollectionUtils.isEmpty(itemList)) {
            return PageResp.emptyResp();
        }

        List<MarketingTaskActionEntity> actionList = iMarketingTaskActionService.listByTaskCode(listReq.getTaskCode());
        Map<String, MarketingTaskActionEntity> actionMap = actionList.stream()
                .collect(Collectors.toMap(MarketingTaskActionEntity::getLogicCode, Function.identity(), (k1, k2) -> k1));
        List<TaskActionExecResultItem> dataList = Lists.newArrayList();
        for (MarketingTaskActionExecItemEntity itemEntity : itemList) {
            MarketingTaskActionEntity taskAction = actionMap.get(itemEntity.getActionCode());
            dataList.add(convertToResultItem(itemEntity, taskAction));
        }
        return PageResp.buildResp(total, dataList);
    }

    private TaskActionExecResultItem convertToResultItem(MarketingTaskActionExecItemEntity item, MarketingTaskActionEntity action) {
        TaskActionExecResultItem resultItem = new TaskActionExecResultItem();
        resultItem.setVirtualKeeperId(item.getVirtualKeeperId());
        resultItem.setVirtualKeeperName(item.getVirtualKeeperName());
        //执行状态
        TaskActionItemExecStatusEnum execStatusEnum = EnumQueryUtil.of(TaskActionItemExecStatusEnum.class).getByCode(item.getExecStatus());
        resultItem.setExecStatus(item.getExecStatus());
        resultItem.setExecStatusDesc(execStatusEnum.getDesc());
        //最终结果
        TaskActionItemFinalRetEnum finalRetEnum =  EnumQueryUtil.of(TaskActionItemFinalRetEnum.class).getByCode(item.getFinalExecRet());
        resultItem.setFinalExecRet(item.getFinalExecRet());
        resultItem.setFinalExecRetDesc(finalRetEnum.getDesc());

        resultItem.setFailReason(item.getExecMsg());
        resultItem.setThirdTaskCreateTime(item.getThirdTaskCreateTime());
        resultItem.setThirdTaskExecTime(item.getThirdTaskExecTime());
        if (action != null) {
            resultItem.setActionOrder(action.getActionOrder());
        }
        return resultItem;
    }

    @Override
    public List<MarketingTaskActionExecItemEntity> queryByActionRecordCodeAndTaskBizCode(Collection<String> actionRecordCodes, String taskBizCode) {
        if (StringUtils.isBlank(taskBizCode) || CollectionUtils.isEmpty(actionRecordCodes)) {
            return Collections.emptyList();
        }
        List<MarketingTaskActionItemBizRelationEntity> relationList = iMarketingTaskActionItemBizRelationService
                .queryByActionRecordCodeAndTaskBizCode(actionRecordCodes, taskBizCode);
        if (CollectionUtils.isEmpty(relationList)) {
            return Collections.emptyList();
        }

        List<String> itemCodeList = relationList.stream().map(MarketingTaskActionItemBizRelationEntity::getActionItemCode).collect(Collectors.toList());
        LambdaQueryWrapper<MarketingTaskActionExecItemEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.in(MarketingTaskActionExecItemEntity::getLogicCode, itemCodeList);
        return list(wrapper);
    }

    @Override
    public List<String> listActionRecordCodeByExecStatus(String triggerCode, TaskActionItemExecStatusEnum execStatusEnum) {
        return marketingTaskActionExecItemMapper.listActionRecordCodeByExecStatus(triggerCode, execStatusEnum.getCode());
    }

    @Override
    public List<String> listTriggerCodeByExecStatus(TaskActionItemExecStatusEnum execStatusEnum, Date startTime, Date endTime) {
        return marketingTaskActionExecItemMapper.listTriggerCodeByExecStatus(execStatusEnum.getCode(), startTime, endTime);
    }

    @Override
    public MarketingTaskActionExecItemEntity getOneBySourceItemCode(String sourceItemCode) {
        LambdaQueryWrapper<MarketingTaskActionExecItemEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(MarketingTaskActionExecItemEntity::getSourceItemCode, sourceItemCode);
        wrapper.eq(MarketingTaskActionExecItemEntity::getIsValid, YesOrNoEnum.YES.getCode());
        return getOne(wrapper);
    }

}
