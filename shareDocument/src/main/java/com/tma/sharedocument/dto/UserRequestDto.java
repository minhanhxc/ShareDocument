/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tma.sharedocument.dto;

/**
 *
 * @author Minh Anh
 */
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserRequestDto {

    @NotBlank(message = "Họ không được để trống")
    @Size(max = 50)
    private String ho;
    @NotBlank(message = "Tên không được để trống")
    @Size(max = 50)
    private String ten;
    @NotBlank(message = "Username không được để trống")
    @Size(max = 50)
    private String username;
    @NotBlank(message = "Password không được để trống")
    @Size(min = 6, message = "Password phải có ít nhất 6 ký tự")
    private String password;
    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    @Size(max = 100)
    private String email;
    private String avatar;

    public UserRequestDto(String ho, String ten, String username, String password, String email, String avatar) {
        this.ho = ho;
        this.ten = ten;
        this.username = username;
        this.password = password;
        this.email = email;
        this.avatar = avatar;
    }

    public UserRequestDto() {
    }

    /**
     * @return the ho
     */
    public String getHo() {
        return ho;
    }

    /**
     * @param ho the ho to set
     */
    public void setHo(String ho) {
        this.ho = ho;
    }

    /**
     * @return the ten
     */
    public String getTen() {
        return ten;
    }

    /**
     * @param ten the ten to set
     */
    public void setTen(String ten) {
        this.ten = ten;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the avatar
     */
    public String getAvatar() {
        return avatar;
    }

    /**
     * @param avatar the avatar to set
     */
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
