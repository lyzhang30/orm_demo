package com.dy.orm.exception;

import java.lang.reflect.Field;

/**
 * @author zhanglianyong
 * 2022/11/7 22:04
 */
public class FieldException extends BaseException {

    public FieldException(String message, Throwable ex) {
        super(message, ex);
    }

    public FieldException(String message) {
        super(message);
    }


}
