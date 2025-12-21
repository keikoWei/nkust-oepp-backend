package com.nkust.oepp.controller;

import com.nkust.oepp.dto.MaintenanceModeResponse;
import com.nkust.oepp.entity.SystemConfig;
import com.nkust.oepp.service.SystemConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/system-config")
public class SystemConfigController {

    @Autowired
    private SystemConfigService systemConfigService;

    /**
     * 取得維護模式狀態
     */
    @GetMapping("/maintenance-mode")
    public ResponseEntity<MaintenanceModeResponse> getMaintenanceMode() {
        SystemConfig config = systemConfigService.getMaintenanceModeConfig();
        boolean isEnabled = Boolean.parseBoolean(config.getConfigValue());
        
        return ResponseEntity.ok(MaintenanceModeResponse.builder()
                .maintenanceMode(isEnabled)
                .message(isEnabled ? "維護模式已開啟" : "維護模式已關閉")
                .build());
    }

    /**
     * 設定維護模式
     */
    @PutMapping("/maintenance-mode")
    public ResponseEntity<MaintenanceModeResponse> setMaintenanceMode(@RequestParam boolean enabled) {
        SystemConfig config = systemConfigService.setMaintenanceMode(enabled);
        boolean isEnabled = Boolean.parseBoolean(config.getConfigValue());
        
        return ResponseEntity.ok(MaintenanceModeResponse.builder()
                .maintenanceMode(isEnabled)
                .message(isEnabled ? "維護模式已開啟" : "維護模式已關閉")
                .build());
    }

    /**
     * 切換維護模式
     */
    @PostMapping("/maintenance-mode/toggle")
    public ResponseEntity<MaintenanceModeResponse> toggleMaintenanceMode() {
        SystemConfig config = systemConfigService.toggleMaintenanceMode();
        boolean isEnabled = Boolean.parseBoolean(config.getConfigValue());
        
        return ResponseEntity.ok(MaintenanceModeResponse.builder()
                .maintenanceMode(isEnabled)
                .message(isEnabled ? "維護模式已開啟" : "維護模式已關閉")
                .build());
    }
}
