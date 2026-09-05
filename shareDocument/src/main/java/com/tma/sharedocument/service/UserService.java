/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tma.sharedocument.service;

import com.tma.sharedocument.dto.AuthResponseDto;
import com.tma.sharedocument.dto.LoginRequestDto;
import com.tma.sharedocument.dto.RegisterRequestDto;
import com.tma.sharedocument.enums.UserRole;
import com.tma.sharedocument.mapper.UserMapper;
import com.tma.sharedocument.pojo.User;
import com.tma.sharedocument.repository.UserRepository;
import com.tma.sharedocument.utils.JwtUtil;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *
 * @author ADMIN
 */
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepo;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthResponseDto register(RegisterRequestDto dto) {
        if (userRepo.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("Username đã tồn tại");
        }
        if (userRepo.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email đã tồn tại");
        }
        User user = userMapper.toPojo(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setUserRole(UserRole.ROLE_USER);
        userRepo.save(user);
          String token = jwtUtil.generateToken(user.getUsername());

        return userMapper.toAuthResponseDto(user, token);
    }

    public AuthResponseDto login(LoginRequestDto dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getUsername(), dto.getPassword()));

        User user = userRepo.findByUsername(dto.getUsername())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user"));

        String token = jwtUtil.generateToken(user.getUsername());

        return userMapper.toAuthResponseDto(user, token);
    }
}
