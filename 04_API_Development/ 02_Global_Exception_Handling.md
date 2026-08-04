# Exception Handling in Spring Boot REST API

## 1. What is Exception Handling?

Exception Handling ka purpose application mein aane wali errors ko properly handle karke client ko meaningful HTTP response dena hai.

Without proper handling:

```text
Request
   ↓
Backend Error
   ↓
Generic / Unclear Response
```

With proper handling:

```text
Request
   ↓
Exception
   ↓
GlobalExceptionHandler
   ↓
Proper HTTP Status
   +
Structured Error JSON
```

---

# 2. Why Exception Handling?

Example:

```http
GET /api/products/999
```

Agar product `999` exist nahi karta, API ko clearly batana chahiye:

```text
404 Not Found
```

Instead of an unclear server error.

A structured response can look like:

```json
{
  "timestamp": "2026-08-03T21:10:00",
  "status": 404,
  "error": "Not Found",
  "message": "Product not found with id: 999",
  "path": "/api/products/999"
}
```

---

# 3. Custom Exception

Application-specific situations ke liye custom exceptions bana sakte hain.

Example:

```text
ProductNotFoundException
```

Create:

```java
package com.vijay.springbootlearning.exception;

public class ProductNotFoundException
        extends RuntimeException {

    public ProductNotFoundException(String message) {
        super(message);
    }
}
```

---

# 4. Why Extend RuntimeException?

```java
extends RuntimeException
```

se `ProductNotFoundException` ek runtime exception ban jaati hai.

```text
RuntimeException
       ↑
ProductNotFoundException
```

Constructor:

```java
public ProductNotFoundException(String message) {
    super(message);
}
```

`super(message)` parent exception ko message pass karta hai.

Later:

```java
exception.getMessage()
```

se wahi message retrieve kar sakte hain.

---

# 5. Throwing Custom Exception

Example:

```java
@GetMapping("/{id}")
public ResponseEntity<String> getProductById(
        @PathVariable int id) {

    if (id != 10) {

        throw new ProductNotFoundException(
                "Product not found with id: " + id
        );
    }

    return ResponseEntity.ok(
            "Product found with id: " + id
    );
}
```

If:

```text
id = 10
```

Result:

```text
200 OK
```

If:

```text
id = 999
```

then:

```java
throw new ProductNotFoundException(...);
```

executes.

---

# 6. `throw` vs `return`

These are different.

## return

```java
return ResponseEntity.ok(product);
```

means:

```text
Normal execution
      ↓
Method finishes
      ↓
Response returned
```

## throw

```java
throw new ProductNotFoundException(
        "Product not found"
);
```

means:

```text
Normal execution stops
        ↓
Exception propagates
        ↓
Spring searches for handler
```

---

# 7. Global Exception Handler

Instead of handling exceptions separately in every controller, we created:

```text
GlobalExceptionHandler
```

Example structure:

```text
com.vijay.springbootlearning
│
├── Controller/
├── dto/
├── service/
│
└── exception/
      ├── ProductNotFoundException.java
      └── GlobalExceptionHandler.java
```

Handler class:

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

}
```

---

# 8. `@RestControllerAdvice`

```java
@RestControllerAdvice
```

allows exception handling logic to be applied across REST controllers.

Instead of:

```text
ProductController → exception logic
OrderController   → exception logic
UserController    → exception logic
```

we can centralize it:

```text
ProductController ──┐
OrderController   ──┼──→ GlobalExceptionHandler
UserController    ──┘
```

---

# 9. `@ExceptionHandler`

`@ExceptionHandler` defines which method handles a particular exception.

Example:

```java
@ExceptionHandler(ProductNotFoundException.class)
```

means:

```text
ProductNotFoundException occurs
              ↓
Execute this handler
```

Initial example:

```java
@ExceptionHandler(ProductNotFoundException.class)
public ResponseEntity<String> handleProductNotFound(
        ProductNotFoundException exception) {

    return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(exception.getMessage());
}
```

Response:

```text
404 Not Found

Product not found with id: 999
```

---

# 10. Exception Handling Flow

```text
GET /api/products/999
        ↓
Controller
        ↓
Product doesn't exist
        ↓
throw ProductNotFoundException
        ↓
Spring
        ↓
GlobalExceptionHandler
        ↓
@ExceptionHandler
        ↓
handleProductNotFound()
        ↓
404 Not Found
```

---

# 11. Problem With Plain Text Errors

This works:

```text
Product not found with id: 999
```

But a structured response is more useful for API clients.

Example:

```json
{
  "timestamp": "...",
  "status": 404,
  "error": "Not Found",
  "message": "Product not found with id: 999",
  "path": "/api/products/999"
}
```

For this we created:

```text
ApiErrorResponse
```

---

# 12. ApiErrorResponse DTO

```java
package com.vijay.springbootlearning.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiErrorResponse {

    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;

    private Map<String, String> validationErrors;

    public ApiErrorResponse(
            LocalDateTime timestamp,
            int status,
            String error,
            String message,
            String path) {

        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getValidationErrors() {
        return validationErrors;
    }

    public void setValidationErrors(
            Map<String, String> validationErrors) {

        this.validationErrors = validationErrors;
    }
}
```

---

# 13. ApiErrorResponse Fields

```text
timestamp
→ Error kab hua

status
→ HTTP status number
→ 400, 404, etc.

error
→ HTTP status description
→ Bad Request / Not Found

message
→ Actual useful error message

path
→ Kis API endpoint par error hua

validationErrors
→ Individual field validation errors
```

---

# 14. `@JsonInclude(NON_NULL)`

We used:

```java
@JsonInclude(JsonInclude.Include.NON_NULL)
```

Meaning:

```text
If a field is null
       ↓
Don't include it in JSON
```

For example, `ProductNotFoundException` mein:

```java
validationErrors == null
```

to response mein:

```json
"validationErrors": null
```

show karna necessary nahi.

`NON_NULL` us field ko omit kar deta hai.

---

# 15. ProductNotFoundException With ApiErrorResponse

Handler:

```java
@ExceptionHandler(ProductNotFoundException.class)
public ResponseEntity<ApiErrorResponse> handleProductNotFound(
        ProductNotFoundException exception,
        HttpServletRequest request) {

    ApiErrorResponse errorResponse =
            new ApiErrorResponse(
                    LocalDateTime.now(),
                    HttpStatus.NOT_FOUND.value(),
                    HttpStatus.NOT_FOUND.getReasonPhrase(),
                    exception.getMessage(),
                    request.getRequestURI()
            );

    return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(errorResponse);
}
```

---

# 16. Understanding the Handler

## Timestamp

```java
LocalDateTime.now()
```

gets current date/time.

---

## Status Number

```java
HttpStatus.NOT_FOUND.value()
```

returns:

```text
404
```

---

## Status Description

```java
HttpStatus.NOT_FOUND.getReasonPhrase()
```

returns:

```text
Not Found
```

---

## Exception Message

```java
exception.getMessage()
```

returns:

```text
Product not found with id: 999
```

---

## Request Path

```java
request.getRequestURI()
```

For:

```http
GET /api/products/999
```

returns:

```text
/api/products/999
```

---

# 17. HttpServletRequest

Handler method can receive:

```java
HttpServletRequest request
```

Import:

```java
import jakarta.servlet.http.HttpServletRequest;
```

We used it to get the current request URI:

```java
request.getRequestURI();
```

This avoids hardcoding:

```java
"/api/products/999"
```

---

# 18. Product Not Found Final Response

Request:

```http
GET /api/products/999
```

Flow:

```text
Controller
    ↓
Product missing
    ↓
ProductNotFoundException
    ↓
GlobalExceptionHandler
    ↓
ApiErrorResponse
    ↓
Jackson
    ↓
JSON
```

Response:

```json
{
  "timestamp": "2026-08-03T21:10:00",
  "status": 404,
  "error": "Not Found",
  "message": "Product not found with id: 999",
  "path": "/api/products/999"
}
```

HTTP Status:

```text
404 Not Found
```

---

# 19. Validation Errors With Same Format

Previously validation returned:

```json
{
  "name": "Product name is required",
  "price": "Price must be greater than 0"
}
```

We improved it so application errors follow a more consistent structure.

Example:

```json
{
  "timestamp": "...",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/products",
  "validationErrors": {
    "name": "Product name is required",
    "price": "Price must be greater than 0",
    "category": "Category is required",
    "stock": "Stock cannot be Negative"
  }
}
```

---

# 20. Validation Exception Handler

```java
@ExceptionHandler(MethodArgumentNotValidException.class)
public ResponseEntity<ApiErrorResponse>
handleValidationException(
        MethodArgumentNotValidException exception,
        HttpServletRequest request) {

    Map<String, String> errors = new HashMap<>();

    exception.getBindingResult()
            .getFieldErrors()
            .forEach(error ->
                    errors.put(
                            error.getField(),
                            error.getDefaultMessage()
                    )
            );

    ApiErrorResponse errorResponse =
            new ApiErrorResponse(
                    LocalDateTime.now(),
                    HttpStatus.BAD_REQUEST.value(),
                    HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    "Validation failed",
                    request.getRequestURI()
            );

    errorResponse.setValidationErrors(errors);

    return ResponseEntity
            .badRequest()
            .body(errorResponse);
}
```

---

# 21. Two Exceptions — One Standard Structure

Now:

```text
Validation Error
      ↓
400 Bad Request
      ↓
ApiErrorResponse
      ↓
validationErrors {}
```

And:

```text
Product Not Found
      ↓
404 Not Found
      ↓
ApiErrorResponse
```

Architecture:

```text
                 Exception
                     ↓
          GlobalExceptionHandler
                     ↓
              ApiErrorResponse
                     ↓
        ┌────────────┴────────────┐
        ↓                         ↓
Validation Error            Product Not Found
        ↓                         ↓
400 Bad Request              404 Not Found
        ↓                         ↓
validationErrors{}          error message
```

---

# 22. Why Standard Error Response?

Without standardization:

```text
Validation → Map
Not Found  → String
Other Error → Different JSON
```

Clients have to handle multiple unrelated formats.

With a standard DTO:

```text
Errors
   ↓
ApiErrorResponse
   ↓
Consistent API structure
```

This makes error responses easier for frontend/client code to process.

---

# 23. Real Project Architecture

During learning we threw the exception from the controller:

```text
Controller
    ↓
ProductNotFoundException
```

In a layered application, business logic commonly belongs in the Service layer:

```text
Controller
     ↓
Service
     ↓
Business Logic
     ↓
Product not found
     ↓
throw ProductNotFoundException
     ↓
GlobalExceptionHandler
     ↓
ApiErrorResponse
     ↓
404
```

This keeps controllers focused on HTTP/request-response concerns.

---

# 24. Complete Architecture

```text
                  CLIENT
                     │
                     ↓
                 REQUEST
                     │
                     ↓
                Controller
                     │
                     ↓
                  Service
                     │
              Business Logic
                     │
          ┌──────────┴──────────┐
          ↓                     ↓
       Success                Error
          │                     │
          ↓                     ↓
      Response          Custom Exception
                                │
                                ↓
                    GlobalExceptionHandler
                                │
                                ↓
                       ApiErrorResponse
                                │
                                ↓
                         HTTP Status
                                │
                                ↓
                              JSON
                                │
                                ↓
                             CLIENT
```

---

# 25. Important Annotations / Classes

```java
@RestControllerAdvice
```

Centralized exception handling for REST controllers.

```java
@ExceptionHandler
```

Handles a particular exception type.

```java
RuntimeException
```

Base class used for our custom runtime exception.

```java
HttpServletRequest
```

Provides information about the current HTTP request.

```java
@JsonInclude(JsonInclude.Include.NON_NULL)
```

Excludes null fields from JSON serialization.

```java
ResponseEntity
```

Controls HTTP status and response body.

---

# 26. Interview Questions

## What is a custom exception?

An application-specific exception created to represent a particular error condition.

Example:

```java
ProductNotFoundException
```

---

## Why use `@RestControllerAdvice`?

To centralize exception handling across REST controllers.

---

## What does `@ExceptionHandler` do?

It maps a particular exception type to a handler method.

Example:

```java
@ExceptionHandler(ProductNotFoundException.class)
```

---

## Why create ApiErrorResponse?

To provide a predictable, structured error response to API clients.

---

## What does `exception.getMessage()` return?

The message stored in the exception.

---

## Why use HttpServletRequest?

Among other request information, it can provide the request URI:

```java
request.getRequestURI()
```

---

## Why use `@JsonInclude(NON_NULL)`?

To prevent fields whose value is `null` from appearing in serialized JSON.

---

## Where should business exceptions generally originate?

In a layered application, business/service logic can throw domain-specific exceptions, while a global exception handler converts them into HTTP responses.

---

# 27. Quick Revision

```text
ProductNotFoundException
→ Custom exception

throw
→ Stops normal execution and raises exception

RuntimeException
→ Parent of our custom exception

@RestControllerAdvice
→ Global REST exception handling

@ExceptionHandler
→ Handles specific exception

ApiErrorResponse
→ Standard error DTO

HttpServletRequest
→ Current request information

request.getRequestURI()
→ Current request path

@JsonInclude(NON_NULL)
→ Hide null fields from JSON

404
→ Resource not found

400
→ Invalid request/validation
```

---

# Final Flow

```text
Error occurs
    ↓
throw Exception
    ↓
GlobalExceptionHandler
    ↓
Matching @ExceptionHandler
    ↓
Create ApiErrorResponse
    ↓
ResponseEntity
    ↓
HTTP Status + JSON
    ↓
Client
```

# Exception Handling Completed ✅