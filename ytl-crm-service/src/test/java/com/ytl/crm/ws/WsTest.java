package com.ytl.crm.ws;

import com.ytl.crm.BaseTest;
import com.ytl.crm.consumer.wechat.WxOfficialTokenHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class WsTest extends BaseTest {

    @Autowired
    private WxOfficialTokenHelper wxOfficialTokenHelper;


    @Test
    void runTask1() {
        String accessToken = wxOfficialTokenHelper.acquireAccessToken();
        System.out.println(accessToken);
    }
}