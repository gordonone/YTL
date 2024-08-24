package com.ytl.crm.domain.resp.ws;

import lombok.Data;

@Data
public class WsAccessToken {

    /**
     * 获取到的凭证，最长为512字节
     */
    private String access_token;

    /**
     * 凭证的有效时间（秒）
     */
    private Long expires_in;

    /**
     * 创建时间(时间戳)
     */
    private Long create_at;

}
