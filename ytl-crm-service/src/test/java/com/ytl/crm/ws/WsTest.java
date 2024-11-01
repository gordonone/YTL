package com.ytl.crm.ws;

import com.ytl.crm.BaseTest;
import com.ytl.crm.consumer.req.wechat.UserContactQueryReq;
import com.ytl.crm.consumer.resp.wechat.ExternalContractListResp;
import com.ytl.crm.consumer.resp.wechat.UserStaffQueryResp;
import com.ytl.crm.consumer.wechat.WxOfficialConsumer;
import com.ytl.crm.consumer.wechat.WxOfficialTokenHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class WsTest extends BaseTest {

    @Autowired
    private WxOfficialTokenHelper wxOfficialTokenHelper;

    @Autowired
    private WxOfficialConsumer wxOfficialConsumer;


    @Test
    void runTask1() {
        String accessToken = wxOfficialTokenHelper.acquireAccessToken();
        System.out.println(accessToken);

//        UserContactQueryReq userContactQueryReq = new UserContactQueryReq();
//        userContactQueryReq.setMobile("18500992868");
//        UserStaffQueryResp userStaffQueryResp = wxOfficialConsumer.getStaffUserId(accessToken, userContactQueryReq);
//        System.out.println(userStaffQueryResp.getUserid());

        //xiao
        ExternalContractListResp externalContractListResp = wxOfficialConsumer.getExternalcontactList(accessToken, "xiao");
        System.out.println(externalContractListResp.getExternalUserId());


        //ExternalContractListResp(errcode=0, errmsg=ok, externalUserId=[wm-4RGCwAAXfkzaeEh-Huud7Oed6VROA])
    }
}