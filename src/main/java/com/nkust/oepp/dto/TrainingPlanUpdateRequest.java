package com.nkust.oepp.dto;

import org.hibernate.validator.constraints.URL;

public class TrainingPlanUpdateRequest {
    
    private String title;
    
    @URL(message = "連結格式不正確")
    private String link;
    
    private Integer sortOrder;
    
    // Getters and Setters
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
}

