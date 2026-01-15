package com.vendor.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class NavigationService {
    @Autowired
    private RestTemplate restTemplate;

    private static final String NAVIGATION_API_URL = "http://external-navigation-system/api/navigate";

    /**
     * 发送导航请求
     * @param cameraId 摄像头ID
     * @param personnelId 工作人员ID
     * @return 导航是否成功
     */
    public boolean sendNavigationRequest(String cameraId, String personnelId) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> requestBody = Map.of(
                "cameraId", cameraId,
                "personnelId", personnelId
            );

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            Map<String, Object> response = restTemplate.exchange(
                NAVIGATION_API_URL,
                HttpMethod.POST,
                entity,
                Map.class
            ).getBody();

            return response != null && "success".equals(response.get("status"));
        } catch (Exception e) {
            System.err.println("Failed to send navigation request: " + e.getMessage());
            return false;
        }
    }
}