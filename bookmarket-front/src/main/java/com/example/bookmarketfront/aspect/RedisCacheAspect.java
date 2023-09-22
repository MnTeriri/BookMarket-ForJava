package com.example.bookmarketfront.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class RedisCacheAspect {
    @Pointcut("execution(* com.example.bookmarketfront..*(..))")
    private void pointCut(){}
}
