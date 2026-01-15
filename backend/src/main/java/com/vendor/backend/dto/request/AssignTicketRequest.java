package com.vendor.backend.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * 分配工单请求DTO
 */
public class AssignTicketRequest {

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
