package com.ytl.crm.domain.req.work;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: ugc-wechat-service
 * <p></p>
 * <p>
 * <PRE>
 * <BR>	修改记录
 * <BR>-----------------------------------------------
 * <BR>	修改日期			修改人			修改内容
 * </PRE>
 * @description:
 * @author: lijh99
 * @create: 2024-07-22 14:57
 * @version: V1.0
 **/
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ConvertUserIdReq {


    @JsonProperty("open_userid_list")
    private String[] openUserIdList;

    @JsonProperty("source_agentid")
    private String sourceAgentId;
}
