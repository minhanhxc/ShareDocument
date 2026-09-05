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



public class CommentRequestDto {

    public CommentRequestDto(String content) {
        this.content = content;
    }

    public CommentRequestDto() {
    }

    @NotBlank(message = "Nội dung bình luận không được để trống")
    private String content;


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
}
