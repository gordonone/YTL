package com.ytl.crm.consumer.dto.resp.wechat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class ExternalContactQueryResp extends WeChatBaseResp {

    /**
     * 外部联系人详情
     */
    @JsonProperty("external_contact")
    private ExternalUserContactDTO externalContact;

    /**
     * 添加了此外部联系人的相关企业人员信息
     */
    @JsonProperty("follow_user")
    private List<ExternalUserFollowUserDTO> followUser;

    /**
     * 分页的cursor，当跟进人多于500人时返回
     */

    @JsonProperty("next_cursor")
    private String nextCursor;

    public ExternalUserFollowUserDTO acquireFollowUser(String empWxId) {
        if (CollectionUtils.isEmpty(this.followUser)) {
            return null;
        }

        return this.followUser.stream()
                .filter(user -> empWxId.equals(user.getUserId()))
                .findAny()
                .orElse(null);
    }


}
