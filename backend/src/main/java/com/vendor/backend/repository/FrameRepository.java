package com.vendor.backend.repository;

import com.vendor.backend.entity.Frame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FrameRepository extends JpaRepository<Frame, Integer> {

    /**
     * 按摄像头ID查询帧
     * @param cameraId 摄像头ID
     * @return 帧列表
     */
    List<Frame> findByCameraId(String cameraId);

    /**
     * 按摄像头ID查询最新帧
     * @param cameraId 摄像头ID
     * @return 最新帧
     */
    @Query("SELECT f FROM Frame f WHERE f.cameraId = :cameraId ORDER BY f.timestamp DESC LIMIT 1")
    Optional<Frame> findLatestByCameraId(@Param("cameraId") String cameraId);

    /**
     * 按摄像头ID和时间范围查询帧
     * @param cameraId 摄像头ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 帧列表
     */
    @Query("SELECT f FROM Frame f WHERE f.cameraId = :cameraId AND f.timestamp BETWEEN :startTime AND :endTime ORDER BY f.timestamp DESC")
    List<Frame> findByCameraIdAndTimestampBetween(@Param("cameraId") String cameraId, @Param("startTime") String startTime, @Param("endTime") String endTime);
}