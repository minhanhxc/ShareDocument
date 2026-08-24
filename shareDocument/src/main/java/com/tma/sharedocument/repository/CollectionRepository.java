/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tma.sharedocument.repository;

import com.tma.sharedocument.dto.CollectionResponseDto;
import com.tma.sharedocument.pojo.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author Minh Anh
 */
public interface CollectionRepository extends JpaRepository<Collection, Long>{
    @Modifying
    @Query(value = "DELETE FROM collection_documents WHERE document_id = :documentId", nativeQuery = true)
    void removeDocumentFromAllCollections(@Param("documentId") Long documentId);
    
    @Query("SELECT new com.tma.sharedocument.dto.CollectionResponseDto(c.id, c.name, SIZE(c.documents)) " +
           "FROM Collection c WHERE c.user.username = :username")
    List<CollectionResponseDto> findAllByUserUsername(@Param("username") String username);

    @Query("SELECT c FROM Collection c LEFT JOIN FETCH c.documents " +
           "WHERE c.id = :id AND c.user.username = :username")
    Optional<Collection> findByIdAndUserUsername(@Param("id") Long id, @Param("username") String username);
}
