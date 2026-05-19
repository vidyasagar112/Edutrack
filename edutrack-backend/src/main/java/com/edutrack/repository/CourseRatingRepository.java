package com.edutrack.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.edutrack.entity.CourseRating;

@Repository
public interface CourseRatingRepository
        extends JpaRepository<CourseRating, Long> {

    List<CourseRating> findByCourseId(Long courseId);

    Optional<CourseRating> findByStudentIdAndCourseId(
            Long studentId, Long courseId);

    boolean existsByStudentIdAndCourseId(
            Long studentId, Long courseId);

    @Query("SELECT AVG(r.rating) FROM CourseRating r " +
           "WHERE r.course.id = :courseId")
    Double getAverageRatingByCourseId(Long courseId);

    long countByCourseId(Long courseId);
}