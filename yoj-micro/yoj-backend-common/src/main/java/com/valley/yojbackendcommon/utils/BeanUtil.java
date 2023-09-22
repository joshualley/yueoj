package com.valley.yojbackendcommon.utils;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 获取Bean的工具类，解决创建的对象，属性的值不能被注入的问题
 */
@Component
public class BeanUtil implements ApplicationContextAware {
    private static ApplicationContext context;
    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        if (context == null) {
            context = applicationContext;
        }
    }
    /**
     * 获取Bean
     * @param name 表示其他要注入的注解name名
     * @return
     */
    public static Object getBean(String name) {
        return context.getBean(name);
    }

    /**
     * 获取Bean
     * @param c Bean的类名
     * @return
     * @param <T>
     */
    public static <T> T getBean(Class<T> c){
        return context.getBean(c);
    }
}
