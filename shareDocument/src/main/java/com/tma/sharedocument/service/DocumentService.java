/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tma.sharedocument.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.tma.sharedocument.dto.DocumentDetailResponseDto;
import com.tma.sharedocument.dto.DocumentRequestDto;
import com.tma.sharedocument.dto.DocumentResponseDto;
import com.tma.sharedocument.elasticsearch.DocumentSearch;
import com.tma.sharedocument.elasticsearch.DocumentSearchService;
import com.tma.sharedocument.mapper.DocumentMapper;
import com.tma.sharedocument.pojo.Category;
import com.tma.sharedocument.pojo.Document;
import com.tma.sharedocument.pojo.Like;
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
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Minh Anh
 */
@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentMapper documentMapper;
    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final ViewRepository viewRepository;
    private final LikeRepository likeRepository;
    private final CollectionRepository collectionRepository;
    private final Cloudinary cloudinary;
    private final DocumentSearchService documentSearchService;

    public String uploadFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap("resource_type", "auto"));
            return uploadResult.get("secure_url").toString();
        } catch (IOException e) {
            throw new RuntimeException("Upload file thất bại: " + e.getMessage());
        }
    }

    public String generateThumbnail(String fileUrl, String fileType) {
        String fileTypeLower = fileType.toLowerCase();
        if (fileTypeLower.equals("pdf")) {
            return fileUrl.replace("/upload/", "/upload/pg_1")
                    .replaceAll("\\.pdf$", ".jpg");
        } else if (fileTypeLower.equals("doc") || fileTypeLower.equals("docx")) {
            return "https://res.cloudinary.com/dc5reshvw/image/upload/v1788634633/ppt_xt8skp.png";
        } else if (fileTypeLower.equals("xls") || fileTypeLower.equals("xlsx")) {
            return "https://res.cloudinary.com/dc5reshvw/image/upload/v1788634644/xls_r5fnjr.png";
        } else if (fileTypeLower.equals("ppt") || fileTypeLower.equals("pptx")) {
            return "https://res.cloudinary.com/dc5reshvw/image/upload/v1788634633/ppt_xt8skp.png";
        } else {
            return "https://res.cloudinary.com/dc5reshvw/image/upload/v1788634633/blank_lbnpei.png";
        }
    }

    @Transactional
    public DocumentResponseDto createDocument(DocumentRequestDto dto, String username) {
        Document document = documentMapper.toPojo(dto);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Danh mục không tồn tại"));
        String fileUrl = this.uploadFile(dto.getFileUrl());
        document.setFileUrl(fileUrl);
        document.setUser(user);
        document.setCategory(category);

        String fileType = StringUtils.getFilenameExtension(dto.getFileUrl().getOriginalFilename());
        document.setFileType(fileType);

        String thumbnail = "";
        if (dto.getThumbnail() == null) {
            thumbnail = this.generateThumbnail(fileUrl, fileType);

        } else {
            thumbnail = this.uploadFile(dto.getThumbnail());
        }

        document.setThumbnail(thumbnail);

        Set<Tag> documentTags = new HashSet<>();

        if (dto.getExistingTagIds()
                != null && !dto.getExistingTagIds().isEmpty()) {
            List<Tag> existingTags = tagRepository.findAllById(dto.getExistingTagIds());
            documentTags.addAll(existingTags);
        }

        if (dto.getNewTagNames()
                != null && !dto.getNewTagNames().isEmpty()) {
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
        DocumentSearch search = documentMapper.toDocumentSearch(document);
        documentSearchService.index(search);
        return documentMapper.toDto(document);
    }

    public Page<DocumentResponseDto> bannerDocument(String keyword, Long categoryId,
            Long tagId, Pageable pageable) {

        Page<Document> documentPage = documentRepository.findAll(keyword, categoryId, tagId, pageable);

        return documentPage.map(documentMapper::toDto);
    }

    @Transactional
    public DocumentResponseDto updateDocument(Long documentId, String username, String title, Long categoryId,
            String description, List<Long> existingTagIds,
            List<String> newTagNames, MultipartFile file, MultipartFile thumbnail) {

        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài liệu"));

        if (!document.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Bạn không có quyền chỉnh sửa tài liệu này");
        }

        document.setTitle(title);
        document.setDescription(description);

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục"));
        document.setCategory(category);

        document.getTags().clear();

        if (existingTagIds != null && !existingTagIds.isEmpty()) {
            List<Tag> existingTags = tagRepository.findAllById(existingTagIds);
            document.getTags().addAll(existingTags);
        }

        if (newTagNames != null && !newTagNames.isEmpty()) {
            for (String tagName : newTagNames) {
                Tag tag = tagRepository.findByName(tagName)
                        .orElseGet(() -> tagRepository.save(new Tag(tagName)));
                document.getTags().add(tag);
            }
        }
        String fileUrl = null;
        String fileType = null;
        if (file != null && !file.isEmpty()) {
            fileUrl = this.uploadFile(file);
            document.setFileUrl(fileUrl);
            fileType = StringUtils.getFilenameExtension(file.getOriginalFilename());
            document.setFileType(fileType);
            String thumbnailUrl = null;
            if (thumbnail != null && !thumbnail.isEmpty()) {
                thumbnailUrl = this.uploadFile(thumbnail);
                document.setThumbnail(thumbnailUrl);

            } else {
                thumbnailUrl = this.generateThumbnail(fileUrl, fileType);
                document.setThumbnail(thumbnailUrl);
            }
        }

        // 5. Lưu vào MySQL
        Document savedDocument = documentRepository.save(document);

        DocumentSearch documentSearch = documentMapper.toDocumentSearch(savedDocument);

        documentSearchService.index(documentSearch);

        return documentMapper.toDto(savedDocument);
    }

    public DocumentDetailResponseDto detailDocument(Long documentId, String username) {
        Document document = documentRepository.findByIdWWithDetail(documentId)
                .orElseThrow(() -> new RuntimeException("Không tim  thấy tài liệu"));

        document.setTotalView(document.getTotalView() + 1);

        View view = new View();
        view.setDocument(document);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));
        view.setUser(user);
        viewRepository.save(view);
        documentRepository.save(document);
        DocumentDetailResponseDto dto = documentMapper.toDetailDto(document);
        dto.setLiked(likeRepository.existsByUserIdAndDocumentId(user.getId(), documentId));
        dto.setBookmarked(collectionRepository.existsByUserIdAndDocumentId(user.getId(), documentId));
        return dto;
    }

    @Transactional
    public void deleteDocument(Long doccumentId, String username) {
        Document document = documentRepository.findById(doccumentId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài liệu"));
        if (!document.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Bạn không có quyền xóa tài liệu");
        }
        collectionRepository.removeDocumentFromAllCollections(doccumentId);
        documentSearchService.delete(doccumentId);
        documentRepository.delete(document);
    }

    public Page<DocumentResponseDto> getUserDocuments(String username, Pageable pageable) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        // Lấy Page từ DB
        Page<Document> documentPage = documentRepository.findByUser(user, pageable);

        // Chuyển đổi Page<Document> thành Page<DocumentResponseDto>
        return documentPage.map(documentMapper::toDto);
    }

    @Transactional
    public boolean toggleLike(String username, Long documentId) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user"));

        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài liệu"));

        var existingLike = likeRepository.findByUserIdAndDocumentId(user.getId(), documentId);

        if (existingLike.isPresent()) {
            // Đã like trước đó -> gỡ like
            likeRepository.delete(existingLike.get());
            document.setTotalLike(Math.max(0L, document.getTotalLike() - 1));
            documentRepository.save(document);
            return false;
        } else {
            Like like = new Like();
            like.setUser(user);
            like.setDocument(document);
            likeRepository.save(like);

            document.setTotalLike(document.getTotalLike() + 1);
            documentRepository.save(document);
            return true;
        }
    }

    public List<Category> listCate() {
        List<Category> categories = categoryRepository.findAll();
        return categories;
    }

    public List<Tag> listTag() {
        List<Tag> tags = tagRepository.findAll();
        return tags;
    }
}
