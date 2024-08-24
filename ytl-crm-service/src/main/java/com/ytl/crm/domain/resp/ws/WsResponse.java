package com.ytl.crm.domain.resp.ws;

import lombok.Data;
import lombok.EqualsAndHashCode;

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
 * @date 2024/7/19 09:45
 * @since 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WsResponse<T> extends WsBaseResponse<T> {

    private static final String SUCCESS_CODE = "00000";

    private boolean success;

    private String time;

    @Override
    public Boolean isOk() {
        return this.getCode() != null && this.getCode().equals(SUCCESS_CODE);
    }

}
