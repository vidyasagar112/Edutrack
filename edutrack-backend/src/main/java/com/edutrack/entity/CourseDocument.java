package com.edutrack.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "course_documents")
public class CourseDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String fileType;

    @Column(nullable = false)
    private String filePath;

    @Column(nullable = false)
    private Long fileSize;

    @Column(name = "created_at", nullable = false,
            updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_by", nullable = false)
    private User uploadedBy;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() { return fileType; }
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Long getFileSize() { return fileSize; }
    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Course getCourse() { return course; }
    public void setCourse(Course course) {
        this.course = course;
    }

    public User getUploadedBy() { return uploadedBy; }
    public void setUploadedBy(User uploadedBy) {
        this.uploadedBy = uploadedBy;
    }
}