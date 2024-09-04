package com.ytl.crm.domain.entity.message;

import com.ziroom.wechat.service.domain.enumerate.WechatMessageTypeEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * @author 11911
 * @version 1.0
 * {@code @date} 2024/8/1 15:13
 */
@Getter
@Setter
public class WechatTextMessageDetail extends BaseWechatMessageDetail {

    public WechatTextMessageDetail() {
        super(WechatMessageTypeEnum.TEXT.getType());
    }

    /**
     * 会话内容
     */
    private String content;
}
