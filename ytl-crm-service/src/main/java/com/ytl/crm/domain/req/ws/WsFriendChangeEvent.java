package com.ytl.crm.domain.req.ws;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * @author 11911
 * @version 1.0
 * {@code @date} 2024/7/22 15:27
 */
@NoArgsConstructor
@Data
public class WsFriendChangeEvent {

    /**
     * 租户id
     */
    @JsonProperty("tenant_id")
    private String tenantId;

    /**
     * 添加此用户的「联系我」方式配置的state参数，可用于识别添加此用户的渠道
     */
    @JsonProperty("state")
    private String state;

    /**
     * 消息类型，固定为change_external_contact
     */
    @JsonProperty("info_type")
    private String infoType;

    /**
     * 变更类型
     * add_external_contact：添加好友
     * edit_external_contact：编辑好友信息，包含客户的标签、备注名、描述、所属企业名称等系统字段和本应用内自定义字段的信息编辑。
     * del_external_contact：员工删除好友
     * del_follow_user：客户删除员工
     */
    @JsonProperty("change_type")
    private String changeType;

    /**
     * 员工userid
     */
    @JsonProperty("user_id")
    private String userId;

    /**
     * 客户id
     */
    @JsonProperty("external_user_id")
    private String externalUserId;

    /**
     * 消息标识，传递参数用，消息体本身不包含
     */
    private String messageId;

    @JsonIgnore
    public boolean isInvalidEvent() {
        return StringUtils.isBlank(this.userId) || StringUtils.isBlank(this.externalUserId) || StringUtils.isBlank(this.changeType);
    }
}
