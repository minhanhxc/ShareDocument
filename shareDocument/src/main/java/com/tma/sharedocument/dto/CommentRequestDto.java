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
import lombok.Data;


@Data
public class CommentRequestDto {

    @NotBlank(message = "Nội dung bình luận không được để trống")
    private String content; 
}
