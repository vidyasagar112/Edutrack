package com.edutrack.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edutrack.dto.response.ProgressResponse;
import com.edutrack.dto.response.ProgressResponse.CourseProgressDetail;
import com.edutrack.entity.Enrollment;
import com.edutrack.entity.User;
import com.edutrack.exception.ResourceNotFoundException;
import com.edutrack.repository.EnrollmentRepository;
import com.edutrack.repository.QuizAttemptRepository;
import com.edutrack.repository.QuizRepository;
import com.edutrack.repository.UserRepository;

@Service
public class ProgressService {

    @Autowired private UserRepository        userRepository;
    @Autowired private EnrollmentRepository  enrollmentRepository;
    @Autowired private QuizRepository        quizRepository;
    @Autowired private QuizAttemptRepository quizAttemptRepository;

    // GET full progress of a student
    public ProgressResponse getStudentProgress(String studentEmail) {

        User student = userRepository.findByEmail(studentEmail)
                .orElseThrow(() ->
                    new ResourceNotFoundException("User", "email", studentEmail));

        List<Enrollment> enrollments = enrollmentRepository
                .findByStudentId(student.getId());

        // count by status
        int totalEnrolled = enrollments.size();
        int completed     = (int) enrollments.stream()
                .filter(e -> "COMPLETED".equals(e.getStatus())).count();
        int inProgress    = (int) enrollments.stream()
                .filter(e -> "ACTIVE".equals(e.getStatus())).count();
        int dropped       = (int) enrollments.stream()
                .filter(e -> "DROPPED".equals(e.getStatus())).count();

        // calculate overall progress
        double overallProgress = totalEnrolled > 0
                ? enrollments.stream()
                    .mapToInt(Enrollment::getProgressPercent)
                    .average()
                    .orElse(0.0)
                : 0.0;

        // build per course details
        List<CourseProgressDetail> courseDetails = enrollments.stream()
                .map(enrollment -> {
                    Long courseId = enrollment.getCourse().getId();

                    int totalQuizzes = quizRepository
                            .findByCourseId(courseId).size();

                    int attemptedQuizzes = quizRepository
                            .findByCourseId(courseId)
                            .stream()
                            .mapToInt(quiz -> quizAttemptRepository
                                    .findByStudentIdAndQuizId(
                                            student.getId(), quiz.getId())
                                    .size())
                            .sum();

                    return new CourseProgressDetail(
                            courseId,
                            enrollment.getCourse().getTitle(),
                            enrollment.getCourse().getSubject(),
                            enrollment.getCourse().getInstructor().getFullName(),
                            enrollment.getProgressPercent(),
                            enrollment.getStatus(),
                            totalQuizzes,
                            attemptedQuizzes
                    );
                })
                .collect(Collectors.toList());

        // round to 2 decimal places
        double roundedProgress = Math.round(overallProgress * 100.0) / 100.0;

        return new ProgressResponse(
                student.getId(),
                student.getFullName(),
                student.getEmail(),
                totalEnrolled,
                completed,
                inProgress,
                dropped,
                roundedProgress,
                courseDetails
        );
    }

    // GET progress of a specific student by id — instructor/admin only
    public ProgressResponse getStudentProgressById(Long studentId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() ->
                    new ResourceNotFoundException("User", "id", studentId));
        return getStudentProgress(student.getEmail());
    }

    // GET progress of all students in a course — instructor only
    // instructor variable removed — was unused
    public List<ProgressResponse> getCourseStudentsProgress(Long courseId,
                                                             String instructorEmail) {
        List<Enrollment> enrollments = enrollmentRepository
                .findByCourseId(courseId);

        return enrollments.stream()
                .map(e -> getStudentProgress(e.getStudent().getEmail()))
                .collect(Collectors.toList());
    }
}