package com.dy.orm.exception;

/**
 * @author zhanglianyong
 * 2022/11/7 21:22
 */
public class BaseException extends RuntimeException {

    private String message;

    private Throwable exception;

    public BaseException(String message, Throwable ex) {
        this.message = message;
        this.exception = ex;
    }

    public BaseException(String message) {
        this.message = message;
    }


    public BaseException(String message, String message1) {
        super(message);
        this.message = message1;
    }

    public BaseException(String message, Throwable cause, String message1) {
        super(message, cause);
        this.message = message1;
    }

    public BaseException(Throwable cause, String message) {
        super(cause);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
