/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tma.sharedocument.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;


public class DocumentDetailResponseDto {
    private Long id;
    private String title;
    private String description;
    private String fileUrl;
    private String fileType;
    private Long uploaderId;
    private String uploaderName;
    private List<String> tagNames;
    private Long categoryId;
    private String categoryName;
    private Long viewCount;
    private Long likeCount;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    public DocumentDetailResponseDto(Long id, String title, String description, String fileUrl, String fileType, Long uploaderId, String uploaderName, List<String> tagNames, Long categoryId, String categoryName, Long viewCount, Long likeCount, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.fileUrl = fileUrl;
        this.fileType = fileType;
        this.uploaderId = uploaderId;
        this.uploaderName = uploaderName;
        this.tagNames = tagNames;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.createdAt = createdAt;
    }

    public DocumentDetailResponseDto() {
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the fileUrl
     */
    public String getFileUrl() {
        return fileUrl;
    }

    /**
     * @param fileUrl the fileUrl to set
     */
    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    /**
     * @return the fileType
     */
    public String getFileType() {
        return fileType;
    }

    /**
     * @param fileType the fileType to set
     */
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    /**
     * @return the uploaderId
     */
    public Long getUploaderId() {
        return uploaderId;
    }

    /**
     * @param uploaderId the uploaderId to set
     */
    public void setUploaderId(Long uploaderId) {
        this.uploaderId = uploaderId;
    }

    /**
     * @return the uploaderName
     */
    public String getUploaderName() {
        return uploaderName;
    }

    /**
     * @param uploaderName the uploaderName to set
     */
    public void setUploaderName(String uploaderName) {
        this.uploaderName = uploaderName;
    }

    /**
     * @return the tagNames
     */
    public List<String> getTagNames() {
        return tagNames;
    }

    /**
     * @param tagNames the tagNames to set
     */
    public void setTagNames(List<String> tagNames) {
        this.tagNames = tagNames;
    }

    /**
     * @return the categoryId
     */
    public Long getCategoryId() {
        return categoryId;
    }

    /**
     * @param categoryId the categoryId to set
     */
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * @return the categoryName
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * @param categoryName the categoryName to set
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    /**
     * @return the viewCount
     */
    public Long getViewCount() {
        return viewCount;
    }

    /**
     * @param viewCount the viewCount to set
     */
    public void setViewCount(Long viewCount) {
        this.viewCount = viewCount;
    }

    /**
     * @return the likeCount
     */
    public Long getLikeCount() {
        return likeCount;
    }

    /**
     * @param likeCount the likeCount to set
     */
    public void setLikeCount(Long likeCount) {
        this.likeCount = likeCount;
    }

    /**
     * @return the createdAt
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * @param createdAt the createdAt to set
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}
