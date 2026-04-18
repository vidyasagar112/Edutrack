package com.edutrack.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "section_progress",
    uniqueConstraints = @UniqueConstraint(
        columnNames = {"student_id", "section_id"}))
public class SectionProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id", nullable = false)
    private CourseSection section;

    @Column(name = "completed", nullable = false)
    private boolean completed = false;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        if (this.completed && this.completedAt == null) {
            this.completedAt = LocalDateTime.now();
        }
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getStudent() { return student; }
    public void setStudent(User student) {
        this.student = student;
    }

    public CourseSection getSection() { return section; }
    public void setSection(CourseSection section) {
        this.section = section;
    }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }
}