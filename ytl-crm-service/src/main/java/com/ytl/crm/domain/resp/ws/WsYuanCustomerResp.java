package com.ytl.crm.domain.resp.ws;


import lombok.Data;

@Data
public class WsYuanCustomerResp {


//      "external_userid": "wmDkH9EAAAK0_VM8XC6lqFK_GUha5WcQ",
//              "name": "赵霞",
//              "type": 1,
//              "gender": 0,
//              "user_id": "jiameng",

    /**
     * 操作员工userId
     */
    private String user_id;

    private String external_userid;

    private String name;

    private boolean is_del;

    private boolean is_loss;

    private int type;


}
