package com.ytl.crm.consumer.dto.req.srs;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SrParisBaseReq extends SrsBaseReq {

    public static final String P_TOKEN = "P-token";

    public static final String ROLE_CODE = "roleCode";

    public static final String TOKEN = "token";

    @ApiModelProperty("token")
    private String token;

    @ApiModelProperty("角色code")
    private String roleCode;
}
