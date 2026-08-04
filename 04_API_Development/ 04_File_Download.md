# 04 - File Download in Spring Boot

## 1. File Download kya hai?

File Download mein server par stored file ko client ko HTTP response ke through bhejte hain.

```text
UPLOAD
Client → Spring Boot → Storage

DOWNLOAD
Client ← Spring Boot ← Storage
```

Example:

```text
uploads/
└── 1a297514-9dae-45fe-ba84-....pdf
              ↓
        Spring Boot API
              ↓
           Client
```

---

# 2. Download Endpoint

```java
@GetMapping("/download/{fileName}")
public ResponseEntity<Resource> downloadFile(
        @PathVariable String fileName) throws IOException {
}
```

Example request:

```text
GET /api/files/download/abc123.pdf
```

`fileName` ko `@PathVariable` receive karega.

---

# 3. Resource

Upload mein humne use kiya:

```java
MultipartFile
```

Download mein:

```java
Resource
```

Import:

```java
import org.springframework.core.io.Resource;
```

Mental model:

```text
Upload:
Client → MultipartFile → Server

Download:
Server → Resource → Client
```

---

# 4. UrlResource

Import:

```java
import org.springframework.core.io.UrlResource;
```

File ko Spring `Resource` ke form mein represent karne ke liye:

```java
Resource resource =
        new UrlResource(filePath.toUri());
```

Flow:

```text
Path
 ↓
URI
 ↓
UrlResource
 ↓
Resource
```

---

# 5. File Path Create Karna

```java
Path uploadPath = Paths.get(UPLOAD_DIR)
        .toAbsolutePath()
        .normalize();
```

Then requested file:

```java
Path filePath = uploadPath
        .resolve(fileName)
        .normalize();
```

Example:

```text
UPLOAD_DIR = uploads

fileName = abc123.pdf
```

Result:

```text
uploads/abc123.pdf
```

---

# 6. Path Security Check

Requested path ko verify karna important hai:

```java
if (!filePath.startsWith(uploadPath)) {

    return ResponseEntity
            .badRequest()
            .build();
}
```

Iska purpose hai ki request expected upload directory ke andar hi resolve ho.

---

# 7. File Exists Check

```java
if (!resource.exists() || !resource.isReadable()) {

    return ResponseEntity
            .notFound()
            .build();
}
```

File nahi mili:

```text
404 Not Found
```

File mili:

```text
Continue Download
```

---

# 8. Content Type Detect Karna

```java
String contentType =
        Files.probeContentType(filePath);
```

Examples:

```text
.pdf  → application/pdf

.png  → image/png

.jpg  → image/jpeg

.txt  → text/plain
```

---

# 9. Unknown Content Type

Kabhi content type detect nahi hota.

Isliye fallback:

```java
if (contentType == null) {
    contentType = "application/octet-stream";
}
```

`application/octet-stream` generic binary content type hai.

---

# 10. MediaType

Detected String MIME type ko Spring `MediaType` mein convert kar sakte hain:

```java
MediaType.parseMediaType(contentType)
```

Example:

```java
.contentType(
    MediaType.parseMediaType(contentType)
)
```

Import:

```java
import org.springframework.http.MediaType;
```

---

# 11. Content-Disposition Header

Download ke liye:

```java
.header(
    HttpHeaders.CONTENT_DISPOSITION,
    "attachment; filename=\"" +
            resource.getFilename() + "\""
)
```

HTTP response conceptually:

```text
Content-Disposition:
attachment; filename="abc123.pdf"
```

`attachment` client ko indicate karta hai ki response downloadable file hai.

---

# 12. Complete Download Endpoint

```java
@GetMapping("/download/{fileName}")
public ResponseEntity<Resource> downloadFile(
        @PathVariable String fileName)
        throws IOException {

    // Upload directory
    Path uploadPath = Paths.get(UPLOAD_DIR)
            .toAbsolutePath()
            .normalize();

    // Requested file
    Path filePath = uploadPath
            .resolve(fileName)
            .normalize();

    // Path security
    if (!filePath.startsWith(uploadPath)) {

        return ResponseEntity
                .badRequest()
                .build();
    }

    // Convert file to Resource
    Resource resource =
            new UrlResource(filePath.toUri());

    // File exists/readable check
    if (!resource.exists()
            || !resource.isReadable()) {

        return ResponseEntity
                .notFound()
                .build();
    }

    // Detect content type
    String contentType =
            Files.probeContentType(filePath);

    if (contentType == null) {
        contentType =
                "application/octet-stream";
    }

    // Return actual file
    return ResponseEntity
            .ok()
            .contentType(
                MediaType.parseMediaType(contentType)
            )
            .header(
                HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" +
                    resource.getFilename() + "\""
            )
            .body(resource);
}
```

---

# 13. Important Imports

```java
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
```

Important:

```java
org.springframework.core.io.Resource
```

use karna hai.

---

# 14. Postman Testing

Request:

```text
GET
http://localhost:8082/api/files/download/{fileName}
```

Example:

```text
GET
/api/files/download/1a297514-9dae-45fe-ba84-example.pdf
```

Successful:

```text
200 OK
```

Wrong filename:

```text
404 Not Found
```

---

# 15. Raw PDF Bytes

Agar client PDF ko raw form mein display kare to kuch aisa dikh sakta hai:

```text
%PDF-1.4
...
stream
...
```

Ye necessarily error nahi hai.

PDF binary data HTTP response mein aa raha hota hai.

Correct:

```text
Content-Type: application/pdf
```

milne par compatible client PDF ko preview/render kar sakta hai.

---

# 16. Upload + Download Complete Flow

```text
              UPLOAD

Client
  ↓
MultipartFile
  ↓
Validation
  ↓
UUID Filename
  ↓
uploads/
  ↓
File Saved


             DOWNLOAD

Client
  ↓
GET /download/{fileName}
  ↓
PathVariable
  ↓
Find File
  ↓
Resource
  ↓
Detect Content-Type
  ↓
ResponseEntity<Resource>
  ↓
Client receives file
```

---

# 17. Important Methods

### Path create

```java
Paths.get()
```

### Path normalize

```java
normalize()
```

### Combine path

```java
resolve()
```

### Convert Path to URI

```java
filePath.toUri()
```

### Check resource exists

```java
resource.exists()
```

### Check readable

```java
resource.isReadable()
```

### Get filename

```java
resource.getFilename()
```

### Detect MIME type

```java
Files.probeContentType(filePath)
```

---

# 18. Important HTTP Status

```text
200 OK
→ File successfully returned

400 Bad Request
→ Invalid/path request

404 Not Found
→ File does not exist
```

---

# 19. Interview Questions

## Q1. Spring Boot mein file download ke liye kya return kar sakte hain?

```java
ResponseEntity<Resource>
```

---

## Q2. Resource kya represent karta hai?

Server-side resource/file ko Spring abstraction ke through represent karta hai.

---

## Q3. UrlResource ka use kya hai?

URI/URL based resource ko access/represent karne ke liye.

---

## Q4. Content-Type kaise detect kiya?

```java
Files.probeContentType(filePath);
```

---

## Q5. PDF ka MIME type kya hai?

```text
application/pdf
```

---

## Q6. PNG ka MIME type?

```text
image/png
```

---

## Q7. JPEG ka MIME type?

```text
image/jpeg
```

---

## Q8. TXT ka MIME type?

```text
text/plain
```

---

## Q9. Content type detect na ho to?

```text
application/octet-stream
```

fallback use kar sakte hain.

---

## Q10. `Content-Disposition: attachment` ka kya role hai?

Client ko indicate karta hai ki response ko downloadable attachment treat kiya jana chahiye.

---

# 20. Quick Revision

```text
MultipartFile
→ Upload

Resource
→ Download

UrlResource
→ Path/URI se Resource

@PathVariable
→ Filename receive

Files.probeContentType()
→ MIME detect

MediaType
→ Response Content-Type

Content-Disposition
→ Download behavior

200
→ Success

404
→ File not found
```

---

# File Download Completed ✅