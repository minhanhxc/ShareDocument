/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tma.sharedocument.service;

import com.tma.sharedocument.pojo.User;
import com.tma.sharedocument.repository.UserRepository;
import com.tma.sharedocument.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @author ADMIN
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
 
    private final UserRepository userRepo;
 
    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = this.userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy user"));
        return new CustomUserDetails(user);
    }
}
