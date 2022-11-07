package com.dy.orm.exception;

import com.dy.orm.annotations.Table;

/**
 * @author zhanglianyong
 * 2022/11/7 21:59
 */
public class TableException extends BaseException {
    public TableException(String message, Throwable ex) {
        super(message, ex);
    }

    public TableException(String message) {
        super(message);
    }

    public TableException(String message, String message1) {
        super(message, message1);
    }

    public TableException(String message, Throwable cause, String message1) {
        super(message, cause, message1);
    }

    public TableException(Throwable cause, String message) {
        super(cause, message);
    }

    public static TableException annotationNOtFound(Class clazz) {
        return new TableException(clazz + "没有注解");
    }
}
