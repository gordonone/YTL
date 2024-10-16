package com.ytl.crm.domain.mq;

import com.ytl.crm.event.wechat.model.customer.EditFriendEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * :配置了客户联系功能的成员修改外部联系人的备注、手机号或标签时，回调该事件
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditExternalContactMsgData implements FriendEventMsgData<EditFriendEvent> {

    /**
     * 企业微信CorpID
     */
    private String toUserName;

    /**
     * 此事件该值固定为sys，表示该消息由系统生成
     */
    private String fromUserName;

    /**
     * 消息创建时间 （整型）
     */
    private String createTime;

    /**
     * 消息的类型，此时固定为event
     */
    private String msgType;

    /**
     * 事件的类型，此时固定为change_external_contact
     */
    private String event;

    /**
     * 此时固定为del_external_contact
     */
    private String changeType;

    /**
     * 企业服务人员的UserID
     */
    private String userID;

    /**
     * 外部联系人的userid，注意不是企业成员的帐号
     */
    private String externalUserID;

    /**
     * 添加此用户的「联系我」方式配置的state参数，可用于识别添加此用户的渠道
     */
    private String state;

    @Override
    public EditFriendEvent toEvent() {
        EditFriendEvent event = new EditFriendEvent();
        event.setCustomerWxId(this.externalUserID);
        event.setEmpWxId(this.userID);
        return event;
    }

    @Override
    public String customerWxId() {
        return this.externalUserID;
    }

    @Override
    public String empWxId() {
        return this.userID;
    }


}
