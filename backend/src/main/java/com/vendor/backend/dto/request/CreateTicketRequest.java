package com.vendor.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;

/**
 * 创建工单请求DTO
 */
public class CreateTicketRequest {

    @NotBlank(message = "摄像头ID不能为空")
    private String cameraId;

    @NotBlank(message = "帧ID不能为空")
    private String frameId;

    @Min(value = 1, message = "物体数量必须大于0")
    private int objectCount;

    // getters and setters
    public String getCameraId() {
        return cameraId;
    }

    public void setCameraId(String cameraId) {
        this.cameraId = cameraId;
    }

    public String getFrameId() {
        return frameId;
    }

    public void setFrameId(String frameId) {
        this.frameId = frameId;
    }

    public int getObjectCount() {
        return objectCount;
    }

    public void setObjectCount(int objectCount) {
        this.objectCount = objectCount;
    }
}
