package com.vendor.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.vendor.backend")
public class VendorManagementBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(VendorManagementBackendApplication.class, args);
    }
}
