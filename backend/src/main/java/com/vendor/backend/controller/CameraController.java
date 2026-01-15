package com.vendor.backend.controller;

import com.vendor.backend.dto.request.CameraStatusRequest;
import com.vendor.backend.dto.response.ResponseDTOs.ApiResponse;
import com.vendor.backend.service.CameraService;
import com.vendor.backend.service.FrameService;
import com.vendor.backend.service.AlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cameras")
public class CameraController {
    @Autowired
    private FrameService frameService;

    @Autowired
    private CameraService cameraService;

    @Autowired
    private AlertService alertService;

    /**
     * 关闭摄像头的所有活跃工单
     * @param cameraId 摄像头ID
     */
    @PostMapping("/{cameraId}/tickets/close")
    public ApiResponse<?> closeActiveTickets(@PathVariable String cameraId) {
        frameService.closeActiveTickets(cameraId);
        return ApiResponse.success("工单关闭成功");
    }

    /**
     * 启用摄像头
     * @param request 启用请求
     */
    @PostMapping("/enable")
    public ApiResponse<?> enableCameras(@Valid @RequestBody CameraStatusRequest request) {
        cameraService.enableCameras(request.getCameraIds());
        return ApiResponse.success("摄像头启用成功");
    }

    /**
     * 禁用摄像头
     * @param request 禁用请求
     */
    @PostMapping("/disable")
    public ApiResponse<?> disableCameras(@Valid @RequestBody CameraStatusRequest request) {
        cameraService.disableCameras(request.getCameraIds());
        return ApiResponse.success("摄像头禁用成功");
    }

    /**
     * 获取摄像头详情
     * @param cameraId 摄像头ID
     * @return 摄像头详情
     */
    @GetMapping("/{cameraId}")
    public ApiResponse<?> getCameraDetails(@PathVariable String cameraId) {
        var details = cameraService.getCameraDetails(cameraId);
        return ApiResponse.success("获取摄像头详情成功", details);
    }

    /**
     * 获取摄像头最新帧的最新播报信息
     * @param cameraId 摄像头ID
     * @param frameId 帧ID
     * @return 播报信息
     */
    @GetMapping("/{cameraId}/frames/{frameId}/latestAlert")
    public ApiResponse<?> getLatestAlert(@PathVariable String cameraId, @PathVariable String frameId) {
        var alert = alertService.getLatestAlertByFrameId(frameId);
        if (alert != null) {
            return ApiResponse.success("获取告警信息成功", Map.of("alertMessage", alert.getMessage()));
        }
        return ApiResponse.success("获取告警信息成功", Map.of("alertMessage", "No alert found"));
    }

    /**
     * 获取识别到物体的引导信息
     * @param cameraId 摄像头ID
     * @param objectId 物体ID
     * @return 引导信息
     */
    @GetMapping("/{cameraId}/objects/{objectId}/details")
    public ApiResponse<?> getObjectDetails(@PathVariable String cameraId, @PathVariable String objectId) {
        // 实际实现中需要根据物体ID查询详细信息
        var details = Map.of(
            "type", "food",
            "evictTimes", 4,
            "ticketId", "ticket-123",
            "assignedPersonnel", "张伟 (执法编号: 123)",
            "evictMessages", List.of(
                "穿红色外套的大爷，此处不允许摆摊，请移步至中山路便民市场，从路口直行200米右转即到。",
                "请注意，此处禁止占道经营，请配合离开。"
            )
        );
        return ApiResponse.success("获取物体详情成功", details);
    }
}