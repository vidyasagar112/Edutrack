package com.edutrack.dto.response;

import java.time.LocalDateTime;

public class CourseDocumentResponse {

    private Long id;
    private String fileName;
    private String fileType;
    private String filePath;
    private Long fileSize;
    private String uploadedBy;
    private Long courseId;
    private LocalDateTime createdAt;

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

    public String getUploadedBy() { return uploadedBy; }
    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getFileSizeFormatted() {
        if (fileSize == null) return "0 KB";
        if (fileSize < 1024) return fileSize + " B";
        if (fileSize < 1024 * 1024)
            return (fileSize / 1024) + " KB";
        return String.format("%.1f MB",
                fileSize / (1024.0 * 1024));
    }
}