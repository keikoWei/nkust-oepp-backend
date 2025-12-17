package com.nkust.oepp.dto;

import com.nkust.oepp.entity.CarouselImage;

import java.time.LocalDateTime;

public class CarouselImageResponse {
    
    private Long id;
    private String title;
    private String centerRole;
    private String centerRoleName;
    private String imageUrl;
    private String clickUrl;
    private Integer sortOrder;
    private Boolean isEnabled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public CarouselImageResponse() {
    }
    
    public CarouselImageResponse(CarouselImage carouselImage) {
        this.id = carouselImage.getId();
        this.title = carouselImage.getTitle();
        this.centerRole = carouselImage.getCenterRole().getCode();
        this.centerRoleName = carouselImage.getCenterRole().getName();
        this.imageUrl = carouselImage.getImageUrl();
        this.clickUrl = carouselImage.getClickUrl();
        this.sortOrder = carouselImage.getSortOrder();
        this.isEnabled = carouselImage.getIsEnabled();
        this.createdAt = carouselImage.getCreatedAt();
        this.updatedAt = carouselImage.getUpdatedAt();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
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
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public String getClickUrl() {
        return clickUrl;
    }
    
    public void setClickUrl(String clickUrl) {
        this.clickUrl = clickUrl;
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

