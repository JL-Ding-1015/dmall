package com.djl.dmall.admin.aop;

import com.djl.dmall.to.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一异常处理，给前端返回500
 */

@Slf4j
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {ArithmeticException.class})
    public Object handlerException01(Exception exception){
        log.error("全局异常感知，信息：{}",exception.getStackTrace());
        return new CommonResult().validateFailed("数学异常");

    }

    @ExceptionHandler(value = {NullPointerException.class})
    public Object handlerException02(Exception exception){
        log.error("全局异常感知，信息：{}",exception.getStackTrace());

        return new CommonResult().validateFailed("空指针异常");
    }

}
