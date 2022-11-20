package com.dy.orm.annotations;

import java.lang.annotation.*;

/**
 * @author zhanglianyong
 * 2022/11/20 16:08
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface Transaction {

     Class<? extends Throwable>[] rollBaseFor() default{};

}
