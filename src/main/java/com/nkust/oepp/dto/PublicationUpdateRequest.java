package com.nkust.oepp.dto;

import org.springframework.web.multipart.MultipartFile;

public class PublicationUpdateRequest {
    
    private String title;
    private MultipartFile mainImage; // 主圖片（可選，不提供則不更新）
    private String iframeLink;
    private Integer sortOrder;
    
    // Getters and Setters
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
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

