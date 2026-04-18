package com.edutrack.dto.request;

import jakarta.validation.constraints.NotBlank;

public class CourseSectionRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;
    private int sectionOrder;
    private int durationMinutes;
    private String contentUrl;

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
}