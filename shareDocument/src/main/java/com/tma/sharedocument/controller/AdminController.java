/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tma.sharedocument.controller;

import com.tma.sharedocument.repository.DocumentRepository;
import com.tma.sharedocument.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author ADMIN
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final UserRepository userRepository;
    
    private final DocumentRepository documentRepository; // Hoặc service tương ứng

    @GetMapping("/login")
    public String loginPage() {
        return "login"; 
    }

    @GetMapping("/dashboard")
    public String dashboardPage(Model model) {
        long totalUsers = userRepository.count();
        long totalDocs = documentRepository.count();
        
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("totalDocs", totalDocs);
        
        return "dashboard"; 
    }

    @GetMapping("/users")
    public String listUsers(Model model, @RequestParam(defaultValue = "0") int page) {
        return "users";
    }

    @GetMapping("/documents")
    public String listDocuments(Model model, @RequestParam(defaultValue = "0") int page) {
        return "admin/documents";
    }
}
