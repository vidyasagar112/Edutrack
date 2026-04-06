package com.edutrack.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edutrack.dto.response.ApiResponse;
import com.edutrack.entity.User;
import com.edutrack.repository.EnrollmentRepository;
import com.edutrack.repository.UserRepository;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminController {

    @Autowired private UserRepository       userRepository;
    @Autowired private EnrollmentRepository enrollmentRepository;

    // GET all users
    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(
                ApiResponse.success("All users", users));
    }

    // GET all enrollments
    @GetMapping("/enrollments")
    public ResponseEntity<ApiResponse<Long>> getAllEnrollments() {
        long count = enrollmentRepository.count();
        return ResponseEntity.ok(
                ApiResponse.success("Total enrollments", count));
    }
    @DeleteMapping("/users/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(
            @PathVariable Long id) {
        userRepository.deleteById(id);
        return ResponseEntity.ok(
                ApiResponse.success("User deleted", null));
    }
}