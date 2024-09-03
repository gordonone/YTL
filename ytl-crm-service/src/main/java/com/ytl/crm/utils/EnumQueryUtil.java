package com.ytl.crm.utils;


import com.ytl.crm.domain.enums.EnumWithCodeAndDesc;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 通用枚举查询类
 *
 * @param <E> 枚举类型
 * @param <K> 枚举值类型
 * @author hongj
 */
public abstract class EnumQueryUtil<E extends Enum<E> & EnumWithCodeAndDesc<K>, K> {
    /**
     * key：枚举的code，value：枚举对象
     */
    private final Map<K, E> codeEnumMap;
    /**
     * key：枚举的code，value：枚举描述
     */
    private final Map<K, String> codeDescMap;
    /**
     * 枚举类
     */
    private final Class<E> clazz;

    /**
     * 构造函数
     *
     * @param clazz 枚举类的对象
     */
    protected EnumQueryUtil(Class<E> clazz) {
        this.clazz = clazz;
        //保证顺序
        Map<K, E> enumMap = new LinkedHashMap<>();
        Map<K, String> descMap = new LinkedHashMap<>();
        for (E e : clazz.getEnumConstants()) {
            enumMap.put(e.getCode(), e);
            descMap.put(e.getCode(), e.getDesc());
        }
        //不可变map
        codeEnumMap = Collections.unmodifiableMap(enumMap);
        codeDescMap = Collections.unmodifiableMap(descMap);
    }

    /**
     * 获取指定枚举类的查询工具
     *
     * @param clazz 枚举类的对象
     * @return 枚举查询工具
     */
    public static <E extends Enum<E> & EnumWithCodeAndDesc<K>, K> EnumQueryUtil<E, K> of(Class<E> clazz) {
        return EnumQueryUtilHolder.getInstance().get(clazz);
    }

    /**
     * 根据枚举值获取枚举对象
     *
     * @param code 枚举值
     * @return 枚举对象
     */
    public E getByCode(K code) {
        return codeEnumMap.get(code);
    }

    public E getByCodeOrDefault(K code, E defaultValue) {
        E retEnum = getByCode(code);
        return retEnum != null ? retEnum : defaultValue;
    }

    /**
     * 根据枚举值获取枚举描述
     *
     * @param code 枚举值
     * @return 枚举描述
     */
    public String getDescByCode(K code) {
        return codeDescMap.get(code);
    }

    /**
     * 获取枚举值与枚举对象的映射
     *
     * @return 枚举值与枚举对象的映射
     */
    public Map<K, E> getEnumMap() {
        return codeEnumMap;
    }

    /**
     * 获取枚举值与枚举描述的映射
     *
     * @return 枚举值与枚举描述的映射
     */
    public Map<K, String> getDescMap() {
        return codeDescMap;
    }

}