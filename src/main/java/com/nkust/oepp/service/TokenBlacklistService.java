package com.nkust.oepp.service;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * Token 黑名單服務
 * 用於管理已登出的 Token，使其立即失效
 */
@Service
public class TokenBlacklistService {

    private final Set<String> blacklistedTokens = new HashSet<>();

    /**
     * 將 Token 加入黑名單
     */
    public void blacklistToken(String token) {
        blacklistedTokens.add(token);
    }

    /**
     * 檢查 Token 是否在黑名單中
     */
    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }

    /**
     * 從黑名單中移除 Token（用於清理過期 Token）
     */
    public void removeToken(String token) {
        blacklistedTokens.remove(token);
    }

    /**
     * 清空所有黑名單（用於測試或維護）
     */
    public void clearBlacklist() {
        blacklistedTokens.clear();
    }

    /**
     * 獲取黑名單大小（用於監控）
     */
    public int getBlacklistSize() {
        return blacklistedTokens.size();
    }
}

