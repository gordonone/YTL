package com.ytl.crm.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * 重试模板
 *
 * @author hongjie
 * @date 2021/6/22 16:53
 */
@Slf4j
public class RetryUtil {

    /**
     * 默认最大重试次数
     */
    private static final int DEFAULT_MAX_RETRY_TIMES = 3;
    /**
     * 默认sleep的次数
     */
    private static final long DEFAULT_SLEEP_MILLS = 30L;

    /**
     * 重试执行（默认重试3次，每次sleep 30ms）
     * （retPredict = null，没有异常就是成功）
     *
     * @param ivkMethodName 执行方法的名字（用于打印日志）
     * @param func          执行函数
     * @return {@link R}
     * @date 28/12/2022 下午8:03
     * @author hongjie
     */
    public static <R> R execute(String ivkMethodName, Supplier<R> func) {
        return execute(ivkMethodName, func, null, DEFAULT_MAX_RETRY_TIMES, DEFAULT_SLEEP_MILLS);
    }

    /**
     * 重试执行（默认重试3次，每次sleep 30ms）
     *
     * @param ivkMethodName 执行方法的名字（用于打印日志）
     * @param func          执行函数（有返回结果）
     * @param retPredict    结果校验
     * @return {@link R}
     * @date 28/12/2022 下午8:05
     * @author hongjie
     */
    public static <R> R execute(String ivkMethodName, Supplier<R> func, Predicate<R> retPredict) {
        return execute(ivkMethodName, func, retPredict, DEFAULT_MAX_RETRY_TIMES, DEFAULT_SLEEP_MILLS);
    }

    /**
     * 重试执行
     *
     * @param ivkMethodName 执行方法的名字（用于打印日志）
     * @param func          执行函数（有返回结果）
     * @param retPredict    结果校验
     * @param maxRetryTimes 最大重试次数
     * @param sleepMills    重试sleep的毫秒数
     * @return {@link R}
     * @date 28/12/2022 下午8:05
     * @author hongjie
     */
    public static <R> R execute(String ivkMethodName, Supplier<R> func, Predicate<R> retPredict, Integer maxRetryTimes, Long sleepMills) {
        boolean isSuccess = false;
        int retryTimes = 0;
        R ret = null;
        while (!isSuccess && retryTimes < maxRetryTimes) {
            try {
                //重置结果
                ret = null;
                //适当休眠
                if (sleepMills != null && retryTimes > 0) {
                    TimeUnit.MILLISECONDS.sleep(sleepMills);
                }
                retryTimes++;
                ret = func.get();

                //如果retPredict未null，是否成功的定义是是否报错
                isSuccess = retPredict == null || retPredict.test(ret);
                if (!isSuccess) {
                    log.error("[{}]结果校验为失败，重试次数retryTimes={}", ivkMethodName, retryTimes);
                }
            } catch (Exception e) {
                log.error("[{}]执行异常，重试次数retryTimes={}", ivkMethodName, retryTimes, e);
            }
        }

        if (!isSuccess) {
            log.error("[{}]最终执行失败", ivkMethodName);
        }
        return ret;
    }

    /**
     * 重试执行（默认重试3次，每次sleep 30ms）
     *
     * @param ivkMethodName 执行方法的名字（用于打印日志）
     * @param func          执行函数（无返回结果）
     * @date 28/12/2022 下午8:05
     * @author hongjie
     */
    public static void execute(String ivkMethodName, Runnable func) {
        execute(ivkMethodName, func, DEFAULT_MAX_RETRY_TIMES, DEFAULT_SLEEP_MILLS);
    }

    /**
     * 重试执行（默认重试3次，每次sleep 30ms）
     *
     * @param ivkMethodName 执行方法的名字（用于打印日志）
     * @param func          执行函数（无返回结果）
     * @param maxRetryTimes 最大重试次数
     * @param sleepMills    重试sleep的毫秒数
     * @date 28/12/2022 下午8:05
     * @author hongjie
     */
    public static void execute(String ivkMethodName, Runnable func, Integer maxRetryTimes, Long sleepMills) {
        boolean isSuccess = false;
        int retryTimes = 0;
        while (!isSuccess && retryTimes < maxRetryTimes) {
            try {
                //适当休眠
                if (sleepMills != null && retryTimes > 0) {
                    TimeUnit.MILLISECONDS.sleep(sleepMills);
                }
                retryTimes++;
                func.run();
                isSuccess = true;
            } catch (Exception e) {
                log.error("[{}]执行异常，重试次数retryTimes={}", ivkMethodName, retryTimes, e);
            }
        }

        if (!isSuccess) {
            log.error("[{}]最终执行失败", ivkMethodName);
        }
    }

}


