package com.nkust.oepp.controller;

import com.nkust.oepp.dto.CourseRequest;
import com.nkust.oepp.dto.CourseResponse;
import com.nkust.oepp.dto.CourseUpdateRequest;
import com.nkust.oepp.entity.Role;
import com.nkust.oepp.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
    
    @Autowired
    private CourseService courseService;
    
    /**
     * 創建課程
     */
    @PostMapping
    public ResponseEntity<CourseResponse> create(@Valid @ModelAttribute CourseRequest request) {
        try {
            CourseResponse response = courseService.create(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IOException e) {
            throw new RuntimeException("文件上傳失敗: " + e.getMessage(), e);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    /**
     * 更新課程
     */
    @PutMapping("/{id}")
    public ResponseEntity<CourseResponse> update(
            @PathVariable Long id,
            @ModelAttribute CourseUpdateRequest request) {
        try {
            CourseResponse response = courseService.update(id, request);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            throw new RuntimeException("文件上傳失敗: " + e.getMessage(), e);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    /**
     * 刪除課程
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        try {
            courseService.delete(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "課程刪除成功");
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            throw new RuntimeException("文件刪除失敗: " + e.getMessage(), e);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    /**
     * 下載檔案（建議使用：path 用 query 傳，避免路徑含 / 造成 404）
     * 例：GET /api/courses/file?path=courses%2Fabc.pdf
     */
    @GetMapping(value = "/file", params = "path")
    public ResponseEntity<Resource> downloadFileByQuery(@RequestParam String path) {
        return doDownloadFile(path);
    }

    /**
     * 下載檔案（路徑在 URL 中，例：/api/courses/file/courses/abc.pdf）
     */
    @GetMapping("/file/{filePath:.+}")
    public ResponseEntity<Resource> downloadFileByPath(@PathVariable String filePath) {
        return doDownloadFile(filePath);
    }

    private ResponseEntity<Resource> doDownloadFile(String filePath) {
        try {
            Path file = Paths.get("upload", filePath);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() && resource.isReadable()) {
                String contentType = determineContentType(filePath);
                String disposition = buildContentDisposition(resource.getFilename(), filePath);
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, disposition)
                        .body(resource);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 組 Content-Disposition，避免檔名含中文等非 ASCII 時 Tomcat 報錯（僅支援 0-255）。
     * 使用 ASCII fallback + RFC 5987 filename*=UTF-8'' 編碼。
     */
    private String buildContentDisposition(String filename, String filePath) {
        String ext = filePath.contains(".") ? filePath.substring(filePath.lastIndexOf('.')) : "";
        String safeAscii = isAscii(filename) ? filename.replace("\"", "%22") : "download" + ext;
        String disposition = "attachment; filename=\"" + safeAscii + "\"";
        if (!isAscii(filename)) {
            try {
                String encoded = URLEncoder.encode(filename, StandardCharsets.UTF_8).replace("+", "%20");
                disposition += "; filename*=UTF-8''" + encoded;
            } catch (Exception ignored) { }
        }
        return disposition;
    }

    private boolean isAscii(String s) {
        if (s == null) return true;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) > 127) return false;
        }
        return true;
    }

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

    /**
     * 根據 ID 獲取課程
     */
    @GetMapping("/{id}")
    public ResponseEntity<CourseResponse> getById(@PathVariable Long id) {
        try {
            CourseResponse response = courseService.getById(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    /**
     * 獲取所有課程（根據用戶權限過濾）
     */
    @GetMapping
    public ResponseEntity<List<CourseResponse>> getAll() {
        List<CourseResponse> responses = courseService.getAll();
        return ResponseEntity.ok(responses);
    }
    
    /**
     * 根據中心角色獲取課程（公開 API，用於前端顯示）
     * 不需要認證，但只返回已發布且啟用的課程
     */
    @GetMapping("/public/{centerRole}")
    public ResponseEntity<List<CourseResponse>> getByCenterRole(
            @PathVariable String centerRole,
            @RequestParam(required = false, defaultValue = "true") Boolean onlyEnabled) {
        try {
            Role role = Role.fromCode(centerRole.toUpperCase());
            List<CourseResponse> responses = courseService.getByCenterRole(role, onlyEnabled);
            return ResponseEntity.ok(responses);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("無效的中心角色: " + centerRole, e);
        }
    }
}

