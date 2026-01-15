package com.vendor.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import java.util.List;

/**
 * 请求DTO类
 */
public class RequestDTOs {

    /**
     * 创建工单请求DTO
     */
    public static class CreateTicketRequest {

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

    /**
     * 分配工单请求DTO
     */
    public static class AssignTicketRequest {

        @NotBlank(message = "人员ID不能为空")
        private String personnelId;

        // getters and setters
        public String getPersonnelId() {
            return personnelId;
        }

        public void setPersonnelId(String personnelId) {
            this.personnelId = personnelId;
        }
    }

    /**
     * 启用/禁用摄像头请求DTO
     */
    public static class CameraStatusRequest {

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

    /**
     * 关闭工单请求DTO
     */
    public static class CloseTicketsRequest {

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

    /**
     * 创建用户请求DTO
     */
    public static class CreateUserRequest {

        @NotBlank(message = "用户名不能为空")
        private String username;

        @NotBlank(message = "密码不能为空")
        private String password;

        @NotBlank(message = "角色不能为空")
        private String role;

        // getters and setters
        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }
    }

    /**
     * 用户登录请求DTO
     */
    public static class LoginRequest {

        @NotBlank(message = "用户名不能为空")
        private String username;

        @NotBlank(message = "密码不能为空")
        private String password;

        // getters and setters
        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    /**
     * 帧创建请求DTO
     */
    public static class CreateFrameRequest {

        @NotBlank(message = "摄像头ID不能为空")
        private String cameraId;
        
        @NotBlank(message = "物体信息不能为空")
        private String objects;
        
        @NotBlank(message = "图像URL不能为空")
        private String imageUrl;

        // getters and setters
        public String getCameraId() {
            return cameraId;
        }

        public void setCameraId(String cameraId) {
            this.cameraId = cameraId;
        }

        public String getObjects() {
            return objects;
        }

        public void setObjects(String objects) {
            this.objects = objects;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }
    }
}