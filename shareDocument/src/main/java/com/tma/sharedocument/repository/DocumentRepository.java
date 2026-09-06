/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tma.sharedocument.repository;

import com.tma.sharedocument.pojo.Document;
import com.tma.sharedocument.pojo.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Minh Anh
 */
@Repository
public interface DocumentRepository extends JpaRepository<Document, Long>,
        JpaSpecificationExecutor<Document> {

    Long countByUser(User user);
    Page<Document> findByUser(User user, Pageable pageable);
    @Query("SELECT d FROM Document d JOIN FETCH d.user JOIN FETCH d.category "
            + "LEFT JOIN FETCH d.tags "
            + "WHERE d.id = :id")
    Optional<Document> findByIdWWithDetail(@Param("id") Long id);

    @Query("SELECT DISTINCT d FROM Document d "
            + "JOIN FETCH d.user u "
            + "JOIN FETCH d.category c "
            + "LEFT JOIN d.tags t "
            + "WHERE (:keyword IS NULL OR :keyword = '' OR LOWER(d.title) LIKE LOWER(CONCAT('%', :keyword, '%'))) "
            + "AND (:categoryId IS NULL OR c.id = :categoryId) "
            + "AND (:tagId IS NULL OR t.id = :tagId)")
    Page<Document> findAll(@Param("keyword") String keyword,
            @Param("categoryId") Long categoryId,
            @Param("tagId") Long tagId,
            Pageable pageable);
}
