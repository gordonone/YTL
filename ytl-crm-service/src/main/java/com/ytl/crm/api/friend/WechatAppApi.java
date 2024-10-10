package com.ytl.crm.api.friend;

import com.ytl.crm.domain.common.BaseResponse;
import com.ytl.crm.domain.req.friend.WechatFriendRelationReq;
import com.ytl.crm.domain.req.friend.WechatQrCodeApplyReq;
import com.ytl.crm.domain.resp.friend.WechatFriendRelationResp;
import com.ytl.crm.domain.resp.friend.WechatQrCodeApplyResp;
import com.ytl.crm.service.ws.impl.friend.WechatAggregateLogicService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author 11911
 * @version 1.0
 * {@code @date} 2024/7/5 9:13
 */
@RestController
@RequestMapping("/app/wechat")
public class WechatAppApi {

    @Resource
    private WechatAggregateLogicService wechatAggregateLogicService;

    /**
     * 查询好友关系
     *
     * @param req 查询入参
     * @return 好友关系
     */
    @PostMapping("/queryFriendRelation")
    public BaseResponse<WechatFriendRelationResp> queryFriendRelation(@RequestBody @Valid WechatFriendRelationReq req) {
        return BaseResponse.responseOk(wechatAggregateLogicService.queryFriendRelation(req));
    }

    /**
     * 查询二维码信息
     *
     * @param req 查询入参
     * @return 二维码相关信息
     */
    @PostMapping("/queryQrCode")
    public BaseResponse<WechatQrCodeApplyResp> queryQrCode(@RequestBody @Valid WechatQrCodeApplyReq req) {
        return BaseResponse.responseOk(wechatAggregateLogicService.queryQrCode(req));
    }

}
