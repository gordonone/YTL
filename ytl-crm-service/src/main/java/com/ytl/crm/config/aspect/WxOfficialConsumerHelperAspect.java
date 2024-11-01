package com.ytl.crm.config.aspect;


import com.ytl.crm.consumer.wechat.WxOfficialTokenException;
import com.ytl.crm.consumer.wechat.WxOfficialTokenHelper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Aspect
@Slf4j
@Component
public class WxOfficialConsumerHelperAspect {

    @Resource
    private WxOfficialTokenHelper wxOfficialTokenHelper;

    // 定义切点，只匹配 public 方法
    @Pointcut("execution(public * com.ytl.crm.consumer.wechat.WxOfficialConsumerHelper.*(..))")
    public void publicMethodPointcut() {
        // 该方法为空，仅作为注解的载体
    }


    /**
     * 统一处理请求AccessToken失效问题
     *
     * @param joinPoint 切点信息
     * @return Object
     * @throws Throwable 异常
     */
    @Around("publicMethodPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable throwable) {
            //如果是token原因
            if (throwable instanceof WxOfficialTokenException || throwable.getCause() instanceof WxOfficialTokenException) {
                //清空缓存，重新再请求一遍
              //  log.error("企微token已失效，清空缓存");
                wxOfficialTokenHelper.clearAccessToken();
                result = joinPoint.proceed();
            } else {
                throw throwable;
            }
        }
        return result;
    }
}
