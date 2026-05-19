package com.edutrack.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edutrack.dto.request.CourseRatingRequest;
import com.edutrack.dto.response.CourseRatingResponse;
import com.edutrack.entity.Course;
import com.edutrack.entity.CourseRating;
import com.edutrack.entity.User;
import com.edutrack.exception.ResourceNotFoundException;
import com.edutrack.repository.CourseRatingRepository;
import com.edutrack.repository.CourseRepository;
import com.edutrack.repository.EnrollmentRepository;
import com.edutrack.repository.UserRepository;

@Service
@Transactional
public class CourseRatingService {

    @Autowired
    private CourseRatingRepository ratingRepository;
    @Autowired private CourseRepository courseRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private EnrollmentRepository
            enrollmentRepository;

    // Add or update rating
    public CourseRatingResponse rateCourse(
            Long courseId,
            CourseRatingRequest request,
            String studentEmail) {

        User student = userRepository
                .findByEmail(studentEmail)
                .orElseThrow(() ->
                    new ResourceNotFoundException(
                            "User", "email", studentEmail));

        Course course = courseRepository
                .findById(courseId)
                .orElseThrow(() ->
                    new ResourceNotFoundException(
                            "Course", "id", courseId));

        // check if enrolled
        boolean enrolled = enrollmentRepository
                .existsByStudentIdAndCourseId(
                        student.getId(), courseId);
        if (!enrolled) {
            throw new RuntimeException(
                    "You must be enrolled to rate!");
        }

        // find existing or create new
        CourseRating rating = ratingRepository
                .findByStudentIdAndCourseId(
                        student.getId(), courseId)
                .orElse(new CourseRating());

        rating.setStudent(student);
        rating.setCourse(course);
        rating.setRating(request.getRating());
        rating.setReview(request.getReview());

        ratingRepository.save(rating);

        // update course average rating
        updateCourseRating(courseId);

        return mapToResponse(rating);
    }

    // Get all ratings for a course
    public List<CourseRatingResponse> getCourseRatings(
            Long courseId) {
        return ratingRepository.findByCourseId(courseId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Get student's rating for a course
    public CourseRatingResponse getMyRating(
            Long courseId, String studentEmail) {
        User student = userRepository
                .findByEmail(studentEmail)
                .orElseThrow(() ->
                    new ResourceNotFoundException(
                            "User", "email", studentEmail));

        return ratingRepository
                .findByStudentIdAndCourseId(
                        student.getId(), courseId)
                .map(this::mapToResponse)
                .orElse(null);
    }

    // Update average rating on course
    private void updateCourseRating(Long courseId) {
        Course course = courseRepository
                .findById(courseId).orElseThrow();

        Double avg = ratingRepository
                .getAverageRatingByCourseId(courseId);
        long count = ratingRepository
                .countByCourseId(courseId);

        course.setAverageRating(
                avg != null ?
                Math.round(avg * 10.0) / 10.0 : 0.0);
        course.setTotalRatings((int) count);
        courseRepository.save(course);
    }

    private CourseRatingResponse mapToResponse(
            CourseRating rating) {
        CourseRatingResponse res =
                new CourseRatingResponse();
        res.setId(rating.getId());
        res.setRating(rating.getRating());
        res.setReview(rating.getReview());
        res.setStudentName(
                rating.getStudent().getFullName());
        res.setCourseId(rating.getCourse().getId());
        res.setCourseTitle(
                rating.getCourse().getTitle());
        res.setCreatedAt(rating.getCreatedAt());
        return res;
    }
}