package com.edutrack.dto.response;

import java.util.List;

public class AnalyticsResponse {

    private Long studentId;
    private String studentName;
    private String studentEmail;

    // overall stats
    private int totalQuizzesAttempted;
    private double averageScore;
    private double averagePercentage;
    private int highestScore;
    private int lowestScore;
    private int totalPassed;
    private int totalFailed;

    // per subject performance
    private List<SubjectPerformance> subjectPerformances;

    // per quiz performance
    private List<QuizPerformanceDetail> quizDetails;

    // suggestions
    private List<String> suggestions;

    // ── inner classes ──────────────────────────────────────

    public static class SubjectPerformance {
        private String subject;
        private int totalAttempts;
        private double averagePercentage;
        private String level; // STRONG | AVERAGE | WEAK

        public SubjectPerformance() {}

        public SubjectPerformance(String subject, int totalAttempts,
                                   double averagePercentage) {
            this.subject = subject;
            this.totalAttempts = totalAttempts;
            this.averagePercentage = Math.round(averagePercentage * 100.0) / 100.0;
            if (averagePercentage >= 75)       this.level = "STRONG";
            else if (averagePercentage >= 50)  this.level = "AVERAGE";
            else                               this.level = "WEAK";
        }

        public String getSubject() { return subject; }
        public void setSubject(String subject) { this.subject = subject; }

        public int getTotalAttempts() { return totalAttempts; }
        public void setTotalAttempts(int totalAttempts) { this.totalAttempts = totalAttempts; }

        public double getAveragePercentage() { return averagePercentage; }
        public void setAveragePercentage(double averagePercentage) { this.averagePercentage = averagePercentage; }

        public String getLevel() { return level; }
        public void setLevel(String level) { this.level = level; }
    }

    public static class QuizPerformanceDetail {
        private Long quizId;
        private String quizTitle;
        private String subject;
        private int score;
        private int totalQuestions;
        private double percentage;
        private String result;

        public QuizPerformanceDetail() {}

        public QuizPerformanceDetail(Long quizId, String quizTitle,
                                      String subject, int score,
                                      int totalQuestions) {
            this.quizId = quizId;
            this.quizTitle = quizTitle;
            this.subject = subject;
            this.score = score;
            this.totalQuestions = totalQuestions;
            this.percentage = totalQuestions > 0
                    ? Math.round((score * 100.0 / totalQuestions) * 100.0) / 100.0
                    : 0;
            this.result = this.percentage >= 50 ? "PASS" : "FAIL";
        }

        public Long getQuizId() { return quizId; }
        public void setQuizId(Long quizId) { this.quizId = quizId; }

        public String getQuizTitle() { return quizTitle; }
        public void setQuizTitle(String quizTitle) { this.quizTitle = quizTitle; }

        public String getSubject() { return subject; }
        public void setSubject(String subject) { this.subject = subject; }

        public int getScore() { return score; }
        public void setScore(int score) { this.score = score; }

        public int getTotalQuestions() { return totalQuestions; }
        public void setTotalQuestions(int totalQuestions) { this.totalQuestions = totalQuestions; }

        public double getPercentage() { return percentage; }
        public void setPercentage(double percentage) { this.percentage = percentage; }

        public String getResult() { return result; }
        public void setResult(String result) { this.result = result; }
    }

    // ── main class constructor ──────────────────────────────

    public AnalyticsResponse() {}

    public AnalyticsResponse(Long studentId, String studentName,
                              String studentEmail, int totalQuizzesAttempted,
                              double averageScore, double averagePercentage,
                              int highestScore, int lowestScore,
                              int totalPassed, int totalFailed,
                              List<SubjectPerformance> subjectPerformances,
                              List<QuizPerformanceDetail> quizDetails,
                              List<String> suggestions) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.studentEmail = studentEmail;
        this.totalQuizzesAttempted = totalQuizzesAttempted;
        this.averageScore = averageScore;
        this.averagePercentage = averagePercentage;
        this.highestScore = highestScore;
        this.lowestScore = lowestScore;
        this.totalPassed = totalPassed;
        this.totalFailed = totalFailed;
        this.subjectPerformances = subjectPerformances;
        this.quizDetails = quizDetails;
        this.suggestions = suggestions;
    }

    // ── getters and setters ─────────────────────────────────

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getStudentEmail() { return studentEmail; }
    public void setStudentEmail(String studentEmail) { this.studentEmail = studentEmail; }

    public int getTotalQuizzesAttempted() { return totalQuizzesAttempted; }
    public void setTotalQuizzesAttempted(int totalQuizzesAttempted) { this.totalQuizzesAttempted = totalQuizzesAttempted; }

    public double getAverageScore() { return averageScore; }
    public void setAverageScore(double averageScore) { this.averageScore = averageScore; }

    public double getAveragePercentage() { return averagePercentage; }
    public void setAveragePercentage(double averagePercentage) { this.averagePercentage = averagePercentage; }

    public int getHighestScore() { return highestScore; }
    public void setHighestScore(int highestScore) { this.highestScore = highestScore; }

    public int getLowestScore() { return lowestScore; }
    public void setLowestScore(int lowestScore) { this.lowestScore = lowestScore; }

    public int getTotalPassed() { return totalPassed; }
    public void setTotalPassed(int totalPassed) { this.totalPassed = totalPassed; }

    public int getTotalFailed() { return totalFailed; }
    public void setTotalFailed(int totalFailed) { this.totalFailed = totalFailed; }

    public List<SubjectPerformance> getSubjectPerformances() { return subjectPerformances; }
    public void setSubjectPerformances(List<SubjectPerformance> subjectPerformances) { this.subjectPerformances = subjectPerformances; }

    public List<QuizPerformanceDetail> getQuizDetails() { return quizDetails; }
    public void setQuizDetails(List<QuizPerformanceDetail> quizDetails) { this.quizDetails = quizDetails; }

    public List<String> getSuggestions() { return suggestions; }
    public void setSuggestions(List<String> suggestions) { this.suggestions = suggestions; }
}