package com.ytl.crm.domain.resp.work;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ziroom.wechat.service.domain.constant.Constants;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * @author 11911
 * @version 1.0
 * {@code @date} 2024/7/31 10:17
 */
@NoArgsConstructor
@Data
public class WorkWechatGroupDetailResp {


    @JsonProperty("errcode")
    private Integer errCode;
    @JsonProperty("errmsg")
    private String errMsg;
    @JsonProperty("group_chat")
    private GroupChat groupChat;

    @NoArgsConstructor
    @Data
    public static class GroupChat {
        @JsonProperty("chat_id")
        private String chatId;
        @JsonProperty("name")
        private String name;
        @JsonProperty("owner")
        private String owner;
        @JsonProperty("create_time")
        private Integer createTime;
        @JsonProperty("notice")
        private String notice;
        @JsonProperty("member_list")
        private List<GroupMember> memberList;
        @JsonProperty("admin_list")
        private List<GroupAdmin> adminList;
        @JsonProperty("member_version")
        private String memberVersion;

        @NoArgsConstructor
        @Data
        public static class GroupMember {
            @JsonProperty("userid")
            private String userid;
            @JsonProperty("type")
            private Integer type;
            @JsonProperty("join_time")
            private Integer joinTime;
            @JsonProperty("join_scene")
            private Integer joinScene;
            @JsonProperty("invitor")
            private Invitor invitor;
            @JsonProperty("group_nickname")
            private String groupNickname;
            @JsonProperty("name")
            private String name;
            @JsonProperty("unionid")
            private String unionId;

            @Data
            public static class Invitor {
                @JsonProperty("userid")
                private String userid;
            }
        }

        @Data
        public static class GroupAdmin {
            @JsonProperty("userid")
            private String userid;
        }
    }

    public boolean isOk() {
        return this.errCode == 0;
    }

    public boolean isValid() {
        return Objects.nonNull(this.groupChat) && StringUtils.isNotBlank(this.groupChat.name);
    }
}
