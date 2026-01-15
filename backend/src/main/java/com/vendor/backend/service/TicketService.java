package com.vendor.backend.service;

import com.vendor.backend.entity.Ticket;
import com.vendor.backend.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TicketService {
    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private CameraService cameraService;

    @Autowired
    private NavigationService navigationService;

    /**
     * 分配工单
     * @param ticketId 工单ID
     * @param personnelId 人员ID
     * @return 更新后的工单
     */
    public Ticket assignTicket(String ticketId, String personnelId) {
        Ticket ticket = ticketRepository.findById(ticketId).orElse(null);
        if (ticket != null) {
            // 使用反射设置字段
            try {
                java.lang.reflect.Field personnelIdField = Ticket.class.getDeclaredField("personnelId");
                personnelIdField.setAccessible(true);
                personnelIdField.set(ticket, personnelId);
                
                java.lang.reflect.Field statusField = Ticket.class.getDeclaredField("status");
                statusField.setAccessible(true);
                statusField.set(ticket, "Assigned");
            } catch (Exception e) {
                return null;
            }

            // 发送导航请求 - 使用反射获取cameraId
            try {
                java.lang.reflect.Field cameraIdField = Ticket.class.getDeclaredField("cameraId");
                cameraIdField.setAccessible(true);
                String ticketCameraId = (String) cameraIdField.get(ticket);
                navigationService.sendNavigationRequest(ticketCameraId, personnelId);
            } catch (Exception e) {
                // 忽略错误
            }

            return ticketRepository.save(ticket);
        }
        return null;
    }

    /**
     * 关闭摄像头的所有工单
     * @param cameraId 摄像头ID
     */
    public void closeTickets(String cameraId) {
        // 查找该摄像头的所有活跃工单
        List<Ticket> activeTickets = ticketRepository.findAll().stream()
                .filter(ticket -> {
                    try {
                        java.lang.reflect.Field cameraIdField = Ticket.class.getDeclaredField("cameraId");
                        cameraIdField.setAccessible(true);
                        String ticketCameraId = (String) cameraIdField.get(ticket);
                        
                        java.lang.reflect.Field statusField = Ticket.class.getDeclaredField("status");
                        statusField.setAccessible(true);
                        String ticketStatus = (String) statusField.get(ticket);
                        
                        return cameraId.equals(ticketCameraId) && !"Verified".equals(ticketStatus);
                    } catch (Exception e) {
                        return false;
                    }
                })
                .toList();

        // 关闭所有活跃工单
        LocalDateTime now = LocalDateTime.now();
        for (Ticket ticket : activeTickets) {
            // 使用反射设置字段
            try {
                java.lang.reflect.Field statusField = Ticket.class.getDeclaredField("status");
                statusField.setAccessible(true);
                statusField.set(ticket, "Verified");
                
                java.lang.reflect.Field closedAtField = Ticket.class.getDeclaredField("closedAt");
                closedAtField.setAccessible(true);
                closedAtField.set(ticket, now);
            } catch (Exception e) {
                // 如果反射失败，跳过
            }
            ticketRepository.save(ticket);
        }

        // 更新摄像头状态
        cameraService.updateActiveTicketStatus(cameraId, false);
    }

    /**
     * 更新工单状态
     * @param ticketId 工单ID
     * @param status 状态
     * @return 更新后的工单
     */
    public Ticket updateTicketStatus(String ticketId, String status) {
        Ticket ticket = ticketRepository.findById(ticketId).orElse(null);
        if (ticket != null) {
            // 使用反射设置字段
            try {
                java.lang.reflect.Field statusField = Ticket.class.getDeclaredField("status");
                statusField.setAccessible(true);
                statusField.set(ticket, status);
            } catch (Exception e) {
                return null;
            }
            return ticketRepository.save(ticket);
        }
        return null;
    }
}