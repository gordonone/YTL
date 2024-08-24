package com.ytl.crm.domain.req.exec;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;

@Data
public class TaskActionExecResultItemListReq {

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    //@NotBlank(message = "任务code不能为空")
    private String taskCode;

    //@NotBlank(message = "动作code不能为空")
    private String actionCode;

    private Integer pageNum = 1;

    private Integer pageSize = 10;
}

