package com.ytl.crm.consumer.dto.resp.wechat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * 添加了此外部联系人的相关企业人员信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExternalUserFollowUserDTO {

    @JsonProperty("userid")
    private String userId;

    private String state;

    @JsonProperty("oper_userid")
    private String operUserId;

    @JsonProperty("add_way")
    private Integer addWay;

    @JsonProperty("remark_mobiles")
    private List<String> remarkMobiles;

    @JsonProperty("remark_corp_name")
    private String remarkCorpName;

    private List<ExternalUserTagDTO> tags;

    @JsonProperty("tag_id")
    private List<String> tagIds;

    @JsonProperty("createtime")
    private Date createTime;

    private String description;

    private String remark;


    /**
     * 企业成员对外部联系人所打的标签
     * https://work.weixin.qq.com/api/doc/90000/90135/92114
     *
     * @author: lijun
     * @date: 2020/11/3 下午4:55
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ExternalUserTagDTO {

        @JsonProperty("group_name")
        private String groupName;

        @JsonProperty("tag_name")
        private String tagName;

        @JsonProperty("tag_id")
        private String tagId;

        private Integer type;

    }
}       
