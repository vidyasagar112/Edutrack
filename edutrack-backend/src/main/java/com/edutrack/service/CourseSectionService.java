package com.edutrack.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edutrack.dto.request.CourseSectionRequest;
import com.edutrack.dto.response.CourseSectionResponse;
import com.edutrack.entity.Course;
import com.edutrack.entity.CourseSection;
import com.edutrack.entity.Enrollment;
import com.edutrack.entity.SectionProgress;
import com.edutrack.entity.User;
import com.edutrack.exception.ResourceNotFoundException;
import com.edutrack.exception.UnauthorizedException;
import com.edutrack.repository.CourseSectionRepository;
import com.edutrack.repository.EnrollmentRepository;
import com.edutrack.repository.CourseRepository;
import com.edutrack.repository.SectionProgressRepository;
import com.edutrack.repository.UserRepository;

@Service
@Transactional
public class CourseSectionService {

    @Autowired private CourseSectionRepository sectionRepository;
    @Autowired private SectionProgressRepository progressRepository;
    @Autowired private CourseRepository courseRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private EnrollmentRepository enrollmentRepository;

    // ── INSTRUCTOR: Add section to course ──────────────
    public CourseSectionResponse addSection(
            Long courseId,
            CourseSectionRequest request,
            String instructorEmail) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() ->
                    new ResourceNotFoundException(
                            "Course", "id", courseId));

        if (!course.getInstructor().getEmail()
                .equals(instructorEmail)) {
            throw new UnauthorizedException(
                    "You are not allowed to add sections");
        }

        CourseSection section = new CourseSection();
        section.setTitle(request.getTitle());
        section.setDescription(request.getDescription());
        section.setSectionOrder(request.getSectionOrder());
        section.setDurationMinutes(request.getDurationMinutes());
        section.setContentUrl(request.getContentUrl());
        section.setCourse(course);

        return mapToResponse(
                sectionRepository.save(section), null);
    }

    // ── INSTRUCTOR: Update section ──────────────────────
    public CourseSectionResponse updateSection(
            Long sectionId,
            CourseSectionRequest request,
            String instructorEmail) {

        CourseSection section = sectionRepository
                .findById(sectionId)
                .orElseThrow(() ->
                    new ResourceNotFoundException(
                            "Section", "id", sectionId));

        if (!section.getCourse().getInstructor().getEmail()
                .equals(instructorEmail)) {
            throw new UnauthorizedException(
                    "You are not allowed to update this section");
        }

        section.setTitle(request.getTitle());
        section.setDescription(request.getDescription());
        section.setSectionOrder(request.getSectionOrder());
        section.setDurationMinutes(request.getDurationMinutes());
        section.setContentUrl(request.getContentUrl());

        return mapToResponse(
                sectionRepository.save(section), null);
    }

    // ── INSTRUCTOR: Delete section ──────────────────────
    public void deleteSection(Long sectionId,
                               String instructorEmail) {
        CourseSection section = sectionRepository
                .findById(sectionId)
                .orElseThrow(() ->
                    new ResourceNotFoundException(
                            "Section", "id", sectionId));

        if (!section.getCourse().getInstructor().getEmail()
                .equals(instructorEmail)) {
            throw new UnauthorizedException(
                    "You are not allowed to delete this section");
        }

        sectionRepository.deleteById(sectionId);
    }

    // ── GET all sections of a course ───────────────────
    public List<CourseSectionResponse> getSectionsByCourse(
            Long courseId, String studentEmail) {

        List<CourseSection> sections = sectionRepository
                .findByCourseIdOrderBySectionOrderAsc(courseId);

        User student = studentEmail != null
                ? userRepository.findByEmail(studentEmail)
                    .orElse(null)
                : null;

        return sections.stream()
                .map(section -> {
                    SectionProgress sp = student != null
                            ? progressRepository
                                .findByStudentIdAndSectionId(
                                        student.getId(),
                                        section.getId())
                                .orElse(null)
                            : null;
                    return mapToResponse(section, sp);
                })
                .collect(Collectors.toList());
    }

    // ── STUDENT: Mark section as complete ──────────────
    public CourseSectionResponse markSectionComplete(
            Long sectionId, String studentEmail) {

        User student = userRepository.findByEmail(studentEmail)
                .orElseThrow(() ->
                    new ResourceNotFoundException(
                            "User", "email", studentEmail));

        CourseSection section = sectionRepository
                .findById(sectionId)
                .orElseThrow(() ->
                    new ResourceNotFoundException(
                            "Section", "id", sectionId));

        // find or create progress record
        SectionProgress sp = progressRepository
                .findByStudentIdAndSectionId(
                        student.getId(), sectionId)
                .orElse(new SectionProgress());

        sp.setStudent(student);
        sp.setSection(section);
        sp.setCompleted(true);
        progressRepository.save(sp);

        // auto update enrollment progress
        updateEnrollmentProgress(
                student.getId(),
                section.getCourse().getId());

        return mapToResponse(section, sp);
    }

    // ── STUDENT: Unmark section ─────────────────────────
    public CourseSectionResponse unmarkSection(
            Long sectionId, String studentEmail) {

        User student = userRepository.findByEmail(studentEmail)
                .orElseThrow(() ->
                    new ResourceNotFoundException(
                            "User", "email", studentEmail));

        CourseSection section = sectionRepository
                .findById(sectionId)
                .orElseThrow(() ->
                    new ResourceNotFoundException(
                            "Section", "id", sectionId));

        progressRepository.findByStudentIdAndSectionId(
                student.getId(), sectionId)
                .ifPresent(sp -> {
                    sp.setCompleted(false);
                    sp.setCompletedAt(null);
                    progressRepository.save(sp);
                });

        // update enrollment progress
        updateEnrollmentProgress(
                student.getId(),
                section.getCourse().getId());

        return mapToResponse(section, null);
    }

    // ── Auto update enrollment progress % ──────────────
    private void updateEnrollmentProgress(
            Long studentId, Long courseId) {

        long totalSections = sectionRepository
                .countByCourseId(courseId);

        if (totalSections == 0) return;

        long completedSections = progressRepository
                .countByStudentIdAndSectionCourseIdAndCompletedTrue(
                        studentId, courseId);

        int progressPercent = (int) Math.round(
                (completedSections * 100.0) / totalSections);

        // update enrollment
        enrollmentRepository
                .findByStudentIdAndCourseId(studentId, courseId)
                .ifPresent(enrollment -> {
                    enrollment.setProgressPercent(progressPercent);
                    if (progressPercent == 100) {
                        enrollment.setStatus("COMPLETED");
                    } else if ("COMPLETED"
                            .equals(enrollment.getStatus())) {
                        enrollment.setStatus("ACTIVE");
                    }
                    enrollmentRepository.save(enrollment);
                });
    }

    // ── MAPPER ──────────────────────────────────────────
    private CourseSectionResponse mapToResponse(
            CourseSection section, SectionProgress sp) {

        CourseSectionResponse res = new CourseSectionResponse();
        res.setId(section.getId());
        res.setTitle(section.getTitle());
        res.setDescription(section.getDescription());
        res.setSectionOrder(section.getSectionOrder());
        res.setDurationMinutes(section.getDurationMinutes());
        res.setContentUrl(section.getContentUrl());
        res.setCourseId(section.getCourse().getId());
        res.setCourseTitle(section.getCourse().getTitle());
        res.setCreatedAt(section.getCreatedAt());
        res.setCompletedByStudent(
                sp != null && sp.isCompleted());
        res.setCompletedAt(sp != null ? sp.getCompletedAt()
                : null);
        return res;
    }
}