package com.edutrack.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edutrack.dto.response.ApiResponse;
import com.edutrack.dto.response.ProgressResponse;
import com.edutrack.service.ProgressService;

@RestController
@RequestMapping("/api/progress")
public class ProgressController {

    @Autowired
    private ProgressService progressService;

    // GET /api/progress/my — student sees own progress
    @GetMapping("/my")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<ProgressResponse>> getMyProgress(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.success(
                progressService.getStudentProgress(
                        userDetails.getUsername())));
    }

    // GET /api/progress/student/{id} — instructor/admin sees student progress
    @GetMapping("/student/{id}")
    @PreAuthorize("hasAnyRole('INSTRUCTOR','ADMIN')")
    public ResponseEntity<ApiResponse<ProgressResponse>> getStudentProgress(
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(
                progressService.getStudentProgressById(id)));
    }

    // GET /api/progress/course/{courseId} — instructor sees all students progress
    @GetMapping("/course/{courseId}")
    @PreAuthorize("hasAnyRole('INSTRUCTOR','ADMIN')")
    public ResponseEntity<ApiResponse<List<ProgressResponse>>> getCourseProgress(
            @PathVariable Long courseId,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.success(
                progressService.getCourseStudentsProgress(
                        courseId, userDetails.getUsername())));
    }
}