package com.vendor.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "cameras")
public class Camera {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "region_id")
    private String regionId;

    @Column(name = "active")
    private boolean active;

    @Column(name = "has_active_ticket")
    private boolean hasActiveTicket;

    @ManyToOne
    @JoinColumn(name = "region_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Region region;
}