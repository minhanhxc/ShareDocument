/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tma.sharedocument.dto;

import java.time.LocalDateTime;
import lombok.Data;

/**
 *
 * @author Minh Anh
 */
@Data
public class DocumentDto {
    private Long id;
    private String title;
    private String description;
    private String fileUrl;
    private String fileType;
    private UserResponseDto user;
    private CateDto category;
    private LocalDateTime createAt;
}   
