package com.nkust.oepp.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class CourseUpdateRequest {
    
    private String title;
    private String contentHtml;
    private MultipartFile mainImage; // 主圖片（可選，不提供則不更新）
    private List<MultipartFile> images; // 新上傳的圖片（會追加到現有圖片）
    private List<MultipartFile> files; // 新上傳的檔案（會追加到現有檔案）
    private List<String> imageUrlsToRemove; // 要刪除的圖片 URL
    private List<String> filePathsToRemove; // 要刪除的檔案路徑
    private Integer sortOrder;
    private Boolean isEnabled;
    private String publishTime; // ISO 8601 格式字符串
    
    // Getters and Setters
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
    
    public MultipartFile getMainImage() {
        return mainImage;
    }
    
    public void setMainImage(MultipartFile mainImage) {
        this.mainImage = mainImage;
    }
    
    public List<MultipartFile> getImages() {
        return images;
    }
    
    public void setImages(List<MultipartFile> images) {
        this.images = images;
    }
    
    public List<MultipartFile> getFiles() {
        return files;
    }
    
    public void setFiles(List<MultipartFile> files) {
        this.files = files;
    }
    
    public List<String> getImageUrlsToRemove() {
        return imageUrlsToRemove;
    }
    
    public void setImageUrlsToRemove(List<String> imageUrlsToRemove) {
        this.imageUrlsToRemove = imageUrlsToRemove;
    }
    
    public List<String> getFilePathsToRemove() {
        return filePathsToRemove;
    }
    
    public void setFilePathsToRemove(List<String> filePathsToRemove) {
        this.filePathsToRemove = filePathsToRemove;
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
    
    public String getPublishTime() {
        return publishTime;
    }
    
    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }
}

