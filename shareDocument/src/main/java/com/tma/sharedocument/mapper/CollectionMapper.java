/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tma.sharedocument.mapper;

import com.tma.sharedocument.dto.CollectionRequestDto;
import com.tma.sharedocument.dto.CollectionResponseDto;
import com.tma.sharedocument.pojo.Collection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 *
 * @author Minh Anh
 */
@Mapper
public interface CollectionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "documents", ignore = true)
    Collection toPojo(CollectionRequestDto c);
    
    @Mapping(source = "user.id", target = "userId")
    @Mapping(target  = "totalDocument", ignore = true)
    CollectionResponseDto toDto(Collection c);
}
