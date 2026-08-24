/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tma.sharedocument.controller;

import com.tma.sharedocument.dto.DocumentDetailResponseDto;
import com.tma.sharedocument.dto.DocumentRequestDto;
import com.tma.sharedocument.dto.DocumentResponseDto;
import com.tma.sharedocument.pojo.User;
import com.tma.sharedocument.service.DocumentService;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Minh Anh
 */
@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private DocumentService documentService;

    @PostMapping
    public ResponseEntity<DocumentResponseDto> createDocument(
            @Valid @RequestBody DocumentRequestDto dto,
            @AuthenticationPrincipal UserDetails user) {
        DocumentResponseDto response = documentService.createDocument(dto, user.getUsername());
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<DocumentResponseDto>> listDocument(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long tagId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<DocumentResponseDto> response = documentService.listDocument(keyword, categoryId, tagId, page, size);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @GetMapping("/{documentId}")
    public ResponseEntity<DocumentResponseDto> detailDocument(
            @PathVariable Long documentId,
            @AuthenticationPrincipal UserDetails user) {
        DocumentDetailResponseDto response = documentService.detailDocument(documentId, user.getUsername());
        return new ResponseEntity(response, HttpStatus.OK);
    }
}
