package com.nkust.oepp.controller;

import com.nkust.oepp.dto.PublicationRequest;
import com.nkust.oepp.dto.PublicationResponse;
import com.nkust.oepp.dto.PublicationUpdateRequest;
import com.nkust.oepp.entity.Role;
import com.nkust.oepp.service.PublicationService;
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
@RequestMapping("/api/publications")
public class PublicationController {
    
    @Autowired
    private PublicationService publicationService;
    
    /**
     * 創建出版品
     */
    @PostMapping
    public ResponseEntity<PublicationResponse> create(@Valid @ModelAttribute PublicationRequest request) {
        try {
            PublicationResponse response = publicationService.create(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IOException e) {
            throw new RuntimeException("圖片上傳失敗: " + e.getMessage(), e);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    /**
     * 更新出版品
     */
    @PutMapping("/{id}")
    public ResponseEntity<PublicationResponse> update(
            @PathVariable Long id,
            @ModelAttribute PublicationUpdateRequest request) {
        try {
            PublicationResponse response = publicationService.update(id, request);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            throw new RuntimeException("圖片上傳失敗: " + e.getMessage(), e);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    /**
     * 刪除出版品
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        try {
            publicationService.delete(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "出版品刪除成功");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    /**
     * 根據 ID 獲取出版品
     */
    @GetMapping("/{id}")
    public ResponseEntity<PublicationResponse> getById(@PathVariable Long id) {
        try {
            PublicationResponse response = publicationService.getById(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    /**
     * 獲取所有出版品（根據用戶權限過濾）
     */
    @GetMapping
    public ResponseEntity<List<PublicationResponse>> getAll() {
        List<PublicationResponse> responses = publicationService.getAll();
        return ResponseEntity.ok(responses);
    }
    
    /**
     * 根據中心角色獲取出版品（公開 API，用於前端顯示）
     * 不需要認證
     */
    @GetMapping("/public/{centerRole}")
    public ResponseEntity<List<PublicationResponse>> getByCenterRole(@PathVariable String centerRole) {
        try {
            Role role = Role.fromCode(centerRole.toUpperCase());
            List<PublicationResponse> responses = publicationService.getByCenterRole(role);
            return ResponseEntity.ok(responses);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("無效的中心角色: " + centerRole, e);
        }
    }
}



