package com.ytl.crm.consumer.resp.wechat;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserStaffQueryResp {
//    {
//        "errcode": 0,
//            "errmsg": "ok",
//            "userid": "zhangsan"
//    }

    @ApiModelProperty("返回码")
    @JsonProperty("errcode")
    private Integer errCode;

    @ApiModelProperty("对返回码的文本描述内容")
    @JsonProperty("errmsg")
    private String errMsg;

    @JsonProperty("userid")
    private String userid;



}
