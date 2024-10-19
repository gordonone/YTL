package com.ytl.crm.consumer.dto.req.srs;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public class SrsCreateCaseReq extends SrParisBaseReq {

    @ApiModelProperty(value = "验重编号，由业务线控制的重单标记")
    private String checkRepeatedCode;

    @ApiModelProperty(value = "系统编号")
    private String systemCode;

    @ApiModelProperty(value = "来源系统编号")
    private Integer sourceCode;

    @NotEmpty(message = "场景不能为空")
    @ApiModelProperty(value = "场景")
    private String scene;

    @ApiModelProperty(value = "请求四级分类")
    private String classificationFourCode;

    @NotNull(message = "用户类型不能为空")
    @ApiModelProperty(value = "用户类型", notes = "1:自如客 2:内部员工 3：未知用户")
    private Integer userType;

    @NotEmpty(message = "用户编号不能为空")
    @ApiModelProperty(value = "用户编号", notes = "租客：uid 业主：uid 内部员工：系统号")
    private String userCode;

    @ApiModelProperty(value = "用户姓名")
    private String userName;

    @ApiModelProperty(value = "用户电话")
    private String userPhone;

    @ApiModelProperty(value = "城市编码")
    private String cityCode;

    @ApiModelProperty(value = "联系人姓名")
    private String linkmanName;

    @ApiModelProperty(value = "联系人手机号")
    private String linkmanPhone;

    @ApiModelProperty(value = "关联合同类型", notes = "1：出房合同 2：收房合同 3：自如寓合同")
    private Integer relatedContractType;

    @ApiModelProperty(value = "关联合同编号")
    private String relatedContractCode;

    @ApiModelProperty(value = "线索类型", notes = "1：对内维修 2：对内保洁 3：市场化维修 4：市场化保洁 5：市场化搬家")
    private Integer clueBusinessType;

    @ApiModelProperty(value = "线索业务单号")
    private String clueBusinessCode;

    @ApiModelProperty(value = "答案列表")
    private Map<String, Object> formData;

    @ApiModelProperty(value = "选项后填空附属信息")
    private Map<String, Object> formDataExtra;

    @ApiModelProperty(value = "关联")
    private OperationRelationInfo operationRelationInfo;

    @Data
    public static class OperationRelationInfo {
        @ApiModelProperty("关联类型，1：UD 2：400  OperationRelationTypeEnum")
        private Integer relationType;
        @ApiModelProperty("关联编号 关联的唯一编号，比如：ud会话id，400通话id")
        private String relationCode;
    }
}
