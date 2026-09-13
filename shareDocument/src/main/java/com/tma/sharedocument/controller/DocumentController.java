/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tma.sharedocument.controller;

import com.tma.sharedocument.dto.DocumentDetailResponseDto;
import com.tma.sharedocument.dto.DocumentRequestDto;
import com.tma.sharedocument.dto.DocumentResponseDto;
import com.tma.sharedocument.dto.LikeStatusResponseDto;
import com.tma.sharedocument.elasticsearch.DocumentSearchService;
import com.tma.sharedocument.pojo.Category;
import com.tma.sharedocument.pojo.Tag;
import com.tma.sharedocument.service.DocumentService;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Minh Anh
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;
    private final DocumentSearchService documentSearchService;

    @PostMapping("/documents/upload")
    public ResponseEntity<DocumentResponseDto> createDocument(
            @Valid @ModelAttribute DocumentRequestDto dto,
            @AuthenticationPrincipal UserDetails user) throws IOException {
        DocumentResponseDto response = documentService.createDocument(dto, user.getUsername());
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @GetMapping("/documents")
    public ResponseEntity<Page<DocumentResponseDto>> bannerDocument(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long tagId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "4") int size,
            @RequestParam(defaultValue = "totalView") String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        Page<DocumentResponseDto> response = documentService.bannerDocument(keyword, categoryId, tagId, pageable);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/documents/search")
    public ResponseEntity<Page<DocumentResponseDto>> searchDocuments(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) List<Long> tagId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        Page<DocumentResponseDto> response = documentSearchService.searchDocuments(keyword, categoryId, tagId, pageable);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/documents/{documentId}")
    public ResponseEntity<DocumentDetailResponseDto> detailDocument(
            @PathVariable Long documentId,
            @AuthenticationPrincipal UserDetails user) {
        DocumentDetailResponseDto response = documentService.detailDocument(documentId, user.getUsername());
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @DeleteMapping("/documents/{documentId}")
    public void removeDocument(
            @PathVariable Long documentId,
            @AuthenticationPrincipal UserDetails userDetails) {

        documentService.deleteDocument(documentId, userDetails.getUsername());
    }

    @PostMapping("/documents/{documentId}/like")
    public ResponseEntity<LikeStatusResponseDto> toggleLike(
            @PathVariable Long documentId,
            @AuthenticationPrincipal UserDetails userDetails) {

        boolean liked = documentService.toggleLike(userDetails.getUsername(), documentId);

        LikeStatusResponseDto response = LikeStatusResponseDto.builder()
                .isLiked(liked)
                .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/documents/edit/{documentId}")
    public ResponseEntity<DocumentResponseDto> updateDocument(
            @PathVariable Long documentId,
            @RequestParam String title,
            @RequestParam Long categoryId,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) List<Long> existingTagIds,
            @RequestParam(required = false) List<String> newTagNames,
            @RequestParam(required = false) MultipartFile file,
            @RequestParam(required = false) MultipartFile thumbnail,
            @AuthenticationPrincipal UserDetails userDetails) {

        DocumentResponseDto response = documentService.updateDocument(
                documentId, userDetails.getUsername(), title, categoryId,
                description, existingTagIds, newTagNames, file, thumbnail
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> listCate() {
        List<Category> response = documentService.listCate();
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @GetMapping("/tags")
    public ResponseEntity<List<Tag>> listTag() {
        List<Tag> response = documentService.listTag();
        return new ResponseEntity(response, HttpStatus.OK);
    }
}
