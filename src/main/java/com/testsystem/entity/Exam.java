package com.testsystem.entity;

import java.time.LocalDateTime;

public class Exam {

    private Long id;

    private String title;
    private String description;

    private LocalDateTime examStartTime;
    private LocalDateTime examEndTime;
    private String location;

    private LocalDateTime signupStartTime;
    private LocalDateTime signupEndTime;

    private Integer maxCandidates;

    private Long createdBy;
    private String status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // ===== getter / setter =====

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getExamStartTime() {
        return examStartTime;
    }

    public void setExamStartTime(LocalDateTime examStartTime) {
        this.examStartTime = examStartTime;
    }

    public LocalDateTime getExamEndTime() {
        return examEndTime;
    }

    public void setExamEndTime(LocalDateTime examEndTime) {
        this.examEndTime = examEndTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDateTime getSignupStartTime() {
        return signupStartTime;
    }

    public void setSignupStartTime(LocalDateTime signupStartTime) {
        this.signupStartTime = signupStartTime;
    }

    public LocalDateTime getSignupEndTime() {
        return signupEndTime;
    }

    public void setSignupEndTime(LocalDateTime signupEndTime) {
        this.signupEndTime = signupEndTime;
    }

    public Integer getMaxCandidates() {
        return maxCandidates;
    }

    public void setMaxCandidates(Integer maxCandidates) {
        this.maxCandidates = maxCandidates;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
