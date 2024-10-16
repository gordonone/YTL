package com.ytl.crm.logic.wechat.interfaces;


import com.ytl.crm.domain.enums.wechat.WechatSourceEnum;

public interface IWechatCustomerLogic {

    /**
     * 外部联系人三方id转官方id，如果不存在，会调用外部接口转换
     */
    String transferToOfficialId(String customerThirdWxId, WechatSourceEnum sourceEnum);

}
