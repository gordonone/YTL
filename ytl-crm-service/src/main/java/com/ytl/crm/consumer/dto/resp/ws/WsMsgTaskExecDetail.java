package com.ytl.crm.consumer.dto.resp.ws;

import lombok.Data;

import java.util.List;

@Data
public class WsMsgTaskExecDetail {
    private Integer total;

    private List<WsMsgTaskExecRecord> records;

    @Data
    public static class WsMsgTaskExecRecord {
        /**
         * 员工id
         */
        private String user_id;

        /**
         * 任务执行状态，-1.创建任务失败；0.未执行；1.执行成功；2.执行失败
         */
        private Integer send_status;

        /**
         * 员工发送时间,格式:时间戳,精度:秒
         */
        private Integer send_time;
        private List<String> msg_id_list;
        private Integer plan_target_qty;
        private Integer success_target_qty;
        private Integer fail_target_qty;
        private Integer excess_target_qty;
        private Integer comment_staff_qty;
        private Integer comment_customer_qty;
        private Integer like_staff_qty;
        private Integer like_customer_qty;
    }
}
