package com.ytl.crm.domain.req.ws;


import com.ytl.crm.domain.resp.common.PageResp;
import lombok.Data;

@Data
public class WsYuanCustomerReq {

//    {
//        "user_id":"jiameng",
//            "add_start_date": 1716200056000,
//            "add_end_date": 1716286456331,
//            "current_index":1,
//            "page_size":2
//    }

    /**
     * 操作员工userId
     */
    private String user_id;

    private Long add_start_date = 1693134594000L;

    private Long add_end_date = System.currentTimeMillis();

    private int current_index = 1;

    private int page_size = 20;


}
