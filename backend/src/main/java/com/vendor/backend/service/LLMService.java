package com.vendor.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class LLMService {
    @Autowired
    private RestTemplate restTemplate;

    private static final String LLM_API_URL = "http://external-llm-system/api/generate";
    private static final String TTS_API_URL = "http://external-tts-system/api/generate";

    /**
     * 生成播报内容
     * @param cameraId 摄像头ID
     * @param objectCount 物体数量
     * @return 播报内容
     */
    public String generateAlertMessage(String cameraId, int objectCount) {
        try {
            Map<String, Object> requestBody = Map.of(
                "prompt", String.format("生成一条城管播报内容，提醒摄像头 %s 附近的 %d 个摊贩离开，语气友好，提供附近的合法经营地点。", cameraId, objectCount),
                "maxTokens", 200
            );

            Map<String, Object> response = restTemplate.postForObject(
                LLM_API_URL,
                requestBody,
                Map.class
            );

            return response != null ? (String) response.get("text") : "请注意，此处禁止占道经营，请配合离开。";
        } catch (Exception e) {
            System.err.println("Failed to generate alert message: " + e.getMessage());
            return "请注意，此处禁止占道经营，请配合离开。";
        }
    }

    /**
     * 生成MP3文件
     * @param text 文本内容
     * @return MP3文件URL
     */
    public String generateMP3(String text) {
        try {
            Map<String, Object> requestBody = Map.of(
                "text", text,
                "voice", "default"
            );

            Map<String, Object> response = restTemplate.postForObject(
                TTS_API_URL,
                requestBody,
                Map.class
            );

            return response != null ? (String) response.get("audioUrl") : null;
        } catch (Exception e) {
            System.err.println("Failed to generate MP3: " + e.getMessage());
            return null;
        }
    }
}