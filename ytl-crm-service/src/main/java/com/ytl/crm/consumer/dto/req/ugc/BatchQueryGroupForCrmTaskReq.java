package com.ytl.crm.consumer.dto.req.ugc;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class BatchQueryGroupForCrmTaskReq {

    @NotEmpty(message = "群号code")
    @ApiModelProperty(value = "群号code")
    private List<String> groupCodeList;

}
