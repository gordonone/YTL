package com.ytl.crm.ws;

import com.ytl.crm.BaseTest;
import com.ytl.crm.config.MarketingTaskApolloConfig;
import com.ytl.crm.domain.entity.task.config.MarketingTaskEntity;
import com.ytl.crm.domain.resp.common.PageResp;
import com.ytl.crm.domain.task.config.MarketingTaskQueryBO;
import com.ytl.crm.service.ws.define.task.config.IMarketingTaskService;
import com.ytl.crm.service.ws.logic.VirtualActualConvertLogicService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;


public class WsTest extends BaseTest {

    @Autowired
    private VirtualActualConvertLogicService virtualActualConvertLogicService;

    @Autowired
    private IMarketingTaskService marketingTaskService;


    @Test
    void runTask() {
        virtualActualConvertLogicService.batchSyncUpdateUserList();
    }

    @Test
    void runTask1() {
        MarketingTaskQueryBO marketingTaskQueryBO = new MarketingTaskQueryBO();
        marketingTaskQueryBO.setTaskName("12");
        PageResp<MarketingTaskEntity> pp = marketingTaskService.queryTaskList(marketingTaskQueryBO);
        System.out.println(pp);
    }
}