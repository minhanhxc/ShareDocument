/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tma.sharedocument.controller;

import com.tma.sharedocument.dto.AuthResponseDto;
import com.tma.sharedocument.dto.DocumentResponseDto;
import com.tma.sharedocument.dto.LoginRequestDto;
import com.tma.sharedocument.dto.RegisterRequestDto;
import com.tma.sharedocument.dto.UserResponseDto;
import com.tma.sharedocument.service.DocumentService;
import com.tma.sharedocument.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author ADMIN
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final DocumentService documentService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(
            @Valid @ModelAttribute RegisterRequestDto dto) {
        AuthResponseDto response = userService.register(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(
            @Valid @RequestBody LoginRequestDto dto) {
        AuthResponseDto response = userService.login(dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/profile")
    public ResponseEntity<UserResponseDto> profile(@AuthenticationPrincipal UserDetails user) {
        UserResponseDto response = userService.profile(user.getUsername());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/profile/my-documents")
    public ResponseEntity<Page<DocumentResponseDto>> getMyDocuments(
            @AuthenticationPrincipal UserDetails user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<DocumentResponseDto> uploadedDocs = documentService.getUserDocuments(user.getUsername(), PageRequest.of(page, size));
        return ResponseEntity.ok(uploadedDocs);
    }
    @PutMapping("/profile")
    public ResponseEntity<UserResponseDto> updateProfile(
            @AuthenticationPrincipal UserDetails user,
            @RequestParam("ho") String ho,
            @RequestParam("ten") String ten,
            @RequestParam(value = "avatar", required = false) MultipartFile avatar) {
        
        
        UserResponseDto updatedProfile = userService.updateUserProfile(user.getUsername(), ho, ten, avatar);
        
        return new ResponseEntity<>(updatedProfile, HttpStatus.OK);
    }
}
