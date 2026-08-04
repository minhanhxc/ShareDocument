/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tma.sharedocument.mapper;

import com.tma.sharedocument.dto.TagRequestDto;
import com.tma.sharedocument.dto.TagResponseDto;
import com.tma.sharedocument.pojo.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 *
 * @author Minh Anh
 */
@Mapper
public interface TagMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "documents", ignore = true)
    Tag toPojo(TagRequestDto t);
    
    TagResponseDto toDto(Tag t);
}
