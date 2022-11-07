package com.dy.orm.annotations;

import java.lang.annotation.*;

/**
 * @author zhanglianyong
 * 2022/11/7 21:04
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Table {
    /**
     * 表名
     */
    String value() ;
}
