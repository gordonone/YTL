package com.ytl.crm.domain.bo.wechat;


import com.ytl.crm.domain.entity.wechat.WechatQrcodeApplyLogEntity;
import lombok.Data;

@Data
public class QrCodeApplyLogRetBO {

    private String applyCode;

    private String empWxId;

    private String empName;

    public static QrCodeApplyLogRetBO build(WechatQrcodeApplyLogEntity logEntity) {
        QrCodeApplyLogRetBO bo = new QrCodeApplyLogRetBO();
        bo.setApplyCode(logEntity.getLogicCode());
        bo.setEmpWxId(logEntity.getEmpWxId());
        bo.setEmpName(logEntity.getEmpName());
        return bo;
    }

}
