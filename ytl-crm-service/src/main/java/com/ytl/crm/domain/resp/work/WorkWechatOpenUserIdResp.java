package com.ytl.crm.domain.resp.work;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @program: ugc-wechat-service
 * <p></p>
 * <p>
 * 自建应用与第三方应用的对接 将代开发应用或第三方应用获取的密文open_userid转换为明文userid。
 * 参考文档： <a href="https://developer.work.weixin.qq.com/document/path/95884">userid转换</a>
 * <PRE>
 * <BR>	修改记录
 * <BR>-----------------------------------------------
 * <BR>	修改日期			修改人			修改内容
 * </PRE>
 * @description:
 * @author: lijh99
 * @create: 2024-07-22 14:47
 * @version: V1.0
 **/
@Data
@NoArgsConstructor
public class WorkWechatOpenUserIdResp {

    @ApiModelProperty("返回码")
    private int errcode;

    @ApiModelProperty("对返回码的文本描述内容")
    private String errmsg;

    @ApiModelProperty("userid_list")
    @JsonProperty("userid_list")
    private List<UserIdInfo> useridList;


    @ApiModelProperty("不合法的open_userid列表")
    @JsonProperty("invalid_open_userid_list")
    private String[] invalidOpenUserIdList;


/*
    参数	说明
    errcode	返回码
    errmsg	对返回码的文本描述内容
    userid_list	明文userid
    userid_list.open_userid	转换成功的open_userid
    userid_list.userid	转换成功的open_userid对应的userid
    invalid_open_userid_list	不合法的open_userid列表
*/

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
