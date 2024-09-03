package com.ytl.crm.domain.resp.task.exec;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class TaskActionExecResultItem {

    private String virtualKeeperId;

    private String virtualKeeperName;

    private Integer actionOrder;

    private String execStatus;

    private String execStatusDesc;

    private String finalExecRet;

    private String finalExecRetDesc;

    private String failReason;

    /**
     * 任务创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date thirdTaskCreateTime;

    /**
     * 三方任务执行时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date thirdTaskExecTime;


}
