package com.edutrack.dto.response;

import java.time.LocalDateTime;

public class EnrollmentResponse {

    private Long id;
    private Long courseId;
    private String courseTitle;
    private String courseSubject;
    private String instructorName;
    private String studentName;
    private String studentEmail;
    private String status;
    private int progressPercent;
    private LocalDateTime enrolledAt;

    public EnrollmentResponse() {}

    public EnrollmentResponse(Long id, Long courseId, String courseTitle,
                               String courseSubject, String instructorName,
                               String studentName, String studentEmail,
                               String status, int progressPercent,
                               LocalDateTime enrolledAt) {
        this.id = id;
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.courseSubject = courseSubject;
        this.instructorName = instructorName;
        this.studentName = studentName;
        this.studentEmail = studentEmail;
        this.status = status;
        this.progressPercent = progressPercent;
        this.enrolledAt = enrolledAt;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }

    public String getCourseTitle() { return courseTitle; }
    public void setCourseTitle(String courseTitle) { this.courseTitle = courseTitle; }

    public String getCourseSubject() { return courseSubject; }
    public void setCourseSubject(String courseSubject) { this.courseSubject = courseSubject; }

    public String getInstructorName() { return instructorName; }
    public void setInstructorName(String instructorName) { this.instructorName = instructorName; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getStudentEmail() { return studentEmail; }
    public void setStudentEmail(String studentEmail) { this.studentEmail = studentEmail; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getProgressPercent() { return progressPercent; }
    public void setProgressPercent(int progressPercent) { this.progressPercent = progressPercent; }

    public LocalDateTime getEnrolledAt() { return enrolledAt; }
    public void setEnrolledAt(LocalDateTime enrolledAt) { this.enrolledAt = enrolledAt; }
}