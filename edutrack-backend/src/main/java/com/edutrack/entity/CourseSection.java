package com.edutrack.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "course_sections")
public class CourseSection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "section_order", nullable = false)
    private int sectionOrder;

    @Column(name = "duration_minutes")
    private int durationMinutes;

    @Column(name = "content_url")
    private String contentUrl;

    @Column(name = "created_at", nullable = false,
            updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

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

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }
}