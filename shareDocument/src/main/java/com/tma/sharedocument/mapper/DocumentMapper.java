/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tma.sharedocument.mapper;

import com.tma.sharedocument.dto.DocumentDetailResponseDto;
import com.tma.sharedocument.dto.DocumentRequestDto;
import com.tma.sharedocument.dto.DocumentResponseDto;
import com.tma.sharedocument.pojo.Document;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 *
 * @author Minh Anh
 */
@Mapper(componentModel = "spring")
public interface DocumentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "fileType", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "totalView", ignore = true)
    @Mapping(target = "totalLike", ignore = true)
    Document toPojo(DocumentRequestDto requestDto);

    @Mapping(source = "user.id", target = "uploaderId")
    @Mapping(source = "user.username", target = "uploaderName")
    @Mapping(target = "tagNames", ignore = true)
    DocumentResponseDto toDto(Document d);

    @Mapping(target = "uploaderId", source = "d.user.id")
    @Mapping(target = "uploaderName", source = "d.user.username")
    @Mapping(target = "categoryId", source = "d.category.id")
    @Mapping(target = "categoryName", source = "d.category.name")
    @Mapping(target = "tagNames", expression = "java(d.getTags().stream().map(tag -> tag.getName()).toList())")
    DocumentDetailResponseDto toDetailDto(Document d);
}
