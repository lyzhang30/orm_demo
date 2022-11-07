package com.dy.orm.exception;

/**
 * @author zhanglianyong
 * 2022/11/7 21:23
 */
public class DataBaseException extends BaseException {


    public DataBaseException(String message, Exception ex) {
        super(message, ex);
    }

    public DataBaseException(String message, String message1) {
        super(message, message1);
    }

    public DataBaseException(String message, Throwable cause, String message1) {
        super(message, cause, message1);
    }

    public DataBaseException(Throwable cause, String message) {
        super(cause, message);
    }

    public static DataBaseException failToConnect(String message, Exception ex) {
        return new DataBaseException(message, ex);
    }
}
