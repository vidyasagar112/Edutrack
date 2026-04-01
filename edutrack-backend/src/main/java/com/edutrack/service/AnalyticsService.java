package com.edutrack.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edutrack.dto.response.AnalyticsResponse;
import com.edutrack.dto.response.AnalyticsResponse.QuizPerformanceDetail;
import com.edutrack.dto.response.AnalyticsResponse.SubjectPerformance;
import com.edutrack.entity.QuizAttempt;
import com.edutrack.entity.User;
import com.edutrack.exception.ResourceNotFoundException;
import com.edutrack.repository.QuizAttemptRepository;
import com.edutrack.repository.UserRepository;

@Service
@Transactional
public class AnalyticsService {

    @Autowired private UserRepository        userRepository;
    @Autowired private QuizAttemptRepository quizAttemptRepository;

    public AnalyticsResponse getMyAnalytics(String studentEmail) {
        User student = userRepository.findByEmail(studentEmail)
                .orElseThrow(() ->
                    new ResourceNotFoundException("User", "email",
                            studentEmail));
        return buildAnalytics(student);
    }

    public AnalyticsResponse getStudentAnalytics(Long studentId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() ->
                    new ResourceNotFoundException("User", "id", studentId));
        return buildAnalytics(student);
    }

    private AnalyticsResponse buildAnalytics(User student) {

        List<QuizAttempt> attempts = quizAttemptRepository
                .findByStudentId(student.getId());

        if (attempts.isEmpty()) {
            return new AnalyticsResponse(
                    student.getId(),
                    student.getFullName(),
                    student.getEmail(),
                    0, 0, 0, 0, 0, 0, 0,
                    new ArrayList<>(),
                    new ArrayList<>(),
                    List.of("Start attempting quizzes to see your analytics!")
            );
        }

        int total   = attempts.size();
        int highest = attempts.stream()
                .mapToInt(QuizAttempt::getScore).max().orElse(0);
        int lowest  = attempts.stream()
                .mapToInt(QuizAttempt::getScore).min().orElse(0);

        double avgScore = attempts.stream()
                .mapToInt(QuizAttempt::getScore)
                .average().orElse(0);

        double avgPercent = attempts.stream()
                .mapToDouble(a -> a.getTotalQuestions() > 0
                        ? (a.getScore() * 100.0 / a.getTotalQuestions())
                        : 0)
                .average().orElse(0);

        int passed = (int) attempts.stream()
                .filter(a -> a.getTotalQuestions() > 0
                        && (a.getScore() * 100.0
                            / a.getTotalQuestions()) >= 50)
                .count();
        int failed = total - passed;

        List<QuizPerformanceDetail> quizDetails = attempts.stream()
                .map(a -> new QuizPerformanceDetail(
                        a.getQuiz().getId(),
                        a.getQuiz().getTitle(),
                        a.getQuiz().getCourse().getSubject(),
                        a.getScore(),
                        a.getTotalQuestions()
                ))
                .collect(Collectors.toList());

        Map<String, List<QuizAttempt>> bySubject = attempts.stream()
                .collect(Collectors.groupingBy(
                        a -> a.getQuiz().getCourse().getSubject()));

        List<SubjectPerformance> subjectPerformances =
                bySubject.entrySet().stream()
                .map(entry -> {
                    double subAvg = entry.getValue().stream()
                            .mapToDouble(a -> a.getTotalQuestions() > 0
                                    ? (a.getScore() * 100.0
                                        / a.getTotalQuestions()) : 0)
                            .average().orElse(0);
                    return new SubjectPerformance(
                            entry.getKey(),
                            entry.getValue().size(),
                            subAvg
                    );
                })
                .collect(Collectors.toList());

        List<String> suggestions = generateSuggestions(
                subjectPerformances, avgPercent, passed, total);

        return new AnalyticsResponse(
                student.getId(),
                student.getFullName(),
                student.getEmail(),
                total,
                Math.round(avgScore * 100.0) / 100.0,
                Math.round(avgPercent * 100.0) / 100.0,
                highest,
                lowest,
                passed,
                failed,
                subjectPerformances,
                quizDetails,
                suggestions
        );
    }

    private List<String> generateSuggestions(
            List<SubjectPerformance> subjects,
            double avgPercent,
            int passed,
            int total) {

        List<String> suggestions = new ArrayList<>();

        if (avgPercent >= 75) {
            suggestions.add(
                    "Excellent performance! Keep up the great work.");
        } else if (avgPercent >= 50) {
            suggestions.add(
                    "Good performance! Focus on weak areas to improve.");
        } else {
            suggestions.add(
                    "Needs improvement. Revisit course materials.");
        }

        subjects.stream()
                .filter(s -> "WEAK".equals(s.getLevel()))
                .forEach(s -> suggestions.add(
                        "Focus more on " + s.getSubject()
                        + " — your average is "
                        + s.getAveragePercentage() + "%."));

        double passRate = total > 0
                ? (passed * 100.0 / total) : 0;
        if (passRate < 50) {
            suggestions.add(
                    "Your pass rate is low. Revise topics before attempting.");
        }

        subjects.stream()
                .filter(s -> "STRONG".equals(s.getLevel()))
                .forEach(s -> suggestions.add(
                        "Great work in " + s.getSubject()
                        + "! Scoring " + s.getAveragePercentage() + "%."));

        return suggestions;
    }
}