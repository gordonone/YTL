package com.ytl.crm.domain.resp.task.exec;

import lombok.Data;

import java.util.Date;

@Data
public class TaskActionExecResultItem {

    private String virtualKeeperId;

    private String virtualKeeperName;

    private String actionOrder;

    private String execStatus;

    private String failReason;

    /**
     * 任务创建时间
     */
    private Date thirdTaskCreateTime;

    /**
     * 三方任务执行时间
     */
    private Date thirdTaskExecTime;


}
