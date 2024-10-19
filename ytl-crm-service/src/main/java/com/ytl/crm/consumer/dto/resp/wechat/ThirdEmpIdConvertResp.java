package com.ytl.crm.consumer.dto.resp.wechat;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class ThirdEmpIdConvertResp  extends WeChatBaseResp {

    @ApiModelProperty("userid_list")
    @JsonProperty("userid_list")
    private List<UserIdInfo> useridList;

    @ApiModelProperty("不合法的open_userid列表")
    @JsonProperty("invalid_open_userid_list")
    private List<String> invalidOpenUserIdList;


    @NoArgsConstructor
    @Data
    public static class UserIdInfo {
        @ApiModelProperty("转换成功的open_userid")
        @JsonProperty("open_userid")
        private String openUserId;

        @ApiModelProperty("转换成功的open_userid对应的userid")
        @JsonProperty("userid")
        private String userId;
    }

}
