package com.ytl.crm.ws;

import com.ytl.crm.BaseTest;
import com.ytl.crm.service.ws.logic.VirtualActualConvertLogicService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class WsTest extends BaseTest {

    @Autowired
    private VirtualActualConvertLogicService virtualActualConvertLogicService;

    @Test
    void runTask() {
        virtualActualConvertLogicService.batchSyncUpdateUserList();
    }
}