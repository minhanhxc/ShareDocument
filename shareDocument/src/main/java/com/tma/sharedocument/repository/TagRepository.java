/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tma.sharedocument.repository;

import com.tma.sharedocument.pojo.Tag;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Minh Anh
 */
public interface TagRepository extends JpaRepository<Tag, Long>{
    List<Tag> findByName(String name);
}
