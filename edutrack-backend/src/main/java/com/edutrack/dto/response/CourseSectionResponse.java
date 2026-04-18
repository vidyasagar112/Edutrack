package com.edutrack.dto.response;

import java.time.LocalDateTime;

public class CourseSectionResponse {

    private Long id;
    private String title;
    private String description;
    private int sectionOrder;
    private int durationMinutes;
    private String contentUrl;
    private Long courseId;
    private String courseTitle;
    private boolean completedByStudent;
    private LocalDateTime completedAt;
    private LocalDateTime createdAt;

    public CourseSectionResponse() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) {
        this.description = description;
    }

    public int getSectionOrder() { return sectionOrder; }
    public void setSectionOrder(int sectionOrder) {
        this.sectionOrder = sectionOrder;
    }

    public int getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public String getContentUrl() { return contentUrl; }
    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getCourseTitle() { return courseTitle; }
    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public boolean isCompletedByStudent() {
        return completedByStudent;
    }
    public void setCompletedByStudent(boolean completedByStudent) {
        this.completedByStudent = completedByStudent;
    }

    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}