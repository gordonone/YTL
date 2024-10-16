package com.ytl.crm.api.wechat;


import com.ytl.crm.domain.common.BaseResponse;
import com.ytl.crm.domain.enums.wechat.CustomerIdTypeEnum;
import com.ytl.crm.domain.req.wechat.WechatFriendQueryByZrUidReq;
import com.ytl.crm.domain.resp.wechat.WechatFriendDetailDTO;
import com.ytl.crm.logic.wechat.interfaces.IWechatFriendLogic;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@Api(value = "微信好友API", tags = "微信好友API")
@RequestMapping("/wechat/friend")
@RequiredArgsConstructor
public class WechatFriendApi {

    private final IWechatFriendLogic iWechatFriendLogic;

    @PostMapping("/relation/queryByZrUid")
    @ApiOperation(value = "根据自如uid查询好友关系")
    public BaseResponse<List<WechatFriendDetailDTO>> queryByZrUid(@RequestBody @Valid WechatFriendQueryByZrUidReq req) {
        return BaseResponse.responseOk(iWechatFriendLogic.queryFriendByCustomerId(CustomerIdTypeEnum.ZIROOM.getCode(), req.getUid()));
    }

}
