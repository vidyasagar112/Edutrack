package com.edutrack.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.edutrack.entity.Quiz;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {

    // get all quizzes under a course
    List<Quiz> findByCourseId(Long courseId);

    // get quizzes by difficulty under a course
    List<Quiz> findByCourseIdAndDifficulty(Long courseId, String difficulty);
}