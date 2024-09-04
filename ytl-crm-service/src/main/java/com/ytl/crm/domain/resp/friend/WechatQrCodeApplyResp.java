package com.ytl.crm.domain.resp.friend;

import com.ytl.crm.domain.entity.friend.WechatApplyQrcodeLogEntity;
import lombok.Data;

/**
 * @author 11911
 * @version 1.0
 * {@code @date} 2024/7/22 14:13
 */
@Data
public class WechatQrCodeApplyResp {

    /**
     * 二维码地址
     */
    private String qrCodeUrl;

    /**
     * 头图标识
     */
    private String headImage;

    /**
     * 虚拟管家姓名
     */
    private String virtualEmpName;

    /**
     * 用户姓名
     */
    private String userName;

    public static WechatQrCodeApplyResp buildByApplyQrCodeLog(WechatApplyQrcodeLogEntity wechatApplyQrcodeLogEntity) {
        WechatQrCodeApplyResp wechatQrCodeApplyResp = new WechatQrCodeApplyResp();
        wechatQrCodeApplyResp.setQrCodeUrl(wechatApplyQrcodeLogEntity.getQrcodeUrl());
        wechatQrCodeApplyResp.setUserName(wechatApplyQrcodeLogEntity.getUserName());
        wechatQrCodeApplyResp.setHeadImage(wechatApplyQrcodeLogEntity.getVirtualEmpAvatar());
        wechatQrCodeApplyResp.setVirtualEmpName(wechatApplyQrcodeLogEntity.getVirtualEmpName());
        return wechatQrCodeApplyResp;
    }
}
