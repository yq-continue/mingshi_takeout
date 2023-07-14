package com.atmingshi.utils;

/**
 * @author yang
 * @create 2023-07-13 16:24
 */
public abstract class TransferId {
    private static ThreadLocal<Long> thread = new ThreadLocal<>();

    public static void setId(Long id){
        thread.set(id);
    }

    public static Long gteId(){
        return thread.get();
    }
}
