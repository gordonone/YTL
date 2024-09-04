package com.ytl.crm.domain.entity.message;

import com.ziroom.wechat.service.domain.constant.MongoConstants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author 11911
 * @version 1.0
 * {@code @date} 2024/8/1 14:50
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = MongoConstants.TABLE_WECHAT_MESSAGE)
public class WechatMessageEntity implements Serializable {

    /**
     * 主键
     */
    @Id
    private String id = UUID.randomUUID().toString().replaceAll("-", "");

    /**
     * 消息id
     */
    @Indexed
    private String msgId;

    /**
     * 消息类型
     */
    private String msgType;

    /**
     * 消息发送时间
     */
    private String msgTime;

    /**
     * 发送者信息
     */
    private WechatMessageUserInfo fromUser;

    /**
     * 接收者列表
     */
    private List<WechatMessageUserInfo> toUserList;

    /**
     * 群id
     */
    private String roomId;

    /**
     * 会话id
     */
    @Indexed
    private String sessionId;

    /**
     * 三方的群id
     */
    @Indexed
    private String thirdRoomId;

    /**
     * 消息内容
     */
    @Field("detail")
    private BaseWechatMessageDetail detail;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 最后修改时间
     */
    private Date lastModifyTime;

}
