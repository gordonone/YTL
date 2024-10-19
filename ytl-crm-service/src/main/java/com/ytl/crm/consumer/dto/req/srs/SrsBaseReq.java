package com.ytl.crm.consumer.dto.req.srs;

import lombok.Data;

@Data
public class SrsBaseReq {

    private static final String ZI_ROOM = "ziroom";

    protected String operatorCode = ZI_ROOM;

    protected String operatorName = ZI_ROOM;

    protected Integer operatorSource;
}
