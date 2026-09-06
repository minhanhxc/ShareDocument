/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tma.sharedocument.dto;

import java.util.List;
import lombok.Data;

/**
 *
 * @author ADMIN
 */
@Data
public class CollectionDetailResponseDto {
    
    private Long id;
    private String name;
    private Integer totalDocument;
    private List<DocumentResponseDto> documents;

    
}
