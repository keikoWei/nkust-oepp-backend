package com.nkust.oepp.dto;

import com.nkust.oepp.entity.TrainingPlan;

import java.time.LocalDateTime;

public class TrainingPlanResponse {
    
    private Long id;
    private String centerRole;
    private String centerRoleName;
    private String title;
    private String link;
    private Integer sortOrder;
    private LocalDateTime createdAt;
    
    public TrainingPlanResponse() {
    }
    
    public TrainingPlanResponse(TrainingPlan trainingPlan) {
        this.id = trainingPlan.getId();
        this.centerRole = trainingPlan.getCenterRole().getCode();
        this.centerRoleName = trainingPlan.getCenterRole().getName();
        this.title = trainingPlan.getTitle();
        this.link = trainingPlan.getLink();
        this.sortOrder = trainingPlan.getSortOrder();
        this.createdAt = trainingPlan.getCreatedAt();
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
    
    public String getLink() {
        return link;
    }
    
    public void setLink(String link) {
        this.link = link;
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

