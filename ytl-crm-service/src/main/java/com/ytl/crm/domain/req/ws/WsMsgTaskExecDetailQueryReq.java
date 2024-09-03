package com.ytl.crm.domain.req.ws;

import lombok.Data;

@Data
public class WsMsgTaskExecDetailQueryReq {

    /**
     * 任务ID
     */
    private String task_id;

    /**
     * 操作员工userId
     */
    private String user_id;

    /**
     * 查询指定员工数据
     */
    private String query_user_id;

    /**
     * 是否需要返回msgIds，默认false
     */
    private Boolean need_msg_id;

    private Integer current_index = 1;

    private Integer page_size = 20;
}
