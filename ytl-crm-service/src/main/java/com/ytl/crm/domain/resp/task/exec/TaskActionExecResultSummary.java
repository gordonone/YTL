package com.ytl.crm.domain.resp.task.exec;

import lombok.Data;

@Data
public class TaskActionExecResultSummary {

    private String actionCode;

    private String actionName;

    private Integer actionOrder;

    private Long successCount;

    private Long failCount;

}
