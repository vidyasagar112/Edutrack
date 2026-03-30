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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.edutrack.dto.request.CourseRequest;
import com.edutrack.dto.response.ApiResponse;
import com.edutrack.dto.response.CourseResponse;
import com.edutrack.service.CourseService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    // GET all published — public
    @GetMapping
    public ResponseEntity<ApiResponse<List<CourseResponse>>> getAllPublished() {
        return ResponseEntity.ok(
                ApiResponse.success(courseService.getAllPublishedCourses()));
    }

    // GET by id — public
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CourseResponse>> getCourseById(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.success(courseService.getCourseById(id)));
    }

    // GET search — public
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<CourseResponse>>> search(
            @RequestParam String keyword) {
        return ResponseEntity.ok(
                ApiResponse.success(courseService.searchCourses(keyword)));
    }

    // GET my courses — instructor only
    @GetMapping("/my-courses")
    @PreAuthorize("hasAnyRole('INSTRUCTOR','ADMIN')")
    public ResponseEntity<ApiResponse<List<CourseResponse>>> getMyCourses(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.success(
                courseService.getCoursesByInstructor(
                        userDetails.getUsername())));
    }

    // POST create — instructor only
    @PostMapping
    @PreAuthorize("hasAnyRole('INSTRUCTOR','ADMIN')")
    public ResponseEntity<ApiResponse<CourseResponse>> createCourse(
            @Valid @RequestBody CourseRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.success(
                "Course created successfully",
                courseService.createCourse(
                        request, userDetails.getUsername())));
    }

    // PUT update — instructor only
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('INSTRUCTOR','ADMIN')")
    public ResponseEntity<ApiResponse<CourseResponse>> updateCourse(
            @PathVariable Long id,
            @Valid @RequestBody CourseRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.success(
                "Course updated successfully",
                courseService.updateCourse(
                        id, request, userDetails.getUsername())));
    }

    // DELETE — instructor only
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('INSTRUCTOR','ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteCourse(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        courseService.deleteCourse(id, userDetails.getUsername());
        return ResponseEntity.ok(
                ApiResponse.success("Course deleted successfully", null));
    }
}