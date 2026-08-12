package com.tma.sharedocument.specification;


import org.springframework.data.jpa.domain.Specification;

import com.tma.sharedocument.pojo.Document;
import com.tma.sharedocument.pojo.Tag;
import jakarta.persistence.criteria.Join;

public class DocumentSpecification {

    public static Specification<Document> hasKeyword(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isBlank()) {
                return cb.conjunction(); // không lọc gì nếu keyword rỗng
            }
            String pattern = "%" + keyword.toLowerCase() + "%";
            return cb.like(cb.lower(root.get("title")), pattern);
        };
    }

    public static Specification<Document> hasCategoryId(Long categoryId) {
        return (root, query, cb) -> {
            if (categoryId == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("category").get("id"), categoryId);
        };
    }

    public static Specification<Document> hasTagId(Long tagId) {
        return (root, query, cb) -> {
            if (tagId == null) {
                return cb.conjunction();
            }
            query.distinct(true); // tránh trùng document khi join bảng N-N
            Join<Document, Tag> tagJoin = root.join("tags");
            return cb.equal(tagJoin.get("id"), tagId);
        };
    }
}