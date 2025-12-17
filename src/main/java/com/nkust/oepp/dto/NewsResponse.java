package com.nkust.oepp.dto;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nkust.oepp.entity.News;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NewsResponse {
    
    private Long id;
    private String centerRole;
    private String centerRoleName;
    private String title;
    private String contentHtml;
    private List<String> imageUrls;
    private List<String> filePaths;
    private Integer sortOrder;
    private Boolean isEnabled;
    private LocalDateTime publishTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    public NewsResponse() {
    }
    
    public NewsResponse(News news) {
        this.id = news.getId();
        this.centerRole = news.getCenterRole().getCode();
        this.centerRoleName = news.getCenterRole().getName();
        this.title = news.getTitle();
        this.contentHtml = news.getContentHtml();
        this.sortOrder = news.getSortOrder();
        this.isEnabled = news.getIsEnabled();
        this.publishTime = news.getPublishTime();
        this.createdAt = news.getCreatedAt();
        this.updatedAt = news.getUpdatedAt();
        
        // 解析 JSON 陣列
        try {
            if (news.getImageUrls() != null && !news.getImageUrls().isEmpty()) {
                this.imageUrls = objectMapper.readValue(news.getImageUrls(), new TypeReference<List<String>>() {});
            } else {
                this.imageUrls = new ArrayList<>();
            }
        } catch (Exception e) {
            this.imageUrls = new ArrayList<>();
        }
        
        try {
            if (news.getFilePaths() != null && !news.getFilePaths().isEmpty()) {
                this.filePaths = objectMapper.readValue(news.getFilePaths(), new TypeReference<List<String>>() {});
            } else {
                this.filePaths = new ArrayList<>();
            }
        } catch (Exception e) {
            this.filePaths = new ArrayList<>();
        }
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getCenterRole() {
        return centerRole;
    }
    
    public void setCenterRole(String centerRole) {
        this.centerRole = centerRole;
    }
    
    public String getCenterRoleName() {
        return centerRoleName;
    }
    
    public void setCenterRoleName(String centerRoleName) {
        this.centerRoleName = centerRoleName;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getContentHtml() {
        return contentHtml;
    }
    
    public void setContentHtml(String contentHtml) {
        this.contentHtml = contentHtml;
    }
    
    public List<String> getImageUrls() {
        return imageUrls;
    }
    
    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
    
    public List<String> getFilePaths() {
        return filePaths;
    }
    
    public void setFilePaths(List<String> filePaths) {
        this.filePaths = filePaths;
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
    
    public LocalDateTime getPublishTime() {
        return publishTime;
    }
    
    public void setPublishTime(LocalDateTime publishTime) {
        this.publishTime = publishTime;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}

