package com.ytl.crm.service.ws.define.wechat;


import com.ytl.crm.mq.model.wechat.official.WxOfficialCallbackMsgDTO;

public interface IWechatOfficialCallbackLogic {

    void handleCallBack(String timestamp, String none, String msgData);

    void handleCallbackMsg(WxOfficialCallbackMsgDTO msgDTO);

}