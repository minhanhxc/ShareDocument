/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tma.sharedocument.dto;

/**
 *
 * @author Minh Anh
 */
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class CommentResponseDto {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private String username;
    private String avatar;

}
