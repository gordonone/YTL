package com.ytl.crm.consumer.dto.resp.ws;

import lombok.Data;

import java.util.List;

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
 * @date 2024/7/22 17:16
 * @since 1.0
 */
@Data
public class WsPageResult<T> {
    private long total;
    private List<T> records;
}
