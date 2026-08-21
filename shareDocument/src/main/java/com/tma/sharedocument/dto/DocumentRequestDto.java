/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tma.sharedocument.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class DocumentRequestDto {

    @NotBlank(message = "Tiêu đề không được để trống")
    private String title;
    private String description;
    @NotBlank(message = "File URL không được để trống")
    private String fileUrl;
    @NotNull(message = "Phải chọn danh mục")
    private Long categoryId;
    private List<Long> existingTagIds;
    private List<String> newTagNames;

    public DocumentRequestDto(String title, String description, String fileUrl, Long categoryId, List<Long> existingTagIds, List<String> newTagNames) {
        this.title = title;
        this.description = description;
        this.fileUrl = fileUrl;
        this.categoryId = categoryId;
        this.existingTagIds = existingTagIds;
        this.newTagNames = newTagNames;
    }

    public DocumentRequestDto() {
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
     * @return the existingTagIds
     */
    public List<Long> getExistingTagIds() {
        return existingTagIds;
    }

    /**
     * @param existingTagIds the existingTagIds to set
     */
    public void setExistingTagIds(List<Long> existingTagIds) {
        this.existingTagIds = existingTagIds;
    }

    /**
     * @return the newTagNames
     */
    public List<String> getNewTagNames() {
        return newTagNames;
    }

    /**
     * @param newTagNames the newTagNames to set
     */
    public void setNewTagNames(List<String> newTagNames) {
        this.newTagNames = newTagNames;
    }

}
