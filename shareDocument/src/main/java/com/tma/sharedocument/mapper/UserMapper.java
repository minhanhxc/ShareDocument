/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tma.sharedocument.mapper;

import com.tma.sharedocument.dto.AuthResponseDto;
import com.tma.sharedocument.dto.RegisterRequestDto;
import com.tma.sharedocument.dto.UserResponseDto;
import com.tma.sharedocument.pojo.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 *
 * @author Minh Anh
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userRole", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "avatar", ignore = true)
    User toPojo(RegisterRequestDto u);

    @Mapping(target = "totalUploaded", ignore = true)
    @Mapping(target = "totalBookmarked", ignore = true)
    UserResponseDto toDto(User u);

    @Mapping(target = "token", source = "token")
    AuthResponseDto toAuthResponseDto(User user, String token);
}
