package com.ytl.crm.service.ws.define.exec.handler.action;


import com.ytl.crm.domain.entity.task.exec.MarketingTaskActionExecRecordEntity;
import com.ytl.crm.domain.task.config.MarketingTaskActionBO;
import com.ytl.crm.domain.task.config.MarketingTaskBO;

public interface IMarketingTaskActionExecHandler {

    boolean support(MarketingTaskActionBO actionBO);

    void execAction(MarketingTaskBO taskBaseInfo, MarketingTaskActionBO actionBO, MarketingTaskActionExecRecordEntity actionExecRecord);

    default void callBackAction(MarketingTaskActionBO actionBO, MarketingTaskActionExecRecordEntity actionExecRecord) {
        //默认空实现
    }

    default void compensateAction(MarketingTaskActionBO actionBO, MarketingTaskActionExecRecordEntity actionExecRecord) {
        //默认空实现
    }


}
