package com.edutrack.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.edutrack.dto.request.EnrollmentRequest;
import com.edutrack.dto.response.ApiResponse;
import com.edutrack.dto.response.EnrollmentResponse;
import com.edutrack.service.EnrollmentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    // POST /api/enrollments — student enrolls in a course
    @PostMapping
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<EnrollmentResponse>> enroll(
            @Valid @RequestBody EnrollmentRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.success(
                "Enrolled successfully",
                enrollmentService.enroll(request, userDetails.getUsername())));
    }

    // GET /api/enrollments/my — student sees their enrollments
    @GetMapping("/my")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<List<EnrollmentResponse>>> getMyEnrollments(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.success(
                enrollmentService.getMyEnrollments(userDetails.getUsername())));
    }

    // GET /api/enrollments/course/{courseId} — instructor sees enrollments
    @GetMapping("/course/{courseId}")
    @PreAuthorize("hasAnyRole('INSTRUCTOR','ADMIN')")
    public ResponseEntity<ApiResponse<List<EnrollmentResponse>>> getByCourse(
            @PathVariable Long courseId,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.success(
                enrollmentService.getEnrollmentsByCourse(
                        courseId, userDetails.getUsername())));
    }

    // PATCH /api/enrollments/{id}/progress — student updates progress
    @PatchMapping("/{id}/progress")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<EnrollmentResponse>> updateProgress(
            @PathVariable Long id,
            @RequestParam int percent,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.success(
                "Progress updated",
                enrollmentService.updateProgress(
                        id, percent, userDetails.getUsername())));
    }

    // PATCH /api/enrollments/{id}/drop — student drops a course
    @PatchMapping("/{id}/drop")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<Void>> drop(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        enrollmentService.dropEnrollment(id, userDetails.getUsername());
        return ResponseEntity.ok(
                ApiResponse.success("Enrollment dropped", null));
    }
}