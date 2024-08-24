package com.ytl.crm.utils;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;

import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.concurrent.atomic.AtomicInteger;

import static java.net.InetAddress.getLocalHost;

/**
 * 订单编号生成器
 *
 * @author Ziroom
 */
@Slf4j
public class GenerateIdUtil {

    private static final int IP;
    private static AtomicInteger counter = new AtomicInteger(0);
    private final static int INDEX_MIN_VALUE = 0;
    private final static int INDEX_MAX_VALUE = 9999;
    private final static int COUNT_LEN = 4;
    private final static int IP_LEN = 2;
    private final static String PADDING_CHAR = "0";

    static {
        int ipAddress;
        try {
            ipAddress = getLocalHostAddress();
            if (ipAddress < 0) {
                ipAddress += 256;
            }
        } catch (Exception e) {
            ipAddress = 0;
        }
        IP = ipAddress;
    }


    /**
     * 生成固定位数的随机数
     *
     * @param length 随机数长度
     * @return string
     */
    private static String getRandom(int length) {
        char[] chars = new char[length];
        for (int i = 0; i < length; i++) {
            chars[i] = '0';
        }
        String patten = new String(chars);
        int randomBase = Integer.valueOf("1" + patten);
        int rand = (int) (Math.random() * randomBase);
        DecimalFormat decimalFormat = new DecimalFormat(patten);
        return decimalFormat.format(rand);
    }

    /**
     * 获取计数，到999时归零
     *
     * @return
     */
    private static int getCount() {
        synchronized (GenerateIdUtil.class) {
            counter.compareAndSet(INDEX_MAX_VALUE, INDEX_MIN_VALUE);
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                log.error("当前线程被中断，获取ID发生异常", e);
            }
            return counter.incrementAndGet();
        }
    }

    private static String transString(Integer num, Integer length) {
        String postfix = Integer.toString(num);
        if (postfix.length() < length) {
            return (Strings.repeat(PADDING_CHAR, length - postfix.length()) + postfix);
        } else {
            return postfix;
        }
    }

    private static Integer getLocalHostAddress() {
        try {
            return (int) getLocalHost().getAddress()[3];
        } catch (UnknownHostException e) {
            log.error("get local host addr error. the error is {}", e);
        }
        return null;
    }
}
