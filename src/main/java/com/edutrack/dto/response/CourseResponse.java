package com.edutrack.dto.response;

import java.time.LocalDateTime;

public class CourseResponse {

    private Long id;
    private String title;
    private String description;
    private String subject;
    private String thumbnailUrl;
    private boolean published;
    private String instructorName;
    private String instructorEmail;
    private long enrollmentCount;
    private LocalDateTime createdAt;

    // Constructor
    public CourseResponse() {}

    public CourseResponse(Long id, String title, String description,
                          String subject, String thumbnailUrl,
                          boolean published, String instructorName,
                          String instructorEmail, long enrollmentCount,
                          LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.subject = subject;
        this.thumbnailUrl = thumbnailUrl;
        this.published = published;
        this.instructorName = instructorName;
        this.instructorEmail = instructorEmail;
        this.enrollmentCount = enrollmentCount;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getThumbnailUrl() { return thumbnailUrl; }
    public void setThumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; }

    public boolean isPublished() { return published; }
    public void setPublished(boolean published) { this.published = published; }

    public String getInstructorName() { return instructorName; }
    public void setInstructorName(String instructorName) { this.instructorName = instructorName; }

    public String getInstructorEmail() { return instructorEmail; }
    public void setInstructorEmail(String instructorEmail) { this.instructorEmail = instructorEmail; }

    public long getEnrollmentCount() { return enrollmentCount; }
    public void setEnrollmentCount(long enrollmentCount) { this.enrollmentCount = enrollmentCount; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}