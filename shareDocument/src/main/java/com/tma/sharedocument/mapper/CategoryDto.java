/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tma.sharedocument.mapper;

import com.tma.sharedocument.dto.CategoryRequestDto;
import com.tma.sharedocument.dto.CategoryResponseDto;
import com.tma.sharedocument.pojo.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 *
 * @author Minh Anh
 */
@Mapper
public interface CategoryDto {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "documents", ignore = true)
    Category toPojo(CategoryRequestDto r);

    CategoryResponseDto toDto(Category c);
}
