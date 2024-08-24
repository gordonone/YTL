package com.ytl.crm.domain.resp.ws;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author 11911
 * @version 1.0
 * {@code @date} 2024/7/22 17:31
 */
@Data
public class WsUserDetailResp {

    /**
     * 用户标识
     */
    @JsonProperty("external_userid")
    private String externalUserid;

    /**
     * 好友关系
     */
    @JsonProperty("follow_user")
    private List<FollowUser> followUser;

    /**
     * 好友信息
     */
    @Data
    public static class FollowUser {
        /**
         * 员工标识
         */
        @JsonProperty("userid")
        private String userid;

        /**
         * 此员工对用户的标识
         */
        @JsonProperty("remark")
        private String remark;
    }

    public String getSpecialUserRemark(String userId) {
        if (CollectionUtils.isEmpty(this.followUser)) {
            return null;
        }

        return this.followUser.stream()
                .filter(user -> userId.equals(user.getUserid()))
                .map(FollowUser::getRemark)
                .findAny()
                .orElse(null);
    }
}
