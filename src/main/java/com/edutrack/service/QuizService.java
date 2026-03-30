package com.edutrack.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edutrack.dto.request.QuestionRequest;
import com.edutrack.dto.request.QuizAttemptRequest;
import com.edutrack.dto.request.QuizRequest;
import com.edutrack.dto.response.QuestionResponse;
import com.edutrack.dto.response.QuizAttemptResponse;
import com.edutrack.dto.response.QuizResponse;
import com.edutrack.entity.Course;
import com.edutrack.entity.Question;
import com.edutrack.entity.Quiz;
import com.edutrack.entity.QuizAttempt;
import com.edutrack.entity.User;
import com.edutrack.exception.ResourceNotFoundException;
import com.edutrack.exception.UnauthorizedException;
import com.edutrack.repository.CourseRepository;
import com.edutrack.repository.QuestionRepository;
import com.edutrack.repository.QuizAttemptRepository;
import com.edutrack.repository.QuizRepository;
import com.edutrack.repository.UserRepository;

@Service
public class QuizService {

    @Autowired private QuizRepository        quizRepository;
    @Autowired private QuestionRepository    questionRepository;
    @Autowired private QuizAttemptRepository quizAttemptRepository;
    @Autowired private CourseRepository      courseRepository;
    @Autowired private UserRepository        userRepository;

    // CREATE quiz — instructor only
    public QuizResponse createQuiz(QuizRequest request, String instructorEmail) {
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() ->
                    new ResourceNotFoundException("Course", "id", request.getCourseId()));

        if (!course.getInstructor().getEmail().equals(instructorEmail)) {
            throw new UnauthorizedException("You are not allowed to add quiz to this course");
        }

        Quiz quiz = new Quiz();
        quiz.setTitle(request.getTitle());
        quiz.setDescription(request.getDescription());
        quiz.setDifficulty(request.getDifficulty());
        quiz.setTimeLimitMinutes(request.getTimeLimitMinutes());
        quiz.setCourse(course);

        return mapToQuizResponse(quizRepository.save(quiz));
    }

    // GET all quizzes by course
    public List<QuizResponse> getQuizzesByCourse(Long courseId) {
        courseRepository.findById(courseId)
                .orElseThrow(() ->
                    new ResourceNotFoundException("Course", "id", courseId));
        return quizRepository.findByCourseId(courseId)
                .stream()
                .map(this::mapToQuizResponse)
                .collect(Collectors.toList());
    }

    // GET quiz by id
    public QuizResponse getQuizById(Long id) {
        return mapToQuizResponse(quizRepository.findById(id)
                .orElseThrow(() ->
                    new ResourceNotFoundException("Quiz", "id", id)));
    }

    // DELETE quiz — instructor only
    public void deleteQuiz(Long id, String instructorEmail) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() ->
                    new ResourceNotFoundException("Quiz", "id", id));

        if (!quiz.getCourse().getInstructor().getEmail().equals(instructorEmail)) {
            throw new UnauthorizedException("You are not allowed to delete this quiz");
        }

        quizRepository.deleteById(id);
    }

    // ADD question to quiz — instructor only
    public QuestionResponse addQuestion(Long quizId,
                                         QuestionRequest request,
                                         String instructorEmail) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() ->
                    new ResourceNotFoundException("Quiz", "id", quizId));

        if (!quiz.getCourse().getInstructor().getEmail().equals(instructorEmail)) {
            throw new UnauthorizedException("You are not allowed to add questions to this quiz");
        }

        Question question = new Question();
        question.setQuestionText(request.getQuestionText());
        question.setOptionA(request.getOptionA());
        question.setOptionB(request.getOptionB());
        question.setOptionC(request.getOptionC());
        question.setOptionD(request.getOptionD());
        question.setCorrectOption(request.getCorrectOption());
        question.setExplanation(request.getExplanation());
        question.setQuiz(quiz);

        return mapToQuestionResponse(questionRepository.save(question));
    }

    // GET all questions of a quiz
    public List<QuestionResponse> getQuestionsByQuiz(Long quizId) {
        quizRepository.findById(quizId)
                .orElseThrow(() ->
                    new ResourceNotFoundException("Quiz", "id", quizId));
        return questionRepository.findByQuizId(quizId)
                .stream()
                .map(this::mapToQuestionResponse)
                .collect(Collectors.toList());
    }

    // DELETE question — instructor only
    public void deleteQuestion(Long questionId, String instructorEmail) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() ->
                    new ResourceNotFoundException("Question", "id", questionId));

        if (!question.getQuiz().getCourse().getInstructor()
                .getEmail().equals(instructorEmail)) {
            throw new UnauthorizedException("You are not allowed to delete this question");
        }

        questionRepository.deleteById(questionId);
    }

    // SUBMIT quiz attempt — student only
    public QuizAttemptResponse submitAttempt(QuizAttemptRequest request,
                                              String studentEmail) {
        User student = userRepository.findByEmail(studentEmail)
                .orElseThrow(() ->
                    new ResourceNotFoundException("User", "email", studentEmail));

        Quiz quiz = quizRepository.findById(request.getQuizId())
                .orElseThrow(() ->
                    new ResourceNotFoundException("Quiz", "id", request.getQuizId()));

        // get all questions of this quiz
        List<Question> questions = questionRepository.findByQuizId(quiz.getId());

        // calculate score
        Map<Long, String> answers = request.getAnswers();
        int score = 0;
        for (Question q : questions) {
            String submitted = answers.get(q.getId());
            if (submitted != null && submitted.equals(q.getCorrectOption())) {
                score++;
            }
        }

        // save attempt
        QuizAttempt attempt = new QuizAttempt();
        attempt.setStudent(student);
        attempt.setQuiz(quiz);
        attempt.setScore(score);
        attempt.setTotalQuestions(questions.size());

        return mapToAttemptResponse(quizAttemptRepository.save(attempt));
    }

    // GET all attempts by student
    public List<QuizAttemptResponse> getMyAttempts(String studentEmail) {
        User student = userRepository.findByEmail(studentEmail)
                .orElseThrow(() ->
                    new ResourceNotFoundException("User", "email", studentEmail));
        return quizAttemptRepository.findByStudentId(student.getId())
                .stream()
                .map(this::mapToAttemptResponse)
                .collect(Collectors.toList());
    }

    // GET attempts for a specific quiz — instructor only
    public List<QuizAttemptResponse> getAttemptsByQuiz(Long quizId,
                                                         String instructorEmail) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() ->
                    new ResourceNotFoundException("Quiz", "id", quizId));

        if (!quiz.getCourse().getInstructor().getEmail().equals(instructorEmail)) {
            throw new UnauthorizedException("You are not allowed to view these attempts");
        }

        return quizAttemptRepository.findByQuizId(quizId)
                .stream()
                .map(this::mapToAttemptResponse)
                .collect(Collectors.toList());
    }

    // MAPPERS
    private QuizResponse mapToQuizResponse(Quiz quiz) {
        int totalQuestions = (int) questionRepository.countByQuizId(quiz.getId());
        return new QuizResponse(
                quiz.getId(),
                quiz.getTitle(),
                quiz.getDescription(),
                quiz.getDifficulty(),
                quiz.getTimeLimitMinutes(),
                quiz.getCourse().getId(),
                quiz.getCourse().getTitle(),
                totalQuestions,
                quiz.getCreatedAt()
        );
    }

    private QuestionResponse mapToQuestionResponse(Question question) {
        return new QuestionResponse(
                question.getId(),
                question.getQuestionText(),
                question.getOptionA(),
                question.getOptionB(),
                question.getOptionC(),
                question.getOptionD(),
                question.getCorrectOption(),
                question.getExplanation(),
                question.getQuiz().getId()
        );
    }

    private QuizAttemptResponse mapToAttemptResponse(QuizAttempt attempt) {
        return new QuizAttemptResponse(
                attempt.getId(),
                attempt.getQuiz().getId(),
                attempt.getQuiz().getTitle(),
                attempt.getStudent().getFullName(),
                attempt.getScore(),
                attempt.getTotalQuestions(),
                attempt.getAttemptedAt()
        );
    }
}