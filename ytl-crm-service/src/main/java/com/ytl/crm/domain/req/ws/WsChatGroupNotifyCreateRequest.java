package com.ytl.crm.domain.req.ws;

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
 * @date 2024/7/19 09:39
 * @since 1.0
 */
@Data
public class WsChatGroupNotifyCreateRequest {

    /**
     * 客户externalUserId
     */
    private List<String> customer;
    /**
     * 客户性别：男或女
     */
    private String gender;
    /**
     * 群主userId
     */
    private String owner;
    /**
     * 客户姓式
     */
    private String surname;
    /**
     * 群名称
     */
    private String title;
    /**
     * 员工userId
     */
    private List<String> user;
    /**
     * 群欢迎语
     */
    private String welcome;
}
