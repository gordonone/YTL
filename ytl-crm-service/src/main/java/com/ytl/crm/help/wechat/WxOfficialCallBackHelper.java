package com.ytl.crm.help.wechat;

import com.alibaba.fastjson.JSONObject;
import com.ytl.crm.utils.wechat.crypt.WeChatBizMsgCryptHandler;
import com.ytl.crm.utils.wechat.json.WeChatJsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WxOfficialCallBackHelper {

    private final WeChatBizMsgCryptHandler weChatBizMsgCryptHandler;

    /**
     * 校验url，配置接口时会调用
     */
    public String verifyUrl(String msgSignature, String timeStamp, String nonce, String echoStr) {
        return weChatBizMsgCryptHandler.verifyURL(msgSignature, timeStamp, nonce, echoStr);
    }

    /**
     * 抽取消息体并解密
     */
    public String extractMsgData(String msgSignature, String timeStamp, String nonce, String postData) {
        String msgData = null;
        try {
            msgData = weChatBizMsgCryptHandler.extractMsgData(msgSignature, timeStamp, nonce, postData);
        } catch (Exception e) {
            log.error("校验消息体异常，无法处理", e);
        }
        return msgData;
    }

    /**
     * xml转json
     */
    public JSONObject transMsgToJson(String msgData) {
        if (StringUtils.isBlank(msgData)) {
            return null;
        }
        try {
            Document document = DocumentHelper.parseText(msgData);
            Element rootElement = document.getRootElement();
            return WeChatJsonUtil.elementToJSONObject(rootElement);
        } catch (DocumentException e) {
            log.error("transPostDataToJSONObject", e);
        }
        return null;
    }

}
