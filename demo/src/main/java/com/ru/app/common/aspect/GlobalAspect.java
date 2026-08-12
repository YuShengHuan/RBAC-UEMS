package com.ru.app.common.aspect;


import com.ru.app.common.service.AuthorityService;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collections;

@Aspect
@Component
public class GlobalAspect {
    private final AuthorityService authorityService;
    GlobalAspect(AuthorityService authorityService){
         this.authorityService=authorityService;
    }
    @Around("execution(* com.ru.app.*.*.controller..*(..))")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        if(authorityService.hasPermByCurrentRoleAndRequestURI()){
            return pjp.proceed();
        }
        return ResponseEntity.ok(Collections.emptyList());
    }
}