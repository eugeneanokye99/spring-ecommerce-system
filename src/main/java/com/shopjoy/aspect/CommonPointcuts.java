package com.shopjoy.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CommonPointcuts {

    @Pointcut("execution(* com.shopjoy.service..*.*(..))")
    public void serviceMethods() {
    }

    @Pointcut("execution(* com.shopjoy.repository..*.*(..))")
    public void repositoryMethods() {
    }

    @Pointcut("execution(* com.shopjoy.controller..*.*(..))")
    public void controllerMethods() {
    }

    @Pointcut("execution(* com.shopjoy.graphql.resolver..*.*(..))")
    public void graphqlResolverMethods() {
    }

    @Pointcut("execution(* com.shopjoy.service.*.create*(..))")
    public void createMethods() {
    }

    @Pointcut("execution(* com.shopjoy.service.*.update*(..))")
    public void updateMethods() {
    }

    @Pointcut("execution(* com.shopjoy.service.*.delete*(..))")
    public void deleteMethods() {
    }

    @Pointcut("serviceMethods() || repositoryMethods() || controllerMethods()")
    public void allApplicationMethods() {
    }

    @Pointcut("createMethods() || updateMethods() || deleteMethods()")
    public void dataModificationMethods() {
    }
}
