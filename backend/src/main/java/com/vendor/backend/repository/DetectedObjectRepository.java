package com.vendor.backend.repository;

import com.vendor.backend.entity.DetectedObject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetectedObjectRepository extends JpaRepository<DetectedObject, String> {
}