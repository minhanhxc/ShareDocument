/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tma.sharedocument.service;

import com.tma.sharedocument.dto.CollectionDetailResponseDto;
import com.tma.sharedocument.dto.CollectionRequestDto;
import com.tma.sharedocument.dto.CollectionResponseDto;
import com.tma.sharedocument.mapper.CollectionMapper;
import com.tma.sharedocument.pojo.Collection;
import com.tma.sharedocument.pojo.Document;
import com.tma.sharedocument.pojo.User;
import com.tma.sharedocument.repository.CollectionRepository;
import com.tma.sharedocument.repository.DocumentRepository;
import com.tma.sharedocument.repository.UserRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ADMIN
 */
@Service
public class CollectionService {

    @Autowired
    private CollectionRepository collectionRepository;
    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CollectionMapper collectionMapper;
    
    @Transactional
    public List<CollectionResponseDto> listCollection(String username) {
        return collectionRepository.findAllByUserUsername(username);
    } 
    
    @Transactional
    public CollectionDetailResponseDto detailCollection(Long collectionId, String username){
       Collection collection = collectionRepository.findByIdAndUserUsername(collectionId, username)
                .orElseThrow(() -> new RuntimeException("Mục yêu thích không tồn tại hoặc bạn không có quyền xem"));

        return collectionMapper.toDetailDto(collection);
    }
    
    @Transactional
    public CollectionResponseDto createCollection(CollectionRequestDto dto, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));
        Collection collection = collectionMapper.toPojo(dto);
        collection.setUser(user);
        collectionRepository.save(collection);
        return collectionMapper.toDto(collection);
    }
    @Transactional
    public void deleteCollection(Long collectionId, String username) {
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new RuntimeException("Mục yêu thích không tồn tại"));
        if (!collection.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Bạn không có quyền xóa Mục yêu thích này");
        }
        collectionRepository.delete(collection);
    }
    @Transactional
    public void addDocument(Long collectionId, Long documentId, String username) {
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new RuntimeException("Mục yêu thích không tồn tại"));

        if (!collection.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Bạn không có quyền thao tác trên Mục yêu thích này");
        }

        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Tài liệu không tồn tại"));

        boolean isAdded = collection.getDocuments().add(document);

        if (!isAdded) {
            throw new RuntimeException("Tài liệu này đã có trong Mục yêu thích rồi");
        }
    }
    @Transactional
    public void removeDocument(Long collectionId, Long documentId, String username){
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new RuntimeException("Mục yêu thích không tồn tại"));

        if (!collection.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Bạn không có quyền thao tác trên Mục yêu thích này");
        }

        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Tài liệu không tồn tại"));
        
        boolean isRemoved = collection.getDocuments().remove(document);
        if (!isRemoved) {
            throw new RuntimeException("Tài liệu này không có trong Mục yêu thích rồi");
        }
    }
}
