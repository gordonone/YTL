package com.ytl.crm.utils;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 日期工具类
 *
 * @author hongjie
 */
@Slf4j
public class DateTimeUtil {
    /**
     * 时间格式枚举
     */
    @Getter
    public enum DateTimeUtilFormat {
        /**
         * 日期格式
         */
        yyyyMMdd("yyyyMMdd"),
        yyyyMMddHH("yyyyMMddHH"),
        yyyyMMddHHmm("yyyyMMddHHmm"),
        yyyyMMddHHmmss("yyyyMMddHHmmss"),
        yyyyMMddHHmmssSSS("yyyyMMddHHmmssSSS"),
        yyyy_MM_dd("yyyy-MM-dd"),
        MM_dd("MM-dd"),
        yyyy_MM_dd_HH("yyyy-MM-dd HH"),
        yyyy_MM_dd_HH_mm("yyyy-MM-dd HH:mm"),
        yyyy_MM_dd_HH_mm_ss("yyyy-MM-dd HH:mm:ss"),
        HH_mm_ss("HH:mm:ss"),
        yyyy_MM_dd_cn("yyyy年MM月dd日");

        String format;

        DateTimeUtilFormat(String format) {
            this.format = format;
        }

        public static DateTimeUtilFormat fromFormat(String format) {
            for (DateTimeUtilFormat item : DateTimeUtilFormat.values()) {
                if (StringUtils.equals(item.format, format)) {
                    return item;
                }
            }
            return null;
        }
    }

    public static Date currentTime() {
        return new Date();
    }

    public static Date todayTimeStart() {
        return getTimeOfDayStart(currentTime());
    }


    /**
     * @param date
     * @param
     * @return
     */
    public static String dateFormat(Date date, String format) {
        DateTimeUtilFormat dateTimeUtilFormat = DateTimeUtilFormat.fromFormat(format);
        if (dateTimeUtilFormat == null) {
            throw new IllegalArgumentException("format格式不合法");
        }
        return toString(date, dateTimeUtilFormat);
    }

    public static String toStringDefault(Date date) {
        return new DateTime(date).toString(DateTimeUtilFormat.yyyy_MM_dd_HH_mm_ss.format);
    }

    /**
     * @param date
     * @param dateTimeUtilFormat
     * @return
     */
    public static String toString(Date date, DateTimeUtilFormat dateTimeUtilFormat) {
        return new DateTime(date).toString(dateTimeUtilFormat.format);
    }

    /**
     * @param dateTime
     * @param dateTimeUtilFormat
     * @return
     */
    public static Date toDate(String dateTime, DateTimeUtilFormat dateTimeUtilFormat) {
        if (StringUtils.isBlank(dateTime)) {
            return null;
        }
        return DateTimeFormat.forPattern(dateTimeUtilFormat.format).withZoneUTC().parseDateTime(dateTime)
                .toLocalDateTime().toDate();
    }

    /**
     * @param dateTime
     * @param dateTimeUtilFormat
     * @return
     */
    public static DateTime toDateTime(String dateTime, DateTimeUtilFormat dateTimeUtilFormat) {
        return DateTimeFormat.forPattern(dateTimeUtilFormat.format).parseLocalDate(dateTime).toDateTimeAtStartOfDay();
    }

    /**
     * 现在距离明天00点还有多少秒
     *
     * @return
     */
    public static int getToTomorrowSecond() {
        String expTime = new DateTime().plusDays(1).toString(DateTimeUtilFormat.yyyy_MM_dd.format) + " 00:00:00";
        DateTime dateTime = DateTime.parse(expTime,
                DateTimeFormat.forPattern(DateTimeUtilFormat.yyyy_MM_dd_HH_mm_ss.format));
        Long second = (dateTime.getMillis() - new DateTime().getMillis()) / 1000;
        return second.intValue();
    }

    /**
     * 是否今天
     *
     * @param date
     * @return
     */
    public static boolean isToday(Date date) {
        Date start = new Date();
        return toString(start, DateTimeUtilFormat.yyyy_MM_dd).equals(toString(date, DateTimeUtilFormat.yyyy_MM_dd));
    }

    /**
     * 是否今天
     *
     * @param date yyyy-MM-dd
     * @return
     */
    public static boolean isToday(String date) {
        Date start = new Date();
        return toString(start, DateTimeUtilFormat.yyyy_MM_dd).equals(date);
    }

    public static Date addDay(Date myDate, int amount) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(myDate);
        cal.add(Calendar.DAY_OF_MONTH, amount);
        return cal.getTime();
    }

    public static Date addHour(Date myDate, int amount) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(myDate);
        cal.add(Calendar.HOUR_OF_DAY, amount);
        return cal.getTime();
    }

    public static Date addMinute(Date myDate, int amount) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(myDate);
        cal.add(Calendar.MINUTE, amount);
        return cal.getTime();
    }

    public static Date addSecond(Date myDate, int amount) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(myDate);
        cal.add(Calendar.SECOND, amount);
        return cal.getTime();
    }

    /**
     * 年
     *
     * @param myDate
     * @param amount
     * @return
     */
    public static Date addYear(Date myDate, int amount) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(myDate);
        cal.add(Calendar.YEAR, amount);
        return cal.getTime();
    }

    /**
     * 判断某个时间是否在两个时间之内([start,end]闭区间)
     *
     * @param checkDate 待判断的时间
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return
     */
    public static boolean isTimeInRange(Date checkDate, Date startDate, Date endDate) {
        boolean inRange = checkDate.after(startDate) && checkDate.before(endDate);
        if (inRange || checkDate.getTime() == startDate.getTime()
                || checkDate.getTime() == endDate.getTime()) {
            return true;
        }
        return false;
    }

    /**
     * 构建开始结束范围
     * 输入2020-03-01 至 2020-03-02
     * 得到 [2020-03-01 00:00:00  2020-03-03 00:00:00)  左闭右开
     */
    public static List<Date> buildStartEndRange(Date startDate, Date endDate) {
        Date openEndDate = addDay(endDate, 1);
        Date start = getTimeOfDayStart(startDate);
        Date end = getTimeOfDayStart(openEndDate);
        return Lists.newArrayList(start, end);
    }

    public static Date getTimeOfDayStart(Date date) {
        String dateStr = DateTimeUtil.toString(date, DateTimeUtilFormat.yyyy_MM_dd);
        String timeOfDateStart = dateStr + " 00:00:00";
        return DateTimeUtil.toDate(timeOfDateStart, DateTimeUtilFormat.yyyy_MM_dd_HH_mm_ss);
    }

    /**
     * 获取指定天数的结束时间
     *
     * @param date
     * @return
     */
    public static Date getTimeOfDayEnd(Date date) {
        String dateStr = DateTimeUtil.toString(date, DateTimeUtilFormat.yyyy_MM_dd);
        String timeOfDateStart = dateStr + " 23:59:59";
        return DateTimeUtil.toDate(timeOfDateStart, DateTimeUtilFormat.yyyy_MM_dd_HH_mm_ss);
    }

    /**
     * 判断两个时间间隔的天数
     *
     * @param date1 前面的时间
     * @param date2 后面的时间
     * @return {@link Long}
     * @date 2020/9/10 16:30
     * @author hongjie
     */
    public static Long calDayInterval(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return null;
        }
        return ((getTimeOfDayStart(date2).getTime() - getTimeOfDayStart(date1).getTime()) / (24 * 60 * 60 * 1000));
    }

    public static Long calDayInterval(Long date1, Long date2) {
        if (date1 == null || date2 == null) {
            return null;
        }
        Long dayInterval = (date2 - date1) / (24 * 60 * 60 * 1000);
        return dayInterval;
    }


    /**
     * 获取date当天的开始和结束时间
     *
     * @param date        基础时间
     * @param containsEnd 是否包含end，包含--使用23:59:59，不包含--使用00:00:00
     * @return {@link Pair < Date, Date>}
     * @date 24/11/2021 下午3:18
     * @author hongjie
     */
    public static Pair<Date, Date> getDateStartToEnd(Date date, boolean containsEnd) {
        if (date == null) {
            throw new IllegalArgumentException("date cannot be null");
        }
        Date start = getTimeOfDayStart(date);
        Date end = containsEnd ? getTimeOfDayEnd(date) : addDay(start, 1);
        return Pair.of(start, end);
    }

    public static Pair<Date, Date> getTodayStartToEnd(boolean containsEnd) {
        Date date = new Date();
        return getDateStartToEnd(date, containsEnd);
    }


    /**
     * 获取指定格式的日期，并精确到指定的时间
     *
     * @param date
     * @param dateTimeUtilFormat
     * @return
     */
    public static Date getDateSpecifiedFormat(Date date, DateTimeUtilFormat dateTimeUtilFormat) {

        String dateStr = DateTimeUtil.dateFormat(date, dateTimeUtilFormat.format);
        return DateTimeUtil.toDate(dateStr, dateTimeUtilFormat);
    }

    /**
     * 判断时间1  是否早于   时间2
     *
     * @param date1 原时间
     * @param date2 参考时间
     * @return {@link boolean}
     * @date 5/1/2022 下午9:57
     * @author hongjie
     */
    public static boolean isBefore(Date date1, Date date2) {
        return date1.getTime() <= date2.getTime();
    }

    /**
     * 判断时间是否在指定的时间段之内
     *
     * @param sourceDate
     * @param targetDate
     * @param days
     * @return
     */
    public static boolean isBetweenSpecialDays(Date sourceDate, Date targetDate, int days) {
        if (Objects.isNull(sourceDate) || Objects.isNull(targetDate)) {
            return false;
        }
        return targetDate.before(sourceDate) & addDay(sourceDate, -days).before(targetDate);
    }

    /**
     * 获取两个时间差值
     *
     * @param timest 开始时间
     * @param timeed 结束时间
     * @param type   1 天，2 时， 3 分， 4 秒， 5 毫秒（ms）
     * @return 成功返回时间差值，不成功返回null
     */
    public static Long getTimeDiff(Date timest, Date timeed, int type) {
        try {
            if (timest == null) {
                log.info("getTimeDiff timest 为空！");
                return null;
            }
            if (timeed == null) {
                log.info("getTimeDiff timeed 为空！");
                return null;
            }

            Long result = null;
            switch (type) {
                case 1:
                    result = getDiffDays(timest, timeed);
                    break;
                case 2:
                    result = getDiffHours(timest, timeed);
                    break;
                case 3:
                    result = getDiffMinutes(timest, timeed);
                    break;
                case 4:
                    result = getDiffSeconds(timest, timeed);
                    break;
                case 5:
                    result = getDiffStamp(timest, timeed);
                    break;
                default:
                    log.info("getTimeDiff type未定义！");
                    break;
            }
            return result;

        } catch (Exception e) {
            log.error("getTimeDiff 异常：" + e.getMessage());
        }
        return null;
    }

    /**
     * 获取两个时间相差的天数
     *
     * @param timest 开始时间
     * @param timeed 结束时间
     * @return 成功返回相差天数，不成功返回null
     */
    public static Long getDiffDays(Date timest, Date timeed) {
        try {
            Calendar calst = Calendar.getInstance();
            Calendar caled = Calendar.getInstance();
            calst.setTime(timest);
            caled.setTime(timeed);
//			log.info("LongDiffDays 开始时间：" + formatDate(timest, "yyyy-MM-dd"));
//			log.info("LongDiffDays 结束时间：" + formatDate(timeed, "yyyy-MM-dd"));

            //设置时间为0时
            calst.set(Calendar.HOUR_OF_DAY, 0);
            calst.set(Calendar.MINUTE, 0);
            calst.set(Calendar.SECOND, 0);

            caled.set(Calendar.HOUR_OF_DAY, 0);
            caled.set(Calendar.MINUTE, 0);
            caled.set(Calendar.SECOND, 0);
            //得到两个日期相差的天数
            Long days = ((Long) (caled.getTime().getTime() / 1000) - (Long) (calst.getTime().getTime() / 1000)) / 3600 / 24;

            return days;
        } catch (Exception e) {
            log.error("LongDiffDays 异常：" + e.getMessage());
        }
        return null;
    }

    /**
     * 获取两个时间相差的分钟数
     *
     * @param timest 开始时间
     * @param timeed 结束时间
     * @return 成功返回相差分钟数，不成功返回null
     */
    private static Long getDiffHours(Date timest, Date timeed) {
        try {
            Calendar calst = Calendar.getInstance();
            Calendar caled = Calendar.getInstance();
            calst.setTime(timest);
            caled.setTime(timeed);
//			log.info("LongDiffDays 开始时间：" + formatDate(timest, "yyyy-MM-dd HH:mm:ss"));
//			log.info("LongDiffDays 结束时间：" + formatDate(timeed, "yyyy-MM-dd HH:mm:ss"));

            //设置时间为0时
//			calst.set(java.util.Calendar.HOUR_OF_DAY, 0);
            calst.set(Calendar.MINUTE, 0);
            calst.set(Calendar.SECOND, 0);

//			caled.set(java.util.Calendar.HOUR_OF_DAY, 0);
            caled.set(Calendar.MINUTE, 0);
            caled.set(Calendar.SECOND, 0);
            //得到两个日期相差的天数
            Long hoerss = ((Long) (caled.getTime().getTime() / 1000) - (Long) (calst.getTime().getTime() / 1000)) / 3600;

            return hoerss;
        } catch (Exception e) {
            log.error("getDiffHours 异常：" + e.getMessage());
        }
        return null;
    }

    /**
     * 获取两个时间相差的分钟数
     *
     * @param timest 开始时间
     * @param timeed 结束时间
     * @return 成功返回相差分钟数，不成功返回null
     */
    private static Long getDiffMinutes(Date timest, Date timeed) {
        try {
            Calendar calst = Calendar.getInstance();
            Calendar caled = Calendar.getInstance();
            calst.setTime(timest);
            caled.setTime(timeed);
//			log.info("getDiffMinutes 开始时间:{},结束时间:{}", formatDate(timest, "yyyy-MM-dd HH:mm:ss"), formatDate(timeed, "yyyy-MM-dd HH:mm:ss"));

            //设置时间为0时
//			calst.set(java.util.Calendar.HOUR_OF_DAY, 0);
//			calst.set(java.util.Calendar.MINUTE, 0);
            calst.set(Calendar.SECOND, 0);

//			caled.set(java.util.Calendar.HOUR_OF_DAY, 0);
//			caled.set(java.util.Calendar.MINUTE, 0);
            caled.set(Calendar.SECOND, 0);
            //得到两个日期相差的天数
            Long minutess = ((Long) (caled.getTime().getTime() / 1000) - (Long) (calst.getTime().getTime() / 1000)) / 60;

            return minutess;
        } catch (Exception e) {
            log.info("getDiffMinutes 异常：" + e.getMessage());
        }
        return null;
    }

    /**
     * 获取两个时间相差的数据刻度
     *
     * @param timest 开始时间
     * @param timeed 结束时间
     * @return 成功返回时间刻度，不成功返回null
     */
    private static Long getDiffSeconds(Date timest, Date timeed) {
        try {
            Calendar calst = Calendar.getInstance();
            Calendar caled = Calendar.getInstance();
            calst.setTime(timest);
            caled.setTime(timeed);

            //得到两个日期相差的天数
            Long minutess = ((Long) (caled.getTime().getTime() / 1000) - (Long) (calst.getTime().getTime() / 1000));

            return minutess;
        } catch (Exception e) {
            log.error("getDiffSeconds 异常：" + e.getMessage());
        }
        return null;
    }

    /**
     * 获取两个时间的相差时间刻度
     *
     * @param timest 开始时间
     * @param timeed 结束时间
     * @return 成功返回时间刻度，不成功返回null
     */
    private static Long getDiffStamp(Date timest, Date timeed) {
        try {
            Calendar calst = Calendar.getInstance();
            Calendar caled = Calendar.getInstance();
            calst.setTime(timest);
            caled.setTime(timeed);

            //得到两个日期相差的天数
            Long stamps = ((Long) (caled.getTime().getTime()) - (Long) (calst.getTime().getTime()));

            return stamps;
        } catch (Exception e) {
            log.error("getDiffStamp 异常：" + e.getMessage());
        }
        return null;
    }

    public static Date getTodayTimeLimit(String timeLimitStr, String dateFormat) {
        LocalTime timeLimit = LocalTime.parse(timeLimitStr, DateTimeFormatter.ofPattern(dateFormat));
        LocalDate today = LocalDate.now();
        LocalDateTime taskEndTime = LocalDateTime.of(today, timeLimit);
        return Date.from(taskEndTime.atZone(ZoneId.systemDefault()).toInstant());
    }

}

