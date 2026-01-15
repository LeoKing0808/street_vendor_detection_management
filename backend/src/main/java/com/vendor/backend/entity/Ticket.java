package com.vendor.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
public class Ticket {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "camera_id")
    private String cameraId;

    @Column(name = "region_id")
    private String regionId;

    @Column(name = "personnel_id")
    private String personnelId;

    @Column(name = "create_frame_id")
    private String createFrameId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "object_count")
    private Integer objectCount;

    @Column(name = "status")
    private String status;

    @Column(name = "close_frame_id")
    private String closeFrameId;

    @Column(name = "closed_at")
    private LocalDateTime closedAt;

    @ManyToOne
    @JoinColumn(name = "camera_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Camera camera;

    @ManyToOne
    @JoinColumn(name = "region_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Region region;

    @ManyToOne
    @JoinColumn(name = "personnel_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Personnel personnel;

    @ManyToOne
    @JoinColumn(name = "create_frame_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Frame createFrame;

    @ManyToOne
    @JoinColumn(name = "close_frame_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Frame closeFrame;

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCameraId() { return cameraId; }
    public void setCameraId(String cameraId) { this.cameraId = cameraId; }

    public String getRegionId() { return regionId; }
    public void setRegionId(String regionId) { this.regionId = regionId; }

    public String getPersonnelId() { return personnelId; }
    public void setPersonnelId(String personnelId) { this.personnelId = personnelId; }

    public String getCreateFrameId() { return createFrameId; }
    public void setCreateFrameId(String createFrameId) { this.createFrameId = createFrameId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Integer getObjectCount() { return objectCount; }
    public void setObjectCount(Integer objectCount) { this.objectCount = objectCount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCloseFrameId() { return closeFrameId; }
    public void setCloseFrameId(String closeFrameId) { this.closeFrameId = closeFrameId; }

    public LocalDateTime getClosedAt() { return closedAt; }
    public void setClosedAt(LocalDateTime closedAt) { this.closedAt = closedAt; }

    public Camera getCamera() { return camera; }
    public void setCamera(Camera camera) { this.camera = camera; }

    public Region getRegion() { return region; }
    public void setRegion(Region region) { this.region = region; }

    public Personnel getPersonnel() { return personnel; }
    public void setPersonnel(Personnel personnel) { this.personnel = personnel; }

    public Frame getCreateFrame() { return createFrame; }
    public void setCreateFrame(Frame createFrame) { this.createFrame = createFrame; }

    public Frame getCloseFrame() { return closeFrame; }
    public void setCloseFrame(Frame closeFrame) { this.closeFrame = closeFrame; }
}