package com.edutrack.dto.response;

import java.time.LocalDateTime;

public class CourseRatingResponse {

    private Long id;
    private int rating;
    private String review;
    private String studentName;
    private Long courseId;
    private String courseTitle;
    private LocalDateTime createdAt;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getReview() { return review; }
    public void setReview(String review) { this.review = review; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getCourseTitle() { return courseTitle; }
    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}