package com.ytl.crm.domain.task.exec;

import lombok.Data;

@Data
public class TaskVirtualKeeperBO {
    /**
     * 小木管家虚拟号
     */
    private String virtualKeeperId;

    /**
     * 小木管家虚拟号-三方id-微盛
     */
    private String virtualKeeperThirdId;

    /**
     * 小木管家虚拟号-姓名
     */
    private String virtualKeeperName;
}
