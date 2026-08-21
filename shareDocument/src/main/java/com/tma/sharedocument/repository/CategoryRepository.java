/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tma.sharedocument.repository;

import com.tma.sharedocument.pojo.Category;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Minh Anh
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>{
    List<Category> findByName(String name);
}
