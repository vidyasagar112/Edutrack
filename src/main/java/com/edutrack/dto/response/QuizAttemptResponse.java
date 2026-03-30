package com.edutrack.dto.response;

import java.time.LocalDateTime;

public class QuizAttemptResponse {

    private Long id;
    private Long quizId;
    private String quizTitle;
    private String studentName;
    private int score;
    private int totalQuestions;
    private double percentage;
    private String result; // PASS | FAIL
    private LocalDateTime attemptedAt;

    public QuizAttemptResponse() {}

    public QuizAttemptResponse(Long id, Long quizId, String quizTitle,
                                String studentName, int score,
                                int totalQuestions, LocalDateTime attemptedAt) {
        this.id = id;
        this.quizId = quizId;
        this.quizTitle = quizTitle;
        this.studentName = studentName;
        this.score = score;
        this.totalQuestions = totalQuestions;
        this.percentage = totalQuestions > 0
                ? Math.round((score * 100.0 / totalQuestions) * 100.0) / 100.0
                : 0;
        this.result = this.percentage >= 50 ? "PASS" : "FAIL";
        this.attemptedAt = attemptedAt;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getQuizId() { return quizId; }
    public void setQuizId(Long quizId) { this.quizId = quizId; }

    public String getQuizTitle() { return quizTitle; }
    public void setQuizTitle(String quizTitle) { this.quizTitle = quizTitle; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public int getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(int totalQuestions) { this.totalQuestions = totalQuestions; }

    public double getPercentage() { return percentage; }
    public void setPercentage(double percentage) { this.percentage = percentage; }

    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }

    public LocalDateTime getAttemptedAt() { return attemptedAt; }
    public void setAttemptedAt(LocalDateTime attemptedAt) { this.attemptedAt = attemptedAt; }
}