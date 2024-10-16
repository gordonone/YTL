package com.ytl.crm.logic.wechat.interfaces;

import com.ytl.crm.domain.enums.wechat.WechatSourceEnum;

public interface IWechatEmpLogic {

    /**
     * 同步微盛应用绑定的员工账号
     * todo 如果后续有多渠道，考虑做成通用的
     */
    void syncWsEmpList();

    /**
     * 同步官方微信id
     */
    void syncOfficialEmpId(WechatSourceEnum sourceEnum);

    /**
     * 是否是系统关心的微信id
     *
     * @param empWxId 官方微信id
     */
    boolean isCareAboutEmpWxId(String empWxId);

    /**
     * 三方员工微信id转企微官方
     */
    String transferToOfficialId(String empThirdWxId, WechatSourceEnum sourceEnum);

}
