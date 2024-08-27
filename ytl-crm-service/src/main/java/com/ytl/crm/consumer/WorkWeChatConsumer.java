package com.ytl.crm.consumer;


import com.ytl.crm.domain.req.work.ConvertExternalUserIdReq;
import com.ytl.crm.domain.req.work.ConvertUserIdReq;
import com.ytl.crm.domain.req.work.WorkWechatGroupDetailReq;
import com.ytl.crm.domain.resp.work.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 企微开发者平台相关接口
 *
 * @author cuiw
 * @version 1.0
 * @date 2024/7/19
 * @since JDK 8.0
 */
@FeignClient(name = "workWeChat", url = "${domain.work-wechat:https://qyapi.weixin.qq.com}")
public interface WorkWeChatConsumer {

    /**
     * <a href="https://developer.work.weixin.qq.com/document/path/91023">获取访问用户身份</a>
     *
     * @param code        通过成员授权获取到的code
     * @param accessToken 企微平台返回的 token
     * @return 企微用户信息
     */
    @GetMapping(path = "/cgi-bin/auth/getuserinfo")
    WorkWechatUserinfoResp getWeChatUserInfo(@RequestParam("code") String code,
                                             @RequestParam("access_token") String accessToken);


    /**
     * <a href="https://developer.work.weixin.qq.com/document/path/95884">userid转换</a>
     *
     * @param request userid转换的请求参数
     * @return 企微用户信息
     */
    @PostMapping(path = "/cgi-bin/batch/openuserid_to_userid?access_token={accessToken}")
    WorkWechatOpenUserIdResp openUserIdToUserId(@RequestBody ConvertUserIdReq request, @RequestParam("accessToken") String accessToken);

    /**
     * <a href="https://developer.work.weixin.qq.com/document/path/95884">external_userid转换</a>
     *
     * @param accessToken token
     * @param req         查询入参
     * @return 响应结果
     */
    @PostMapping("/cgi-bin/externalcontact/from_service_external_userid")
    WorkWechatExternalUserIdResp serviceExternalUserIdToExternalUserId(@RequestParam("access_token") String accessToken, @RequestBody ConvertExternalUserIdReq req);

    /**
     * 获取token
     * <a href="https://developer.work.weixin.qq.com/document/path/91039">external_userid转换</a>
     *
     * @param corpId     id
     * @param corpSecret 密钥
     * @return tokens
     */
    @GetMapping("/cgi-bin/gettoken")
    WorkWechatTokenResp getAccessToken(@RequestParam("corpid") String corpId, @RequestParam("corpsecret") String corpSecret);

    /**
     * 获取群详情
     * <a href="https://developer.work.weixin.qq.com/document/path/92122">external_userid转换</a>
     *
     * @param accessToken token
     * @param req         查询入参
     * @return 群详情
     */
    @PostMapping("/cgi-bin/externalcontact/groupchat/get")
    WorkWechatGroupDetailResp queryGroupDetail(@RequestParam("access_token") String accessToken, @RequestBody WorkWechatGroupDetailReq req);
}
