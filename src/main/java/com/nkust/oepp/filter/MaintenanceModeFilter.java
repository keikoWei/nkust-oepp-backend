package com.nkust.oepp.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nkust.oepp.service.SystemConfigService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class MaintenanceModeFilter extends OncePerRequestFilter {

    @Autowired
    private SystemConfigService systemConfigService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 檢查維護模式是否開啟
        if (systemConfigService.isMaintenanceModeEnabled()) {
            String path = request.getRequestURI();
            
            // 允許的端點：登入、登出、取得維護模式狀態、設定維護模式（僅限管理員）
            // 以及公開的 API 端點
            if (isAllowedPath(path)) {
                filterChain.doFilter(request, response);
                return;
            }

            // 其他請求返回維護模式訊息
            response.setStatus(HttpStatus.SERVICE_UNAVAILABLE.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", HttpStatus.SERVICE_UNAVAILABLE.value());
            errorResponse.put("error", "Service Unavailable");
            errorResponse.put("message", "系統目前處於維護模式，請稍後再試");
            errorResponse.put("maintenanceMode", true);

            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
            return;
        }

        filterChain.doFilter(request, response);
    }

    /**
     * 檢查路徑是否允許在維護模式下訪問
     */
    private boolean isAllowedPath(String path) {
        // 允許登入、登出
        if (path.startsWith("/api/auth/login") || path.startsWith("/api/auth/logout")) {
            return true;
        }
        
        // 允許所有維護模式相關的端點（包括取得狀態、設定、切換）
        // 設定和切換端點需要認證，但維護模式 Filter 允許通過，由後續的 JWT Filter 處理認證
        if (path.startsWith("/api/system-config/maintenance-mode")) {
            return true;
        }
        
        // 允許公開的 API 端點（如果需要）
        if (path.startsWith("/api/carousel/public/") ||
            path.startsWith("/api/news/public/") ||
            path.startsWith("/api/courses/public/") ||
            path.startsWith("/api/training-plans/public/") ||
            path.startsWith("/api/publications/public/")) {
            return true;
        }
        
        // 允許錯誤處理端點
        if (path.equals("/error") || path.startsWith("/actuator/")) {
            return true;
        }
        
        return false;
    }
}
