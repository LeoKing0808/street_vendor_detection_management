package com.vendor.backend.repository;

import com.vendor.backend.entity.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, String> {

    /**
     * 按摄像头ID和状态查询工单
     * @param cameraId 摄像头ID
     * @param status 工单状态
     * @return 工单列表
     */
    List<Ticket> findByCameraIdAndStatus(String cameraId, String status);

    /**
     * 按摄像头ID查询所有工单
     * @param cameraId 摄像头ID
     * @return 工单列表
     */
    List<Ticket> findByCameraId(String cameraId);

    /**
     * 按摄像头ID和非指定状态查询工单（用于查找活跃工单）
     * @param cameraId 摄像头ID
     * @param status 排除的状态
     * @return 工单列表
     */
    List<Ticket> findByCameraIdAndStatusNot(String cameraId, String status);

    /**
     * 分页查询工单
     * @param cameraId 摄像头ID（可选）
     * @param status 工单状态（可选）
     * @param pageable 分页参数
     * @return 分页工单列表
     */
    @Query("SELECT t FROM Ticket t WHERE (:cameraId IS NULL OR t.cameraId = :cameraId) AND (:status IS NULL OR t.status = :status)")
    Page<Ticket> findByCameraIdAndStatus(@Param("cameraId") String cameraId, @Param("status") String status, Pageable pageable);
    /**
     * 按摄像头ID查询所有工单（带分页）
     * @param cameraId 摄像头ID
     * @param pageable 分页参数
     * @return 分页工单列表
     */
    @Query("SELECT t FROM Ticket t WHERE t.cameraId = :cameraId")
    Page<Ticket> findByCameraId(String cameraId, Pageable pageable);
}