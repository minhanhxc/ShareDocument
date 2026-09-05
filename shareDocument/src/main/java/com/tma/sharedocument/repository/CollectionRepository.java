/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tma.sharedocument.repository;

import com.tma.sharedocument.pojo.Collection;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


/**
 *
 * @author Minh Anh
 */
public interface CollectionRepository extends JpaRepository<Collection, Long>{
    Optional<Collection> findByUserId(Long userId);
 
    boolean existsByUserId(Long userId);
    @Modifying
    @Query(value = "DELETE FROM collection_documents WHERE document_id = :documentId",
           nativeQuery = true)
    void removeDocumentFromAllCollections(Long documentId);
    @Query(value = "SELECT COUNT(*) FROM collection c " +
                   "JOIN collection_documents cd ON c.id = cd.collection_id " +
                   "WHERE c.user_id = :userId AND cd.document_id = :documentId",
           nativeQuery = true)
    long countByUserIdAndDocumentId(Long userId, Long documentId);
 
    default boolean existsByUserIdAndDocumentId(Long userId, Long documentId) {
        return countByUserIdAndDocumentId(userId, documentId) > 0;
    }
}
