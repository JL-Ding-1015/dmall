package com.djl.dmall.admin.aop;

import com.djl.dmall.to.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

@Slf4j
@Aspect
@Component
public class DataValidAspect {

    @Around("execution(* com.djl.dmall.admin..*Controller.*(..) )")
    public Object validAround(ProceedingJoinPoint pjp) {
        Object proceed = null;
        try {
            Object[] args = pjp.getArgs();
            for (Object arg : args) {
                if(arg instanceof BindingResult){
                    BindingResult result = (BindingResult) arg;
                    if(result.getErrorCount() > 0){
                        return new CommonResult().validateFailed(result);
                    }
                }
            }
            log.debug("校验切面方法执行..." );

            proceed = pjp.proceed(pjp.getArgs());

        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        } finally {
            System.out.println("this is finally");
        }


        return proceed;
    }



}
