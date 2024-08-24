package com.ytl.crm.domain.resp.task.exec;

import lombok.Data;

import java.util.List;

@Data
public class TaskActionExecResultSummaryPageInit {
    private List<ActionSimpleInfo> actionList;

    @Data
    public static class ActionSimpleInfo {
        private String actionCode;
        private String actionName;
        private Integer actionOrder;
    }
}
