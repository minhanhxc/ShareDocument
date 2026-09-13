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
import static io.jsonwebtoken.lang.Collections.size;
import java.util.List;
import lombok.RequiredArgsConstructor;
import static org.hibernate.query.Page.page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ADMIN
 */
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    @Transactional
    public Page<CommentResponseDto> listComments(Long documentId, int page, int size) {
        if (!documentRepository.existsById(documentId)) {
            throw new RuntimeException("Tài liệu không tồn tại");
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Comment> commentPage  = commentRepository.findByDocumentId(documentId, pageable);

        return commentPage.map(commentMapper::toDto);
    }
    @Transactional
    public CommentResponseDto addComment(Long documentId, CommentRequestDto request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Tài liệu không tồn tại"));
        Comment comment = commentMapper.toPojo(request);
        comment.setUser(user);
        comment.setDocument(document);
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
