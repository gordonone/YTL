package com.ytl.crm.mq.model.wechat.official;

import lombok.experimental.UtilityClass;

/**
 * callBack 类型
 * https://open.work.weixin.qq.com/api/doc/90000/90135/92130#%E5%A4%96%E9%83%A8%E8%81%94%E7%B3%BB%E4%BA%BA%E5%85%8D%E9%AA%8C%E8%AF%81%E6%B7%BB%E5%8A%A0%E6%88%90%E5%91%98%E4%BA%8B%E4%BB%B6
 *
 * @author: lijun
 * @date: 2021/4/28 上午11:42
 */
@UtilityClass
public class OfficialCallBackType {


    /**
     * 事件的类型,外部联系人变更
     */
    public static final String EVENT_CHANGE_CONTACT = "change_external_contact";

    /**
     * 客户群变更事件
     * 客户群被修改后（群名变更，群成员增加或移除），回调该事件
     */
    public static final String EVENT_CHANGE_CHAT = "change_external_chat";


    /********************************外部联系人相关********************************/


    /**
     * 添加企业客户事件
     * 配置了客户联系功能的成员添加外部联系人时，回调该事件
     */
    public static final String CHANGE_TYPE_CONTACT_ADD = "add_external_contact";

    /**
     * 编辑企业客户事件
     * 配置了客户联系功能的成员修改外部联系人的备注、手机号或标签时，回调该事件
     */
    public static final String CHANGE_TYPE_CONTACT_EDIT = "edit_external_contact";

    /**
     * 外部联系人免验证添加成员事件
     * 外部联系人添加了配置了客户联系功能且开启了免验证的成员时（此时成员尚未确认添加对方为好友），回调该事件
     */
    public static final String CHANGE_TYPE_ADD_HALF = "add_half_external_contact";

    /**
     * 删除企业客户事件
     * 配置了客户联系功能的成员删除外部联系人时，回调该事件
     */
    public static final String CHANGE_TYPE_CONTACT_DEL = "del_external_contact";

    /**
     * 删除跟进成员事件
     * 配置了客户联系功能的成员被外部联系人删除时，回调该事件
     */
    public static final String CHANGE_TYPE_FOLLOW_USER_DEL = "del_follow_user";

    /**
     * 客户接替失败事件
     * 企业将客户分配给新的成员接替后，客户添加失败时回调该事件
     */
    public static final String CHANGE_TYPE_TRANSFER_FAIL = "transfer_fail";

    /********************************外部联系人相关********************************/


    /**********************************日程相关**********************************/

    /**
     * 行程 - 修改日历
     */
    public static final String EVENT_MODIFY_CALENDAR = "modify_calendar";

    /**
     * 行程 - 删除日历
     */
    public static final String EVENT_DELETE_CALENDAR = "delete_calendar";

    /**
     * 行程 - 通过API添加日程
     */
    public static final String EVENT_ADD_CALENDAR = "add_schedule";

    /**
     * 行程 - 修改日程
     */
    public static final String EVENT_MODIFY_SCHEDULE = "modify_schedule";

    /**
     * 行程 - 删除日程
     */
    public static final String EVENT_DELETE_SCHEDULE = "delete_schedule";

    /**********************************日程相关**********************************/

    /**********************************客户群相关事件**********************************/
    /**
     * 客户群
     */
    public static final String EVENT_CHAT = "change_external_chat";

    /**
     * 客户群 - 创建
     */
    public static final String EVENT_CHAT_CREATE = "create";

    /**
     * 客户群 - 修改 - 群名变更，群成员增加或移除，群主变更，群公告变更
     */
    public static final String EVENT_CHAT_UPDATE = "update";

    /**
     * 客户群 - 解散
     */
    public static final String EVENT_CHAT_DISMISS = "dismiss";
    /**********************************客户群相关事件**********************************/


}