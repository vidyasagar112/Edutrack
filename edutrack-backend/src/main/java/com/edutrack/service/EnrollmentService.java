package com.edutrack.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edutrack.dto.request.EnrollmentRequest;
import com.edutrack.dto.response.EnrollmentResponse;
import com.edutrack.entity.Course;
import com.edutrack.entity.Enrollment;
import com.edutrack.entity.User;
import com.edutrack.exception.ResourceNotFoundException;
import com.edutrack.exception.UnauthorizedException;
import com.edutrack.repository.CourseRepository;
import com.edutrack.repository.EnrollmentRepository;
import com.edutrack.repository.UserRepository;

@Service
@Transactional
public class EnrollmentService {

    @Autowired private EnrollmentRepository enrollmentRepository;
    @Autowired private UserRepository       userRepository;
    @Autowired private CourseRepository     courseRepository;
    @Autowired private EmailService         emailService;

    public EnrollmentResponse enroll(EnrollmentRequest request,
                                      String studentEmail) {
        User student = userRepository.findByEmail(studentEmail)
                .orElseThrow(() ->
                    new ResourceNotFoundException("User", "email",
                            studentEmail));

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() ->
                    new ResourceNotFoundException("Course", "id",
                            request.getCourseId()));

        if (enrollmentRepository.existsByStudentIdAndCourseId(
                student.getId(), course.getId())) {
            throw new UnauthorizedException(
                    "You are already enrolled in this course");
        }

        if (!course.isPublished()) {
            throw new UnauthorizedException(
                    "This course is not published yet");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setStatus("ACTIVE");
        enrollment.setProgressPercent(0);

        Enrollment saved = enrollmentRepository.save(enrollment);

        emailService.sendEnrollmentEmail(
                student.getEmail(),
                student.getFullName(),
                course.getTitle(),
                course.getInstructor().getFullName()
        );

        return mapToResponse(saved);
    }

    public List<EnrollmentResponse> getMyEnrollments(String studentEmail) {
        User student = userRepository.findByEmail(studentEmail)
                .orElseThrow(() ->
                    new ResourceNotFoundException("User", "email",
                            studentEmail));
        return enrollmentRepository.findByStudentId(student.getId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<EnrollmentResponse> getEnrollmentsByCourse(
            Long courseId, String instructorEmail) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() ->
                    new ResourceNotFoundException("Course", "id",
                            courseId));

        if (!course.getInstructor().getEmail().equals(instructorEmail)) {
            throw new UnauthorizedException(
                    "You are not allowed to view enrollments for this course");
        }

        return enrollmentRepository.findByCourseId(courseId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public EnrollmentResponse updateProgress(Long enrollmentId,
                                              int progressPercent,
                                              String studentEmail) {
        Enrollment enrollment = enrollmentRepository
                .findById(enrollmentId)
                .orElseThrow(() ->
                    new ResourceNotFoundException("Enrollment", "id",
                            enrollmentId));

        if (!enrollment.getStudent().getEmail().equals(studentEmail)) {
            throw new UnauthorizedException(
                    "You cannot update this enrollment");
        }

        if (progressPercent < 0 || progressPercent > 100) {
            throw new IllegalArgumentException(
                    "Progress must be between 0 and 100");
        }

        enrollment.setProgressPercent(progressPercent);

        if (progressPercent == 100) {
            enrollment.setStatus("COMPLETED");
            emailService.sendCourseCompletionEmail(
                    enrollment.getStudent().getEmail(),
                    enrollment.getStudent().getFullName(),
                    enrollment.getCourse().getTitle()
            );
        }

        return mapToResponse(enrollmentRepository.save(enrollment));
    }

    public void dropEnrollment(Long enrollmentId, String studentEmail) {
        Enrollment enrollment = enrollmentRepository
                .findById(enrollmentId)
                .orElseThrow(() ->
                    new ResourceNotFoundException("Enrollment", "id",
                            enrollmentId));

        if (!enrollment.getStudent().getEmail().equals(studentEmail)) {
            throw new UnauthorizedException(
                    "You cannot drop this enrollment");
        }

        enrollment.setStatus("DROPPED");
        enrollmentRepository.save(enrollment);
    }

    private EnrollmentResponse mapToResponse(Enrollment enrollment) {
        return new EnrollmentResponse(
                enrollment.getId(),
                enrollment.getCourse().getId(),
                enrollment.getCourse().getTitle(),
                enrollment.getCourse().getSubject(),
                enrollment.getCourse().getInstructor().getFullName(),
                enrollment.getStudent().getFullName(),
                enrollment.getStudent().getEmail(),
                enrollment.getStatus(),
                enrollment.getProgressPercent(),
                enrollment.getEnrolledAt()
        );
    }
}