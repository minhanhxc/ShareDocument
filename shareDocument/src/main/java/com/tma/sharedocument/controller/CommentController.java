/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tma.sharedocument.controller;

import com.tma.sharedocument.dto.CommentRequestDto;
import com.tma.sharedocument.dto.CommentResponseDto;
import com.tma.sharedocument.service.CommentService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
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
 * @author ADMIN
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/documents/{documentId}/comments")
    public ResponseEntity<Page<CommentResponseDto>> listComments(
            @PathVariable Long documentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<CommentResponseDto> response = commentService.listComments(documentId, page, size);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/documents/{documentId}/comments")
    public ResponseEntity<CommentResponseDto> addComment(
            @PathVariable Long documentId,
            @Valid @RequestBody CommentRequestDto request,
            @AuthenticationPrincipal UserDetails user) {
        CommentResponseDto newComment = commentService.addComment(documentId, request, user.getUsername());
        return new ResponseEntity<>(newComment, HttpStatus.CREATED);
    }

    @DeleteMapping("/comments/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetails user) {
        commentService.deleteComment(commentId, user.getUsername());
    }
}
