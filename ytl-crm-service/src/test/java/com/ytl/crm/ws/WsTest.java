package com.ytl.crm.ws;

import com.ytl.crm.BaseTest;
import com.ytl.crm.domain.bo.task.config.MarketingTaskQueryBO;
import com.ytl.crm.service.interfaces.task.config.IMarketingTaskService;
import com.ytl.crm.logic.test.VirtualActualConvertLogicService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


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
        MarketingTaskQueryBO marketingTaskQueryBO=new MarketingTaskQueryBO();
        marketingTaskQueryBO.setTaskName("测试");
        marketingTaskService.queryTaskList(marketingTaskQueryBO);
    }
}