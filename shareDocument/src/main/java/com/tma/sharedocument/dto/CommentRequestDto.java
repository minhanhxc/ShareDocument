/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tma.sharedocument.dto;

/**
 *
 * @author Minh Anh
 */
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;



public class CommentRequestDto {

    public CommentRequestDto(String content, Long documentId) {
        this.content = content;
        this.documentId = documentId;
    }

    public CommentRequestDto() {
    }

    @NotBlank(message = "Nội dung bình luận không được để trống")
    private String content;
    @NotNull(message = "Phải chỉ định document")
    private Long documentId;

    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return the documentId
     */
    public Long getDocumentId() {
        return documentId;
    }

    /**
     * @param documentId the documentId to set
     */
    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }
}
