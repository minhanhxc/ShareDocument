/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tma.sharedocument.dto;

import java.util.List;

/**
 *
 * @author ADMIN
 */
public class CollectionDetailResponseDto {
    
    private Long id;
    private String name;
    private Integer totalDocument;
    private List<DocumentResponseDto> documents;

    public CollectionDetailResponseDto(Long id, String name, Integer totalDocument, List<DocumentResponseDto> documents) {
        this.id = id;
        this.name = name;
        this.totalDocument = totalDocument;
        this.documents = documents;
    }

    public CollectionDetailResponseDto() {
    }
    
    
    

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the documents
     */
    public List<DocumentResponseDto> getDocuments() {
        return documents;
    }

    /**
     * @param documents the documents to set
     */
    public void setDocuments(List<DocumentResponseDto> documents) {
        this.documents = documents;
    }

    /**
     * @return the totalDocument
     */
    public Integer getTotalDocument() {
        return totalDocument;
    }

    /**
     * @param totalDocument the totalDocument to set
     */
    public void setTotalDocument(Integer totalDocument) {
        this.totalDocument = totalDocument;
    }
}
