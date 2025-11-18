package com.testsystem.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class User {

    // 数据库主键，对应 t_user.id
    private Long id;

    // 登录账号 & 密码
    private String username;
    private String password;   // 现在先用明文，后面可以改成加密后的字符串

    // 角色："USER" 或 "ADMIN"
    private String role;

    // 真实信息
    private String realName;
    private String idCard;
    // "MALE" / "FEMALE" / "OTHER"
    private String gender;

    // 出生日期
    private LocalDate dateOfBirth;

    // 在读高校 & 年级
    private String university;
    private String grade;

    // 创建 / 更新时间
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // ========= Getter / Setter =========

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
