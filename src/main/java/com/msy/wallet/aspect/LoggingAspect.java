package com.msy.wallet.aspect;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
    private final HttpServletRequest request;

    public LoggingAspect(HttpServletRequest request) {
        this.request = request;
    }

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void controllerMethods() {}

    @Around("controllerMethods()")
    public Object logRequestAndResponse(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getMethod().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String requestUri = request.getRequestURI();
        String httpMethod = request.getMethod();
        Object[] args = joinPoint.getArgs();

        logger.info("Request [{}] {} - {}.{} | Params: {}", httpMethod, requestUri, className, methodName, Arrays.toString(args));

        Object response;
        try {
            response = joinPoint.proceed();
            long duration = System.currentTimeMillis() - startTime;
            logger.info("Response [{}] {} - {}.{} | Duration: {}ms | Result: {}", httpMethod, requestUri, className, methodName, duration, response);
            return response;
        } catch (Exception ex) {
            long duration = System.currentTimeMillis() - startTime;
            logger.error("Error [{}] {} - {}.{} | Duration: {}ms | Exception: {}", httpMethod, requestUri, className, methodName, duration, ex.getMessage(), ex);
            throw ex;
        }
    }
}
