package com.ytl.crm.domain.bo.wechat;

import com.ytl.crm.domain.enums.wechat.QrCodeApplyTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ChannelQrCodeApplyBO extends QrCodeApplyBO {

    public ChannelQrCodeApplyBO() {
        super();
        typeEnum = QrCodeApplyTypeEnum.CHANNEL;
    }

}
