package com.nkust.oepp.dto;

import com.nkust.oepp.entity.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class NewsRequest {
    
    @NotBlank(message = "標題不能為空")
    private String title;
    
    @NotNull(message = "中心角色不能為空")
    private Role centerRole;
    
    @NotBlank(message = "內容不能為空")
    private String contentHtml;
    
    private List<MultipartFile> images; // 多個圖片
    
    private List<MultipartFile> files; // 多個檔案
    
    private Integer sortOrder = 0;
    
    private Boolean isEnabled = true;
    
    private String publishTime; // ISO 8601 格式字符串，例如：2025-12-19T08:36:00 或 2025-12-19T08:36:00.000Z
    
    // Getters and Setters
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public Role getCenterRole() {
        return centerRole;
    }
    
    public void setCenterRole(Role centerRole) {
        this.centerRole = centerRole;
    }
    
    public String getContentHtml() {
        return contentHtml;
    }
    
    public void setContentHtml(String contentHtml) {
        this.contentHtml = contentHtml;
    }
    
    public List<MultipartFile> getImages() {
        return images;
    }
    
    public void setImages(List<MultipartFile> images) {
        this.images = images;
    }
    
    public List<MultipartFile> getFiles() {
        return files;
    }
    
    public void setFiles(List<MultipartFile> files) {
        this.files = files;
    }
    
    public Integer getSortOrder() {
        return sortOrder;
    }
    
    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }
    
    public Boolean getIsEnabled() {
        return isEnabled;
    }
    
    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }
    
    public String getPublishTime() {
        return publishTime;
    }
    
    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }
}

