package com.ytl.crm.utils;


import com.ytl.crm.domain.enums.EnumWithCodeAndDesc;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 工具类的holder
 *
 * @author hongj
 * @date 2023/3/24 0:04
 */
public class EnumQueryUtilHolder {

    /**
     * 懒汉单例
     */
    private static final EnumQueryUtilHolder INSTANCE = new EnumQueryUtilHolder();

    /**
     * 不同枚举类的的util缓存
     */
    private final Map<Class<? extends EnumWithCodeAndDesc<?>>, EnumQueryUtil<?, ?>> cache = new ConcurrentHashMap<>();

    /**
     * 构造函数(私有)
     */
    private EnumQueryUtilHolder() {

    }

    /**
     * 获取单例持有者
     *
     * @return 单例持有者
     */
    public static EnumQueryUtilHolder getInstance() {
        return INSTANCE;
    }

    /**
     * 获取指定枚举类的查询工具
     *
     * @param clazz 枚举类的对象
     * @return 枚举查询工具
     */
    @SuppressWarnings("unchecked")
    public <E extends Enum<E> & EnumWithCodeAndDesc<K>, K> EnumQueryUtil<E, K> get(Class<E> clazz) {
        EnumQueryUtil<E, K> util = (EnumQueryUtil<E, K>) cache.get(clazz);
        if (util == null) {
            synchronized (EnumQueryUtilHolder.class) {
                util = (EnumQueryUtil<E, K>) cache.get(clazz);
                if (util == null) {
                    util = new EnumQueryUtil<E, K>(clazz) {
                    };
                    cache.put(clazz, util);
                }
            }
        }
        return util;
    }

}