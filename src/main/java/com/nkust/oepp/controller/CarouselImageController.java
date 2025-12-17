package com.nkust.oepp.controller;

import com.nkust.oepp.dto.CarouselImageRequest;
import com.nkust.oepp.dto.CarouselImageResponse;
import com.nkust.oepp.dto.CarouselImageUpdateRequest;
import com.nkust.oepp.entity.Role;
import com.nkust.oepp.service.CarouselImageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/carousel")
public class CarouselImageController {
    
    @Autowired
    private CarouselImageService carouselImageService;
    
    /**
     * 創建輪播圖
     */
    @PostMapping
    public ResponseEntity<CarouselImageResponse> create(@Valid @ModelAttribute CarouselImageRequest request) {
        try {
            CarouselImageResponse response = carouselImageService.create(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IOException e) {
            throw new RuntimeException("圖片上傳失敗: " + e.getMessage(), e);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    /**
     * 更新輪播圖
     */
    @PutMapping("/{id}")
    public ResponseEntity<CarouselImageResponse> update(
            @PathVariable Long id,
            @ModelAttribute CarouselImageUpdateRequest request) {
        try {
            CarouselImageResponse response = carouselImageService.update(id, request);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            throw new RuntimeException("圖片上傳失敗: " + e.getMessage(), e);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    /**
     * 刪除輪播圖
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        try {
            carouselImageService.delete(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "輪播圖刪除成功");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    /**
     * 根據 ID 獲取輪播圖
     */
    @GetMapping("/{id}")
    public ResponseEntity<CarouselImageResponse> getById(@PathVariable Long id) {
        try {
            CarouselImageResponse response = carouselImageService.getById(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    /**
     * 獲取所有輪播圖（根據用戶權限過濾）
     */
    @GetMapping
    public ResponseEntity<List<CarouselImageResponse>> getAll() {
        List<CarouselImageResponse> responses = carouselImageService.getAll();
        return ResponseEntity.ok(responses);
    }
    
    /**
     * 根據中心角色獲取輪播圖（公開 API，用於前端顯示）
     * 不需要認證，但只返回啟用的輪播圖
     */
    @GetMapping("/public/{centerRole}")
    public ResponseEntity<List<CarouselImageResponse>> getByCenterRole(
            @PathVariable String centerRole,
            @RequestParam(required = false, defaultValue = "true") Boolean onlyEnabled) {
        try {
            Role role = Role.fromCode(centerRole.toUpperCase());
            List<CarouselImageResponse> responses = carouselImageService.getByCenterRole(role, onlyEnabled);
            return ResponseEntity.ok(responses);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("無效的中心角色: " + centerRole, e);
        }
    }
}

