package com.edutrack.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.edutrack.entity.Course;

@Repository
public interface CourseRepository
        extends JpaRepository<Course, Long> {

    List<Course> findByInstructorId(Long instructorId);

    List<Course> findByPublishedTrue();

    List<Course> findBySubjectIgnoreCase(String subject);

    List<Course> findByTitleContainingIgnoreCase(
            String keyword);

    // Search with filters
    @Query("SELECT c FROM Course c WHERE " +
           "c.published = true AND " +
           "(:keyword IS NULL OR " +
           " LOWER(c.title) LIKE LOWER(CONCAT('%'," +
           "  :keyword,'%')) OR " +
           " LOWER(c.subject) LIKE LOWER(CONCAT('%'," +
           "  :keyword,'%'))) AND " +
           "(:category IS NULL OR " +
           " c.category = :category) AND " +
           "(:subject IS NULL OR " +
           " LOWER(c.subject) = LOWER(:subject))" +
           " ORDER BY c.averageRating DESC")
    List<Course> searchWithFilters(
            @Param("keyword") String keyword,
            @Param("category") String category,
            @Param("subject") String subject);

    // Get distinct categories
    @Query("SELECT DISTINCT c.category FROM Course c " +
           "WHERE c.published = true " +
           "AND c.category IS NOT NULL")
    List<String> findDistinctCategories();

    // Get distinct subjects
    @Query("SELECT DISTINCT c.subject FROM Course c " +
           "WHERE c.published = true")
    List<String> findDistinctSubjects();
}