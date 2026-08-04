# File Upload in Spring Boot

## 1. What is File Upload?

File Upload ka matlab client se file receive karke backend par process/store karna.

Examples:

```text
E-commerce  → Product Images
Job Portal  → Resume PDF
Social App  → Profile Picture
Documents   → PDF / TXT / DOCX
```

Basic flow:

```text
Client / Postman
      ↓
Multipart Request
      ↓
Spring Boot
      ↓
MultipartFile
      ↓
Validation
      ↓
Storage
```

---

# 2. MultipartFile

Spring Boot mein uploaded file ko receive karne ke liye:

```java
MultipartFile
```

use hota hai.

Import:

```java
import org.springframework.web.multipart.MultipartFile;
```

Example:

```java
@PostMapping("/upload")
public String uploadFile(
        @RequestParam("file") MultipartFile file) {

    return file.getOriginalFilename();
}
```

---

# 3. `@RequestParam("file")`

```java
@RequestParam("file") MultipartFile file
```

multipart request se `file` naam ka part receive karta hai.

```text
Postman

Key = file
Type = File
      ↓
@RequestParam("file")
      ↓
MultipartFile
```

---

# 4. Useful MultipartFile Methods

Original filename:

```java
file.getOriginalFilename();
```

Content type:

```java
file.getContentType();
```

Examples:

```text
application/pdf
image/jpeg
image/png
text/plain
```

File size:

```java
file.getSize();
```

Size bytes mein milta hai.

Check empty file:

```java
file.isEmpty();
```

File bytes:

```java
file.getBytes();
```

Save file:

```java
file.transferTo(path);
```

---

# 5. Postman File Upload

Request:

```text
POST /api/files/upload
```

Postman:

```text
Body
 ↓
form-data

Key   → file
Type  → File
Value → Select File
```

Postman multipart request ka `Content-Type` aur boundary automatically generate kar sakta hai.

---

# 6. Upload Directory

Local server par files store karne ke liye:

```java
private static final String UPLOAD_DIR = "uploads";
```

Path:

```java
Path uploadPath = Paths.get(UPLOAD_DIR);
```

If folder doesn't exist:

```java
if (!Files.exists(uploadPath)) {
    Files.createDirectories(uploadPath);
}
```

Result:

```text
springboot-learning/
│
├── src/
├── pom.xml
└── uploads/
```

---

# 7. Saving File

Original filename:

```java
String originalFileName =
        file.getOriginalFilename();
```

Create path:

```java
Path filePath =
        uploadPath.resolve(originalFileName);
```

Save:

```java
file.transferTo(filePath);
```

Flow:

```text
MultipartFile
      ↓
uploads/file.pdf
      ↓
transferTo()
      ↓
Disk Storage
```

---

# 8. Problem With Original Filename

Suppose first user uploads:

```text
resume.pdf
```

Server:

```text
uploads/resume.pdf
```

Second user also uploads:

```text
resume.pdf
```

Same storage name can cause collision/overwrite.

Therefore storage filename should be unique.

---

# 9. UUID Filename

Java provides:

```java
UUID
```

Import:

```java
import java.util.UUID;
```

Generate:

```java
UUID.randomUUID();
```

Example:

```text
550e8400-e29b-41d4-a716-446655440000
```

---

# 10. Preserve File Extension

Original:

```text
resume.pdf
```

Extract extension:

```java
String extension = "";

if (originalFileName != null &&
        originalFileName.contains(".")) {

    extension = originalFileName.substring(
            originalFileName.lastIndexOf(".")
    );
}
```

Result:

```text
.pdf
```

Generate unique filename:

```java
String uniqueFileName =
        UUID.randomUUID() + extension;
```

Example:

```text
resume.pdf

↓

550e8400-e29b-41d4-a716-446655440000.pdf
```

---

# 11. Original Name vs Stored Name

```text
Original Filename
→ User/client filename
→ resume.pdf

Stored Filename
→ Server-generated unique filename
→ UUID.pdf
```

The original filename can be kept as metadata while the server uses the unique filename for storage.

---

# 12. Empty File Validation

```java
if (file.isEmpty()) {

    return ResponseEntity
            .badRequest()
            .body("File cannot be empty");
}
```

Result:

```text
400 Bad Request
```

---

# 13. File Type Validation

Get MIME type:

```java
String contentType =
        file.getContentType();
```

Allow selected types:

```java
if (contentType == null ||
        !(contentType.equals("application/pdf")
                || contentType.equals("image/jpeg")
                || contentType.equals("image/png")
                || contentType.equals("text/plain"))) {

    return ResponseEntity
            .badRequest()
            .body(
                "Only PDF, JPG, PNG and TXT files are allowed"
            );
}
```

Allowed:

```text
application/pdf → PDF

image/jpeg
→ JPG / JPEG

image/png
→ PNG

text/plain
→ TXT
```

---

# 14. Adding Another File Type

If another type needs to be allowed, its accepted MIME type can be added.

Example for TXT:

```java
|| contentType.equals("text/plain")
```

So validation rules can be changed according to application requirements.

---

# 15. File Size Validation

Suppose maximum size:

```text
5 MB
```

Calculation:

```text
1 KB = 1024 bytes

1 MB = 1024 × 1024 bytes

5 MB = 5 × 1024 × 1024 bytes
```

Code:

```java
long maxFileSize =
        5 * 1024 * 1024;
```

Check:

```java
if (file.getSize() > maxFileSize) {

    return ResponseEntity
            .status(HttpStatus.PAYLOAD_TOO_LARGE)
            .body("File size cannot exceed 5 MB");
}
```

Result:

```text
413 Payload Too Large
```

---

# 16. Correct Validation Order

Validate before saving:

```text
MultipartFile
      ↓
Empty Check
      ↓
File Type Check
      ↓
File Size Check
      ↓
Create Upload Directory
      ↓
Generate UUID
      ↓
Create File Path
      ↓
Save File
```

Invalid files should not reach the storage step.

---

# 17. ResponseEntity With File Upload

Instead of:

```java
return "Invalid file";
```

we can return appropriate HTTP status codes.

Method:

```java
public ResponseEntity<String> uploadFile(...)
```

Successful upload:

```java
return ResponseEntity
        .status(HttpStatus.CREATED)
        .body("File uploaded successfully");
```

Invalid file:

```java
return ResponseEntity
        .badRequest()
        .body("Invalid file");
```

Too large:

```java
return ResponseEntity
        .status(HttpStatus.PAYLOAD_TOO_LARGE)
        .body("File size cannot exceed 5 MB");
```

---

# 18. Important HTTP Status Codes

```text
201 Created
→ File successfully stored

400 Bad Request
→ Empty / unsupported file

413 Payload Too Large
→ File exceeds allowed size
```

---

# 19. Complete Upload Example

```java
@PostMapping("/upload")
public ResponseEntity<String> uploadFile(
        @RequestParam("file") MultipartFile file)
        throws IOException {

    // Empty check
    if (file.isEmpty()) {
        return ResponseEntity
                .badRequest()
                .body("File cannot be empty");
    }

    // Content type
    String contentType =
            file.getContentType();

    if (contentType == null ||
            !(contentType.equals("application/pdf")
                    || contentType.equals("image/jpeg")
                    || contentType.equals("image/png")
                    || contentType.equals("text/plain"))) {

        return ResponseEntity
                .badRequest()
                .body(
                    "Only PDF, JPG, PNG and TXT files are allowed"
                );
    }

    // Size check
    long maxFileSize =
            5 * 1024 * 1024;

    if (file.getSize() > maxFileSize) {

        return ResponseEntity
                .status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body(
                    "File size cannot exceed 5 MB"
                );
    }

    // Upload directory
    Path uploadPath =
            Paths.get(UPLOAD_DIR);

    if (!Files.exists(uploadPath)) {
        Files.createDirectories(uploadPath);
    }

    // Original filename
    String originalFileName =
            file.getOriginalFilename();

    // Extension
    String extension = "";

    if (originalFileName != null &&
            originalFileName.contains(".")) {

        extension =
                originalFileName.substring(
                    originalFileName.lastIndexOf(".")
                );
    }

    // Unique filename
    String uniqueFileName =
            UUID.randomUUID() + extension;

    // Final path
    Path filePath =
            uploadPath.resolve(uniqueFileName);

    // Save
    file.transferTo(filePath);

    return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(
                    "File uploaded successfully"
                    + "\nOriginal File Name: "
                    + originalFileName
                    + "\nSaved File Name: "
                    + uniqueFileName
                    + "\nContent Type: "
                    + contentType
                    + "\nSize: "
                    + file.getSize()
                    + " bytes"
            );
}
```

---

# 20. Local vs Database vs Cloud Storage

## Local Storage

```text
Spring Boot
     ↓
uploads/
     ↓
Actual File
```

Good for:

```text
Learning
Development
Small/local applications
```

---

## Database Storage

File can be converted into:

```java
byte[]
```

using:

```java
file.getBytes();
```

and stored using a BLOB/LONGBLOB field.

Concept:

```text
MultipartFile
     ↓
byte[]
     ↓
Database BLOB
```

For large files, storing all file bytes directly in a relational database is often not the preferred architecture.

---

## Cloud/Object Storage

Common architecture:

```text
Client
   ↓
Spring Boot
   ↓
Cloud Object Storage
   ↓
Actual File
```

Database can store:

```text
Original Filename
Stored/Object Key
Content Type
File Size
File URL/reference
```

Conceptually:

```text
Database
→ Metadata / reference

Cloud Storage
→ Actual file
```

---

# 21. Security Note

This:

```java
file.getContentType()
```

is useful for basic validation.

However, high-security production systems should not rely only on client-supplied filename or MIME metadata.

More robust systems may inspect actual file content/signatures and apply additional security checks.

---

# 22. Real Project Flow

```text
Frontend
   ↓
Multipart Upload
   ↓
Controller
   ↓
Empty Check
   ↓
Type Validation
   ↓
Size Validation
   ↓
Storage Service
   ↓
Generate Unique Object Name
   ↓
Local / Cloud Storage
   ↓
Store Metadata
   ↓
Database
```

As the application grows, file-storage logic should generally move out of the Controller into a Service.

---

# 23. Interview Questions

## What is MultipartFile?

`MultipartFile` represents an uploaded file received through a multipart HTTP request.

## How do you get the original filename?

```java
file.getOriginalFilename();
```

## How do you get file size?

```java
file.getSize();
```

## How do you get MIME type?

```java
file.getContentType();
```

## How do you check an empty file?

```java
file.isEmpty();
```

## How do you save MultipartFile?

```java
file.transferTo(path);
```

## Why use UUID for stored filenames?

To reduce filename collisions and avoid overwriting files with the same original name.

## Why preserve extension?

So the stored filename can retain the appropriate extension such as:

```text
.pdf
.jpg
.png
.txt
```

## What is HTTP 413?

```text
413 Payload Too Large
```

It indicates that the request payload exceeds the accepted size.

---

# 24. Quick Revision

```text
MultipartFile
→ Uploaded file

@RequestParam("file")
→ Receive multipart file

getOriginalFilename()
→ Original name

getContentType()
→ MIME type

getSize()
→ Size in bytes

isEmpty()
→ Empty check

transferTo()
→ Save file

UUID.randomUUID()
→ Unique storage name

Files.createDirectories()
→ Create upload directory

201
→ Upload successful

400
→ Invalid file

413
→ File too large
```

---

# Final Mental Model

```text
             FILE
              ↓
          MultipartFile
              ↓
        ┌──── Validation ────┐
        │                    │
        ↓                    ↓
     Invalid               Valid
        │                    │
        ↓                    ↓
  400 / 413            Generate UUID
                             ↓
                         File Path
                             ↓
                       transferTo()
                             ↓
                          Storage
                             ↓
                         201 Created
```

# File Upload Completed ✅