package com.ytl.crm.domain.resp.ws;

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
 * @date 2024/7/22 16:08
 * @since 1.0
 */
@Data
public class WsGroupMember {

    private String name;

    private String avatar;

    private String uid;

    private Integer type;

}
