package com.edutrack.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.edutrack.entity.SectionProgress;

@Repository
public interface SectionProgressRepository
        extends JpaRepository<SectionProgress, Long> {

    List<SectionProgress> findByStudentId(Long studentId);

    Optional<SectionProgress> findByStudentIdAndSectionId(
            Long studentId, Long sectionId);

    long countByStudentIdAndSectionCourseIdAndCompletedTrue(
            Long studentId, Long courseId);

    List<SectionProgress> findByStudentIdAndSectionCourseId(
            Long studentId, Long courseId);
}