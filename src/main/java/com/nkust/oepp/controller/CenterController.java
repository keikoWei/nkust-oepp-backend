package com.nkust.oepp.controller;

import com.nkust.oepp.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 各中心後台控制器
 * 用於測試角色權限控制
 */
@RestController
@RequestMapping("/api")
public class CenterController {

    /**
     * 處本部後台
     */
    @GetMapping("/headquarters/dashboard")
    public ResponseEntity<Map<String, Object>> headquartersDashboard() {
        User user = getCurrentUser();
        Map<String, Object> response = new HashMap<>();
        response.put("center", "處本部");
        response.put("message", "歡迎使用處本部後台管理系統");
        response.put("user", user.getUsername());
        response.put("role", user.getRoleName());
        return ResponseEntity.ok(response);
    }

    /**
     * 教育推廣中心後台
     */
    @GetMapping("/education/dashboard")
    public ResponseEntity<Map<String, Object>> educationDashboard() {
        User user = getCurrentUser();
        Map<String, Object> response = new HashMap<>();
        response.put("center", "教育推廣中心");
        response.put("message", "歡迎使用教育推廣中心後台管理系統");
        response.put("user", user.getUsername());
        response.put("role", user.getRoleName());
        return ResponseEntity.ok(response);
    }

    /**
     * 產品推廣中心後台
     */
    @GetMapping("/product/dashboard")
    public ResponseEntity<Map<String, Object>> productDashboard() {
        User user = getCurrentUser();
        Map<String, Object> response = new HashMap<>();
        response.put("center", "產品推廣中心");
        response.put("message", "歡迎使用產品推廣中心後台管理系統");
        response.put("user", user.getUsername());
        response.put("role", user.getRoleName());
        return ResponseEntity.ok(response);
    }

    /**
     * 會展管理中心後台
     */
    @GetMapping("/exhibition/dashboard")
    public ResponseEntity<Map<String, Object>> exhibitionDashboard() {
        User user = getCurrentUser();
        Map<String, Object> response = new HashMap<>();
        response.put("center", "會展管理中心");
        response.put("message", "歡迎使用會展管理中心後台管理系統");
        response.put("user", user.getUsername());
        response.put("role", user.getRoleName());
        return ResponseEntity.ok(response);
    }

    /**
     * 經營管理中心後台
     */
    @GetMapping("/management/dashboard")
    public ResponseEntity<Map<String, Object>> managementDashboard() {
        User user = getCurrentUser();
        Map<String, Object> response = new HashMap<>();
        response.put("center", "經營管理中心");
        response.put("message", "歡迎使用經營管理中心後台管理系統");
        response.put("user", user.getUsername());
        response.put("role", user.getRoleName());
        return ResponseEntity.ok(response);
    }

    /**
     * 獲取當前登入用戶
     */
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            return (User) authentication.getPrincipal();
        }
        throw new RuntimeException("無法獲取當前用戶");
    }
}

