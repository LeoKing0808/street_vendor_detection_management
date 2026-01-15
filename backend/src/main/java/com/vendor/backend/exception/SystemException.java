package com.vendor.backend.exception;

/**
 * 系统异常类
 */
public class SystemException extends BusinessException {

    public SystemException(String message) {
        super(message, "SYSTEM_ERROR");
    }

    public SystemException(String message, Throwable cause) {
        super(message, "SYSTEM_ERROR", cause);
    }
}
