package com.edutrack.service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.edutrack.dto.response.CourseDocumentResponse;
import com.edutrack.entity.Course;
import com.edutrack.entity.CourseDocument;
import com.edutrack.entity.User;
import com.edutrack.exception.ResourceNotFoundException;
import com.edutrack.exception.UnauthorizedException;
import com.edutrack.repository.CourseDocumentRepository;
import com.edutrack.repository.CourseRepository;
import com.edutrack.repository.UserRepository;

@Service
@Transactional
public class CourseDocumentService {

    @Autowired
    private CourseDocumentRepository documentRepository;
    @Autowired private CourseRepository courseRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private FileStorageService fileStorageService;

    @Value("${app.upload.documents}")
    private String documentUploadDir;

    // Upload document
    public CourseDocumentResponse uploadDocument(
            Long courseId,
            MultipartFile file,
            String instructorEmail) throws IOException {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() ->
                    new ResourceNotFoundException(
                            "Course", "id", courseId));

        if (!course.getInstructor().getEmail()
                .equals(instructorEmail)) {
            throw new UnauthorizedException(
                    "Not allowed to upload to this course");
        }

        User instructor = userRepository
                .findByEmail(instructorEmail)
                .orElseThrow(() ->
                    new ResourceNotFoundException(
                            "User", "email",
                            instructorEmail));

        String storedFileName =
                fileStorageService.storeDocument(file);

        CourseDocument document = new CourseDocument();
        document.setFileName(
                file.getOriginalFilename());
        document.setFileType(file.getContentType());
        document.setFilePath(storedFileName);
        document.setFileSize(file.getSize());
        document.setCourse(course);
        document.setUploadedBy(instructor);

        return mapToResponse(
                documentRepository.save(document));
    }

    // Get all documents for a course
    public List<CourseDocumentResponse>
            getDocumentsByCourse(Long courseId) {
        return documentRepository.findByCourseId(courseId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Delete document
    public void deleteDocument(Long documentId,
                                String instructorEmail) {
        CourseDocument document = documentRepository
                .findById(documentId)
                .orElseThrow(() ->
                    new ResourceNotFoundException(
                            "Document", "id", documentId));

        if (!document.getCourse().getInstructor()
                .getEmail().equals(instructorEmail)) {
            throw new UnauthorizedException(
                    "Not allowed to delete this document");
        }

        documentRepository.deleteById(documentId);
    }

    private CourseDocumentResponse mapToResponse(
            CourseDocument doc) {
        CourseDocumentResponse res =
                new CourseDocumentResponse();
        res.setId(doc.getId());
        res.setFileName(doc.getFileName());
        res.setFileType(doc.getFileType());
        res.setFilePath("/api/documents/download/"
                + doc.getId());
        res.setFileSize(doc.getFileSize());
        res.setUploadedBy(
                doc.getUploadedBy().getFullName());
        res.setCourseId(doc.getCourse().getId());
        res.setCreatedAt(doc.getCreatedAt());
        return res;
    }
}