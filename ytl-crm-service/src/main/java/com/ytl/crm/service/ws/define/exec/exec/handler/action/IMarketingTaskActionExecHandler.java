package com.ytl.crm.service.ws.define.exec.exec.handler.action;

import com.ziroom.ugc.crm.service.web.domain.bo.task.exec.MarketingTaskActionBO;
import com.ziroom.ugc.crm.service.web.domain.bo.task.exec.MarketingTaskBO;
import com.ziroom.ugc.crm.service.web.domain.bo.task.exec.MarketingTaskConfigBO;
import com.ziroom.ugc.crm.service.web.domain.entity.task.exec.MarketingTaskActionExecRecordEntity;

public interface IMarketingTaskActionExecHandler {

    boolean support(MarketingTaskActionBO actionBO);

    void execAction(MarketingTaskConfigBO configBO, MarketingTaskActionBO actionBO, MarketingTaskActionExecRecordEntity actionExecRecord);

    default void callBackAction(MarketingTaskBO taskBO, MarketingTaskActionBO actionBO, MarketingTaskActionExecRecordEntity actionExecRecord) {
    }

    default void compensateAction(MarketingTaskConfigBO configBO, MarketingTaskActionBO actionBO, MarketingTaskActionExecRecordEntity actionExecRecord) {
    }

}
