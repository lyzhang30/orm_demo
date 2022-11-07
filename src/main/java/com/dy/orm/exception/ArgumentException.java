package com.dy.orm.exception;

/**
 * @author zhanglianyong
 * 2022/11/7 21:42
 */
public class ArgumentException extends BaseException {
    public ArgumentException(String message, Throwable ex) {
        super(message, ex);
    }


    public ArgumentException(String message, String message1) {
        super(message, message1);
    }

    public ArgumentException(String message) {
        super(message);
    }
    public ArgumentException(String message, Throwable cause, String message1) {
        super(message, cause, message1);
    }

    public ArgumentException(Throwable cause, String message) {
        super(cause, message);
    }

    public static ArgumentException IllegalArgumentException(String message) {
        return new ArgumentException(message);
    }

}
