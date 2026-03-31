package com.edutrack.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edutrack.dto.request.CourseRequest;
import com.edutrack.dto.response.CourseResponse;
import com.edutrack.entity.Course;
import com.edutrack.entity.User;
import com.edutrack.exception.ResourceNotFoundException;
import com.edutrack.exception.UnauthorizedException;
import com.edutrack.repository.CourseRepository;
import com.edutrack.repository.EnrollmentRepository;
import com.edutrack.repository.UserRepository;

@Service
public class CourseService {

    @Autowired private CourseRepository     courseRepository;
    @Autowired private UserRepository       userRepository;
    @Autowired private EnrollmentRepository enrollmentRepository;

    // CREATE
    public CourseResponse createCourse(CourseRequest request, String instructorEmail) {
        User instructor = userRepository.findByEmail(instructorEmail)
                .orElseThrow(() ->
                    new ResourceNotFoundException("User", "email", instructorEmail));

        Course course = new Course();
        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setSubject(request.getSubject());
        course.setThumbnailUrl(request.getThumbnailUrl());
        course.setPublished(request.isPublished());
        course.setInstructor(instructor);

        return mapToResponse(courseRepository.save(course));
    }

    // GET ALL PUBLISHED
    public List<CourseResponse> getAllPublishedCourses() {
        return courseRepository.findByPublishedTrue()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // GET BY INSTRUCTOR
    public List<CourseResponse> getCoursesByInstructor(String instructorEmail) {
        User instructor = userRepository.findByEmail(instructorEmail)
                .orElseThrow(() ->
                    new ResourceNotFoundException("User", "email", instructorEmail));
        return courseRepository.findByInstructorId(instructor.getId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // GET BY ID
    public CourseResponse getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() ->
                    new ResourceNotFoundException("Course", "id", id));
        return mapToResponse(course);
    }

    // UPDATE
    public CourseResponse updateCourse(Long id, CourseRequest request,
                                        String instructorEmail) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() ->
                    new ResourceNotFoundException("Course", "id", id));

        if (!course.getInstructor().getEmail().equals(instructorEmail)) {
            throw new UnauthorizedException("You are not allowed to update this course");
        }

        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setSubject(request.getSubject());
        course.setThumbnailUrl(request.getThumbnailUrl());
        course.setPublished(request.isPublished());

        return mapToResponse(courseRepository.save(course));
    }

    // DELETE
    public void deleteCourse(Long id, String instructorEmail) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() ->
                    new ResourceNotFoundException("Course", "id", id));

        if (!course.getInstructor().getEmail().equals(instructorEmail)) {
            throw new UnauthorizedException("You are not allowed to delete this course");
        }

        courseRepository.deleteById(id);
    }

    // SEARCH
    public List<CourseResponse> searchCourses(String keyword) {
        return courseRepository.findByTitleContainingIgnoreCase(keyword)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // MAPPER
    private CourseResponse mapToResponse(Course course) {
        long enrollmentCount = enrollmentRepository
                .countByCourseId(course.getId());
        return new CourseResponse(
                course.getId(),
                course.getTitle(),
                course.getDescription(),
                course.getSubject(),
                course.getThumbnailUrl(),
                course.isPublished(),
                course.getInstructor().getFullName(),
                course.getInstructor().getEmail(),
                enrollmentCount,
                course.getCreatedAt()
        );
    }
}