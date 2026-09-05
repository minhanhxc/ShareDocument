/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tma.sharedocument.service;

import com.tma.sharedocument.dto.CommentRequestDto;
import com.tma.sharedocument.dto.CommentResponseDto;
import com.tma.sharedocument.mapper.CommentMapper;
import com.tma.sharedocument.pojo.Comment;
import com.tma.sharedocument.pojo.Document;
import com.tma.sharedocument.pojo.User;
import com.tma.sharedocument.repository.CommentRepository;
import com.tma.sharedocument.repository.DocumentRepository;
import com.tma.sharedocument.repository.UserRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ADMIN
 */
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CommentMapper commentMapper;
    @Transactional
    public List<CommentResponseDto> getCommentsByDocument(Long documentId) {
        if (!documentRepository.existsById(documentId)) {
            throw new RuntimeException("Tài liệu không tồn tại");
        }
        List<Comment> comments = commentRepository.findByDocumentId(documentId);
        
        return comments.stream().map(commentMapper::toDto).toList();
    }
    @Transactional
    public CommentResponseDto addComment(Long documentId, CommentRequestDto request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Tài liệu không tồn tại"));

        Comment comment = commentMapper.toPojo(request);
        Comment savedComment = commentRepository.save(comment);

        return commentMapper.toDto(savedComment);
    }

    @Transactional
    public void deleteComment(Long commentId, String username) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Bình luận không tồn tại"));

        if (!comment.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Bạn không có quyền xóa bình luận của người khác");
        }

        commentRepository.delete(comment);
    }
}
