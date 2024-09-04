package com.ytl.crm.domain.enums.friend;

import com.ytl.crm.domain.enums.EnumWithCodeAndDesc;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 11911
 * @version 1.0
 * {@code @date} 2024/7/22 16:40
 */
@Getter
@AllArgsConstructor
public enum WechatFriendRelationStatusEnum implements EnumWithCodeAndDesc<Integer> {

    /**
     * 状态
     */
    ADDED(0, "已添加"),
    DELETED(1, "已删除");

    private final Integer code;

    private final String desc;


    @Override
    public boolean equalsCode(Integer code) {
        return this.code.intValue() == code;
    }
}
