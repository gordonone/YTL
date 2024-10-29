package com.ytl.crm.api.callback.wechat;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WechatAuthApi {

    @RequestMapping("/*")
    public String getCheckInfo() {
        return "CdDEJztJXZXjNBHg";
    }

}

