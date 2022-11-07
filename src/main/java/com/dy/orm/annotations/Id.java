package com.dy.orm.annotations;

import java.lang.annotation.*;

/**
 * @author zhanglianyong
 * 2022/11/7 21:08
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface Id {
    /**
     * 注解的值
     */
    String value();

    /**
     * 类型
     */
    String type() default "Long";

    /**
     * 长度
     */
    int length() default 30;

    /**
     * 是否为自增
     */
    boolean increment() default true;

}
