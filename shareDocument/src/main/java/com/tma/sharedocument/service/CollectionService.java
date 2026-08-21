/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tma.sharedocument.service;

import com.tma.sharedocument.dto.CollectionRequestDto;
import com.tma.sharedocument.pojo.Collection;
import com.tma.sharedocument.pojo.Document;
import com.tma.sharedocument.pojo.User;
import com.tma.sharedocument.repository.CollectionRepository;
import com.tma.sharedocument.repository.DocumentRepository;
import com.tma.sharedocument.repository.UserRepository;
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
    
    @Transactional
    public Collection createCollection(CollectionRequestDto request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));
        Collection collection = new Collection();
        collection.setName(request.getName());
        collection.setUser(user);
        
        return collectionRepository.save(collection);
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
