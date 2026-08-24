/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tma.sharedocument.service;

import com.tma.sharedocument.dto.DocumentDetailResponseDto;
import com.tma.sharedocument.dto.DocumentRequestDto;
import com.tma.sharedocument.dto.DocumentResponseDto;
import com.tma.sharedocument.mapper.DocumentMapper;
import com.tma.sharedocument.pojo.Category;
import com.tma.sharedocument.pojo.Document;
import com.tma.sharedocument.pojo.Tag;
import com.tma.sharedocument.pojo.User;
import com.tma.sharedocument.pojo.View;
import com.tma.sharedocument.repository.CategoryRepository;
import com.tma.sharedocument.repository.CollectionRepository;
import com.tma.sharedocument.repository.DocumentRepository;
import com.tma.sharedocument.repository.LikeRepository;
import com.tma.sharedocument.repository.TagRepository;
import com.tma.sharedocument.repository.UserRepository;
import com.tma.sharedocument.repository.ViewRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 *
 * @author Minh Anh
 */
@Service
public class DocumentService {

    @Autowired
    private DocumentMapper documentMapper;
    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private ViewRepository viewRepository;
    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private CollectionRepository collectionRepository;

    public DocumentResponseDto createDocument(DocumentRequestDto dto, String username) {
        Document document = documentMapper.toPojo(dto);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Danh mục không tồn tại"));
        
        document.setUser(user);
        document.setCategory(category);
        document.setFileType(StringUtils.getFilenameExtension(document.getFileUrl()));

        Set<Tag> documentTags = new HashSet<>();
        if (dto.getExistingTagIds() != null && !dto.getExistingTagIds().isEmpty()) {
            List<Tag> existingTags = tagRepository.findAllById(dto.getExistingTagIds());
            documentTags.addAll(existingTags);
        }

        if (dto.getNewTagNames() != null && !dto.getNewTagNames().isEmpty()) {
            for (String tagName : dto.getNewTagNames()) {
                String cleanName = tagName.trim();
                Tag tag = tagRepository.findByName(cleanName)
                        .orElseGet(() -> {
                            Tag newTag = new Tag();
                            newTag.setName(cleanName);
                            return tagRepository.save(newTag);
                        });

                documentTags.add(tag);
            }
        }
        document.setTags(documentTags);
        documentRepository.save(document);
        return documentMapper.toDto(document);
        
    }

    public Page<DocumentResponseDto> listDocument(String keyword, Long categoryId,
            Long tagId, int page, int size) {


        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<Document> documentPage = documentRepository.findAll(keyword, categoryId, tagId, pageable);

        return documentPage.map(documentMapper::toDto);
    }
    
    public DocumentDetailResponseDto detailDocument(Long documentId, String username){
        Document document = documentRepository.findByIdWWithDetail(documentId)
                .orElseThrow(() -> new RuntimeException("Không tim  thấy tài liệu"));
        
        document.setTotalView(document.getTotalView()+ 1);
        
        View view = new View();
        view.setDocument(document);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));
        view.setUser(user);
        viewRepository.save(view);
        documentRepository.save(document);
       
        return documentMapper.toDetailDto(document);
    }
    @Transactional
    public void deleteDocument(Long doccumentId, String username) {
        Document document = documentRepository.findById(doccumentId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài liệu"));
        if(!document.getUser().getUsername().equals(username))
            throw new RuntimeException("Bạn không có quyền xóa tài liệu");
        collectionRepository.removeDocumentFromAllCollections(doccumentId);
        documentRepository.delete(document);
    }
}
