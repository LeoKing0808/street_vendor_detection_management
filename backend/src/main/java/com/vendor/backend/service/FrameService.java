package com.vendor.backend.service;

import com.vendor.backend.entity.Frame;
import com.vendor.backend.entity.Ticket;
import com.vendor.backend.repository.FrameRepository;
import com.vendor.backend.repository.TicketRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
@Service
public class FrameService {
    private static final Logger logger = LoggerFactory.getLogger(FrameService.class);
    
    @Autowired
    private FrameRepository frameRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private CameraService cameraService;

    @Autowired
    private AlertService alertService;

    @Autowired
    private NavigationService navigationService;

    /**
     * 批量保存帧信息
     * @param frames 帧信息列表
     * @return 保存的帧信息
     */
    @org.springframework.transaction.annotation.Transactional(rollbackFor = Exception.class)
    public List<Frame> saveFrames(List<Frame> frames) {
        List<Frame> savedFrames = frameRepository.saveAll(frames);

        // 为每个帧创建告警
        for (Frame frame : savedFrames) {
            int objectCount = countObjects(frame.getObjects());
            alertService.createAlert(frame.getCameraId(), frame.getId().toString(), objectCount);

            // 检查是否需要分配城管
            if (alertService.needAssignPersonnel(frame.getCameraId())) {
                // 这里可以添加分配城管的逻辑
            }
        }

        return savedFrames;
    }

    /**
     * 统计物体数量（已优化：使用 Jackson 解析 JSON）
     * @param objects 物体JSON字符串
     * @return 物体数量
     */
    private int countObjects(String objects) {
        if (objects == null || objects.isEmpty() || objects.equals("{}")) {
            return 0;
        }
        try {
            // 使用 Jackson 解析 JSON，更可靠
            com.fasterxml.jackson.databind.ObjectMapper mapper = 
                new com.fasterxml.jackson.databind.ObjectMapper();
            java.util.Map<String, Object> map = mapper.readValue(objects, java.util.Map.class);
            return map.size();
        } catch (Exception e) {
            logger.error("解析 objects JSON 失败: {}", objects, e);
            return 0;
        }
    }

    /**
     * 创建工单
     * @param cameraId 摄像头ID
     * @param frameId 帧ID
     * @param objectCount 物体数量
     * @return 创建的工单
     */
    @org.springframework.transaction.annotation.Transactional(rollbackFor = Exception.class)
    public Ticket createTicket(String cameraId, String frameId, int objectCount) {
        // 检查摄像头是否有未完结工单
        if (cameraService.hasActiveTicket(cameraId)) {
            return null; // 已有未完结工单，不创建新工单
        }

        // 获取摄像头所属区域
        String regionId = cameraService.getCameraRegion(cameraId);
        if (regionId == null) {
            return null; // 摄像头不存在或区域信息缺失
        }

        Ticket ticket = new Ticket();
        // 使用反射设置字段，避免Lombok在Docker中的问题
        try {
            String ticketId = UUID.randomUUID().toString();
            LocalDateTime now = LocalDateTime.now();
            
            java.lang.reflect.Field idField = Ticket.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(ticket, ticketId);
            
            java.lang.reflect.Field cameraIdField = Ticket.class.getDeclaredField("cameraId");
            cameraIdField.setAccessible(true);
            cameraIdField.set(ticket, cameraId);
            
            java.lang.reflect.Field regionIdField = Ticket.class.getDeclaredField("regionId");
            regionIdField.setAccessible(true);
            regionIdField.set(ticket, regionId);
            
            java.lang.reflect.Field createFrameIdField = Ticket.class.getDeclaredField("createFrameId");
            createFrameIdField.setAccessible(true);
            createFrameIdField.set(ticket, frameId);
            
            java.lang.reflect.Field createdAtField = Ticket.class.getDeclaredField("createdAt");
            createdAtField.setAccessible(true);
            createdAtField.set(ticket, now);
            
            java.lang.reflect.Field objectCountField = Ticket.class.getDeclaredField("objectCount");
            objectCountField.setAccessible(true);
            objectCountField.set(ticket, objectCount);
            
            java.lang.reflect.Field statusField = Ticket.class.getDeclaredField("status");
            statusField.setAccessible(true);
            statusField.set(ticket, "Unassigned");
        } catch (Exception e) {
            logger.error("创建工单失败", e);
            return null;
        }

        // 更新摄像头状态
        cameraService.updateActiveTicketStatus(cameraId, true);

        return ticketRepository.save(ticket);
    }

    /**
     * 关闭摄像头的所有活跃工单
     * @param cameraId 摄像头ID
     */
    @org.springframework.transaction.annotation.Transactional(rollbackFor = Exception.class)
    public void closeActiveTickets(String cameraId) {
        // 查找该摄像头的所有活跃工单
        List<Ticket> activeTickets = ticketRepository.findByCameraIdAndStatusNot(cameraId, "Verified");

        // 关闭所有活跃工单
        LocalDateTime now = LocalDateTime.now();
        for (Ticket ticket : activeTickets) {
            // 使用反射设置字段，避免Lombok在Docker中的问题
            try {
                java.lang.reflect.Field statusField = Ticket.class.getDeclaredField("status");
                statusField.setAccessible(true);
                statusField.set(ticket, "Verified");
                
                java.lang.reflect.Field closedAtField = Ticket.class.getDeclaredField("closedAt");
                closedAtField.setAccessible(true);
                closedAtField.set(ticket, now);
            } catch (Exception e) {
                logger.error("设置工单字段失败", e);
            }
            ticketRepository.save(ticket);
        }

        // 更新摄像头状态
        cameraService.updateActiveTicketStatus(cameraId, false);
    }

    /**
     * 获取摄像头的历史工单
     * @param cameraId 摄像头ID
     * @param timeRange 时间范围
     * @param status 状态
     * @return 工单列表
     */
    public List<Ticket> getTickets(String cameraId, String timeRange, String status) {
        if (status != null) {
            return ticketRepository.findByCameraIdAndStatus(cameraId, status);
        } else {
            return ticketRepository.findByCameraId(cameraId);
        }
    }

    /**
     * 分页获取工单
     * @param cameraId 摄像头ID
     * @param status 状态
     * @param pageable 分页参数
     * @return 分页工单列表
     */
    public Page<Ticket> getTicketsPage(String cameraId, String status, Pageable pageable) {
        return ticketRepository.findByCameraIdAndStatus(cameraId, status, pageable);
    }

    /**
     * 获取摄像头的最新帧
     * @param cameraId 摄像头ID
     * @return 最新帧
     */
    public Frame getLatestFrame(String cameraId) {
        return frameRepository.findLatestByCameraId(cameraId).orElse(null);
    }
}