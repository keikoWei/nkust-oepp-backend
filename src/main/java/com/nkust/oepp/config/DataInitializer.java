package com.nkust.oepp.config;

import com.nkust.oepp.entity.SystemConfig;
import com.nkust.oepp.repository.SystemConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 資料初始化器
 * 僅初始化系統必需的配置
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private SystemConfigRepository systemConfigRepository;

    @Override
    public void run(String... args) throws Exception {
        // 初始化系統設定 (維護模式)
        createSystemConfigIfNotExists("MAINTENANCE_MODE", "false");
    }

    /**
     * 創建系統配置（如果不存在）
     * 使用 if (!exists) 判斷，避免重複創建
     */
    private void createSystemConfigIfNotExists(String configKey, String configValue) {
        if (!systemConfigRepository.existsByConfigKey(configKey)) {
            SystemConfig config = SystemConfig.builder()
                    .configKey(configKey)
                    .configValue(configValue)
                    .build();
            systemConfigRepository.save(config);
        }
    }
}

