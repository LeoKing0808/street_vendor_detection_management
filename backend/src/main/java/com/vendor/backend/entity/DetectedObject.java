package com.vendor.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "objects")
public class DetectedObject {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "frame_id")
    private String frameId;

    @Column(name = "type")
    private String type;

    @ManyToOne
    @JoinColumn(name = "frame_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Frame frame;
}