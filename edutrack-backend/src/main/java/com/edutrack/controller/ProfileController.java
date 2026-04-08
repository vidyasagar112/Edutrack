package com.edutrack.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edutrack.dto.request.ProfileRequest;
import com.edutrack.dto.response.ApiResponse;
import com.edutrack.dto.response.ProfileResponse;
import com.edutrack.service.ProfileService;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    @Autowired private ProfileService profileService;

    // GET /api/profile/me — own profile
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<ProfileResponse>> getMyProfile(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.success(
                profileService.getMyProfile(
                        userDetails.getUsername())));
    }

    // PUT /api/profile/me — update own profile
    @PutMapping("/me")
    public ResponseEntity<ApiResponse<ProfileResponse>> updateProfile(
            @RequestBody ProfileRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.success(
                "Profile updated successfully",
                profileService.updateProfile(
                        userDetails.getUsername(), request)));
    }

    // GET /api/profile/{id} — view any student profile
    // instructor and admin can view
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_INSTRUCTOR','ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<ProfileResponse>> getProfileById(
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(
                profileService.getProfileById(id)));
    }
}