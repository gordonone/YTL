package com.ytl.crm.domain.resp.ws;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * <p></p>
 *
 * <PRE>
 * <BR>	修改记录
 * <BR>-----------------------------------------------
 * <BR>	修改日期			修改人			修改内容
 * </PRE>
 *
 * @author lixs5
 * @version 1.0
 * @date 2024/7/22 17:21
 * @since 1.0
 */
@Data
public class WsGroupInfo {

    /**
     * 客户群ID
     */
    @JsonProperty("chat_id")
    private String chatId;
    /**
     *客户群名称
     */
    @JsonProperty("name")
    private String name;

    /**
     *群主ID
     */
    @JsonProperty("owner")
    private String owner;

    /**
     *群公告
     */
    @JsonProperty("notice")
    private String notice;

    /**
     *群的创建时间
     */
    @JsonProperty("create_time")
    private Long createTime;

    /**
     *群人数
     */
    @JsonProperty("count")
    private Integer count;

    /**
     *群管理员ID集合
     */
    @JsonProperty("chat_administrator_user_ids")
    private List<String> chatAdministratorUserIds;
    /**
     *群标签信息集合
     */
    @JsonProperty("chat_tag_infos")
    private List<WsChatTag> chatTagInfos;

    /**
     *群标签ID
     */
    @JsonProperty("chat_tag_id")
    private String chatTagId;

    /**
     *群标签名称
     */
    @JsonProperty("chat_tag_name")
    private String chatTagName;

    /**
     *群标签组ID
     */
    @JsonProperty("chat_tag_group_id")
    private String chatTagGroupId;

    /**
     *群标签组名称
     */
    @JsonProperty("chat_tag_group_name")
    private String chatTagGroupName;



}
