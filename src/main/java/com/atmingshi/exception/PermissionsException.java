package com.atmingshi.exception;

/**
 * @author yang
 * @create 2023-07-19 16:20
 */
public class PermissionsException extends RuntimeException {
    /**
     * 前台用户与后台用户权限异常
     * 前台用户与后台用户不允许同时登录
     * @param message
     */
    public PermissionsException(String message){
        super(message);
    }
}
