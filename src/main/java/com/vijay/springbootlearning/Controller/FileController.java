    package com.vijay.springbootlearning.Controller;
    import org.springframework.core.io.Resource;
    import org.springframework.core.io.UrlResource;
    import org.springframework.http.HttpHeaders;
    import org.springframework.http.MediaType;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;
    import org.springframework.web.multipart.MultipartFile;

    import java.io.IOException;
    import java.nio.file.Files;
    import java.nio.file.Path;
    import java.nio.file.Paths;
    import java.util.UUID;

    @RestController
    @RequestMapping("/api/files")
    public class FileController {
        @GetMapping("/download/{fileName}")
        public ResponseEntity<Resource> downloadFile(
                @PathVariable String fileName) throws IOException {

            Path uploadPath = Paths.get(UPLOAD_DIR)
                    .toAbsolutePath()
                    .normalize();

            Path filePath = uploadPath
                    .resolve(fileName)
                    .normalize();

            if (!filePath.startsWith(uploadPath)) {
                return ResponseEntity
                        .badRequest()
                        .build();
            }

            Resource resource =
                    new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity
                        .notFound()
                        .build();
            }

            // File ka actual content type detect karo
            String contentType = Files.probeContentType(filePath);

            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity
                    .ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(
                            HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" +
                                    resource.getFilename() + "\""
                    )
                    .body(resource);
        }
        // File save hone wala folder
        private static final String UPLOAD_DIR = "uploads";

        @PostMapping("/upload")
        public String uploadFile(
                @RequestParam("file") MultipartFile file)
                throws IOException {

            // 1. Empty validation
            if (file.isEmpty()) {
                return "File cannot be empty";
            }

            // 2. File type validation
            String contentType = file.getContentType();

            if (contentType == null ||
                    !(contentType.equals("application/pdf")
                            || contentType.equals("image/jpeg")
                            || contentType.equals("image/png"))) {

                return "Only PDF, JPG and PNG files are allowed";
            }

            // 3. Size validation
            long maxFileSize = 5 * 1024 * 1024;

            if (file.getSize() > maxFileSize) {
                return "File size cannot exceed 5 MB";
            }

            // 4. Upload directory
            Path uploadPath = Paths.get(UPLOAD_DIR);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 5. Original filename
            String originalFileName = file.getOriginalFilename();

            String extension = "";

            if (originalFileName != null && originalFileName.contains(".")) {
                extension = originalFileName.substring(
                        originalFileName.lastIndexOf(".")
                );
            }

            // 6. Unique filename
            String uniqueFileName =
                    UUID.randomUUID() + extension;

            // 7. Final path
            Path filePath =
                    uploadPath.resolve(uniqueFileName);

            // 8. Save
            file.transferTo(filePath);

            return "File uploaded successfully"
                    + "\nOriginal File Name: " + originalFileName
                    + "\nSaved File Name: " + uniqueFileName
                    + "\nContent Type: " + contentType
                    + "\nSize: " + file.getSize() + " bytes";
        }
    }