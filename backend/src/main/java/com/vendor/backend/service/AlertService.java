package com.vendor.backend.service;

import com.vendor.backend.entity.Alert;
import com.vendor.backend.repository.AlertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class AlertService {
    @Autowired
    private AlertRepository alertRepository;

    @Autowired
    private LLMService llmService;

    @Autowired
    private BroadcastService broadcastService;

    private static final int BROADCAST_THRESHOLD = 3; // 播报驱离次数阈值

    /**
     * 创建告警
     * @param cameraId 摄像头ID
     * @param frameId 帧ID
     * @param objectCount 物体数量
     * @return 创建的告警
     */
    public Alert createAlert(String cameraId, String frameId, int objectCount) {
        // 生成播报内容
        String message = llmService.generateAlertMessage(cameraId, objectCount);

        // 生成MP3文件
        String audioFileUrl = llmService.generateMP3(message);

        // 创建告警
        Alert alert = new Alert();
        alert.setId(UUID.randomUUID().toString());
        alert.setFrameId(frameId);
        alert.setMessage(message);
        alert.setAudioFileUrl(audioFileUrl);
        alert.setCreatedAt(LocalDateTime.now());

        // 保存告警
        Alert savedAlert = alertRepository.save(alert);

        // 发送播报
        broadcastService.sendBroadcast(message, cameraId);

        return savedAlert;
    }

    /**
     * 获取摄像头的告警次数
     * @param cameraId 摄像头ID
     * @return 告警次数
     */
    public int getAlertCount(String cameraId) {
        // 这里需要根据实际的查询逻辑实现
        // 暂时返回0
        return 0;
    }

    /**
     * 检查是否需要分配城管
     * @param cameraId 摄像头ID
     * @return 是否需要分配城管
     */
    public boolean needAssignPersonnel(String cameraId) {
        return getAlertCount(cameraId) >= BROADCAST_THRESHOLD;
    }

    /**
     * 获取帧的最新告警
     * @param frameId 帧ID
     * @return 最新告警
     */
    public Alert getLatestAlertByFrameId(String frameId) {
        List<Alert> alerts = alertRepository.findAll().stream()
                .filter(alert -> frameId.equals(alert.getFrameId()))
                .sorted((a1, a2) -> a2.getCreatedAt().compareTo(a1.getCreatedAt()))
                .toList();

        return alerts.isEmpty() ? null : alerts.get(0);
    }

    /**
     * 从alert表中读取alert并调用播报系统播报
     * @param alertId 告警ID
     * @param cameraId 摄像头ID
     * @return 是否播报成功
     */
    public boolean broadcastAlert(String alertId, String cameraId) {
        Alert alert = alertRepository.findById(alertId).orElse(null);
        if (alert == null) {
            return false;
        }

        return broadcastService.sendBroadcast(alert.getMessage(), cameraId);
    }
}