package com.vendor.backend.dto.request;

import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * 启用/禁用摄像头请求DTO
 */
public class CameraStatusRequest {

    @NotNull(message = "摄像头ID列表不能为空")
    private List<String> cameraIds;

    // getters and setters
    public List<String> getCameraIds() {
        return cameraIds;
    }

    public void setCameraIds(List<String> cameraIds) {
        this.cameraIds = cameraIds;
    }
}
