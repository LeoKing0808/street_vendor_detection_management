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
public class BroadcastService {
    @Autowired
    private RestTemplate restTemplate;

    private static final String BROADCAST_API_URL = "http://external-broadcast-system/api/broadcast";

    /**
     * 发送播报请求
     * @param message 播报消息
     * @param cameraId 摄像头ID
     * @return 播报是否成功
     */
    public boolean sendBroadcast(String message, String cameraId) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> requestBody = Map.of(
                "message", message,
                "cameraId", cameraId
            );

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            Map<String, Object> response = restTemplate.exchange(
                BROADCAST_API_URL,
                HttpMethod.POST,
                entity,
                Map.class
            ).getBody();

            return response != null && "success".equals(response.get("status"));
        } catch (Exception e) {
            System.err.println("Failed to send broadcast: " + e.getMessage());
            return false;
        }
    }
}