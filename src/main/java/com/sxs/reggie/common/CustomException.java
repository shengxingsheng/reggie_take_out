package com.sxs.reggie.common;

/**
 * 自定义业务层异常
 * @author sxs
 * @create 2022-08-22 18:43
 */
public class CustomException extends RuntimeException{

    public CustomException(String message) {
        super(message);
    }
}
