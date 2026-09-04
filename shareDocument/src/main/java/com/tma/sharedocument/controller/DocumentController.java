/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tma.sharedocument.controller;

import com.tma.sharedocument.dto.DocumentDetailResponseDto;
import com.tma.sharedocument.dto.DocumentRequestDto;
import com.tma.sharedocument.dto.DocumentResponseDto;
import com.tma.sharedocument.pojo.Category;
import com.tma.sharedocument.service.DocumentService;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Minh Anh
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping("/documents/upload")
    public ResponseEntity<DocumentResponseDto> createDocument(
            @Valid @ModelAttribute DocumentRequestDto dto,
            @AuthenticationPrincipal UserDetails user) throws IOException {
        DocumentResponseDto response = documentService.createDocument(dto, user.getUsername());
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @GetMapping("/documents")
    public ResponseEntity<List<DocumentResponseDto>> listDocument(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long tagId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy) {
        Page<DocumentResponseDto> response = documentService.listDocument(keyword, categoryId, tagId, page, size, sortBy);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @GetMapping("/documents/{documentId}")
    public ResponseEntity<DocumentResponseDto> detailDocument(
            @PathVariable Long documentId,
            @AuthenticationPrincipal UserDetails user) {
        DocumentDetailResponseDto response = documentService.detailDocument(documentId, user.getUsername());
        return new ResponseEntity(response, HttpStatus.OK);
    }
    
    @GetMapping("/categories")
    public ResponseEntity<Category> listCate(){
        List<Category> response = documentService.listCate();
        return new ResponseEntity(response, HttpStatus.OK);
    }
}
