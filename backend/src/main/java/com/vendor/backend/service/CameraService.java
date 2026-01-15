package com.vendor.backend.service;

import com.vendor.backend.entity.Camera;
import com.vendor.backend.entity.Ticket;
import com.vendor.backend.repository.CameraRepository;
import com.vendor.backend.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CameraService {
    @Autowired
    private CameraRepository cameraRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    @Lazy
    private FrameService frameService;

    /**
     * 检查摄像头是否有未完结工单
     * @param cameraId 摄像头ID
     * @return 是否有未完结工单
     */
    public boolean hasActiveTicket(String cameraId) {
        Camera camera = cameraRepository.findById(cameraId).orElse(null);
        if (camera == null) {
            return false;
        }
        // 使用反射获取字段值
        try {
            java.lang.reflect.Field field = Camera.class.getDeclaredField("hasActiveTicket");
            field.setAccessible(true);
            return (Boolean) field.get(camera);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 更新摄像头的活跃工单状态
     * @param cameraId 摄像头ID
     * @param hasActiveTicket 是否有活跃工单
     */
    public void updateActiveTicketStatus(String cameraId, boolean hasActiveTicket) {
        Camera camera = cameraRepository.findById(cameraId).orElse(null);
        if (camera != null) {
            // 使用反射设置字段值
            try {
                java.lang.reflect.Field field = Camera.class.getDeclaredField("hasActiveTicket");
                field.setAccessible(true);
                field.set(camera, hasActiveTicket);
                cameraRepository.save(camera);
            } catch (Exception e) {
                // 忽略错误
            }
        }
    }

    /**
     * 获取摄像头所属区域
     * @param cameraId 摄像头ID
     * @return 区域ID
     */
    public String getCameraRegion(String cameraId) {
        Camera camera = cameraRepository.findById(cameraId).orElse(null);
        if (camera == null) {
            return null;
        }
        // 使用反射获取字段值
        try {
            java.lang.reflect.Field field = Camera.class.getDeclaredField("regionId");
            field.setAccessible(true);
            return (String) field.get(camera);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 关闭工单时更新摄像头状态
     * @param cameraId 摄像头ID
     */
    public void updateStatusAfterTicketClose(String cameraId) {
        // 检查该摄像头是否有其他未完结工单
        List<Ticket> activeTickets = ticketRepository.findByCameraIdAndStatusNot(cameraId, "Verified");
        boolean hasActiveTickets = !activeTickets.isEmpty();

        // 更新摄像头状态
        updateActiveTicketStatus(cameraId, hasActiveTickets);
    }

    /**
     * 启用摄像头（已优化：批量查询，避免 N+1 问题）
     * @param cameraIds 摄像头ID列表
     */
    @org.springframework.transaction.annotation.Transactional
    public void enableCameras(List<String> cameraIds) {
        // 批量查询，避免 N+1 问题
        List<Camera> cameras = cameraRepository.findAllById(cameraIds);
        cameras.forEach(camera -> {
            // 使用反射设置字段值
            try {
                java.lang.reflect.Field field = Camera.class.getDeclaredField("active");
                field.setAccessible(true);
                field.set(camera, true);
            } catch (Exception e) {
                // 忽略错误
            }
        });
        cameraRepository.saveAll(cameras); // 批量保存
    }

    /**
     * 禁用摄像头（已优化：批量查询，避免 N+1 问题）
     * @param cameraIds 摄像头ID列表
     */
    @org.springframework.transaction.annotation.Transactional
    public void disableCameras(List<String> cameraIds) {
        // 批量查询，避免 N+1 问题
        List<Camera> cameras = cameraRepository.findAllById(cameraIds);
        cameras.forEach(camera -> {
            // 使用反射设置字段值
            try {
                java.lang.reflect.Field field = Camera.class.getDeclaredField("active");
                field.setAccessible(true);
                field.set(camera, false);
            } catch (Exception e) {
                // 忽略错误
            }
        });
        cameraRepository.saveAll(cameras); // 批量保存
    }

    /**
     * 获取摄像头详情
     * @param cameraId 摄像头ID
     * @return 摄像头详情
     */
    public Map<String, Object> getCameraDetails(String cameraId) {
        Camera camera = cameraRepository.findById(cameraId).orElse(null);
        if (camera != null) {
            // 获取最新帧
            var latestFrame = frameService.getLatestFrame(cameraId);
            
            Map<String, Object> details = new java.util.HashMap<>();
            // 使用反射获取字段值
            try {
                java.lang.reflect.Field activeField = Camera.class.getDeclaredField("active");
                activeField.setAccessible(true);
                details.put("active", activeField.get(camera));
                
                java.lang.reflect.Field hasActiveTicketField = Camera.class.getDeclaredField("hasActiveTicket");
                hasActiveTicketField.setAccessible(true);
                details.put("hasActiveTicket", hasActiveTicketField.get(camera));
            } catch (Exception e) {
                details.put("active", false);
                details.put("hasActiveTicket", false);
            }

            // 添加最新帧信息
            if (latestFrame != null) {
                details.put("latestFrameObjects", Map.of(
                    "frameId", latestFrame.getId(),
                    "timestamp", latestFrame.getTimestamp(),
                    "objects", latestFrame.getObjects(),
                    "imageUrl", latestFrame.getImageUrl(),
                    "objectDetails", List.of()
                ));
            }

            return details;
        }
        return null;
    }
}