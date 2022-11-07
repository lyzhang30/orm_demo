package com.dy.orm.annotations;

import java.lang.annotation.*;

/**
 * @author zhanglianyong
 * 2022/11/7 21:07
 *
 * 对象和表字段的一一映射
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface Column {

    /**
     * 注解的值
     */
    String value();

    /**
     * 类型
     */
    String type() default "String";

    /**
     * 长度
     */
    int length() default 30;

}
