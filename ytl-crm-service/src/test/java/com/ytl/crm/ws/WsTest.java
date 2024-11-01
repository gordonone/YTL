package com.ytl.crm.ws;

import com.ytl.crm.BaseTest;
import com.ytl.crm.consumer.req.wechat.SendMsgTemplateReq;
import com.ytl.crm.consumer.req.wechat.UserContactQueryReq;
import com.ytl.crm.consumer.resp.wechat.ExternalContractListResp;
import com.ytl.crm.consumer.resp.wechat.UserStaffQueryResp;
import com.ytl.crm.consumer.wechat.WxOfficialConsumer;
import com.ytl.crm.consumer.wechat.WxOfficialTokenHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


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

    @Test
    public void runTask2() {

        String accessToken = wxOfficialTokenHelper.acquireAccessToken();
        System.out.println(accessToken);

        SendMsgTemplateReq sendMsgTemplateReq = new SendMsgTemplateReq();
        sendMsgTemplateReq.setSender("xiao");
        sendMsgTemplateReq.setAllowSelect(true);
        sendMsgTemplateReq.setExternalUserid(Arrays.asList("wm-4RGCwAAXfkzaeEh-Huud7Oed6VROA"));

        sendMsgTemplateReq.setChatType("single");
        sendMsgTemplateReq.setText(new SendMsgTemplateReq.TextContext("测试aaaaa"));


        List<SendMsgTemplateReq.MsgContext> list = new ArrayList<>();

        SendMsgTemplateReq.MsgContext msgContext = new SendMsgTemplateReq.MsgContext();
        msgContext.msgType="link";

        SendMsgTemplateReq.LinkContext linkContext = new SendMsgTemplateReq.LinkContext();
        linkContext.url = "http://www.baidu.com";
        linkContext.desc = "xxxxl";
        linkContext.title="titletitletitle";

        msgContext.link = linkContext;


        list.add(msgContext);

        sendMsgTemplateReq.setAttachments(list);


        wxOfficialConsumer.addMsgTemplate(accessToken, sendMsgTemplateReq);


        //msg-4RGCwAAuJaS6k9c-QS8sA-7Uehl2w
    }
}