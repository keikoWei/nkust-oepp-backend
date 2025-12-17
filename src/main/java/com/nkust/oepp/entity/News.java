package com.nkust.oepp.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "news")
public class News {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "center_role", nullable = false, length = 30)
    private Role centerRole;
    
    @Column(nullable = false, length = 200)
    private String title;
    
    @Column(name = "content_html", nullable = false, columnDefinition = "LONGTEXT")
    private String contentHtml;
    
    @Column(name = "image_urls", columnDefinition = "TEXT")
    private String imageUrls; // JSON 陣列格式儲存多個圖片 URL
    
    @Column(name = "file_paths", columnDefinition = "TEXT")
    private String filePaths; // JSON 陣列格式儲存多個檔案路徑
    
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;
    
    @Column(name = "is_enabled", nullable = false)
    private Boolean isEnabled = true;
    
    @Column(name = "publish_time")
    private LocalDateTime publishTime;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Role getCenterRole() {
        return centerRole;
    }
    
    public void setCenterRole(Role centerRole) {
        this.centerRole = centerRole;
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
    
    public String getImageUrls() {
        return imageUrls;
    }
    
    public void setImageUrls(String imageUrls) {
        this.imageUrls = imageUrls;
    }
    
    public String getFilePaths() {
        return filePaths;
    }
    
    public void setFilePaths(String filePaths) {
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

