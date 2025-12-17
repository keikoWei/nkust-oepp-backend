package com.nkust.oepp.dto;

import org.springframework.web.multipart.MultipartFile;

public class CarouselImageUpdateRequest {
    
    private String title;
    private MultipartFile image;
    private String clickUrl;
    private Integer sortOrder;
    private Boolean isEnabled;
    
    // Getters and Setters
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public MultipartFile getImage() {
        return image;
    }
    
    public void setImage(MultipartFile image) {
        this.image = image;
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
}

