package com.edutrack.dto.response;

import java.util.List;

public class ProgressResponse {

    private Long studentId;
    private String studentName;
    private String studentEmail;
    private int totalEnrolledCourses;
    private int completedCourses;
    private int inProgressCourses;
    private int droppedCourses;
    private double overallProgressPercent;
    private List<CourseProgressDetail> courseDetails;

    // inner class for per course progress
    public static class CourseProgressDetail {
        private Long courseId;
        private String courseTitle;
        private String courseSubject;
        private String instructorName;
        private int progressPercent;
        private String status;
        private int totalQuizzes;
        private int attemptedQuizzes;

        public CourseProgressDetail() {}

        public CourseProgressDetail(Long courseId, String courseTitle,
                                     String courseSubject, String instructorName,
                                     int progressPercent, String status,
                                     int totalQuizzes, int attemptedQuizzes) {
            this.courseId = courseId;
            this.courseTitle = courseTitle;
            this.courseSubject = courseSubject;
            this.instructorName = instructorName;
            this.progressPercent = progressPercent;
            this.status = status;
            this.totalQuizzes = totalQuizzes;
            this.attemptedQuizzes = attemptedQuizzes;
        }

        // Getters and Setters
        public Long getCourseId() { return courseId; }
        public void setCourseId(Long courseId) { this.courseId = courseId; }

        public String getCourseTitle() { return courseTitle; }
        public void setCourseTitle(String courseTitle) { this.courseTitle = courseTitle; }

        public String getCourseSubject() { return courseSubject; }
        public void setCourseSubject(String courseSubject) { this.courseSubject = courseSubject; }

        public String getInstructorName() { return instructorName; }
        public void setInstructorName(String instructorName) { this.instructorName = instructorName; }

        public int getProgressPercent() { return progressPercent; }
        public void setProgressPercent(int progressPercent) { this.progressPercent = progressPercent; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }

        public int getTotalQuizzes() { return totalQuizzes; }
        public void setTotalQuizzes(int totalQuizzes) { this.totalQuizzes = totalQuizzes; }

        public int getAttemptedQuizzes() { return attemptedQuizzes; }
        public void setAttemptedQuizzes(int attemptedQuizzes) { this.attemptedQuizzes = attemptedQuizzes; }
    }

    public ProgressResponse() {}

    public ProgressResponse(Long studentId, String studentName,
                             String studentEmail, int totalEnrolledCourses,
                             int completedCourses, int inProgressCourses,
                             int droppedCourses, double overallProgressPercent,
                             List<CourseProgressDetail> courseDetails) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.studentEmail = studentEmail;
        this.totalEnrolledCourses = totalEnrolledCourses;
        this.completedCourses = completedCourses;
        this.inProgressCourses = inProgressCourses;
        this.droppedCourses = droppedCourses;
        this.overallProgressPercent = overallProgressPercent;
        this.courseDetails = courseDetails;
    }

    // Getters and Setters
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getStudentEmail() { return studentEmail; }
    public void setStudentEmail(String studentEmail) { this.studentEmail = studentEmail; }

    public int getTotalEnrolledCourses() { return totalEnrolledCourses; }
    public void setTotalEnrolledCourses(int totalEnrolledCourses) { this.totalEnrolledCourses = totalEnrolledCourses; }

    public int getCompletedCourses() { return completedCourses; }
    public void setCompletedCourses(int completedCourses) { this.completedCourses = completedCourses; }

    public int getInProgressCourses() { return inProgressCourses; }
    public void setInProgressCourses(int inProgressCourses) { this.inProgressCourses = inProgressCourses; }

    public int getDroppedCourses() { return droppedCourses; }
    public void setDroppedCourses(int droppedCourses) { this.droppedCourses = droppedCourses; }

    public double getOverallProgressPercent() { return overallProgressPercent; }
    public void setOverallProgressPercent(double overallProgressPercent) { this.overallProgressPercent = overallProgressPercent; }

    public List<CourseProgressDetail> getCourseDetails() { return courseDetails; }
    public void setCourseDetails(List<CourseProgressDetail> courseDetails) { this.courseDetails = courseDetails; }
}