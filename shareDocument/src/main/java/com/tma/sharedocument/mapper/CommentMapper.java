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
@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "document", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Comment toPojo(CommentRequestDto c);

    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "user.avatar", target = "avatar")
    @Mapping(target = "fullName", expression = "java(comment.getUser().getHo() + \" \" + comment.getUser().getTen())")
    CommentResponseDto toDto(Comment c);
}
