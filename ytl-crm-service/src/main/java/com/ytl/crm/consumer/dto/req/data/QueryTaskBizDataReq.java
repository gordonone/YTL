package com.ytl.crm.consumer.dto.req.data;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class QueryTaskBizDataReq extends DataServiceBaseReq {

    private Integer offset;

    private Integer pageSize;

}
