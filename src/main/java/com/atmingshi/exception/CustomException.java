package com.atmingshi.exception;

/**
 * 自定义异常，用于抛出异常
 * @author yang
 * @create 2023-07-13 22:20
 */
public class CustomException extends RuntimeException{

    public CustomException(String message){
        super(message);
    }

}
