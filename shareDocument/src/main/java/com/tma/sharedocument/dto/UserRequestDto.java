/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tma.sharedocument.dto;

import com.tma.sharedocument.enums.UserRole;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Minh Anh
 */
@Data
public class UserRequestDto {
    private Long id;
    private String ho;
    private String ten;
    private MultipartFile avatar;
    private String username;
    private String password;
    private String email;
    private UserRole userRole;
    private Boolean status;
}
