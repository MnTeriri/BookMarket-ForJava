package com.example.bookmarketfront.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class ControllerAspect {
    @Around("execution(* com.example.bookmarketfront.controller..*(..))")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();//获取方法参数值数组
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();//得到其方法签名
        log.debug("执行函数{},请求参数为{}", methodSignature.getName(), args);
        return joinPoint.proceed();
    }
}
