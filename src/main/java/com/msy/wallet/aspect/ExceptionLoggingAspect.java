package com.msy.wallet.aspect;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ExceptionLoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionLoggingAspect.class);
    private final HttpServletRequest request;

    public ExceptionLoggingAspect(HttpServletRequest request) {
        this.request = request;
    }

    @Pointcut("within(@org.springframework.web.bind.annotation.ControllerAdvice *)")
    public void exceptionHandlerMethods() {}

    @AfterReturning(pointcut = "exceptionHandlerMethods()", returning = "response")
    public void logHandledException(JoinPoint joinPoint, Object response) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getMethod().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String requestUri = request.getRequestURI();
        String httpMethod = request.getMethod();

        logger.error("⚠️  Handled Exception [{}] {} - {}.{} | Response: {}", httpMethod, requestUri, className, methodName, response);
    }
}

