package com.nkust.oepp.dto;

import com.nkust.oepp.entity.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public class CarouselImageRequest {
    
    @NotBlank(message = "標題不能為空")
    private String title;
    
    @NotNull(message = "中心角色不能為空")
    private Role centerRole;
    
    @NotNull(message = "圖片不能為空")
    private MultipartFile image;
    
    private String clickUrl;
    
    private Integer sortOrder = 0;
    
    private Boolean isEnabled = true;
    
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

