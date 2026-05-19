package com.edutrack.dto.request;

import jakarta.validation.constraints.NotBlank;

public class CourseRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotBlank(message = "Subject is required")
    private String subject;

    private String thumbnailUrl;

    private boolean published;
    
    private String category;
    private String tags;

    // Getters and Setters
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
    
    public String getCategory() { return category; }
    public void setCategory(String category) {
        this.category = category;
    }

    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }
}