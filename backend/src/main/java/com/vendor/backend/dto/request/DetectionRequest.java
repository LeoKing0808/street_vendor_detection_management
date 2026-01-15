package com.vendor.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

/**
 * AI 检测结果请求 DTO
 */
@Data
public class DetectionRequest {
    @NotBlank(message = "摄像头ID不能为空")
    private String cameraId;

    @NotNull(message = "检测结果不能为空")
    private Boolean detected;

    private Map<String, Object> objects;

    private String imageUrl;

    // 显式添加所有 getter 方法
    public String getCameraId() {
        return cameraId;
    }

    public Boolean getDetected() {
        return detected;
    }

    public Map<String, Object> getObjects() {
        return objects;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
