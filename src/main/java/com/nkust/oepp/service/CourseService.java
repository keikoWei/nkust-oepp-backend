package com.nkust.oepp.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nkust.oepp.dto.CourseRequest;
import com.nkust.oepp.dto.CourseResponse;
import com.nkust.oepp.dto.CourseUpdateRequest;
import com.nkust.oepp.entity.Course;
import com.nkust.oepp.entity.Role;
import com.nkust.oepp.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {
    
    @Autowired
    private CourseRepository courseRepository;
    
    @Autowired
    private ImageKitService imageKitService;
    
    @Autowired
    private FileStorageService fileStorageService;
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * 將字符串轉換為 LocalDateTime
     * 支援多種格式：
     * - ISO 8601 with Z: 2025-12-19T08:36:00.000Z
     * - ISO 8601 without Z: 2025-12-19T08:36:00
     * - ISO 8601 with timezone: 2025-12-19T08:36:00+08:00
     */
    private LocalDateTime parsePublishTime(String publishTimeStr) {
        if (publishTimeStr == null || publishTimeStr.trim().isEmpty()) {
            return null;
        }
        
        try {
            // 嘗試解析 ISO 8601 with Z (UTC)
            if (publishTimeStr.endsWith("Z")) {
                Instant instant = Instant.parse(publishTimeStr);
                return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
            }
            
            // 嘗試解析 ISO 8601 with timezone offset
            if (publishTimeStr.contains("+") || (publishTimeStr.contains("-") && publishTimeStr.length() > 19)) {
                Instant instant = Instant.parse(publishTimeStr);
                return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
            }
            
            // 嘗試解析本地時間格式 (ISO 8601 without timezone)
            try {
                return LocalDateTime.parse(publishTimeStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            } catch (DateTimeParseException e) {
                // 嘗試其他常見格式
                DateTimeFormatter[] formatters = {
                    DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                };
                
                for (DateTimeFormatter formatter : formatters) {
                    try {
                        return LocalDateTime.parse(publishTimeStr, formatter);
                    } catch (DateTimeParseException ignored) {
                        // 繼續嘗試下一個格式
                    }
                }
                
                throw new RuntimeException("無法解析日期時間格式: " + publishTimeStr);
            }
        } catch (Exception e) {
            throw new RuntimeException("日期時間格式錯誤: " + publishTimeStr + ", " + e.getMessage(), e);
        }
    }
    
    /**
     * 獲取當前用戶的角色
     */
    private Role getCurrentUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String roleCode = userDetails.getAuthorities().iterator().next().getAuthority()
                    .replace("ROLE_", "");
            return Role.fromCode(roleCode);
        }
        return null;
    }
    
    /**
     * 檢查用戶是否有權限操作該中心
     */
    private boolean hasPermission(Role centerRole) {
        Role currentRole = getCurrentUserRole();
        if (currentRole == null) {
            return false;
        }
        // 超級管理員可以操作所有中心
        if (currentRole == Role.SUPER_ADMIN) {
            return true;
        }
        // 其他用戶只能操作自己的中心
        return currentRole == centerRole;
    }
    
    /**
     * 創建課程
     */
    @Transactional
    public CourseResponse create(CourseRequest request) throws IOException {
        // 權限檢查
        if (!hasPermission(request.getCenterRole())) {
            throw new RuntimeException("您沒有權限操作此中心的課程");
        }
        
        // 上傳主圖片到 ImageKit
        String mainImageUrl = imageKitService.uploadImage(request.getMainImage(), null, "courses");
        
        // 上傳圖片到 ImageKit
        List<String> imageUrls = new ArrayList<>();
        if (request.getImages() != null && !request.getImages().isEmpty()) {
            for (MultipartFile image : request.getImages()) {
                if (image != null && !image.isEmpty()) {
                    String imageUrl = imageKitService.uploadImage(image, null, "courses");
                    imageUrls.add(imageUrl);
                }
            }
        }
        
        // 上傳檔案到本地
        List<String> filePaths = new ArrayList<>();
        if (request.getFiles() != null && !request.getFiles().isEmpty()) {
            for (MultipartFile file : request.getFiles()) {
                if (file != null && !file.isEmpty()) {
                    String filePath = fileStorageService.storeFile(file, "courses");
                    filePaths.add(filePath);
                }
            }
        }
        
        // 創建課程實體
        Course course = new Course();
        course.setCenterRole(request.getCenterRole());
        course.setTitle(request.getTitle());
        course.setContentHtml(request.getContentHtml());
        course.setMainImageUrl(mainImageUrl);
        course.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0);
        course.setIsEnabled(request.getIsEnabled() != null ? request.getIsEnabled() : true);
        course.setPublishTime(parsePublishTime(request.getPublishTime()));
        
        // 將圖片和檔案路徑轉換為 JSON 儲存
        if (!imageUrls.isEmpty()) {
            course.setImageUrls(objectMapper.writeValueAsString(imageUrls));
        }
        if (!filePaths.isEmpty()) {
            course.setFilePaths(objectMapper.writeValueAsString(filePaths));
        }
        
        course = courseRepository.save(course);
        
        return new CourseResponse(course);
    }
    
    /**
     * 更新課程
     */
    @Transactional
    public CourseResponse update(Long id, CourseUpdateRequest request) throws IOException {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("找不到指定的課程"));
        
        // 權限檢查
        if (!hasPermission(course.getCenterRole())) {
            throw new RuntimeException("您沒有權限操作此中心的課程");
        }
        
        // 更新基本資訊
        if (request.getTitle() != null) {
            course.setTitle(request.getTitle());
        }
        if (request.getContentHtml() != null) {
            course.setContentHtml(request.getContentHtml());
        }
        if (request.getSortOrder() != null) {
            course.setSortOrder(request.getSortOrder());
        }
        if (request.getIsEnabled() != null) {
            course.setIsEnabled(request.getIsEnabled());
        }
        if (request.getPublishTime() != null) {
            course.setPublishTime(parsePublishTime(request.getPublishTime()));
        }
        
        // 更新主圖片
        if (request.getMainImage() != null && !request.getMainImage().isEmpty()) {
            String mainImageUrl = imageKitService.uploadImage(request.getMainImage(), null, "courses");
            course.setMainImageUrl(mainImageUrl);
        }
        
        // 處理圖片
        List<String> imageUrls = new ArrayList<>();
        if (course.getImageUrls() != null && !course.getImageUrls().isEmpty()) {
            try {
                imageUrls = objectMapper.readValue(course.getImageUrls(), new TypeReference<List<String>>() {});
            } catch (Exception e) {
                imageUrls = new ArrayList<>();
            }
        }
        
        // 刪除指定的圖片
        if (request.getImageUrlsToRemove() != null && !request.getImageUrlsToRemove().isEmpty()) {
            imageUrls.removeAll(request.getImageUrlsToRemove());
        }
        
        // 添加新上傳的圖片
        if (request.getImages() != null && !request.getImages().isEmpty()) {
            for (MultipartFile image : request.getImages()) {
                if (image != null && !image.isEmpty()) {
                    String imageUrl = imageKitService.uploadImage(image, null, "courses");
                    imageUrls.add(imageUrl);
                }
            }
        }
        
        course.setImageUrls(imageUrls.isEmpty() ? null : objectMapper.writeValueAsString(imageUrls));
        
        // 處理檔案
        List<String> filePaths = new ArrayList<>();
        if (course.getFilePaths() != null && !course.getFilePaths().isEmpty()) {
            try {
                filePaths = objectMapper.readValue(course.getFilePaths(), new TypeReference<List<String>>() {});
            } catch (Exception e) {
                filePaths = new ArrayList<>();
            }
        }
        
        // 刪除指定的檔案
        if (request.getFilePathsToRemove() != null && !request.getFilePathsToRemove().isEmpty()) {
            for (String filePathToRemove : request.getFilePathsToRemove()) {
                try {
                    fileStorageService.deleteFile(filePathToRemove);
                } catch (Exception e) {
                    // 記錄錯誤但不中斷流程
                    System.err.println("刪除檔案失敗: " + filePathToRemove + ", " + e.getMessage());
                }
                filePaths.remove(filePathToRemove);
            }
        }
        
        // 添加新上傳的檔案
        if (request.getFiles() != null && !request.getFiles().isEmpty()) {
            for (MultipartFile file : request.getFiles()) {
                if (file != null && !file.isEmpty()) {
                    String filePath = fileStorageService.storeFile(file, "courses");
                    filePaths.add(filePath);
                }
            }
        }
        
        course.setFilePaths(filePaths.isEmpty() ? null : objectMapper.writeValueAsString(filePaths));
        
        course = courseRepository.save(course);
        
        return new CourseResponse(course);
    }
    
    /**
     * 刪除課程
     */
    @Transactional
    public void delete(Long id) throws IOException {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("找不到指定的課程"));
        
        // 權限檢查
        if (!hasPermission(course.getCenterRole())) {
            throw new RuntimeException("您沒有權限操作此中心的課程");
        }
        
        // 刪除相關檔案
        if (course.getFilePaths() != null && !course.getFilePaths().isEmpty()) {
            try {
                List<String> filePaths = objectMapper.readValue(course.getFilePaths(), new TypeReference<List<String>>() {});
                for (String filePath : filePaths) {
                    try {
                        fileStorageService.deleteFile(filePath);
                    } catch (Exception e) {
                        System.err.println("刪除檔案失敗: " + filePath + ", " + e.getMessage());
                    }
                }
            } catch (Exception e) {
                System.err.println("解析檔案路徑失敗: " + e.getMessage());
            }
        }
        
        courseRepository.delete(course);
    }
    
    /**
     * 根據 ID 獲取課程
     */
    public CourseResponse getById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("找不到指定的課程"));
        return new CourseResponse(course);
    }
    
    /**
     * 獲取所有課程（根據用戶權限過濾，按排序排序）
     */
    public List<CourseResponse> getAll() {
        Role currentRole = getCurrentUserRole();
        List<Course> courseList;
        
        if (currentRole == Role.SUPER_ADMIN) {
            // 超級管理員可以看到所有課程（按排序和建立時間排序）
            courseList = courseRepository.findAll().stream()
                    .sorted((c1, c2) -> {
                        int sortCompare = Integer.compare(
                            c1.getSortOrder() != null ? c1.getSortOrder() : 0,
                            c2.getSortOrder() != null ? c2.getSortOrder() : 0
                        );
                        if (sortCompare != 0) {
                            return sortCompare;
                        }
                        if (c1.getCreatedAt() != null && c2.getCreatedAt() != null) {
                            return c2.getCreatedAt().compareTo(c1.getCreatedAt());
                        }
                        return 0;
                    })
                    .collect(Collectors.toList());
        } else if (currentRole != null) {
            // 其他用戶只能看到自己中心的課程（已按排序排序）
            courseList = courseRepository.findByCenterRole(currentRole);
        } else {
            courseList = new ArrayList<>();
        }
        
        return courseList.stream()
                .map(CourseResponse::new)
                .collect(Collectors.toList());
    }
    
    /**
     * 根據中心角色獲取課程（公開 API，用於前端顯示）
     */
    public List<CourseResponse> getByCenterRole(Role centerRole, Boolean onlyEnabled) {
        List<Course> courseList;
        
        if (onlyEnabled != null && onlyEnabled) {
            // 只返回已發布且啟用的課程
            courseList = courseRepository.findPublishedByCenterRole(centerRole, LocalDateTime.now());
        } else {
            // 返回所有課程（包括未啟用的）
            courseList = courseRepository.findByCenterRole(centerRole);
        }
        
        return courseList.stream()
                .map(CourseResponse::new)
                .collect(Collectors.toList());
    }
}

