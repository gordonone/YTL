package com.ytl.crm.domain.resp.task.exec;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SrCaseRelatedMsgActionInfo {

    @ApiModelProperty(value = "三方任务id")
    private String thirdTaskId;

    @ApiModelProperty(value = "三方任务执行状态")
    private String thirdTaskExecStatus;

    @ApiModelProperty(value = "三方任务创建时间")
    private Date thirdTaskCreateTime;

    @ApiModelProperty(value = "三方任务执行时间")
    private Date thirdTaskExecTime;

}
