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
import org.springframework.transaction.annotation.Transactional;

import com.edutrack.dto.response.ApiResponse;
import com.edutrack.entity.User;
import com.edutrack.repository.EnrollmentRepository;
import com.edutrack.repository.QuizAttemptRepository;
import com.edutrack.repository.UserRepository;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminController {

    @Autowired private UserRepository        userRepository;
    @Autowired private EnrollmentRepository  enrollmentRepository;
    @Autowired private QuizAttemptRepository quizAttemptRepository;

    // GET all users
    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(
                ApiResponse.success("All users", users));
    }

    // GET total enrollments count
    @GetMapping("/enrollments")
    public ResponseEntity<ApiResponse<Long>> getAllEnrollments() {
        long count = enrollmentRepository.count();
        return ResponseEntity.ok(
                ApiResponse.success("Total enrollments", count));
    }

    // DELETE user — removes all related data first
    @DeleteMapping("/users/{id}")
    @Transactional
    public ResponseEntity<ApiResponse<Void>> deleteUser(
            @PathVariable Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "User not found with id: " + id));

        // Step 1 — delete quiz attempts by this student
        quizAttemptRepository.deleteAll(
                quizAttemptRepository.findByStudentId(id));

        // Step 2 — delete enrollments of this student
        enrollmentRepository.deleteAll(
                enrollmentRepository.findByStudentId(id));

        // Step 3 — clear user roles
        user.getRoles().clear();
        userRepository.save(user);

        // Step 4 — delete the user
        userRepository.deleteById(id);

        return ResponseEntity.ok(
                ApiResponse.success("User deleted successfully",
                        null));
    }
}