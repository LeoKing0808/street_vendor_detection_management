package com.vendor.backend.dto.response;

/**
 * 响应DTO类
 */
public class ResponseDTOs {

    /**
     * 通用响应DTO
     */
    public static class ApiResponse<T> {

        private boolean success;
        private String message;
        private T data;
        private String errorCode;

        // 构造方法
        private ApiResponse(boolean success, String message, T data, String errorCode) {
            this.success = success;
            this.message = message;
            this.data = data;
            this.errorCode = errorCode;
        }

        // 静态工厂方法
        public static <T> ApiResponse<T> success(T data) {
            return new ApiResponse<>(true, "操作成功", data, null);
        }

        public static <T> ApiResponse<T> success(String message, T data) {
            return new ApiResponse<>(true, message, data, null);
        }

        public static ApiResponse<?> error(String message) {
            return new ApiResponse<>(false, message, null, null);
        }

        public static ApiResponse<?> error(String message, String errorCode) {
            return new ApiResponse<>(false, message, null, errorCode);
        }

        // getters
        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }

        public T getData() {
            return data;
        }

        public String getErrorCode() {
            return errorCode;
        }
    }

    /**
     * 分页响应DTO
     */
    public static class PageResponse<T> {

        private T data;
        private int page;
        private int size;
        private long total;
        private int totalPages;

        // 构造方法
        public PageResponse(T data, int page, int size, long total, int totalPages) {
            this.data = data;
            this.page = page;
            this.size = size;
            this.total = total;
            this.totalPages = totalPages;
        }

        // getters
        public T getData() {
            return data;
        }

        public int getPage() {
            return page;
        }

        public int getSize() {
            return size;
        }

        public long getTotal() {
            return total;
        }

        public int getTotalPages() {
            return totalPages;
        }
    }
}