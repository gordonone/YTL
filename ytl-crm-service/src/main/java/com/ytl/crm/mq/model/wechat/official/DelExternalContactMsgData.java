package com.ytl.crm.mq.model.wechat.official;

import com.ziroom.ugc.crm.service.web.domain.enums.wechat.CustomerDelType;
import com.ziroom.ugc.crm.service.web.event.wechat.model.customer.DelFriendEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * :配置了客户联系功能的成员删除外部联系人时，回调该事件
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DelExternalContactMsgData implements FriendEventMsgData<DelFriendEvent> {

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

    @Override
    public DelFriendEvent toEvent() {
        DelFriendEvent event = new DelFriendEvent();
        event.setDelType(CustomerDelType.EMP_FIRST);
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
