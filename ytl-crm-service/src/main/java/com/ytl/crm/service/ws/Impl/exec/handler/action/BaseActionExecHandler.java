package com.ytl.crm.service.ws.Impl.exec.handler.action;


import com.ytl.crm.config.MarketingTaskApolloConfig;
import com.ytl.crm.domain.entity.task.exec.MarketingTaskActionExecItemEntity;
import com.ytl.crm.domain.entity.task.exec.MarketingTaskActionExecRecordEntity;
import com.ytl.crm.domain.entity.task.exec.MarketingTaskBizInfoEntity;
import com.ytl.crm.domain.enums.task.exec.TaskActionItemExecStatusEnum;
import com.ytl.crm.service.ws.define.exec.exec.IMarketingTaskActionExecItemService;
import com.ytl.crm.service.ws.define.exec.exec.IMarketingTaskActionExecRecordService;
import com.ytl.crm.service.ws.define.exec.exec.IMarketingTaskBizInfoService;
import com.ytl.crm.service.ws.define.exec.handler.action.IMarketingTaskActionExecHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public abstract class BaseActionExecHandler implements IMarketingTaskActionExecHandler {

    @Resource
    protected MarketingTaskApolloConfig marketingTaskApolloConfig;
    @Resource
    protected IMarketingTaskBizInfoService iMarketingTaskBizInfoService;
    @Resource
    protected IMarketingTaskActionExecRecordService iMarketingTaskActionExecRecordService;
    @Resource
    protected IMarketingTaskActionExecItemService iMarketingTaskActionExecItemService;

    public void initActionExecItem(MarketingTaskActionExecRecordEntity actionExecRecord,
                                   List<MarketingTaskBizInfoEntity> bizInfoList) {
        //根据code查询已经保存的数据，没有保存的重新保存
        String taskCode = actionExecRecord.getTaskCode();
        String actionRecordCode = actionExecRecord.getLogicCode();
        List<String> bizInfoCodeList = bizInfoList.stream().map(MarketingTaskBizInfoEntity::getLogicCode).collect(Collectors.toList());
        List<MarketingTaskActionExecItemEntity> existItemList = iMarketingTaskActionExecItemService.queryByBizInfoCode(actionRecordCode, bizInfoCodeList);
        Set<String> existBizCodeSet = CollectionUtils.isEmpty(existItemList) ? Collections.emptySet()
                : existItemList.stream().map(MarketingTaskActionExecItemEntity::getTaskBizCode).collect(Collectors.toSet());

        //构建实体
        List<MarketingTaskBizInfoEntity> needCreateItemBizList = bizInfoList.stream()
                .filter(item -> !existBizCodeSet.contains(item.getLogicCode())).collect(Collectors.toList());
        List<MarketingTaskActionExecItemEntity> needCreateItemList = CollectionUtils.isEmpty(needCreateItemBizList) ? Collections.emptyList() :
                needCreateItemBizList.stream().map(item -> buildInitExecItem(actionExecRecord, item))
                        .collect(Collectors.toList());

        //批量保存数据
        if (!CollectionUtils.isEmpty(needCreateItemList)) {
            boolean saveRet = iMarketingTaskActionExecItemService.batchSaveItem(needCreateItemList);
            log.info("批量保存动作执行Item结果，saveRet={}", saveRet);
        }
    }

    public MarketingTaskActionExecItemEntity buildInitExecItem(MarketingTaskActionExecRecordEntity actionExecRecord,
                                                               MarketingTaskBizInfoEntity bizInfoEntity) {
        MarketingTaskActionExecItemEntity itemEntity = new MarketingTaskActionExecItemEntity();
        itemEntity.setTaskCode(actionExecRecord.getTaskCode());
        itemEntity.setTriggerCode(actionExecRecord.getTriggerCode());
        itemEntity.setActionCode(actionExecRecord.getActionCode());
        itemEntity.setActionRecordCode(actionExecRecord.getLogicCode());

        itemEntity.setExecStatus(TaskActionItemExecStatusEnum.INIT.getCode());

        itemEntity.setTaskBizCode(bizInfoEntity.getLogicCode());
        itemEntity.setVirtualKeeperId(bizInfoEntity.getVirtualKeeperId());
        itemEntity.setVirtualKeeperThirdId(bizInfoEntity.getVirtualKeeperThirdId());
        itemEntity.setVirtualKeeperName(bizInfoEntity.getVirtualKeeperName());
        return itemEntity;
    }
}
