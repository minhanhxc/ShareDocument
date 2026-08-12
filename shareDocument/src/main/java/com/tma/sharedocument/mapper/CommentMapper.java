/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tma.sharedocument.mapper;

import com.tma.sharedocument.dto.CommentRequestDto;
import com.tma.sharedocument.dto.CommentResponseDto;
import com.tma.sharedocument.pojo.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 *
 * @author Minh Anh
 */
@Mapper
public interface CommentMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)      
    @Mapping(target = "documentId", ignore = true)  
    @Mapping(target = "createdAt", ignore = true)
    Comment toPojo(CommentRequestDto c);
    
    @Mapping(source = "userId.id", target = "userId")
    @Mapping(source = "userId.username", target = "username")
    @Mapping(source = "userId.avatar", target = "avatar")

    CommentResponseDto toDto(Comment c);
}
