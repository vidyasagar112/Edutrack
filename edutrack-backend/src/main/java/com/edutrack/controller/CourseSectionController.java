package com.edutrack.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edutrack.dto.request.CourseSectionRequest;
import com.edutrack.dto.response.ApiResponse;
import com.edutrack.dto.response.CourseSectionResponse;
import com.edutrack.service.CourseSectionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/sections")
public class CourseSectionController {

    @Autowired
    private CourseSectionService sectionService;

    // GET all sections of a course
    @GetMapping("/course/{courseId}")
    public ResponseEntity<ApiResponse<List<CourseSectionResponse>>>
            getSections(
            @PathVariable Long courseId,
            @AuthenticationPrincipal UserDetails userDetails) {

        String email = userDetails != null
                ? userDetails.getUsername() : null;

        return ResponseEntity.ok(ApiResponse.success(
                sectionService.getSectionsByCourse(
                        courseId, email)));
    }

    // POST add section — instructor only
    @PostMapping("/course/{courseId}")
    @PreAuthorize("hasAnyAuthority('ROLE_INSTRUCTOR','ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<CourseSectionResponse>>
            addSection(
            @PathVariable Long courseId,
            @Valid @RequestBody CourseSectionRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity.ok(ApiResponse.success(
                "Section added successfully",
                sectionService.addSection(
                        courseId, request,
                        userDetails.getUsername())));
    }

    // PUT update section — instructor only
    @PutMapping("/{sectionId}")
    @PreAuthorize("hasAnyAuthority('ROLE_INSTRUCTOR','ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<CourseSectionResponse>>
            updateSection(
            @PathVariable Long sectionId,
            @Valid @RequestBody CourseSectionRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity.ok(ApiResponse.success(
                "Section updated successfully",
                sectionService.updateSection(
                        sectionId, request,
                        userDetails.getUsername())));
    }

    // DELETE section — instructor only
    @DeleteMapping("/{sectionId}")
    @PreAuthorize("hasAnyAuthority('ROLE_INSTRUCTOR','ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteSection(
            @PathVariable Long sectionId,
            @AuthenticationPrincipal UserDetails userDetails) {

        sectionService.deleteSection(
                sectionId, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success(
                "Section deleted successfully", null));
    }

    // PATCH mark section complete — student only
    @PatchMapping("/{sectionId}/complete")
    @PreAuthorize("hasAuthority('ROLE_STUDENT')")
    public ResponseEntity<ApiResponse<CourseSectionResponse>>
            markComplete(
            @PathVariable Long sectionId,
            @AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity.ok(ApiResponse.success(
                "Section marked as complete!",
                sectionService.markSectionComplete(
                        sectionId,
                        userDetails.getUsername())));
    }

    // PATCH unmark section — student only
    @PatchMapping("/{sectionId}/uncomplete")
    @PreAuthorize("hasAuthority('ROLE_STUDENT')")
    public ResponseEntity<ApiResponse<CourseSectionResponse>>
            unmarkComplete(
            @PathVariable Long sectionId,
            @AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity.ok(ApiResponse.success(
                "Section unmarked",
                sectionService.unmarkSection(
                        sectionId,
                        userDetails.getUsername())));
    }
}