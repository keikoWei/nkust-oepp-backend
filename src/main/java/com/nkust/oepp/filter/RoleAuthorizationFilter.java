package com.nkust.oepp.filter;

import com.nkust.oepp.entity.User;
import com.nkust.oepp.util.RoleUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 角色授權過濾器
 * 檢查用戶是否有權限訪問特定中心的路徑
 */
@Component
public class RoleAuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof User) {
            User user = (User) authentication.getPrincipal();
            String requestPath = request.getRequestURI();

            // 提取中心代碼
            String centerCode = RoleUtil.extractCenterCodeFromPath(requestPath);

            // 如果有中心代碼，檢查權限
            if (centerCode != null && !user.isSuperAdmin()) {
                if (!user.canAccessCenter(centerCode)) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write("{\"error\":\"無權限訪問此中心\",\"message\":\"您沒有權限訪問 " + centerCode + " 的資源\"}");
                    return;
                }
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        // 排除公開端點
        return path.startsWith("/api/auth/") ||
               path.startsWith("/api/actuator/") ||
               path.startsWith("/api/error");
    }
}

