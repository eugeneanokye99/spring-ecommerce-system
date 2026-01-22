package com.shopjoy.aspect;

import com.shopjoy.util.AspectUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Aspect
@Component
public class LoggingAspect {
    
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    
    @Before("com.shopjoy.aspect.CommonPointcuts.serviceMethods()")
    public void logBeforeServiceMethod(JoinPoint joinPoint) {
        String className = AspectUtils.extractClassName(joinPoint);
        String methodName = AspectUtils.extractMethodName(joinPoint);
        String args = AspectUtils.sanitizeArgs(joinPoint.getArgs());
        String timestamp = LocalDateTime.now().format(timeFormatter);
        
        logger.info("[{}] ENTERING: {}.{} with arguments: {}", 
            timestamp, className, methodName, args);
    }
    
    @Before("com.shopjoy.aspect.CommonPointcuts.repositoryMethods()")
    public void logBeforeRepositoryMethod(JoinPoint joinPoint) {
        String className = AspectUtils.extractClassName(joinPoint);
        String methodName = AspectUtils.extractMethodName(joinPoint);
        String args = AspectUtils.sanitizeArgs(joinPoint.getArgs());
        
        logger.debug("DB CALL: {}.{} with arguments: {}", 
            className, methodName, args);
    }
    
    @Before("com.shopjoy.aspect.CommonPointcuts.controllerMethods()")
    public void logBeforeControllerMethod(JoinPoint joinPoint) {
        String className = AspectUtils.extractClassName(joinPoint);
        String methodName = AspectUtils.extractMethodName(joinPoint);
        String args = AspectUtils.sanitizeArgs(joinPoint.getArgs());
        
        logger.info("API ENDPOINT CALLED: {}.{} with arguments: {}", 
            className, methodName, args);
    }
    
    @AfterReturning(pointcut = "com.shopjoy.aspect.CommonPointcuts.serviceMethods()", returning = "result")
    public void logAfterServiceMethod(JoinPoint joinPoint, Object result) {
        String className = AspectUtils.extractClassName(joinPoint);
        String methodName = AspectUtils.extractMethodName(joinPoint);
        String resultStr = result != null ? result.getClass().getSimpleName() : "void";
        String timestamp = LocalDateTime.now().format(timeFormatter);
        
        logger.info("[{}] EXITING: {}.{} returned: {}", 
            timestamp, className, methodName, resultStr);
    }
    
    @AfterReturning(pointcut = "com.shopjoy.aspect.CommonPointcuts.controllerMethods()", returning = "result")
    public void logAfterControllerMethod(JoinPoint joinPoint, Object result) {
        String className = AspectUtils.extractClassName(joinPoint);
        String methodName = AspectUtils.extractMethodName(joinPoint);
        String resultStr = result != null ? result.getClass().getSimpleName() : "void";
        
        logger.info("API ENDPOINT COMPLETED: {}.{} returned: {}", 
            className, methodName, resultStr);
    }
    
    @AfterThrowing(pointcut = "com.shopjoy.aspect.CommonPointcuts.serviceMethods()", throwing = "exception")
    public void logServiceException(JoinPoint joinPoint, Exception exception) {
        String className = AspectUtils.extractClassName(joinPoint);
        String methodName = AspectUtils.extractMethodName(joinPoint);
        String args = AspectUtils.sanitizeArgs(joinPoint.getArgs());
        String timestamp = LocalDateTime.now().format(timeFormatter);
        
        logger.error("[{}] EXCEPTION in {}.{} with arguments: {}", 
            timestamp, className, methodName, args);
        logger.error("Exception type: {}", exception.getClass().getSimpleName());
        logger.error("Exception message: {}", exception.getMessage());
        logger.error("Stack trace:", exception);
    }
    
    @AfterThrowing(pointcut = "com.shopjoy.aspect.CommonPointcuts.repositoryMethods()", throwing = "exception")
    public void logRepositoryException(JoinPoint joinPoint, Exception exception) {
        String className = AspectUtils.extractClassName(joinPoint);
        String methodName = AspectUtils.extractMethodName(joinPoint);
        String args = AspectUtils.sanitizeArgs(joinPoint.getArgs());
        
        logger.error("DB EXCEPTION in {}.{} with arguments: {}", 
            className, methodName, args);
        logger.error("Database exception: {}", exception.getMessage());
        logger.error("Stack trace:", exception);
    }
    
    @AfterThrowing(pointcut = "com.shopjoy.aspect.CommonPointcuts.controllerMethods()", throwing = "exception")
    public void logControllerException(JoinPoint joinPoint, Exception exception) {
        String className = AspectUtils.extractClassName(joinPoint);
        String methodName = AspectUtils.extractMethodName(joinPoint);
        String args = AspectUtils.sanitizeArgs(joinPoint.getArgs());
        
        logger.error("API ENDPOINT EXCEPTION in {}.{} with arguments: {}", 
            className, methodName, args);
        logger.error("Controller exception: {}", exception.getMessage());
        logger.error("Stack trace:", exception);
    }
    
    @Around("com.shopjoy.aspect.CommonPointcuts.dataModificationMethods()")
    public Object logDataModificationMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodSignature = AspectUtils.extractMethodSignature(joinPoint);
        String args = AspectUtils.sanitizeArgs(joinPoint.getArgs());
        long startTime = System.currentTimeMillis();
        
        logger.info("DATA MODIFICATION STARTED: {} with arguments: {}", 
            methodSignature, args);
        
        try {
            Object result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - startTime;
            
            logger.info("DATA MODIFICATION COMPLETED: {} in {}", 
                methodSignature, AspectUtils.formatExecutionTime(executionTime));
            
            return result;
        } catch (Exception e) {
            long executionTime = System.currentTimeMillis() - startTime;
            
            logger.error("DATA MODIFICATION FAILED: {} after {} with exception: {}", 
                methodSignature, AspectUtils.formatExecutionTime(executionTime), e.getMessage());
            
            throw e;
        }
    }
}
