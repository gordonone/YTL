package com.ytl.crm.domain.req.friend;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author 11911
 * @version 1.0
 * {@code @date} 2024/7/22 14:09
 */
@Data
public class WechatQrCodeApplyReq {

    /**
     * 活码标识
     */
    @NotNull(message = "活码标识不能为空")
    private String applyCode;

}
