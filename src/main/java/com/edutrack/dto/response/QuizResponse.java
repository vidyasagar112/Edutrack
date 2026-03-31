package com.edutrack.dto.response;

import java.time.LocalDateTime;

public class QuizResponse {

    private Long id;
    private String title;
    private String description;
    private String difficulty;
    private int timeLimitMinutes;
    private Long courseId;
    private String courseTitle;
    private int totalQuestions;
    private LocalDateTime createdAt;

    public QuizResponse() {}

    public QuizResponse(Long id, String title, String description,
                        String difficulty, int timeLimitMinutes,
                        Long courseId, String courseTitle,
                        int totalQuestions, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.difficulty = difficulty;
        this.timeLimitMinutes = timeLimitMinutes;
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.totalQuestions = totalQuestions;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public int getTimeLimitMinutes() { return timeLimitMinutes; }
    public void setTimeLimitMinutes(int timeLimitMinutes) { this.timeLimitMinutes = timeLimitMinutes; }

    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }

    public String getCourseTitle() { return courseTitle; }
    public void setCourseTitle(String courseTitle) { this.courseTitle = courseTitle; }

    public int getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(int totalQuestions) { this.totalQuestions = totalQuestions; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}