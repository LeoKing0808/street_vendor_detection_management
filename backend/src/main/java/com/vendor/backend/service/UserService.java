package com.vendor.backend.service;

import com.vendor.backend.entity.User;
import com.vendor.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 用户服务
 * 已修复：密码加密存储和验证
 */
@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    
    @Autowired
    private UserRepository userRepository;

    // 使用 BCrypt 进行密码加密
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * 创建用户（密码会自动加密）
     * @param username 用户名
     * @param password 明文密码
     * @return 创建的用户，如果用户名已存在则返回 null
     */
    @Transactional
    public User createUser(String username, String password) {
        // 检查用户名是否已存在
        if (userRepository.findByUsername(username) != null) {
            logger.warn("尝试创建已存在的用户: {}", username);
            return null; // 用户名已存在
        }

        User user = new User();
        user.setUsername(username);
        // 密码加密存储
        user.setPassword(passwordEncoder.encode(password));
        
        logger.info("创建新用户: {}", username);
        return userRepository.save(user);
    }

    /**
     * 用户登录（使用加密密码验证）
     * @param username 用户名
     * @param rawPassword 明文密码
     * @return 登录结果
     */
    public Map<String, Object> login(String username, String rawPassword) {
        User user = userRepository.findByUsername(username);
        
        if (user != null && passwordEncoder.matches(rawPassword, user.getPassword())) {
            // 登录成功
            logger.info("用户登录成功: {}", username);
            return Map.of(
                "status", "success",
                "message", "Login successful",
                "userId", user.getId()
            );
        } else {
            // 登录失败
            logger.warn("用户登录失败: {}", username);
            return Map.of(
                "status", "error",
                "message", "Invalid username or password"
            );
        }
    }
}