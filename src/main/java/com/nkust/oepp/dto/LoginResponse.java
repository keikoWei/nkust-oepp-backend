package com.nkust.oepp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {
    private String token;
    @Builder.Default
    private String type = "Bearer";
    private String username;
    private String role;  // 角色代碼，如 SUPER_ADMIN, HEADQUARTERS 等
    private String roleName;  // 角色名稱，如 "大帳號", "處本部" 等
    private Long id;
}

