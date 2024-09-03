package com.ytl.crm.service.ws.Impl.exec;


import com.google.common.collect.Lists;
import com.ytl.crm.domain.entity.task.config.MarketingTaskEntity;
import com.ytl.crm.domain.entity.task.exec.MarketingTaskActionExecRecordEntity;
import com.ytl.crm.domain.entity.task.exec.MarketingTaskBizInfoEntity;
import com.ytl.crm.domain.entity.task.exec.MarketingTaskTriggerRecordEntity;
import com.ytl.crm.domain.enums.task.config.TaskActionDependencyEnum;
import com.ytl.crm.domain.enums.task.config.TaskStatusEnum;
import com.ytl.crm.domain.enums.task.exec.TaskActionExecStatusEnum;
import com.ytl.crm.domain.enums.task.exec.TaskTriggerStatusEnum;
import com.ytl.crm.domain.task.config.MarketingTaskActionBO;
import com.ytl.crm.domain.task.config.MarketingTaskBO;
import com.ytl.crm.domain.task.config.MarketingTaskConfigBO;
import com.ytl.crm.service.ws.Impl.exec.handler.MarketingTaskHandlerFactory;
import com.ytl.crm.service.ws.define.exec.IMarketTaskActionExecLogic;
import com.ytl.crm.service.ws.define.exec.IMarketingTaskConfigLogic;
import com.ytl.crm.service.ws.define.exec.exec.IMarketingTaskActionExecItemService;
import com.ytl.crm.service.ws.define.exec.exec.IMarketingTaskActionExecRecordService;
import com.ytl.crm.service.ws.define.exec.exec.IMarketingTaskBizInfoService;
import com.ytl.crm.service.ws.define.exec.exec.IMarketingTaskTriggerRecordService;
import com.ytl.crm.service.ws.define.exec.handler.action.IMarketingTaskActionExecHandler;
import com.ytl.crm.utils.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;


/**
 * 任务动作执行逻辑
 */
@Slf4j
@Service
@RequiredArgsConstructor

//todo 任务执行逻辑
public class MarketTaskActionExecLogicImpl{



}
