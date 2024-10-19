package com.ytl.crm.consumer.dto.resp.srs;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SrsCreateCaseResp {

    @ApiModelProperty("服务请求号")
    private String caseCode;

}
