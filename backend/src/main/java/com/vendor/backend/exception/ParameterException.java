package com.vendor.backend.exception;

/**
 * 参数异常类
 */
public class ParameterException extends BusinessException {

    public ParameterException(String message) {
        super(message, "PARAM_ERROR");
    }

    public ParameterException(String message, Throwable cause) {
        super(message, "PARAM_ERROR", cause);
    }
}
