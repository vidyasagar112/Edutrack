package com.edutrack.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "course_ratings",
    uniqueConstraints = @UniqueConstraint(
        columnNames = {"student_id", "course_id"}))
public class CourseRating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int rating; // 1 to 5

    @Column(columnDefinition = "TEXT")
    private String review;

    @Column(name = "created_at", nullable = false,
            updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

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

    public int getRating() { return rating; }
    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReview() { return review; }
    public void setReview(String review) {
        this.review = review;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public User getStudent() { return student; }
    public void setStudent(User student) {
        this.student = student;
    }

    public Course getCourse() { return course; }
    public void setCourse(Course course) {
        this.course = course;
    }
}