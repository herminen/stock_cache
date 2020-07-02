package com.lh.stock.stockcache.util;

import org.springframework.context.ApplicationContext;

/**
 * Created with IntelliJ IDEA.
 * User: ASUS
 * Date: 2020/7/2
 * Time: 23:19
 * Description: No Description
 */
public class SpringContextUtil {
    private static ApplicationContext applicationContext;

    public static ApplicationContext getApplicationContext(){
        return applicationContext;
    }

    public static void setApplicationContext(ApplicationContext applicationContext){
        SpringContextUtil.applicationContext = applicationContext;
    }

    public static <T> T getBean(String name){
        return (T)applicationContext.getBean(name);
    }

    public static <T> T getBean(Class<T> clazz){
        return applicationContext.getBean(clazz);
    }

}
