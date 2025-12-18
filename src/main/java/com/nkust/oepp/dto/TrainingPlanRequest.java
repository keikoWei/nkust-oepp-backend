package com.nkust.oepp.dto;

import com.nkust.oepp.entity.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.URL;

public class TrainingPlanRequest {
    
    @NotBlank(message = "標題不能為空")
    private String title;
    
    @NotNull(message = "中心角色不能為空")
    private Role centerRole;
    
    @NotBlank(message = "連結不能為空")
    @URL(message = "連結格式不正確")
    private String link;
    
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

