package com.edutrack.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.edutrack.entity.Enrollment;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    // get all courses a student is enrolled in
    List<Enrollment> findByStudentId(Long studentId);

    // get all students enrolled in a course
    List<Enrollment> findByCourseId(Long courseId);

    // find specific enrollment of a student in a course
    Optional<Enrollment> findByStudentIdAndCourseId(Long studentId, Long courseId);

    // check if student is already enrolled (prevent duplicate)
    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);

    // count total students in a course
    long countByCourseId(Long courseId);
}