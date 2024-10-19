package com.ytl.crm.consumer.dto.req.storage;

import lombok.Data;

@Data
public class BaseReq {
    private String clientId;
    private String secret;
    private String bizId;
    private String serviceId;
    private String path;
    private String bucket;
    private String region;
}
