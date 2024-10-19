package com.ytl.crm.consumer.dto.resp.data;

import lombok.Data;

@Data
public class TaskBizDataDto {
    private String uid;
    private String group_code;
    private String contract_num;
    /**
     * yyyy-MM-dd 数据刷新时间
     */
    private String dt;
}
