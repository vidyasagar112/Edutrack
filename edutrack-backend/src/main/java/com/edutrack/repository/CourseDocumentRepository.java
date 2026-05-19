package com.edutrack.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.edutrack.entity.CourseDocument;

@Repository
public interface CourseDocumentRepository
        extends JpaRepository<CourseDocument, Long> {

    List<CourseDocument> findByCourseId(Long courseId);

    void deleteByCourseId(Long courseId);
}