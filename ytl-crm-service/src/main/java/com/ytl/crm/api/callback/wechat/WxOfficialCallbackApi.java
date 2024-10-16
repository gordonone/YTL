package com.ytl.crm.api.callback.wechat;

import com.ytl.crm.help.wechat.WxOfficialCallBackHelper;
import com.ytl.crm.logic.wechat.interfaces.IWechatOfficialCallbackLogic;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import static com.ytl.crm.constants.WxOfficialCallBackConstant.ParamConstant.*;


@Slf4j
@RestController
@Api(value = "微信官方回调", tags = "微信官方回调")
@RequestMapping("/callback/wechat/official")
@RequiredArgsConstructor
public class WxOfficialCallbackApi {

    private final WxOfficialCallBackHelper wxOfficialCallBackHelper;
    private final IWechatOfficialCallbackLogic iWeChatCallbackLogic;

    /**
     * 用以验证企业微信回调接口的有效性
     */
    @GetMapping(value = "/notify")
    public String verifyCallBack(
            @RequestParam(name = MSG_SIGNATURE) String msgSignature,
            @RequestParam(name = TIMESTAMP) String timestamp,
            @RequestParam(name = NONCE) String none,
            @RequestParam(name = ECHO_STR) String echoStr) {
        String verifyResult = wxOfficialCallBackHelper.verifyUrl(msgSignature, timestamp, none, echoStr);
        log.info("verifyURL = {}", verifyResult);
        return verifyResult;
    }

    /**
     * 用以接收企业微信回调
     */
    @PostMapping(value = "/notify")
    public String handleCallBack(@RequestParam(name = MSG_SIGNATURE) String msgSignature,
                                 @RequestParam(name = TIMESTAMP) String timestamp,
                                 @RequestParam(name = NONCE) String nonce,
                                 @RequestBody String xml) {
        log.info("企微回调--msgSignature={}---timestamp---{}---nonce---{}---xml--{}", msgSignature, timestamp, nonce, xml);
        String msgData = wxOfficialCallBackHelper.extractMsgData(msgSignature, timestamp, nonce, xml);
        if (StringUtils.isNotBlank(msgData)) {
            //处理回调
            iWeChatCallbackLogic.handleCallBack(timestamp, nonce, msgData);
        }
        return "SUCCESS";
    }
}
