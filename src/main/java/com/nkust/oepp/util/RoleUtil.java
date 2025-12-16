package com.nkust.oepp.util;

import com.nkust.oepp.entity.Role;

/**
 * 角色工具類
 */
public class RoleUtil {

    /**
     * 從 URL 路徑提取中心代碼
     * 例如: /api/headquarters/... -> HEADQUARTERS
     */
    public static String extractCenterCodeFromPath(String path) {
        if (path == null || path.isEmpty()) {
            return null;
        }

        // 移除前綴 /api/
        String cleanPath = path.replaceFirst("^/api/", "");
        
        // 提取第一個路徑段
        String[] parts = cleanPath.split("/");
        if (parts.length > 0) {
            String firstPart = parts[0].toUpperCase();
            
            // 映射路徑到角色代碼
            switch (firstPart) {
                case "HEADQUARTERS":
                case "HEADQUARTER":
                    return Role.HEADQUARTERS.getCode();
                case "EDUCATION":
                case "EDUCATION-CENTER":
                    return Role.EDUCATION_CENTER.getCode();
                case "PRODUCT":
                case "PRODUCT-CENTER":
                    return Role.PRODUCT_CENTER.getCode();
                case "EXHIBITION":
                case "EXHIBITION-CENTER":
                    return Role.EXHIBITION_CENTER.getCode();
                case "MANAGEMENT":
                case "MANAGEMENT-CENTER":
                    return Role.MANAGEMENT_CENTER.getCode();
                default:
                    return null;
            }
        }
        
        return null;
    }

    /**
     * 檢查角色代碼是否有效
     */
    public static boolean isValidRoleCode(String roleCode) {
        try {
            Role.fromCode(roleCode);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}

