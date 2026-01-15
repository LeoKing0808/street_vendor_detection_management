package com.vendor.backend.controller;

import com.vendor.backend.service.AlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/alerts")
public class AlertController {
    @Autowired
    private AlertService alertService;

    /**
     * 播报告警
     * @param alertId 告警ID
     * @param request 播报请求
     * @return 播报结果
     */
    @PostMapping("/{alertId}/broadcast")
    public Map<String, Object> broadcastAlert(@PathVariable String alertId, @RequestBody Map<String, Object> request) {
        String cameraId = (String) request.get("cameraId");
        boolean success = alertService.broadcastAlert(alertId, cameraId);
        return Map.of("status", success ? "success" : "error");
    }
}