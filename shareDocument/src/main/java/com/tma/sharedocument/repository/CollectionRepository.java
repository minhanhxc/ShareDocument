/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tma.sharedocument.repository;

import com.tma.sharedocument.pojo.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author Minh Anh
 */
public interface CollectionRepository extends JpaRepository<Collection, Long>{
    List<Collection> findByUserId(Long userId);
    @Modifying
    @Query(value = "DELETE FROM collection_documents WHERE document_id = :documentId", nativeQuery = true)
    void removeDocumentFromAllCollections(@Param("documentId") Long documentId);
}
