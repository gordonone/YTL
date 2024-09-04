package com.ytl.crm.domain.entity.message;

import com.ziroom.wechat.service.domain.enumerate.WechatUserRoleEnum;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;

/**
 * @author 11911
 * @version 1.0
 * {@code @date} 2024/8/1 15:42
 */
@Data
public class WechatMessageUserInfo {

    /**
     * 三方应用的企微id
     */
    private String thirdWechatId;

    /**
     * 自建应用的企微id
     */
    private String selfWechatId;

    /**
     * 原始的id，企微-工号，用户-uid
     */
    @Indexed
    private String originalId;

    /**
     * 用户角色 emp:员工，user：客户
     */
    private String role;

    public boolean isCustomer() {
        return WechatUserRoleEnum.USER.getCode().equals(this.role);
    }
}
