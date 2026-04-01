package com.edutrack.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edutrack.dto.response.AnalyticsResponse;
import com.edutrack.dto.response.ApiResponse;
import com.edutrack.service.AnalyticsService;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    // GET /api/analytics/my — student sees own analytics
    @GetMapping("/my")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<AnalyticsResponse>> getMyAnalytics(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.success(
                analyticsService.getMyAnalytics(
                        userDetails.getUsername())));
    }

    // GET /api/analytics/student/{id} — instructor/admin sees student analytics
    @GetMapping("/student/{id}")
    @PreAuthorize("hasAnyRole('INSTRUCTOR','ADMIN')")
    public ResponseEntity<ApiResponse<AnalyticsResponse>> getStudentAnalytics(
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(
                analyticsService.getStudentAnalytics(id)));
    }
}