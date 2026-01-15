package com.vendor.backend.handler;

import com.vendor.backend.dto.response.ResponseDTOs;
import com.vendor.backend.exception.BusinessException;
import com.vendor.backend.exception.ParameterException;
import com.vendor.backend.exception.ResourceNotFoundException;
import com.vendor.backend.exception.SystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ResponseDTOs.ApiResponse<?>> handleBusinessException(BusinessException e) {
        logger.warn("Business exception: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ResponseDTOs.ApiResponse.error(e.getMessage(), e.getErrorCode()));
    }

    /**
     * 处理参数异常
     */
    @ExceptionHandler(ParameterException.class)
    public ResponseEntity<ResponseDTOs.ApiResponse<?>> handleParameterException(ParameterException e) {
        logger.warn("Parameter exception: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ResponseDTOs.ApiResponse.error(e.getMessage(), e.getErrorCode()));
    }

    /**
     * 处理资源不存在异常
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResponseDTOs.ApiResponse<?>> handleResourceNotFoundException(ResourceNotFoundException e) {
        logger.warn("Resource not found exception: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ResponseDTOs.ApiResponse.error(e.getMessage(), e.getErrorCode()));
    }

    /**
     * 处理系统异常
     */
    @ExceptionHandler(SystemException.class)
    public ResponseEntity<ResponseDTOs.ApiResponse<?>> handleSystemException(SystemException e) {
        logger.error("System exception: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseDTOs.ApiResponse.error("系统内部错误，请稍后重试", e.getErrorCode()));
    }

    /**
     * 处理参数验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDTOs.ApiResponse<?>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        String errorMessage = bindingResult.getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.joining(", "));
        logger.warn("Method argument not valid exception: {}", errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ResponseDTOs.ApiResponse.error(errorMessage, "PARAM_VALIDATION_ERROR"));
    }

    /**
     * 处理其他异常
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDTOs.ApiResponse<?>> handleException(Exception e) {
        logger.error("Unexpected exception: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseDTOs.ApiResponse.error("系统内部错误，请稍后重试", "UNEXPECTED_ERROR"));
    }
}
