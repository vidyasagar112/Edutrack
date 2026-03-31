package com.edutrack.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.edutrack.entity.QuizAttempt;

@Repository
public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Long> {

    // get all quiz attempts by a student
    List<QuizAttempt> findByStudentId(Long studentId);

    // get all attempts for a specific quiz
    List<QuizAttempt> findByQuizId(Long quizId);

    // get attempts of a student for a specific quiz
    List<QuizAttempt> findByStudentIdAndQuizId(Long studentId, Long quizId);

    // count total quizzes attempted by a student
    long countByStudentId(Long studentId);
}