package com.ytl.crm.consumer.dto.req.zoro;

import lombok.Data;

import java.util.List;

@Data
public class ZoroQueryUserByGroupIdReq {

    /**
     * 分群id, 通过画像后台获取
     */
    private Integer groupId;

    /**
     * 条数,最大1000
     */
    private Integer limit;

    /**
     * 要查询的标签
     */
    private List<String> labels;

    /**
     * 	查询用户,第一次请求没有uid,之后每次必须携带服务端返回的uid
     */
    private String uid;

    /**
     * 查询业主,第一次请求没有phone,之后每次必须携带服务端返回的phone
     */
    private String phone;


}
