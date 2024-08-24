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
 * @date 2024/7/22 15:54
 * @since 1.0
 */
@Data
public class WsAccessZiroomToken {
    /**
     * 获取到的凭证，最长为512字节
     */
    private String accessToken;

    /**
     * 凭证的有效时间（秒）
     */
    private Long expiresIn;

    /**
     * 创建时间(时间戳)
     */
    private Long createAt;
}
