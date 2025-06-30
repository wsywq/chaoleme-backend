package com.ywq.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Aspect
@Component
public class MethodCountAspect {
    private final Map<String, AtomicInteger> methodCountMap = new ConcurrentHashMap<>();

    @Pointcut("@annotation(com.ywq.annotations.MethodCount)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object countMethodCalls(ProceedingJoinPoint joinPoint) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        String name = method.getName();
        Object result = null;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            AtomicInteger atomicInteger = methodCountMap.computeIfAbsent(name, key -> new AtomicInteger(0));
            atomicInteger.incrementAndGet();
        }
        return result;
    }

    public int getMethodCallCount(String methodName) {
        AtomicInteger counter = methodCountMap.get(methodName);
        return (counter != null) ? counter.get() : 0;
    }

}
