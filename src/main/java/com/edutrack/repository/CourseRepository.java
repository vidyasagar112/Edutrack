package com.edutrack.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.edutrack.entity.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    // get all courses by instructor
    List<Course> findByInstructorId(Long instructorId);

    // get all published courses (visible to students)
    List<Course> findByPublishedTrue();

    // search courses by subject
    List<Course> findBySubjectIgnoreCase(String subject);

    // search courses by keyword in title
    List<Course> findByTitleContainingIgnoreCase(String keyword);
}