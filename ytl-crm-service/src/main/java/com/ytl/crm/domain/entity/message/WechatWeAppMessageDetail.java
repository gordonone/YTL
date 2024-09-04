package com.ytl.crm.domain.entity.message;

import com.ziroom.wechat.service.domain.enumerate.WechatMessageTypeEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * @author 11911
 * @version 1.0
 * {@code @date} 2024/8/1 15:35
 */
@Getter
@Setter
public class WechatWeAppMessageDetail extends BaseWechatMessageDetail {

    public WechatWeAppMessageDetail() {
        super(WechatMessageTypeEnum.WE_APP.getType());
    }

    private String title;

    private String description;

    private String userName;

    private String displayName;
}
