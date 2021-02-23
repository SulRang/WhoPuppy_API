package com.whopuppy.domain.user;

import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class Admin {
    private Long id;
    private Long user_id;
    private Long adminPartionId;
    private Long adminGradeId;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public Long getAdminPartionId() {
        return adminPartionId;
    }

    public void setAdminPartionId(Long adminPartionId) {
        this.adminPartionId = adminPartionId;
    }

    public Long getAdminGradeId() {
        return adminGradeId;
    }

    public void setAdminGradeId(Long adminGradeId) {
        this.adminGradeId = adminGradeId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}
