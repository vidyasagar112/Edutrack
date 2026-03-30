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

    // POST /api/quizzes — instructor creates quiz
    @PostMapping
    @PreAuthorize("hasAnyRole('INSTRUCTOR','ADMIN')")
    public ResponseEntity<ApiResponse<QuizResponse>> createQuiz(
            @Valid @RequestBody QuizRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.success(
                "Quiz created successfully",
                quizService.createQuiz(request, userDetails.getUsername())));
    }

    // GET /api/quizzes/course/{courseId} — get all quizzes of a course
    @GetMapping("/course/{courseId}")
    public ResponseEntity<ApiResponse<List<QuizResponse>>> getByCourse(
            @PathVariable Long courseId) {
        return ResponseEntity.ok(
                ApiResponse.success(quizService.getQuizzesByCourse(courseId)));
    }

    // GET /api/quizzes/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<QuizResponse>> getById(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.success(quizService.getQuizById(id)));
    }

    // DELETE /api/quizzes/{id} — instructor only
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('INSTRUCTOR','ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteQuiz(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        quizService.deleteQuiz(id, userDetails.getUsername());
        return ResponseEntity.ok(
                ApiResponse.success("Quiz deleted successfully", null));
    }

    // POST /api/quizzes/{quizId}/questions — add question
    @PostMapping("/{quizId}/questions")
    @PreAuthorize("hasAnyRole('INSTRUCTOR','ADMIN')")
    public ResponseEntity<ApiResponse<QuestionResponse>> addQuestion(
            @PathVariable Long quizId,
            @Valid @RequestBody QuestionRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.success(
                "Question added successfully",
                quizService.addQuestion(quizId, request, userDetails.getUsername())));
    }

    // GET /api/quizzes/{quizId}/questions
    @GetMapping("/{quizId}/questions")
    public ResponseEntity<ApiResponse<List<QuestionResponse>>> getQuestions(
            @PathVariable Long quizId) {
        return ResponseEntity.ok(
                ApiResponse.success(quizService.getQuestionsByQuiz(quizId)));
    }

    // DELETE /api/quizzes/questions/{questionId} — instructor only
    @DeleteMapping("/questions/{questionId}")
    @PreAuthorize("hasAnyRole('INSTRUCTOR','ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteQuestion(
            @PathVariable Long questionId,
            @AuthenticationPrincipal UserDetails userDetails) {
        quizService.deleteQuestion(questionId, userDetails.getUsername());
        return ResponseEntity.ok(
                ApiResponse.success("Question deleted successfully", null));
    }

    // POST /api/quizzes/attempt — student submits quiz
    @PostMapping("/attempt")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<QuizAttemptResponse>> submitAttempt(
            @Valid @RequestBody QuizAttemptRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.success(
                "Quiz submitted successfully",
                quizService.submitAttempt(request, userDetails.getUsername())));
    }

    // GET /api/quizzes/attempts/my — student sees their attempts
    @GetMapping("/attempts/my")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<List<QuizAttemptResponse>>> getMyAttempts(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
                ApiResponse.success(quizService.getMyAttempts(
                        userDetails.getUsername())));
    }

    // GET /api/quizzes/{quizId}/attempts — instructor sees all attempts
    @GetMapping("/{quizId}/attempts")
    @PreAuthorize("hasAnyRole('INSTRUCTOR','ADMIN')")
    public ResponseEntity<ApiResponse<List<QuizAttemptResponse>>> getAttemptsByQuiz(
            @PathVariable Long quizId,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
                ApiResponse.success(quizService.getAttemptsByQuiz(
                        quizId, userDetails.getUsername())));
    }
}