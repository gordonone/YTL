package com.ytl.crm.ws;

import com.ytl.crm.service.ws.logic.VirtualActualConvertLogicService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WsTest {

    @Autowired
    private VirtualActualConvertLogicService virtualActualConvertLogicService;

    @Test
    void runTask() {
        virtualActualConvertLogicService.batchSyncUpdateUserList();
    }
}