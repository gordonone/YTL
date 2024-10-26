package com.ytl.crm.consumer.wechat;

import com.ytl.crm.consumer.req.wechat.*;
import com.ytl.crm.consumer.resp.wechat.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "wxOfficialCustomer", url = "${wxOfficial.domain:https://qyapi.weixin.qq.com}")
public interface WxOfficialConsumer {

    /**
     * 获取token
     * <a href="https://developer.work.weixin.qq.com/document/path/91039">
     *
     * @param corpId     企业ID
     * @param corpSecret 应用的凭证密钥
     * @return
     */
    @GetMapping("/cgi-bin/gettoken")
    @ApiOperation(value = "获取token", httpMethod = "GET", notes = "获取token")
    WeChatAccessTokenDTO acquireAccessToken(@RequestParam("corpid") String corpId, @RequestParam("corpsecret") String corpSecret);

    @PostMapping("/cgi-bin/externalcontact/add_contact_way")
    @ApiOperation(value = "创建联系我方式-二维码", httpMethod = "POST", notes = "获取token")
    WeChatQrCodeDTO createEmpQrCode(@RequestParam("access_token") String accessToken, @RequestBody WechatCreateQrCodeReq req);

    @PostMapping("/cgi-bin/externalcontact/del_contact_way")
    @ApiOperation(value = "删除联系我方式-二维码", httpMethod = "POST", notes = "获取token")
    WeChatBaseResp deleteEmpQrCode(@RequestParam("access_token") String accessToken, @RequestBody WechatDeleteQrCodeReq req);

    /**
     * <a href="https://developer.work.weixin.qq.com/document/path/95884">userid转换</a>
     *
     * @param req userid转换的请求参数
     * @return 企微用户信息
     */
    @PostMapping(path = "/cgi-bin/batch/openuserid_to_userid")
    ThirdEmpIdConvertResp thirdEmpIdConvert(@RequestParam("access_token") String accessToken, @RequestBody ThirdEmpIdConvertReq req);

    /**
     * <a href="https://developer.work.weixin.qq.com/document/path/95884">external_userid转换</a>
     *
     * @param req external_userid转换的请求参数
     * @return 企微用户信息
     */
    @PostMapping(path = "/cgi-bin/externalcontact/from_service_external_userid")
    ThirdCustomerIdConvertResp thirdCustomerConvert(@RequestParam("access_token") String accessToken, @RequestBody ThirdCustomerIdConvertReq req);


    /**
     * <a href="https://developer.work.weixin.qq.com/document/path/92114">获取客户详情</a>
     */
    @GetMapping(path = "/cgi-bin/externalcontact/get")
    ExternalContactQueryResp getExternalContact(@RequestParam("access_token") String accessToken, @RequestParam("external_userid") String externalUserId);


    @PostMapping(path = "/cgi-bin/externalcontact/add_msg_template")
    SendMsgResp addMsgTemplate(@RequestParam("access_token") String accessToken, @RequestParam("sendMsgTemplateReq") SendMsgTemplateReq sendMsgTemplateReq);


    @PostMapping(path = "/cgi-bin/user/getuserid")
    UserStaffQueryResp getStaffUserId(@RequestParam("access_token") String accessToken, @RequestParam("userContactQueryReq") UserContactQueryReq userContactQueryReq);

    //https://qyapi.weixin.qq.com?access_token=ACCESS_TOKEN&userid=USERID
    @GetMapping(path = "/cgi-bin/externalcontact/list")
    ExternalContractListResp getExternalcontactList(@RequestParam("access_token") String accessToken, @RequestParam("userid") String userid);


}
