/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tma.sharedocument.repository;

import com.tma.sharedocument.pojo.Document;
import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Minh Anh
 */
@Repository
public interface DocumentRepository extends JpaRepository<Document, Long>{
    List<Document> findByCategoryId(Long categoryId);
    List<Document> findByUserId(Long userId);

    List<Document> findByTitleContainingIgnoreCase(String keyword);

    List<Document> findByTagsId(Long tagId);
}
