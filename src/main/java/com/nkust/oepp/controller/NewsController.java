package com.nkust.oepp.controller;

import com.nkust.oepp.dto.NewsRequest;
import com.nkust.oepp.dto.NewsResponse;
import com.nkust.oepp.dto.NewsUpdateRequest;
import com.nkust.oepp.entity.Role;
import com.nkust.oepp.service.NewsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/news")
public class NewsController {
    
    @Autowired
    private NewsService newsService;
    
    /**
     * 創建消息
     */
    @PostMapping
    public ResponseEntity<NewsResponse> create(@Valid @ModelAttribute NewsRequest request) {
        try {
            NewsResponse response = newsService.create(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IOException e) {
            throw new RuntimeException("文件上傳失敗: " + e.getMessage(), e);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    /**
     * 更新消息
     */
    @PutMapping("/{id}")
    public ResponseEntity<NewsResponse> update(
            @PathVariable Long id,
            @ModelAttribute NewsUpdateRequest request) {
        try {
            NewsResponse response = newsService.update(id, request);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            throw new RuntimeException("文件上傳失敗: " + e.getMessage(), e);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    /**
     * 刪除消息
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        try {
            newsService.delete(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "消息刪除成功");
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            throw new RuntimeException("文件刪除失敗: " + e.getMessage(), e);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    /**
     * 根據 ID 獲取消息
     */
    @GetMapping("/{id}")
    public ResponseEntity<NewsResponse> getById(@PathVariable Long id) {
        try {
            NewsResponse response = newsService.getById(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    /**
     * 獲取所有消息（根據用戶權限過濾）
     */
    @GetMapping
    public ResponseEntity<List<NewsResponse>> getAll() {
        List<NewsResponse> responses = newsService.getAll();
        return ResponseEntity.ok(responses);
    }
    
    /**
     * 根據中心角色獲取消息（公開 API，用於前端顯示）
     * 不需要認證，但只返回已發布且啟用的消息
     */
    @GetMapping("/public/{centerRole}")
    public ResponseEntity<List<NewsResponse>> getByCenterRole(
            @PathVariable String centerRole,
            @RequestParam(required = false, defaultValue = "true") Boolean onlyEnabled) {
        try {
            Role role = Role.fromCode(centerRole.toUpperCase());
            List<NewsResponse> responses = newsService.getByCenterRole(role, onlyEnabled);
            return ResponseEntity.ok(responses);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("無效的中心角色: " + centerRole, e);
        }
    }
    
    /**
     * 下載檔案
     */
    @GetMapping("/file/{filePath:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filePath) {
        try {
            Path file = Paths.get("upload", filePath);
            Resource resource = new UrlResource(file.toUri());
            
            if (resource.exists() && resource.isReadable()) {
                String contentType = determineContentType(filePath);
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 根據檔案路徑判斷 Content-Type
     */
    private String determineContentType(String filePath) {
        String extension = filePath.substring(filePath.lastIndexOf('.') + 1).toLowerCase();
        switch (extension) {
            case "pdf":
                return "application/pdf";
            case "doc":
                return "application/msword";
            case "docx":
                return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            case "xls":
                return "application/vnd.ms-excel";
            case "xlsx":
                return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            default:
                return "application/octet-stream";
        }
    }
}

