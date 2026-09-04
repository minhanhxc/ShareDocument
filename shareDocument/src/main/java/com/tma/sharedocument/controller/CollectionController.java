/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tma.sharedocument.controller;

import com.tma.sharedocument.dto.CollectionDetailResponseDto;
import com.tma.sharedocument.dto.CollectionRequestDto;
import com.tma.sharedocument.dto.CollectionResponseDto;
import com.tma.sharedocument.service.CollectionService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author ADMIN
 */
@RestController
@RequestMapping("/api/collections")
public class CollectionController {

    private final CollectionService collectionService;
    public CollectionController(CollectionService collectionService) {
        this.collectionService = collectionService;
    }

    @PostMapping
    public ResponseEntity<CollectionResponseDto> createCollection(
            @RequestBody CollectionRequestDto dto,
            @AuthenticationPrincipal UserDetails user) {
        CollectionResponseDto response = collectionService.createCollection(dto, user.getUsername());
        return new ResponseEntity(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CollectionResponseDto>> myCollections(
            @AuthenticationPrincipal UserDetails user) {
        List<CollectionResponseDto> response = collectionService.listCollection(user.getUsername());
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @GetMapping("/{collectionId}")
    public ResponseEntity<CollectionDetailResponseDto> detailCollecction(
            @PathVariable Long collectionId,
            @AuthenticationPrincipal UserDetails user) {
        CollectionDetailResponseDto response = collectionService.detailCollection(collectionId, user.getUsername());
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @DeleteMapping("/{collectionId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCollection(
            @PathVariable Long collectionId,
            @AuthenticationPrincipal UserDetails user) {
        collectionService.deleteCollection(collectionId, user.getUsername());
    }

    @PostMapping("/{collectionId}/documents/{documentId}")
    @ResponseStatus(HttpStatus.OK)
    public void addDocumentToCollection(
            @PathVariable Long collectionId,
            @PathVariable Long documentId,
            @AuthenticationPrincipal UserDetails user) {
        collectionService.addDocument(collectionId, documentId, user.getUsername());
    }

    @DeleteMapping("/{collectionId}/documents/{documentId}")
    @ResponseStatus(HttpStatus.OK)
    public void removeDocumentFromCollection(
            @PathVariable Long collectionId,
            @PathVariable Long documentId,
            @AuthenticationPrincipal UserDetails user) {
        collectionService.removeDocument(collectionId, documentId, user.getUsername());
    }

}
