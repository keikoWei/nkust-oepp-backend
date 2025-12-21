package com.nkust.oepp.service;

import com.nkust.oepp.entity.SystemConfig;
import com.nkust.oepp.repository.SystemConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SystemConfigService {

    @Autowired
    private SystemConfigRepository systemConfigRepository;

    private static final String MAINTENANCE_MODE_KEY = "MAINTENANCE_MODE";

    /**
     * 檢查維護模式是否開啟
     */
    public boolean isMaintenanceModeEnabled() {
        return systemConfigRepository.findByConfigKey(MAINTENANCE_MODE_KEY)
                .map(config -> Boolean.parseBoolean(config.getConfigValue()))
                .orElse(false);
    }

    /**
     * 取得維護模式狀態
     */
    public SystemConfig getMaintenanceModeConfig() {
        return systemConfigRepository.findByConfigKey(MAINTENANCE_MODE_KEY)
                .orElseGet(() -> {
                    // 如果不存在，創建預設值
                    SystemConfig config = SystemConfig.builder()
                            .configKey(MAINTENANCE_MODE_KEY)
                            .configValue("false")
                            .build();
                    return systemConfigRepository.save(config);
                });
    }

    /**
     * 設定維護模式
     */
    @Transactional
    public SystemConfig setMaintenanceMode(boolean enabled) {
        SystemConfig config = systemConfigRepository.findByConfigKey(MAINTENANCE_MODE_KEY)
                .orElseGet(() -> SystemConfig.builder()
                        .configKey(MAINTENANCE_MODE_KEY)
                        .build());

        config.setConfigValue(String.valueOf(enabled));
        return systemConfigRepository.save(config);
    }

    /**
     * 切換維護模式
     */
    @Transactional
    public SystemConfig toggleMaintenanceMode() {
        boolean currentStatus = isMaintenanceModeEnabled();
        return setMaintenanceMode(!currentStatus);
    }
}
