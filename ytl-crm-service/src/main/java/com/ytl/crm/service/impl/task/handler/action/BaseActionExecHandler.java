package com.ytl.crm.service.impl.task.handler.action;


import com.ytl.crm.config.MarketingTaskApolloConfig;
import com.ytl.crm.service.interfaces.task.exec.IMarketingTaskActionExecItemService;
import com.ytl.crm.service.interfaces.task.exec.IMarketingTaskActionExecRecordService;
import com.ytl.crm.service.interfaces.task.exec.IMarketingTaskActionItemBizRelationService;
import com.ytl.crm.service.interfaces.task.exec.IMarketingTaskBizInfoService;
import com.ytl.crm.service.interfaces.task.handler.action.IMarketingTaskActionExecHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

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
    @Resource
    protected IMarketingTaskActionItemBizRelationService actionItemBizRelationService;



}
