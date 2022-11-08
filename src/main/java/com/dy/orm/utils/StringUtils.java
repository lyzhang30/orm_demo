package com.dy.orm.utils;

import com.dy.orm.exception.ArgumentException;

/**
 * @author zhanglianyong
 * 2022/11/8 10:44
 */
public class StringUtils {


    public static void getStringNotNull(Object property) {
        if (org.apache.commons.lang.StringUtils.isEmpty((String) property)) {
            throw ArgumentException.IllegalArgumentException(property + "没有get方法");
        }

    }
}
