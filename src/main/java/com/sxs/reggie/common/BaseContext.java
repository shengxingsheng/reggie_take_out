package com.sxs.reggie.common;

/**
 * 基于ThreadLocal封装的工具类，用来在同一线程中获取和存储变量
 * @author sxs
 * @create 2022-08-22 16:49
 */
public class BaseContext {
    public static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    /**
     * 将用户的id放入线程中
     * @param id
     */
    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }
    /**
     * 从线程中取出用户的id
     */
    public static Long getCurrentId(){
        return threadLocal.get();
    }
}
