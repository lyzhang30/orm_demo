package com.dy.orm.utils;

import com.dy.orm.exception.ArgumentException;

import java.util.Locale;

/**
 * @author zhanglianyong
 * 2022/11/8 10:13
 */
public class GetterSetterUtils {

    /**
     * 获取get方法
     *
     * @param property 属性
     * @return get名字
     */
    public static String createGet(String property) {
        StringUtils.getStringNotNull(property);
        String substring = property.substring(0, 1);
        String firstChar = substring.toUpperCase(Locale.ROOT);
        System.out.println();
        StringBuilder sb = new StringBuilder();
        sb.append("get").append(firstChar).append(property.substring(1));
        return sb.toString();
    }

    /**
     *  获取set方法
     *
     * @param property 属性
     * @return set方法
     */
    public static String createSet(String property) {
        StringUtils.getStringNotNull(property);
        String substring = property.substring(0, 1);
        String firstChar = substring.toUpperCase(Locale.ROOT);
        System.out.println();
        StringBuilder sb = new StringBuilder();
        sb.append("set").append(firstChar).append(property.substring(1));
        return sb.toString();
    }



}
