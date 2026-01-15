package com.vendor.backend.controller;

import com.vendor.backend.dto.request.CreateTicketRequest;
import com.vendor.backend.dto.request.AssignTicketRequest;
import com.vendor.backend.dto.request.CloseTicketsRequest;
import com.vendor.backend.dto.response.ResponseDTOs.ApiResponse;
import com.vendor.backend.entity.Ticket;
import com.vendor.backend.service.FrameService;
import com.vendor.backend.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.util.Map;

@RestController
@RequestMapping("/api/tickets")
@Validated
public class TicketController {
    @Autowired
    private FrameService frameService;

    @Autowired
    private TicketService ticketService;

    /**
     * 创建工单
     * @param request 创建工单请求
     * @return 创建的工单
     */
    @PostMapping
    public ApiResponse<?> createTicket(@Valid @RequestBody CreateTicketRequest request) {
        Ticket ticket = frameService.createTicket(
                request.getCameraId(),
                request.getFrameId(),
                request.getObjectCount()
        );
        if (ticket == null) {
            return ApiResponse.error("工单创建失败：摄像头已有未完结工单或摄像头信息不存在");
        }
        return ApiResponse.success("工单创建成功", ticket);
    }

    /**
     * 分配工单
     * @param ticketId 工单ID
     * @param request 分配请求
     * @return 更新后的工单
     */
    @PostMapping("/{ticketId}/assign")
    public ApiResponse<Ticket> assignTicket(
            @PathVariable String ticketId,
            @Valid @RequestBody AssignTicketRequest request) {
        Ticket ticket = ticketService.assignTicket(ticketId, request.getPersonnelId());
        return ApiResponse.success("工单分配成功", ticket);
    }

    /**
     * 关闭工单
     * @param request 关闭请求
     */
    @PostMapping("/close")
    public ApiResponse<?> closeTickets(@Valid @RequestBody CloseTicketsRequest request) {
        ticketService.closeTickets(request.getCameraId());
        return ApiResponse.success("工单关闭成功");
    }

    /**
     * 获取工单列表
     * @param cameraId 摄像头ID
     * @param timeRange 时间范围
     * @param status 状态
     * @return 工单列表
     */
    @GetMapping
    public ApiResponse<?> getTickets(
            @RequestParam String cameraId,
            @RequestParam(required = false) String timeRange,
            @RequestParam(required = false) String status) {
        var tickets = frameService.getTickets(cameraId, timeRange, status);
        return ApiResponse.success("获取工单列表成功", Map.of("tickets", tickets));
    }

    /**
     * 分页获取工单列表
     * @param cameraId 摄像头ID
     * @param status 状态
     * @param page 页码
     * @param size 每页大小
     * @return 分页工单列表
     */
    @GetMapping("/page")
    public ApiResponse<?> getTicketsPage(
            @RequestParam(required = false) String cameraId,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Ticket> ticketsPage = frameService.getTicketsPage(cameraId, status, pageable);
        return ApiResponse.success("获取工单列表成功", Map.of(
                "tickets", ticketsPage.getContent(),
                "page", page,
                "size", size,
                "total", ticketsPage.getTotalElements(),
                "totalPages", ticketsPage.getTotalPages()
        ));
    }
}