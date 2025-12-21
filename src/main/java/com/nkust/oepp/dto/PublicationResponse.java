package com.nkust.oepp.dto;

import com.nkust.oepp.entity.Publication;

import java.time.LocalDateTime;

public class PublicationResponse {
    
    private Long id;
    private String centerRole;
    private String centerRoleName;
    private String title;
    private String mainImageUrl;
    private String iframeLink;
    private Integer sortOrder;
    private LocalDateTime createdAt;
    
    public PublicationResponse() {
    }
    
    public PublicationResponse(Publication publication) {
        this.id = publication.getId();
        this.centerRole = publication.getCenterRole().getCode();
        this.centerRoleName = publication.getCenterRole().getName();
        this.title = publication.getTitle();
        this.mainImageUrl = publication.getMainImageUrl();
        this.iframeLink = publication.getIframeLink();
        this.sortOrder = publication.getSortOrder();
        this.createdAt = publication.getCreatedAt();
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
    
    public String getMainImageUrl() {
        return mainImageUrl;
    }
    
    public void setMainImageUrl(String mainImageUrl) {
        this.mainImageUrl = mainImageUrl;
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
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}



