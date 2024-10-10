package com.ytl.crm.service.ws.define.wechat;


import com.ytl.crm.domain.bo.wechat.ChannelQrCodeApplyBO;
import com.ytl.crm.domain.bo.wechat.CustomerQrCodeApplyBO;
import com.ytl.crm.domain.entity.wechat.WechatQrcodeApplyLogEntity;
import com.ytl.crm.domain.req.wechat.CustomerQrCodeGenReq;
import com.ytl.crm.domain.req.wechat.WechatBaseReq;
import com.ytl.crm.domain.resp.wechat.CustomerWeChatQrCodeDTO;

public interface IWechatQrCodeLogic {

    boolean checkTokenValid(WechatBaseReq baseReq);

    /**
     * 申请渠道码 - 需要指定微信id
     */
    WechatQrcodeApplyLogEntity applyChannelQrCode(ChannelQrCodeApplyBO applyBO);

    /**
     * 申请客户码 - 需要指定微信id
     */
    WechatQrcodeApplyLogEntity applyCustomerQrCode(CustomerQrCodeApplyBO applyBO);

    /**
     * 通过申请code查询二维码
     *
     * @param applyCode    申请code
     * @param qrCodeSource 二维码渠道 {@link com.ziroom.ugc.crm.service.web.domain.enums.wechat.WechatSourceEnum}
     * @return 二维码
     */
    CustomerWeChatQrCodeDTO queryQrCodeByApplyCode(String applyCode, String qrCodeSource);

    /**
     * 生成申请记录 + 生成二维码
     *
     * @param genReq 申请req
     */
    CustomerWeChatQrCodeDTO genCustomerQrCode(CustomerQrCodeGenReq genReq);

    /**
     * 删除过期的二维码
     */
    void deleteExpireQrCode();

}
