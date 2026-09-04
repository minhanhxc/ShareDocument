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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author ADMIN
 */
@RestController
@RequestMapping("/api") 
public class CommentController {

    private CommentService commentService;



    @GetMapping("/documents/{documentId}/comments")
    public ResponseEntity<List<CommentResponseDto>> getCommentsByDocument(
            @PathVariable Long documentId){
        List<CommentResponseDto> comments = commentService.getCommentsByDocument(documentId);
        return new ResponseEntity(comments, HttpStatus.OK);
    }


    @PostMapping("/documents/{documentId}/comments")
    public ResponseEntity<CommentResponseDto> addComment(
            @PathVariable Long documentId,
            @Valid @RequestBody CommentRequestDto request, 
            @AuthenticationPrincipal UserDetails user) {
        CommentResponseDto newComment = commentService.addComment(documentId, request, user.getUsername());
        return new ResponseEntity(newComment, HttpStatus.CREATED);
    }


    @DeleteMapping("/comments/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetails user) {
        commentService.deleteComment(commentId, user.getUsername());
    }
}
