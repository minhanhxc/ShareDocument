/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tma.sharedocument.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.tma.sharedocument.dto.AuthResponseDto;
import com.tma.sharedocument.dto.LoginRequestDto;
import com.tma.sharedocument.dto.RegisterRequestDto;
import com.tma.sharedocument.dto.UserResponseDto;
import com.tma.sharedocument.enums.UserRole;
import com.tma.sharedocument.mapper.UserMapper;
import com.tma.sharedocument.pojo.User;
import com.tma.sharedocument.repository.CollectionRepository;
import com.tma.sharedocument.repository.DocumentRepository;
import com.tma.sharedocument.repository.UserRepository;
import com.tma.sharedocument.utils.JwtUtil;
import java.io.IOException;
import java.util.Map;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author ADMIN
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final DocumentRepository documentRepository;
    private final CollectionRepository collectionRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final Cloudinary cloudinary;

    public String uploadFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap("resource_type", "auto"));
            return uploadResult.get("secure_url").toString();
        } catch (IOException e) {
            throw new RuntimeException("Upload avatar thất bại: " + e.getMessage());
        }
    }

    public AuthResponseDto register(RegisterRequestDto dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("Username đã tồn tại");
        }
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email đã tồn tại");
        }
        User user = userMapper.toPojo(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setUserRole(UserRole.ROLE_USER);

        if (dto.getAvatar() != null) {
            user.setAvatar(uploadFile(dto.getAvatar()));
        }
        userRepository.save(user);
        String token = jwtUtil.generateToken(user.getUsername());
        return userMapper.toAuthResponseDto(user, token);
    }

    public AuthResponseDto login(LoginRequestDto dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getUsername(), dto.getPassword()));

        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user"));

        String token = jwtUtil.generateToken(user.getUsername());

        return userMapper.toAuthResponseDto(user, token);
    }

    public UserResponseDto profile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
        UserResponseDto dto = userMapper.toDto(user);

        dto.setTotalUploaded(collectionRepository.countDocumentInCollection(user.getId()));
        dto.setTotalBookmarked(documentRepository.countByUser(user));
        return dto;
    }

    @Transactional
    public UserResponseDto updateUserProfile(String username, String ho, String ten, MultipartFile avatarFile) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
        user.setHo(ho);
        user.setTen(ten);
        if (avatarFile != null && !avatarFile.isEmpty()) {
            String avatarUrl = this.uploadFile(avatarFile);
            user.setAvatar(avatarUrl);
        }
        UserResponseDto dto = userMapper.toDto(user);
        dto.setTotalUploaded(collectionRepository.countDocumentInCollection(user.getId()));
        dto.setTotalBookmarked(documentRepository.countByUser(user));
        return dto;
    }
}
