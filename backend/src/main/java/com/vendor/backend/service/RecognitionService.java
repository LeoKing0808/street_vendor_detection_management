package com.vendor.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;

@Service
public class RecognitionService {
    @Autowired
    private RedisTemplate<String, Integer> redisTemplate;

    private static final String CAMERA_COUNT_KEY = "camera:%s:count";
    private static final int DETECTION_THRESHOLD = 5; // 连续检测到物体的帧数阈值
    private static final long TTL_SECONDS = 300; // 5分钟过期

    // Lua脚本：原子递增计数并检查阈值
    private final DefaultRedisScript<Boolean> incrementScript;
    
    // Lua脚本：获取计数
    private final DefaultRedisScript<Integer> getCountScript;

    public RecognitionService() {
        // 初始化递增脚本
        String incrementScriptText = 
            "local key = KEYS[1]\n" +
            "local threshold = tonumber(ARGV[1])\n" +
            "local ttl = tonumber(ARGV[2])\n" +
            "\n" +
            "local current = redis.call('get', key)\n" +
            "if not current then\n" +
            "    current = 0\n" +
            "end\n" +
            "\n" +
            "current = current + 1\n" +
            "redis.call('set', key, current, 'EX', ttl)\n" +
            "\n" +
            "return current >= threshold\n";
        
        incrementScript = new DefaultRedisScript<>(incrementScriptText, Boolean.class);
        
        // 初始化获取计数脚本
        String getCountScriptText = 
            "local key = KEYS[1]\n" +
            "local current = redis.call('get', key)\n" +
            "if not current then\n" +
            "    return 0\n" +
            "end\n" +
            "return tonumber(current)\n";
        
        getCountScript = new DefaultRedisScript<>(getCountScriptText, Integer.class);
    }

    /**
     * 更新摄像头的连续检测帧数
     * @param cameraId 摄像头ID
     * @param detected 是否检测到物体
     * @return 是否达到阈值
     */
    public boolean updateDetectionCount(String cameraId, boolean detected) {
        String key = String.format(CAMERA_COUNT_KEY, cameraId);
        
        if (detected) {
            // 使用Lua脚本原子递增计数并检查阈值
            Boolean result = redisTemplate.execute(
                incrementScript,
                Collections.singletonList(key),
                String.valueOf(DETECTION_THRESHOLD),
                String.valueOf(TTL_SECONDS)
            );
            return result != null && result;
        } else {
            // 未检测到物体，重置计数
            redisTemplate.delete(key);
            return false;
        }
    }

    /**
     * 重置摄像头的检测计数
     * @param cameraId 摄像头ID
     */
    public void resetDetectionCount(String cameraId) {
        String key = String.format(CAMERA_COUNT_KEY, cameraId);
        redisTemplate.delete(key);
    }

    /**
     * 获取摄像头的当前检测计数
     * @param cameraId 摄像头ID
     * @return 当前计数
     */
    public Integer getDetectionCount(String cameraId) {
        String key = String.format(CAMERA_COUNT_KEY, cameraId);
        Integer result = redisTemplate.execute(
            getCountScript,
            Collections.singletonList(key)
        );
        return result != null ? result : 0;
    }
}