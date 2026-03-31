package com.edutrack.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.edutrack.entity.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    // get all questions of a quiz
    List<Question> findByQuizId(Long quizId);

    // count total questions in a quiz
    long countByQuizId(Long quizId);
}