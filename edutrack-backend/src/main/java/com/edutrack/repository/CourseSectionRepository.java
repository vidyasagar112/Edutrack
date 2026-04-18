package com.edutrack.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.edutrack.entity.CourseSection;

@Repository
public interface CourseSectionRepository
        extends JpaRepository<CourseSection, Long> {

    List<CourseSection> findByCourseIdOrderBySectionOrderAsc(
            Long courseId);

    long countByCourseId(Long courseId);
}