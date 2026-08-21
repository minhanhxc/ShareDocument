/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tma.sharedocument.dto;

/**
 *
 * @author Minh Anh
 */
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;


public class DocumentResponseDto {

    private Long id;
    private String title;
    private Long uploaderId;
    private String uploaderName;
    private List<String> tagNames;
    private String categoryName;
    private Long viewCount;
    private Long likeCount;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    public DocumentResponseDto(Long id, String title, Long uploaderId, String uploaderName, List<String> tagNames, String categoryName, Long viewCount, Long likeCount, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.uploaderId = uploaderId;
        this.uploaderName = uploaderName;
        this.tagNames = tagNames;
        this.categoryName = categoryName;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.createdAt = createdAt;
    }

    public DocumentResponseDto() {
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
     * @return the category
     */


    /**
     * @param category the category to set
     */


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
}