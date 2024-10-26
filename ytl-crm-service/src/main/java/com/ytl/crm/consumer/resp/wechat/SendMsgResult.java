package com.ytl.crm.consumer.resp.wechat;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class SendMsgResult {

//    {
//        "errcode": 0,
//            "errmsg": "ok",
//            "fail_list":["wmqfasd1e1927831123109rBAAAA"],
//        "msgid":"msgGCAAAXtWyujaWJHDDGi0mAAAA"
//    }

    private Integer errcode;
    private String errmsg;

    @JsonProperty("fail_list")
    private List<String> failList;
    private String msgid;

}
