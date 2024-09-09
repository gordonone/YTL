package com.ytl.crm.ws;

import com.ytl.crm.BaseTest;
import com.ytl.crm.config.MarketingTaskApolloConfig;
import com.ytl.crm.service.ws.logic.VirtualActualConvertLogicService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;


public class WsTest extends BaseTest {

    @Autowired
    private VirtualActualConvertLogicService virtualActualConvertLogicService;

    @Autowired
    private MarketingTaskApolloConfig marketingTaskApolloConfig;

    @Test
    void runTask() {
        virtualActualConvertLogicService.batchSyncUpdateUserList();
    }

    @Test
    void runTask1() {

    }
}