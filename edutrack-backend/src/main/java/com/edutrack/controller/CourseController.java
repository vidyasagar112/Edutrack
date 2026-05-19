package com.edutrack.controller;

import java.io.IOException;
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
import org.springframework.web.multipart.MultipartFile;

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

    @GetMapping
    public ResponseEntity<ApiResponse<List<CourseResponse>>>
            getAllPublished() {
        return ResponseEntity.ok(ApiResponse.success(
                courseService.getAllPublishedCourses()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CourseResponse>> getCourseById(
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(
                courseService.getCourseById(id)));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<CourseResponse>>> search(
            @RequestParam String keyword) {
        return ResponseEntity.ok(ApiResponse.success(
                courseService.searchCourses(keyword)));
    }

    @GetMapping("/my-courses")
    @PreAuthorize("hasAnyAuthority('ROLE_INSTRUCTOR','ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<List<CourseResponse>>> getMyCourses(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.success(
                courseService.getCoursesByInstructor(
                        userDetails.getUsername())));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_INSTRUCTOR','ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<CourseResponse>> createCourse(
            @Valid @RequestBody CourseRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.success(
                "Course created successfully",
                courseService.createCourse(
                        request, userDetails.getUsername())));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_INSTRUCTOR','ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<CourseResponse>> updateCourse(
            @PathVariable Long id,
            @Valid @RequestBody CourseRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.success(
                "Course updated successfully",
                courseService.updateCourse(
                        id, request, userDetails.getUsername())));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_INSTRUCTOR','ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteCourse(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        courseService.deleteCourse(id, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success(
                "Course deleted successfully", null));
    }
    
 // Add these endpoints

 // GET /api/courses/search-filter
 @GetMapping("/search-filter")
 public ResponseEntity<ApiResponse<List<CourseResponse>>>
         searchWithFilters(
         @RequestParam(required = false) String keyword,
         @RequestParam(required = false) String category,
         @RequestParam(required = false) String subject) {
     return ResponseEntity.ok(ApiResponse.success(
             courseService.searchWithFilters(
                     keyword, category, subject)));
 }

 // GET /api/courses/categories
 @GetMapping("/categories")
 public ResponseEntity<ApiResponse<List<String>>>
         getCategories() {
     return ResponseEntity.ok(ApiResponse.success(
             courseService.getCategories()));
 }

 // GET /api/courses/subjects
 @GetMapping("/subjects")
 public ResponseEntity<ApiResponse<List<String>>>
         getSubjects() {
     return ResponseEntity.ok(ApiResponse.success(
             courseService.getSubjects()));
 }

 // POST /api/courses/{id}/thumbnail
 @PostMapping("/{id}/thumbnail")
 @PreAuthorize("hasAnyAuthority('ROLE_INSTRUCTOR'," +
               "'ROLE_ADMIN')")
 public ResponseEntity<ApiResponse<Void>>
         uploadThumbnail(
         @PathVariable Long id,
         @RequestParam("file") MultipartFile file,
         @AuthenticationPrincipal
         UserDetails userDetails) throws IOException {

     courseService.uploadThumbnail(
             id, file, userDetails.getUsername());
     return ResponseEntity.ok(ApiResponse.success(
             "Thumbnail uploaded!", null));
 }
}