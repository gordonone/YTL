package com.ytl.crm.service.ws.define.exec.handler.action;


import com.ytl.crm.domain.entity.task.exec.MarketingTaskActionExecRecordEntity;
import com.ytl.crm.domain.task.config.MarketingTaskActionBO;

public interface IMarketingTaskActionExecHandler {

    boolean support(MarketingTaskActionBO actionBO);

    void execAction(MarketingTaskActionBO actionBO, MarketingTaskActionExecRecordEntity actionExecRecord);

    void callBackAction(MarketingTaskActionBO actionBO, MarketingTaskActionExecRecordEntity actionExecRecord);

    void compensateAction(MarketingTaskActionBO actionBO, MarketingTaskActionExecRecordEntity actionExecRecord);

}
