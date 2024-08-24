package com.ytl.crm.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * @author 11911
 * @version 1.0
 * {@code @date} 2024/3/6 16:33
 */
public class LocalDateTimeUtil {

    private LocalDateTimeUtil() {
    }

    /**
     * 默认格式化
     *
     * @param localDateTime 时间
     * @return 时间字符串
     */
    public static String format(LocalDateTime localDateTime) {
        return Optional.ofNullable(localDateTime)
                .map(dateTime -> dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .orElse("");
    }
}
