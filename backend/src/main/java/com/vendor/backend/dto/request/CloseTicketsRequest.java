package com.vendor.backend.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * 关闭工单请求DTO
 */
public class CloseTicketsRequest {

    @NotBlank(message = "摄像头ID不能为空")
    private String cameraId;

    // getters and setters
    public String getCameraId() {
        return cameraId;
    }

    public void setCameraId(String cameraId) {
        this.cameraId = cameraId;
    }
}
