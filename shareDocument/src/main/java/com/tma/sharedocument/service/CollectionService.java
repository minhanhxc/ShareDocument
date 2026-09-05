/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tma.sharedocument.service;


import com.tma.sharedocument.dto.DocumentResponseDto;
import com.tma.sharedocument.mapper.DocumentMapper;
import com.tma.sharedocument.pojo.Collection;
import com.tma.sharedocument.pojo.Document;
import com.tma.sharedocument.pojo.User;
import com.tma.sharedocument.repository.CollectionRepository;
import com.tma.sharedocument.repository.DocumentRepository;
import com.tma.sharedocument.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 *
 * @author ADMIN
 */
@Service
@RequiredArgsConstructor
public class CollectionService {

    private final CollectionRepository collectionRepository;
    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;
    private final DocumentMapper documentMapper;
    
    
    
    public Collection getOrCreateCollection(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user"));
 
        return collectionRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Collection newCollection = new Collection();
                    newCollection.setUser(user);
                    return collectionRepository.save(newCollection);
                });
    }
 
    public boolean toggleDocument(String username, Long documentId) {
        Collection collection = getOrCreateCollection(username);
 
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài liệu"));
 
        boolean alreadyExists = collection.getDocuments().contains(document);
 
        if (alreadyExists) {
            collection.getDocuments().remove(document);
            collectionRepository.save(collection);
            return false;
        } else {
            collection.getDocuments().add(document);
            collectionRepository.save(collection);
            return true;
        }
    }
    
    public List<DocumentResponseDto> getFavoriteDocuments(String username) {
        Collection collection = getOrCreateCollection(username);
 
        return collection.getDocuments().stream()
                .map(documentMapper::toDto)
                .collect(Collectors.toList());
    }
}
