package com.edutrack.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edutrack.dto.request.CourseRatingRequest;
import com.edutrack.dto.response.ApiResponse;
import com.edutrack.dto.response.CourseRatingResponse;
import com.edutrack.service.CourseRatingService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/ratings")
public class CourseRatingController {

    @Autowired
    private CourseRatingService ratingService;

    @PostMapping("/course/{courseId}")
    @PreAuthorize("hasAuthority('ROLE_STUDENT')")
    public ResponseEntity<ApiResponse<CourseRatingResponse>> rateCourse(
            @PathVariable Long courseId,
            @Valid @RequestBody CourseRatingRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        CourseRatingResponse result = ratingService.rateCourse(
                courseId, request, userDetails.getUsername());

        return ResponseEntity.ok(
                ApiResponse.success("Rating submitted!", result));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<ApiResponse<List<CourseRatingResponse>>> getCourseRatings(
            @PathVariable Long courseId) {

        List<CourseRatingResponse> ratings =
                ratingService.getCourseRatings(courseId);

        return ResponseEntity.ok(ApiResponse.success(ratings));
    }

    @GetMapping("/course/{courseId}/my")
    @PreAuthorize("hasAuthority('ROLE_STUDENT')")
    public ResponseEntity<ApiResponse<CourseRatingResponse>> getMyRating(
            @PathVariable Long courseId,
            @AuthenticationPrincipal UserDetails userDetails) {

        CourseRatingResponse rating = ratingService.getMyRating(
                courseId, userDetails.getUsername());

        return ResponseEntity.ok(ApiResponse.success(rating));
    }
}