package com.ytl.crm.service.ws.Impl.exec.handler.action;


import com.ytl.crm.config.MarketingTaskApolloConfig;
import com.ytl.crm.service.ws.define.exec.exec.IMarketingTaskActionExecItemService;
import com.ytl.crm.service.ws.define.exec.exec.IMarketingTaskActionExecRecordService;
import com.ytl.crm.service.ws.define.exec.exec.IMarketingTaskActionItemBizRelationService;
import com.ytl.crm.service.ws.define.exec.exec.IMarketingTaskBizInfoService;
import com.ytl.crm.service.ws.define.exec.handler.action.IMarketingTaskActionExecHandler;
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
