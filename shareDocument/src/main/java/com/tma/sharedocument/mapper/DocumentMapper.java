/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tma.sharedocument.mapper;

import com.tma.sharedocument.dto.DocumentRequestDto;
import com.tma.sharedocument.dto.DocumentResponseDto;
import com.tma.sharedocument.pojo.Document;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 *
 * @author Minh Anh
 */
@Mapper
public interface DocumentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "likes", ignore = true)
    @Mapping(target = "views", ignore = true)
    Document toPojo(DocumentRequestDto requestDto);

    @Mapping(source = "user.id", target = "uploaderId")
    @Mapping(source = "user.username", target = "uploaderName")
    @Mapping(target = "tagNames", ignore = true)   
    @Mapping(target = "viewCount", ignore = true)  
    @Mapping(target = "likeCount", ignore = true)
    DocumentResponseDto toDto(Document d);
}
