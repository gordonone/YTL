package com.ytl.crm.consumer.dto.resp.ugc;

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
public class WechatGroupForCrmTaskDTO {

    @ApiModelProperty(value = "群号，logicCode")
    private String groupCode;

    @ApiModelProperty(value = "自建应用id")
    private String selfGroupChatId;

    @ApiModelProperty(value = "群聊id-微盛")
    private String groupChatId;

    @ApiModelProperty(value = "群主id-虚拟表id-企微配置")
    private String ownerEmpId;

    @ApiModelProperty(value = "群主id三方id-微盛")
    private String ownerEmpThirdId;

    @ApiModelProperty(value = "用户id")
    private String uid;

    @ApiModelProperty(value = "用户微信外部id")
    private String userExternalId;

    @ApiModelProperty(value = "群聊名称")
    private String groupName;

    @ApiModelProperty(value = "群状态 1已创建，-0已解散")
    private Integer groupStatus;

    @ApiModelProperty(value = "是否有效 0:无效  1:有效")
    private Boolean isValid;

    @ApiModelProperty(value = "是否删除 0:未删除  1:已删除")
    private Boolean isDelete;

    @ApiModelProperty(value = "群业务绑定信息")
    private List<BizBindDTO> bizBindList;

    @ApiModelProperty(value = "群成员信息")
    private List<GroupMemberDTO> groupCustomerList;

    @ApiModelProperty(value = "群主信息")
    private VirtualKeeperDTO ownerInfo;


    @Data
    public static class BizBindDTO {

        @ApiModelProperty(value = "合同类型")
        private String contractType;

        @ApiModelProperty(value = "合同号")
        private String contractCode;

    }

    @Data
    public static class GroupMemberDTO {

        @ApiModelProperty(value = "0-未知 1-企业员工 2-客户 3-小乐管家（企业员工）")
        private Integer memberType;

        @ApiModelProperty(value = "成员ID: emp_id/用户uid")
        private String memberCode;

        @ApiModelProperty(value = "成员三方应用ID: emp_third_id/用户外部联系id")
        private String memberThirdCode;

    }

    @Data
    public static class VirtualKeeperDTO {

        @ApiModelProperty(value = "真实管家id")
        private String actualId;

        @ApiModelProperty(value = "真实管家姓名")
        private String actualName;

        @ApiModelProperty(value = "虚拟管家id")
        private String virtualId;

        @ApiModelProperty(value = "虚拟管家姓名")
        private String virtualName;

        @ApiModelProperty(value = "虚拟管家头像")
        private String virtualAvatar;

        @ApiModelProperty(value = "晚班客服ID")
        private String eveningCsActualId;

        @ApiModelProperty(value = "晚班客服姓名")
        private String eveningCsActualName;

    }

}
