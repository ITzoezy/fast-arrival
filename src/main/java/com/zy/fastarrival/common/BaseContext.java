package com.zy.fastarrival.common;

/**
 * 基于ThreadLocal封装工具类，用户保存和获取当前登录用户id
 */
public class BaseContext {

    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    /**
     * 设置id值
     * @param ids
     */
    public static void setCurrentId(Long ids){

        threadLocal.set(ids);
    }

    /**
     * 获取值
     * @return
     */
    public static Long getCurrentId(){

       return threadLocal.get();
    }
}
