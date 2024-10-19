package com.ytl.crm.consumer.dto.req.inquire;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RentContractQueryReq {

    private static final String QUERY_DETAIL_REQUEST_ID = "1270974067356209152";

    /**
     * requestId : 1270645233473818624
     * systemName : xxx
     * account : xxxx
     * parameter : {"uid":"7a54a7b7-b15d-c335-486e-ca3a58482182","contractStates":["ygb"]}
     */
    private String requestId;

    private String systemName;

    @ApiModelProperty(value = "用户账号")
    private String account;

    private Parameter parameter;

    /**
     * 根据合同号构建
     *
     * @param contractCode 合同号
     * @return 查询合同的入参
     */
    public static RentContractQueryReq buildByContractCode(String contractCode) {
        Parameter queryRentListParameterRequest = new Parameter();
        queryRentListParameterRequest.setContractCode(contractCode);

        RentContractQueryReq queryRentRequest = new RentContractQueryReq();
        queryRentRequest.setRequestId(QUERY_DETAIL_REQUEST_ID);
        queryRentRequest.setSystemName("私域企微项目");
        queryRentRequest.setAccount("system");
        queryRentRequest.setParameter(queryRentListParameterRequest);
        return queryRentRequest;
    }

    @Data
    public static class Parameter {
        @ApiModelProperty(value = "uid - 查询列表必填")
        private String uid;

        @ApiModelProperty(value = "合同状态 - 查询列表 非必填:签约中,待管家交割,待支付,已支付,待提交审核，待审核，已驳回 已审核，已到期，已退租，已关闭")
        private List<String> contractStates;

        @ApiModelProperty(value = "合同号 - 查询详情必填")
        private String contractCode;
    }
}
