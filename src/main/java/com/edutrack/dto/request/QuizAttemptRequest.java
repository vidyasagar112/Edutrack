package com.edutrack.dto.request;

import java.util.Map;
import jakarta.validation.constraints.NotNull;

public class QuizAttemptRequest {

    @NotNull(message = "Quiz ID is required")
    private Long quizId;

    // key = questionId, value = selected option (A/B/C/D)
    @NotNull(message = "Answers are required")
    private Map<Long, String> answers;

    public Long getQuizId() { return quizId; }
    public void setQuizId(Long quizId) { this.quizId = quizId; }

    public Map<Long, String> getAnswers() { return answers; }
    public void setAnswers(Map<Long, String> answers) { this.answers = answers; }
}