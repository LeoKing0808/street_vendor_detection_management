package com.vendor.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "regions")
public class Region {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;
}