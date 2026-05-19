package com.edutrack.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {

    @Value("${app.upload.documents}")
    private String documentUploadDir;

    @Value("${app.upload.thumbnails}")
    private String thumbnailUploadDir;

    private static final List<String> ALLOWED_DOC_TYPES =
            Arrays.asList(
                "application/pdf",
                "application/msword",
                "application/vnd.openxmlformats-officedocument" +
                    ".wordprocessingml.document",
                "application/vnd.ms-excel",
                "application/vnd.openxmlformats-officedocument" +
                    ".spreadsheetml.sheet",
                "application/vnd.ms-powerpoint",
                "application/vnd.openxmlformats-officedocument" +
                    ".presentationml.presentation"
            );

    private static final List<String> ALLOWED_IMAGE_TYPES =
            Arrays.asList(
                "image/jpeg",
                "image/png",
                "image/webp"
            );

    // Store document file
    public String storeDocument(MultipartFile file)
            throws IOException {
        validateFileType(file, ALLOWED_DOC_TYPES);
        return storeFile(file, documentUploadDir);
    }

    // Store thumbnail image
    public String storeThumbnail(MultipartFile file)
            throws IOException {
        validateFileType(file, ALLOWED_IMAGE_TYPES);
        return storeFile(file, thumbnailUploadDir);
    }

    private String storeFile(MultipartFile file,
                               String uploadDir)
            throws IOException {
        // create directory if not exists
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // generate unique filename
        String originalName = StringUtils.cleanPath(
                file.getOriginalFilename());
        String extension = originalName.contains(".")
                ? originalName.substring(
                    originalName.lastIndexOf("."))
                : "";
        String uniqueName = UUID.randomUUID()
                .toString() + extension;

        // save file
        Path targetPath = uploadPath.resolve(uniqueName);
        Files.copy(file.getInputStream(), targetPath,
                StandardCopyOption.REPLACE_EXISTING);

        return uniqueName;
    }

    private void validateFileType(MultipartFile file,
                                   List<String> allowed) {
        String contentType = file.getContentType();
        if (contentType == null ||
                !allowed.contains(contentType)) {
            throw new RuntimeException(
                    "File type not allowed: " + contentType);
        }
    }

    public String getFileExtension(String contentType) {
        return switch (contentType) {
            case "application/pdf" -> "pdf";
            case "application/msword" -> "doc";
            case "application/vnd.openxmlformats-" +
                 "officedocument.wordprocessingml.document"
                -> "docx";
            case "application/vnd.ms-excel" -> "xls";
            case "application/vnd.openxmlformats-" +
                 "officedocument.spreadsheetml.sheet"
                -> "xlsx";
            case "application/vnd.ms-powerpoint" -> "ppt";
            case "application/vnd.openxmlformats-" +
                 "officedocument.presentationml.presentation"
                -> "pptx";
            default -> "bin";
        };
    }

    public String getFileIcon(String fileType) {
        if (fileType == null) return "📄";
        if (fileType.contains("pdf")) return "📕";
        if (fileType.contains("word") ||
            fileType.contains("msword")) return "📘";
        if (fileType.contains("excel") ||
            fileType.contains("sheet")) return "📗";
        if (fileType.contains("powerpoint") ||
            fileType.contains("presentation")) return "📙";
        return "📄";
    }
}