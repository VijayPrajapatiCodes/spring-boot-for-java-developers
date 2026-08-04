# 05 - Multipart in Spring Boot

## 1. What is Multipart?

Multipart request ka use ek hi HTTP request mein multiple types ka data send karne ke liye hota hai.

Example:

```text
Product Data + Product Image
```

Ek request:

```text
multipart/form-data
        │
        ├── product → JSON
        │
        └── image   → File
```

Real-world examples:

```text
E-commerce
→ Product details + Product image

Job Portal
→ Candidate details + Resume

Social Media
→ Post details + Image/Video

User Profile
→ User details + Profile picture
```

---

# 2. Normal JSON vs Multipart

## Normal JSON Request

```http
POST /api/products
```

Body:

```json
{
  "id": 101,
  "name": "Laptop",
  "price": 50000,
  "category": "Electronics",
  "stock": 10
}
```

Controller:

```java
@PostMapping
public ProductRequest createProduct(
        @RequestBody ProductRequest product) {

    return product;
}
```

Flow:

```text
application/json
       ↓
@RequestBody
       ↓
ProductRequest
```

---

## Multipart Request

Suppose product data ke saath file bhi send karni hai:

```text
Product JSON + Image
```

Controller:

```java
@PostMapping("/with-image")
public ResponseEntity<String> createProductWithImage(
        @RequestPart("product") ProductRequest product,
        @RequestPart("image") MultipartFile image) {

    return ResponseEntity.ok(
            "Product Name: " + product.getName()
                    + "\nPrice: " + product.getPrice()
                    + "\nCategory: " + product.getCategory()
                    + "\nStock: " + product.getStock()
                    + "\nImage Name: " + image.getOriginalFilename()
                    + "\nImage Type: " + image.getContentType()
                    + "\nImage Size: " + image.getSize()
    );
}
```

---

# 3. `@RequestPart`

Multipart request ke individual parts ko receive karne ke liye `@RequestPart` use kar sakte hain.

Example:

```java
@RequestPart("product") ProductRequest product
```

Ye multipart request ke:

```text
product
```

part ko receive karega.

Agar us part ka Content-Type:

```text
application/json
```

hai, Spring/Jackson JSON ko:

```text
JSON
 ↓
ProductRequest
```

mein deserialize kar sakta hai.

---

# 4. Receiving File

File part:

```java
@RequestPart("image") MultipartFile image
```

Flow:

```text
image part
    ↓
MultipartFile
    ↓
Java object
```

Useful methods:

```java
image.getOriginalFilename();
image.getContentType();
image.getSize();
image.isEmpty();
image.getBytes();
```

---

# 5. Complete Multipart Flow

```text
                CLIENT
                   │
                   ↓
          multipart/form-data
                   │
          ┌────────┴────────┐
          │                 │
          ↓                 ↓
       product            image
          │                 │
 application/json      image/jpeg
          │                 │
          ↓                 ↓
       Jackson         MultipartFile
          │                 │
          ↓                 │
   ProductRequest            │
          └────────┬─────────┘
                   ↓
             Controller
```

---

# 6. Postman Configuration

Endpoint:

```text
POST
http://localhost:8082/api/products/with-image
```

Go to:

```text
Body
 ↓
form-data
```

Create two parts:

| Key | Type | Content-Type |
|---|---|---|
| product | Text | application/json |
| image | File | image/jpeg / image/png etc. |

Product value:

```json
{
  "id": 101,
  "name": "Laptop",
  "price": 50000,
  "category": "Electronics",
  "stock": 10
}
```

Image:

```text
laptop.jpg
```

---

# 7. Content-Type is Important

Overall HTTP request:

```text
Content-Type: multipart/form-data
```

But individual parts can have their own Content-Type.

Example:

```text
Entire Request
Content-Type: multipart/form-data

        │
        ├── product
        │      Content-Type: application/json
        │
        └── image
               Content-Type: image/jpeg
```

---

# 8. Important Postman Rule

Main Headers tab mein manually:

```text
Content-Type: application/json
```

set nahi karna chahiye when sending multipart form-data.

Postman ko overall request header generate karne do:

```text
Content-Type:
multipart/form-data; boundary=...
```

`boundary` multipart request ke different parts ko separate karta hai.

---

# 9. `415 Unsupported Media Type`

Practical mein hume error mila:

```text
415 Unsupported Media Type
```

Server log:

```text
Content-Type 'application/octet-stream'
is not supported
```

Reason:

```java
@RequestPart("product")
ProductRequest product
```

JSON object expect kar raha tha, lekin `product` part ko:

```text
application/json
```

Content-Type nahi mila tha.

Correct configuration:

```text
product
Content-Type → application/json
```

Then Spring samajh sakta hai:

```text
Ye JSON hai
    ↓
Jackson
    ↓
ProductRequest
```

---

# 10. Wrong Overall Content-Type

Agar entire request ko:

```text
application/json
```

bhej diya, lekin controller multipart expect kar raha hai:

```java
@RequestPart(...)
```

to request multipart ke roop mein process nahi hogi.

Wrong:

```text
Entire Request
     ↓
application/json ❌
```

Correct:

```text
Entire Request
     ↓
multipart/form-data ✅
```

---

# 11. File Content-Type

File ka Content-Type actual file ke according hona chahiye.

JPG:

```text
image/jpeg
```

PNG:

```text
image/png
```

PDF:

```text
application/pdf
```

TXT:

```text
text/plain
```

Example:

```java
image.getContentType();
```

Output:

```text
image/jpeg
```

---

# 12. `@RequestBody` vs `@RequestPart`

## `@RequestBody`

Normal request body ko Java object mein convert karta hai.

```java
@RequestBody ProductRequest product
```

Request:

```text
Content-Type: application/json
```

Flow:

```text
JSON Body
   ↓
@RequestBody
   ↓
ProductRequest
```

---

## `@RequestPart`

Multipart request ke specific part ko receive karta hai.

```java
@RequestPart("product")
ProductRequest product
```

Flow:

```text
multipart/form-data
       ↓
product part
       ↓
application/json
       ↓
ProductRequest
```

---

# 13. `@RequestParam` vs `@RequestPart`

Single file upload mein humne use kiya:

```java
@RequestParam("file")
MultipartFile file
```

Example:

```java
@PostMapping("/upload")
public String uploadFile(
        @RequestParam("file") MultipartFile file) {
}
```

Multipart JSON + File case mein:

```java
@RequestPart("product")
ProductRequest product
```

and:

```java
@RequestPart("image")
MultipartFile image
```

use kiya.

Mental model:

```text
@RequestBody
→ Normal JSON Body

@RequestParam
→ Request parameters / simple multipart values/files

@RequestPart
→ Multipart request ke individual parts
→ JSON object + File scenarios mein useful
```

---

# 14. Validation With Multipart

Existing DTO validation ko multipart JSON part ke saath bhi use kar sakte hain.

Example:

```java
@PostMapping("/with-image")
public ResponseEntity<String> createProductWithImage(

        @Valid
        @RequestPart("product")
        ProductRequest product,

        @RequestPart("image")
        MultipartFile image) {

    return ResponseEntity.ok("Success");
}
```

Flow:

```text
JSON
 ↓
ProductRequest
 ↓
@Valid
 ↓
DTO Validation
```

So existing annotations such as:

```java
@NotBlank
@Positive
@Min
```

etc. DTO par kaam kar sakti hain.

---

# 15. Multipart + File Upload

Multipart receive karne ke baad file ko save bhi kar sakte hain.

Concept:

```text
Product JSON + Image
        ↓
Multipart Request
        ↓
Controller
        │
        ├── ProductRequest
        │
        └── MultipartFile
                  ↓
            Validation
                  ↓
            Generate UUID
                  ↓
              uploads/
```

Unique filename:

```java
String uniqueFileName =
        UUID.randomUUID() + extension;
```

Save:

```java
file.transferTo(filePath);
```

Ye same file-upload logic hai jo humne previous chapter mein padha.

---

# 16. Better Endpoint Declaration

Endpoint explicitly bata sakta hai ki woh multipart request consume karta hai:

```java
@PostMapping(
        value = "/with-image",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
)
public ResponseEntity<String> createProductWithImage(
        @RequestPart("product") ProductRequest product,
        @RequestPart("image") MultipartFile image) {

    return ResponseEntity.ok("Success");
}
```

Import:

```java
import org.springframework.http.MediaType;
```

---

# 17. Real Project Architecture

Learning phase mein controller directly file handle kar sakta hai.

Larger application mein structure:

```text
Client
  ↓
ProductController
  ↓
ProductService
  │
  ├── Product Data Processing
  │
  └── File Storage Service
            ↓
       Local / Cloud
```

Example concept:

```text
POST /api/products

Product JSON
+
Product Image
      ↓
ProductController
      ↓
ProductService
      ↓
 ┌────┴─────────┐
 ↓              ↓
Database      Storage
Product       Image
Data          File
```

---

# 18. Database + File Storage Concept

Real project mein generally product record mein image ka reference store kiya ja sakta hai.

Example:

```text
products

id
name
price
category
stock
image_url / image_key
```

Actual image:

```text
Local Storage
or
Cloud/Object Storage
```

Database:

```text
Product information
+
Image reference
```

Storage:

```text
Actual Image
```

---

# 19. Common Errors

## Error 1

```text
415 Unsupported Media Type
```

Check:

```text
product Content-Type
→ application/json
```

---

## Error 2

Multipart request detect nahi ho rahi.

Check overall request:

```text
Content-Type
→ multipart/form-data
```

---

## Error 3

JSON ProductRequest mein convert nahi ho raha.

Check:

```text
JSON field names
        ↓
DTO field names
```

Example:

```json
{
  "name": "Laptop"
}
```

DTO:

```java
private String name;
```

---

## Error 4

File null/missing.

Check key:

```java
@RequestPart("image")
```

Postman key bhi exactly:

```text
image
```

hona chahiye.

---

# 20. Interview Questions

## Q1. Multipart request kya hoti hai?

Ek HTTP request jisme multiple parts/data types send kiye ja sakte hain, jaise JSON + file.

---

## Q2. Spring Boot mein multipart file kaise receive karte hain?

```java
MultipartFile
```

---

## Q3. `@RequestPart` kya karta hai?

Multipart request ke specific part ko controller parameter ke saath bind karta hai.

---

## Q4. JSON + file ek request mein kaise bhejenge?

```text
multipart/form-data
```

with:

```text
product → application/json
image   → image/jpeg
```

---

## Q5. `@RequestBody` aur `@RequestPart` mein difference?

```text
@RequestBody
→ Normal request body

@RequestPart
→ Multipart request ka specific part
```

---

## Q6. 415 ka meaning?

```text
415 Unsupported Media Type
```

Server request/part ke supplied media type ko expected format mein process nahi kar pa raha.

---

## Q7. Product JSON ka Content-Type?

```text
application/json
```

---

## Q8. Overall multipart request ka Content-Type?

```text
multipart/form-data
```

---

## Q9. Multipart ke saath DTO validation possible hai?

Yes.

```java
@Valid
@RequestPart("product")
ProductRequest product
```

---

# 21. Quick Revision

```text
multipart/form-data
→ Multiple parts in one request

@RequestPart
→ Specific multipart part receive

MultipartFile
→ Uploaded file

ProductRequest
→ JSON → Java DTO

application/json
→ JSON part

image/jpeg
→ JPEG file

image/png
→ PNG file

application/pdf
→ PDF file

415
→ Unsupported Media Type

@RequestBody
→ Normal JSON

@RequestPart
→ Multipart JSON/File
```

---

# Final Mental Model

```text
                    POST REQUEST
                         ↓
                multipart/form-data
                         ↓
              ┌──────────┴──────────┐
              ↓                     ↓
           product                 image
              ↓                     ↓
      application/json          image/jpeg
              ↓                     ↓
           Jackson             MultipartFile
              ↓                     ↓
       ProductRequest               │
              │                     │
              └──────────┬──────────┘
                         ↓
                   @RequestPart
                         ↓
                    Controller
                         ↓
               Business Logic
```

# Multipart Completed ✅