package com.edutrack.controller;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.edutrack.dto.response.ApiResponse;
import com.edutrack.dto.response.CourseDocumentResponse;
import com.edutrack.entity.CourseDocument;
import com.edutrack.repository.CourseDocumentRepository;
import com.edutrack.service.CourseDocumentService;

@RestController
@RequestMapping("/api/documents")
public class CourseDocumentController {

    @Autowired
    private CourseDocumentService documentService;

    @Autowired
    private CourseDocumentRepository documentRepository;

    @Value("${app.upload.documents}")
    private String documentUploadDir;

    @PostMapping("/upload/{courseId}")
    @PreAuthorize("hasAnyAuthority('ROLE_INSTRUCTOR', 'ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<CourseDocumentResponse>> uploadDocument(
            @PathVariable Long courseId,
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal UserDetails userDetails)
            throws IOException {

        CourseDocumentResponse result = documentService.uploadDocument(
                courseId, file, userDetails.getUsername());

        return ResponseEntity.ok(
                ApiResponse.success("Document uploaded successfully", result));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<ApiResponse<List<CourseDocumentResponse>>> getDocuments(
            @PathVariable Long courseId) {

        List<CourseDocumentResponse> docs =
                documentService.getDocumentsByCourse(courseId);

        return ResponseEntity.ok(ApiResponse.success(docs));
    }

    @GetMapping("/download/{documentId}")
    public ResponseEntity<Resource> downloadDocument(
            @PathVariable Long documentId)
            throws IOException {

        CourseDocument document = documentRepository
                .findById(documentId)
                .orElseThrow(() -> new RuntimeException(
                        "Document not found: " + documentId));

        Path filePath = Paths.get(documentUploadDir)
                .resolve(document.getFilePath())
                .normalize();

        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists()) {
            throw new RuntimeException(
                    "File not found: " + document.getFilePath());
        }

        String contentType = document.getFileType() != null
                ? document.getFileType()
                : "application/octet-stream";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\""
                                + document.getFileName() + "\"")
                .body(resource);
    }

    @DeleteMapping("/{documentId}")
    @PreAuthorize("hasAnyAuthority('ROLE_INSTRUCTOR', 'ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteDocument(
            @PathVariable Long documentId,
            @AuthenticationPrincipal UserDetails userDetails) {

        documentService.deleteDocument(documentId, userDetails.getUsername());

        return ResponseEntity.ok(
                ApiResponse.success("Document deleted successfully", null));
    }
}