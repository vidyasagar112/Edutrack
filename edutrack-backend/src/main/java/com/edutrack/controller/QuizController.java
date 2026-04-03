package com.edutrack.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edutrack.dto.request.QuestionRequest;
import com.edutrack.dto.request.QuizAttemptRequest;
import com.edutrack.dto.request.QuizRequest;
import com.edutrack.dto.response.ApiResponse;
import com.edutrack.dto.response.QuestionResponse;
import com.edutrack.dto.response.QuizAttemptResponse;
import com.edutrack.dto.response.QuizResponse;
import com.edutrack.service.QuizService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    @Autowired
    private QuizService quizService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_INSTRUCTOR','ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<QuizResponse>> createQuiz(
            @Valid @RequestBody QuizRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.success(
                "Quiz created successfully",
                quizService.createQuiz(
                        request, userDetails.getUsername())));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<ApiResponse<List<QuizResponse>>> getByCourse(
            @PathVariable Long courseId) {
        return ResponseEntity.ok(ApiResponse.success(
                quizService.getQuizzesByCourse(courseId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<QuizResponse>> getById(
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(
                quizService.getQuizById(id)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_INSTRUCTOR','ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteQuiz(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        quizService.deleteQuiz(id, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success(
                "Quiz deleted successfully", null));
    }

    @PostMapping("/{quizId}/questions")
    @PreAuthorize("hasAnyAuthority('ROLE_INSTRUCTOR','ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<QuestionResponse>> addQuestion(
            @PathVariable Long quizId,
            @Valid @RequestBody QuestionRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.success(
                "Question added successfully",
                quizService.addQuestion(
                        quizId, request, userDetails.getUsername())));
    }

    @GetMapping("/{quizId}/questions")
    public ResponseEntity<ApiResponse<List<QuestionResponse>>>
            getQuestions(@PathVariable Long quizId) {
        return ResponseEntity.ok(ApiResponse.success(
                quizService.getQuestionsByQuiz(quizId)));
    }

    @DeleteMapping("/questions/{questionId}")
    @PreAuthorize("hasAnyAuthority('ROLE_INSTRUCTOR','ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteQuestion(
            @PathVariable Long questionId,
            @AuthenticationPrincipal UserDetails userDetails) {
        quizService.deleteQuestion(
                questionId, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success(
                "Question deleted successfully", null));
    }

    @PostMapping("/attempt")
    @PreAuthorize("hasAuthority('ROLE_STUDENT')")
    public ResponseEntity<ApiResponse<QuizAttemptResponse>>
            submitAttempt(
            @Valid @RequestBody QuizAttemptRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.success(
                "Quiz submitted successfully",
                quizService.submitAttempt(
                        request, userDetails.getUsername())));
    }

    @GetMapping("/attempts/my")
    @PreAuthorize("hasAuthority('ROLE_STUDENT')")
    public ResponseEntity<ApiResponse<List<QuizAttemptResponse>>>
            getMyAttempts(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.success(
                quizService.getMyAttempts(userDetails.getUsername())));
    }

    @GetMapping("/{quizId}/attempts")
    @PreAuthorize("hasAnyAuthority('ROLE_INSTRUCTOR','ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<List<QuizAttemptResponse>>>
            getAttemptsByQuiz(
            @PathVariable Long quizId,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.success(
                quizService.getAttemptsByQuiz(
                        quizId, userDetails.getUsername())));
    }
}