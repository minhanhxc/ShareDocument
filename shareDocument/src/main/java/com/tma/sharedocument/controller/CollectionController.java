/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tma.sharedocument.controller;

import com.tma.sharedocument.dto.DocumentResponseDto;
import com.tma.sharedocument.dto.LikeStatusResponseDto;
import com.tma.sharedocument.service.CollectionService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author ADMIN
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CollectionController {

    private final CollectionService collectionService;

    @GetMapping("/collection")
    public ResponseEntity<List<DocumentResponseDto>> getFavorites(
            @AuthenticationPrincipal UserDetails userDetails) {
        List<DocumentResponseDto> response = collectionService.getFavoriteDocuments(userDetails.getUsername());
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @PostMapping("/collection/{documentId}")
    public ResponseEntity<LikeStatusResponseDto> toggleDocument(
            @PathVariable Long documentId,
            @AuthenticationPrincipal UserDetails userDetails) {

        boolean added = collectionService.toggleDocument(userDetails.getUsername(), documentId);

        LikeStatusResponseDto response = LikeStatusResponseDto.builder()
                .isLiked(added)
                .build();

        return new ResponseEntity(response, HttpStatus.OK);
    }
}
