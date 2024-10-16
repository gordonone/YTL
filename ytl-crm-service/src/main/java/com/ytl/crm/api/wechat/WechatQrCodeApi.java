package com.ytl.crm.api.wechat;

import com.ytl.crm.domain.common.BaseResponse;
import com.ytl.crm.domain.req.wechat.CustomerQrCodeGenReq;
import com.ytl.crm.domain.resp.wechat.CustomerWeChatQrCodeDTO;
import com.ytl.crm.logic.wechat.interfaces.IWechatQrCodeLogic;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@Api(value = "微信二维码API", tags = "微信二维码API")
@RequestMapping("/wechat/qrCode")
@RequiredArgsConstructor
public class WechatQrCodeApi {

    private final IWechatQrCodeLogic iWechatQrCodeLogic;

    @PostMapping("/customer/gen")
    @ApiOperation(value = "申请客户码，一客一码")
    public BaseResponse<CustomerWeChatQrCodeDTO> genCustomerQrCode(@RequestBody @Valid CustomerQrCodeGenReq genReq) {
        boolean tokenValid = iWechatQrCodeLogic.checkTokenValid(genReq);
        if (!tokenValid) {
            BaseResponse.responseFail("无效的token");
        }
        return BaseResponse.responseOk(iWechatQrCodeLogic.genCustomerQrCode(genReq));
    }

}
