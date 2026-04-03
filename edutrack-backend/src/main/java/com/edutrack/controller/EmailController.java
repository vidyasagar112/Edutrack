package com.edutrack.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edutrack.dto.request.EmailRequest;
import com.edutrack.dto.response.ApiResponse;
import com.edutrack.service.EmailService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send/{userId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> sendCustomEmail(
            @PathVariable Long userId,
            @Valid @RequestBody EmailRequest request) {
        emailService.sendCustomEmail(
                userId, request.getSubject(), request.getBody());
        return ResponseEntity.ok(ApiResponse.success(
                "Email sent successfully", null));
    }
}