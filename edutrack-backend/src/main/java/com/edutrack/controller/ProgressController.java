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

    @GetMapping("/my")
    @PreAuthorize("hasAuthority('ROLE_STUDENT')")
    public ResponseEntity<ApiResponse<ProgressResponse>> getMyProgress(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.success(
                progressService.getStudentProgress(
                        userDetails.getUsername())));
    }

    @GetMapping("/student/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_INSTRUCTOR','ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<ProgressResponse>>
            getStudentProgress(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(
                progressService.getStudentProgressById(id)));
    }

    @GetMapping("/course/{courseId}")
    @PreAuthorize("hasAnyAuthority('ROLE_INSTRUCTOR','ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<List<ProgressResponse>>>
            getCourseProgress(
            @PathVariable Long courseId,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.success(
                progressService.getCourseStudentsProgress(
                        courseId, userDetails.getUsername())));
    }
}