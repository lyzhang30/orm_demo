package com.dy.orm.utils;

import com.dy.orm.exception.ArgumentException;

import java.util.Collection;
import java.util.Collections;

/**
 * @author zhanglianyong
 * 2022/11/8 10:48
 */
public class CollectionUtils {

    public static boolean CollectionIsNull(Collection collections) {
        if (null == collections) {
            return true;
        }
        return collections.size() < 1;
    }
}
