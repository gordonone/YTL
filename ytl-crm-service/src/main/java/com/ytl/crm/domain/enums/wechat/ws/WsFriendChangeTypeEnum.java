package com.ytl.crm.domain.enums.wechat.ws;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 11911
 * @version 1.0
 * {@code @date} 2024/7/22 15:31
 */
@Getter
@AllArgsConstructor
public enum WsFriendChangeTypeEnum {

    /**
     * 好友变更事件
     */
    ADD("add_external_contact", "添加好友"),
    EDIT("edit_external_contact", "编辑好友"),
    DEL_USER("del_external_contact", "员工删除好友"),
    DEL_EMP("del_follow_user", "客户删除员工");

    /**
     * 标识
     */
    private final String code;

    /**
     * 解释
     */
    private final String name;

}
