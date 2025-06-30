package com.ywq.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MethodCostAspect {

    @Pointcut("execution(* com.ywq.service.DishService.*(..))")
    public void pointcut(){}

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result;
        try{
            result = joinPoint.proceed();
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
        long end = System.currentTimeMillis();
        long cost = end - start;
        System.out.println("方法执行耗时：" + cost + "ms");
        return result;
    }
}
