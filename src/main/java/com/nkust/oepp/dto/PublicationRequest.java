package com.nkust.oepp.dto;

import com.nkust.oepp.entity.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public class PublicationRequest {
    
    @NotBlank(message = "標題不能為空")
    private String title;
    
    @NotNull(message = "中心角色不能為空")
    private Role centerRole;
    
    @NotNull(message = "主圖片不能為空")
    private MultipartFile mainImage; // 主圖片
    
    @NotBlank(message = "iframe 連結不能為空")
    private String iframeLink;
    
    private Integer sortOrder = 0;
    
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
    
    public MultipartFile getMainImage() {
        return mainImage;
    }
    
    public void setMainImage(MultipartFile mainImage) {
        this.mainImage = mainImage;
    }
    
    public String getIframeLink() {
        return iframeLink;
    }
    
    public void setIframeLink(String iframeLink) {
        this.iframeLink = iframeLink;
    }
    
    public Integer getSortOrder() {
        return sortOrder;
    }
    
    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }
}



