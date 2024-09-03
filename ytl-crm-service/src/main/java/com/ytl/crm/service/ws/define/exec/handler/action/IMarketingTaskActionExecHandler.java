package com.ytl.crm.service.ws.define.exec.handler.action;


import com.ytl.crm.domain.entity.task.exec.MarketingTaskActionExecRecordEntity;
import com.ytl.crm.domain.task.exec.MarketingTaskActionBO;
import com.ytl.crm.domain.task.exec.MarketingTaskBO;
import com.ytl.crm.domain.task.exec.MarketingTaskConfigBO;

public interface IMarketingTaskActionExecHandler {

    boolean support(MarketingTaskActionBO actionBO);

    void execAction(MarketingTaskConfigBO configBO, MarketingTaskActionBO actionBO, MarketingTaskActionExecRecordEntity actionExecRecord);

    default void callBackAction(MarketingTaskBO taskBO, MarketingTaskActionBO actionBO, MarketingTaskActionExecRecordEntity actionExecRecord) {
    }

    default void compensateAction(MarketingTaskConfigBO configBO, MarketingTaskActionBO actionBO, MarketingTaskActionExecRecordEntity actionExecRecord) {
    }


}
