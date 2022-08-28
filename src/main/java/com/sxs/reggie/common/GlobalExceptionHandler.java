package com.sxs.reggie.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 异常处理器
 * @author sxs
 * @create 2022-08-20 16:06
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> doException(SQLIntegrityConstraintViolationException e){
        String message = e.getMessage();
        if (message.contains("Duplicate entry")){
            String msg = message.split(" ")[2]+"已存在！";
            return R.error(msg);
        }
        return R.error("未知错误");
    }
    @ExceptionHandler(CustomException.class)
    public R<String> doCustomException(CustomException e){
        log.error("自定义异常：{}",e.getMessage());
        return R.error(e.getMessage());
    }
}
