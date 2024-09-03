package com.ytl.crm.api.friend;


import com.ytl.crm.domain.common.BaseResponse;
import com.ytl.crm.service.ws.Impl.WeChatZMagicLogicService;
import com.ytl.crm.service.ws.Impl.WechatAggregateLogicService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 企微 z-magic 应用相关接口
 *
 * @author cuiw
 * @version 1.0
 * @date 2024/7/19
 * @since JDK 8.0
 */
@RequestMapping("/zmagic")
@RestController
public class WeChatZMagicApi {

    @Resource
    private WeChatZMagicLogicService weChatZMagicLogicService;

    @Resource
    private WechatAggregateLogicService wechatAggregateLogicService;


}
