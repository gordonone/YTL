package com.ytl.crm.domain.req.exec;

import lombok.Data;


@Data
public class SrCaseRelatedMsgActionQueryReq {

    //@NotBlank(message = "SR单号不能为空")
    private String caseCode;

}
