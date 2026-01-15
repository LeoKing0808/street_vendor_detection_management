package com.vendor.backend.controller;

import com.vendor.backend.dto.request.DetectionRequest;
import com.vendor.backend.dto.response.ResponseDTOs;
import com.vendor.backend.entity.Frame;
import com.vendor.backend.exception.BusinessException;
import com.vendor.backend.service.RecognitionService;
import com.vendor.backend.service.FrameService;
import com.vendor.backend.service.BroadcastService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 识别控制器
 * 已优化：使用 DTO 验证、统一异常处理、添加日志
 */
@RestController
@RequestMapping("/api/recognition")
public class RecognitionController {
    private static final Logger logger = LoggerFactory.getLogger(RecognitionController.class);
    
    @Autowired
    private RecognitionService recognitionService;

    @Autowired
    private FrameService frameService;

    @Autowired
    private BroadcastService broadcastService;

    /**
     * 处理AI引擎的检测结果
     * @param request 检测结果请求（已通过验证）
     * @return 处理结果
     */
    @PostMapping("/detect")
    public ResponseDTOs.ApiResponse<Map<String, Object>> handleDetection(@Valid @RequestBody DetectionRequest request) {
        logger.info("收到检测请求: cameraId={}, detected={}", request.getCameraId(), request.getDetected());
        
        // 更新检测计数
        boolean thresholdReached = recognitionService.updateDetectionCount(
            request.getCameraId(), 
            request.getDetected()
        );

        if (request.getDetected() && thresholdReached) {
            // 达到阈值，处理检测结果
            return processDetection(request);
        } else if (!request.getDetected()) {
            // 未检测到物体，关闭所有活跃工单
            frameService.closeActiveTickets(request.getCameraId());
            logger.info("未检测到物体，已关闭摄像头 {} 的所有活跃工单", request.getCameraId());
            return ResponseDTOs.ApiResponse.success(Map.of(
                "message", "No objects detected, active tickets closed"
            ));
        } else {
            // 检测到物体但未达到阈值
            return ResponseDTOs.ApiResponse.success(Map.of(
                "message", "Detection count updated",
                "thresholdReached", false
            ));
        }
    }

    /**
     * 处理达到阈值的检测结果
     */
    private ResponseDTOs.ApiResponse<Map<String, Object>> processDetection(DetectionRequest request) {
        try {
            // 创建帧信息
            Frame frame = new Frame();
            frame.setCameraId(request.getCameraId());
            frame.setTimestamp(LocalDateTime.now());
            
            // 使用 Jackson 将 objects 转为 JSON 字符串
            if (request.getObjects() != null) {
                com.fasterxml.jackson.databind.ObjectMapper mapper = 
                    new com.fasterxml.jackson.databind.ObjectMapper();
                frame.setObjects(mapper.writeValueAsString(request.getObjects()));
            } else {
                frame.setObjects("{}");
            }
            
            frame.setImageUrl(request.getImageUrl());

            // 保存帧信息
            List<Frame> savedFrames = frameService.saveFrames(List.of(frame));
            Frame savedFrame = savedFrames.get(0);

            // 创建工单
            int objectCount = request.getObjects() != null ? request.getObjects().size() : 0;
            frameService.createTicket(request.getCameraId(), savedFrame.getId().toString(), objectCount);

            // 发送播报
            broadcastService.sendBroadcast(
                String.format("摄像头 %s 检测到 %d 个小贩", request.getCameraId(), objectCount),
                request.getCameraId()
            );

            // 重置计数
            recognitionService.resetDetectionCount(request.getCameraId());

            logger.info("成功处理检测结果: cameraId={}, frameId={}, objectCount={}", 
                request.getCameraId(), savedFrame.getId(), objectCount);

            return ResponseDTOs.ApiResponse.success(Map.of(
                "message", "Detection processed successfully",
                "frameId", savedFrame.getId(),
                "objectCount", objectCount
            ));
        } catch (Exception e) {
            logger.error("处理检测结果失败: cameraId={}", request.getCameraId(), e);
            throw new BusinessException("Failed to process detection: " + e.getMessage());
        }
    }
}