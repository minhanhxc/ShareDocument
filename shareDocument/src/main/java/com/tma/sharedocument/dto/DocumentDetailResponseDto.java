/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tma.sharedocument.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentDetailResponseDto {
    private Long id;
    private String title;
    private String description;
    private String fileUrl;
    private String fileType;
    private String thumbnail;
    private Long uploaderId;
    private String uploaderName;
    private List<String> tagNames;
    private Long categoryId;
    private String categoryName;
    private Long totalView;
    private Long totalLike;
    private boolean isLiked;
    private boolean isBookmarked;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}
