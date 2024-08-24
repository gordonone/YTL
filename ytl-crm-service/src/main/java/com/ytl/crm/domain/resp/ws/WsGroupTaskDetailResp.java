package com.ytl.crm.domain.resp.ws;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 11911
 * @version 1.0
 * {@code @date} 2024/7/22 14:45
 */
@NoArgsConstructor
@Data
public class WsGroupTaskDetailResp {

    /**
     * 任务ID
     */
    private String id;


    /**
     * tenantId
     */
    private String tenantId;



    /**
     * 群名称
     */
    private String name;

    /**
     * 群欢迎语
     */
    private String welcome;


    /**
     * 群ID
     */
    private String groupId;


    /**
     *群主
     */
    private WsGroupMember owner;


    /**
     *群成员（员工）
     */
    private List<WsGroupMember> users;


    /**
     *群成员（客户）
     */
    private List<WsGroupMember> customers;


}
