//package com.example.bookmarketfront.aspect;
//
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.Signature;
//import org.aspectj.lang.annotation.*;
//import org.springframework.stereotype.Component;
//
//@Component
//@Aspect
//@Slf4j
//public class LogAspect {
//    public long t1 ;
//    public long t2;
//
//    @Pointcut("execution(* com.example.bookmarketfront..*(..))")
//    private void pointCut(){}
//
//    //指定的方法在切面切入目标方法之前执行，注入后做什么动作，参数是切点方法
//    @Before("pointCut()")
//    public void logAdvice(JoinPoint joinPoint){
//        Signature signature = joinPoint.getSignature();// 获取签名
//        String declaringTypeName = signature.getDeclaringTypeName();//获取切入的包名
//        String funcName = signature.getName();//获取即将执行的方法名
//        t1 = System.currentTimeMillis();
//        //log.debug("执行方法：{}",funcName);
//    }
//
//    //指定的方法在切面切入目标方法之后执行
//    @After("pointCut()")
//    public void logAdvice1(JoinPoint joinPoint) throws Throwable {
//        Signature signature = joinPoint.getSignature();// 获取签名
//        t2=System.currentTimeMillis();
//        log.debug("执行方法：{}，执行时间：{}ms",signature.getName(),(t2-t1));
//        System.out.println();
//    }
//}
