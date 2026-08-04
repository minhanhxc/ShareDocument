/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tma.sharedocument.repository;

import com.tma.sharedocument.pojo.Like;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Minh Anh
 */
@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Long countByDocumentId(Long documentId);
    
    Boolean existsByUserIdAndDocumentId(Long userId, Long documentId);
    
    List<Like> findByUserIdAndDocumentId(Long userId, Long documentId);
}
