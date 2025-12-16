package com.nkust.oepp.entity;

public enum Role {
    // 大帳號 - 所有後台頁面都可使用
    SUPER_ADMIN("SUPER_ADMIN", "大帳號"),
    
    // 處本部
    HEADQUARTERS("HEADQUARTERS", "處本部"),
    
    // 教育推廣中心
    EDUCATION_CENTER("EDUCATION_CENTER", "教育推廣中心"),
    
    // 產品推廣中心
    PRODUCT_CENTER("PRODUCT_CENTER", "產品推廣中心"),
    
    // 會展管理中心
    EXHIBITION_CENTER("EXHIBITION_CENTER", "會展管理中心"),
    
    // 經營管理中心
    MANAGEMENT_CENTER("MANAGEMENT_CENTER", "經營管理中心");

    private final String code;
    private final String name;

    Role(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static Role fromCode(String code) {
        for (Role role : Role.values()) {
            if (role.code.equals(code)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown role code: " + code);
    }

    /**
     * 檢查是否為超級管理員
     */
    public boolean isSuperAdmin() {
        return this == SUPER_ADMIN;
    }

    /**
     * 檢查是否有權限訪問指定中心
     */
    public boolean canAccessCenter(String centerCode) {
        if (isSuperAdmin()) {
            return true; // 超級管理員可以訪問所有中心
        }
        return this.code.equals(centerCode);
    }
}

