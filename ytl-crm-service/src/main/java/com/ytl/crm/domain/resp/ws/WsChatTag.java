package com.ytl.crm.domain.resp.ws;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

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
 * @date 2024/7/22 17:33
 * @since 1.0
 */
@Data
public class WsChatTag {
    /**
     * 群标签ID
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
