package com.edutrack.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.edutrack.dto.request.LoginRequest;
import com.edutrack.dto.request.RegisterRequest;
import com.edutrack.dto.response.ApiResponse;
import com.edutrack.dto.response.AuthResponse;
import com.edutrack.service.AuthService;

import jakarta.validation.Valid;

import com.edutrack.dto.request.ForgotPasswordRequest;
import com.edutrack.dto.request.ResetPasswordRequest;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // POST /api/auth/register
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(
                ApiResponse.success("Registered successfully", response));
    }

    // POST /api/auth/login
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(
                ApiResponse.success("Login successful", response));
    }
    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Void>> forgotPassword(
            @Valid @RequestBody
            ForgotPasswordRequest request) {
        authService.forgotPassword(request.getEmail());
        return ResponseEntity.ok(ApiResponse.success(
                "Reset link sent to your email!", null));
    }

    // POST /api/auth/reset-password
    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(
            @Valid @RequestBody
            ResetPasswordRequest request) {
        authService.resetPassword(
                request.getToken(),
                request.getNewPassword());
        return ResponseEntity.ok(ApiResponse.success(
                "Password reset successfully!", null));
    }

    // GET /api/auth/verify-email?token=xxx
    @GetMapping("/verify-email")
    public ResponseEntity<ApiResponse<Void>> verifyEmail(
            @RequestParam String token) {
        authService.verifyEmail(token);
        return ResponseEntity.ok(ApiResponse.success(
                "Email verified successfully!", null));
    }

    // POST /api/auth/resend-verification
    @PostMapping("/resend-verification")
    public ResponseEntity<ApiResponse<Void>>
            resendVerification(
            @Valid @RequestBody
            ForgotPasswordRequest request) {
        authService.sendVerificationEmail(
                request.getEmail());
        return ResponseEntity.ok(ApiResponse.success(
                "Verification email sent!", null));
    }
}