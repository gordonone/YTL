package com.ytl.crm.domain.enums;

/**
 * @author Ziroom
 */
public interface EnumWithCodeAndDesc<T> {
    /**
     * 枚举code
     *
     * @return T
     * @author hongj
     * @date 2023/3/23 22:58
     */
    T getCode();

    /**
     * 枚举描述
     *
     * @return java.lang.String
     * @author hongj
     * @date 2023/3/23 22:59
     */
    String getDesc();

    /**
     * 判断枚举的code是否和入参相等，字符串不区分大小写
     *
     * @param code code
     * @return true 相等，false 不相等
     */
    default boolean equalsCode(T code) {
        if (code instanceof String) {
            return ((String) getCode()).equalsIgnoreCase((String) code);
        }
        return getCode().equals(code);
    }
}