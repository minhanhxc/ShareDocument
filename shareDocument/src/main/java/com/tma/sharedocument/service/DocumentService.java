/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tma.sharedocument.service;

import com.tma.sharedocument.dto.DocumentRequestDto;
import com.tma.sharedocument.dto.DocumentResponseDto;
import com.tma.sharedocument.mapper.DocumentMapper;
import com.tma.sharedocument.pojo.Category;
import com.tma.sharedocument.pojo.Document;
import com.tma.sharedocument.pojo.Tag;
import com.tma.sharedocument.pojo.User;
import com.tma.sharedocument.repository.CategoryRepository;
import com.tma.sharedocument.repository.DocumentRepository;
import com.tma.sharedocument.repository.TagRepository;
import com.tma.sharedocument.repository.UserRepository;
import com.tma.sharedocument.specification.DocumentSpecification;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
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

    public DocumentResponseDto createDocument(DocumentRequestDto dto, String username) {
        Document document = documentMapper.toPojo(dto);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Danh mục không tồn tại"));

        document.setUserId(user);
        document.setCategoryId(category);
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
        Document savedDoc = documentRepository.save(document);

        return documentMapper.toDto(savedDoc);
    }

    public Page<DocumentResponseDto> listDocument(String keyword, Long categoryId,
            Long tagId, int page, int size) {
        Specification<Document> spec = Specification
                .where(DocumentSpecification.hasKeyword(keyword))
                .and(DocumentSpecification.hasCategoryId(categoryId))
                .and(DocumentSpecification.hasTagId(tagId));

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<Document> documentPage = documentRepository.findAll(spec, pageable);

        return documentPage.map(documentMapper::toDto);

    }
}
