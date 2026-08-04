# Request Validation in Spring Boot

## 1. What is Request Validation?

Request Validation ka use client se aane wale data ko validate karne ke liye hota hai.

Client technically correct datatype bhej sakta hai, lekin data logically invalid ho sakta hai.

Example:

```json
{
  "name": "",
  "price": -50000,
  "category": "",
  "stock": -10
}
```

Java ke according:

```text
""      → String  ✅
-50000  → double  ✅
""      → String  ✅
-10     → int     ✅
```

Lekin business rules ke according:

```text
Empty name       ❌
Negative price   ❌
Empty category   ❌
Negative stock   ❌
```

Isliye Request Validation ki zarurat hoti hai.

---

# 2. Validation Dependency

Spring Boot project mein validation ke liye dependency:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

---

# 3. Validation Annotations

Validation annotations DTO fields par lagaye jaate hain.

Example:

```java
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public class ProductRequest {

    private int id;

    @NotBlank(message = "Product name is required")
    private String name;

    @Positive(message = "Price must be greater than 0")
    private double price;

    @NotBlank(message = "Category is required")
    private String category;

    @PositiveOrZero(message = "Stock cannot be Negative")
    private int stock;

    // constructors
    // getters
    // setters
}
```

---

# 4. `@NotBlank`

Used mainly with Strings.

```java
@NotBlank(message = "Product name is required")
private String name;
```

It rejects values such as:

```text
null
""
"   "
```

Valid:

```text
"Laptop"
```

Invalid:

```text
""
```

---

# 5. `@Positive`

Value must be greater than zero.

```java
@Positive(message = "Price must be greater than 0")
private double price;
```

Rule:

```text
price > 0
```

Examples:

```text
50000  → Valid   ✅
1      → Valid   ✅

0      → Invalid ❌
-500   → Invalid ❌
```

---

# 6. `@PositiveOrZero`

Value can be zero or positive.

```java
@PositiveOrZero(message = "Stock cannot be Negative")
private int stock;
```

Rule:

```text
stock >= 0
```

Examples:

```text
10   → Valid   ✅
0    → Valid   ✅
-10  → Invalid ❌
```

This makes sense for stock because a product can have:

```text
stock = 0
```

when it is out of stock.

---

# 7. `@Valid`

DTO par validation annotations lagana alone enough nahi hai.

Controller mein validation trigger karne ke liye:

```java
@Valid
```

use karte hain.

Import:

```java
import jakarta.validation.Valid;
```

Example:

```java
@PostMapping
public ResponseEntity<ProductRequest> createProduct(
        @Valid @RequestBody ProductRequest product) {

    return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(product);
}
```

Important:

```text
@RequestBody
     ↓
JSON ko Java Object banata hai

@Valid
     ↓
Java Object ko validate karwata hai
```

---

# 8. Complete Validation Flow

```text
POST /api/products
       ↓
JSON Request
       ↓
@RequestBody
       ↓
ProductRequest Object
       ↓
@Valid
       ↓
Validation Annotations
       ↓
┌──────────────┴──────────────┐
│                             │
Valid                       Invalid
│                             │
↓                             ↓
Controller executes      Validation fails
│                             │
↓                             ↓
201 Created             400 Bad Request
```

---

# 9. Valid Request

Example:

```json
{
  "id": 101,
  "name": "Laptop",
  "price": 50000,
  "category": "Electronics",
  "stock": 10
}
```

Validation:

```text
name     → Valid ✅
price    → Valid ✅
category → Valid ✅
stock    → Valid ✅
```

Result:

```text
201 Created
```

---

# 10. Invalid Request

Example:

```json
{
  "id": 101,
  "name": "",
  "price": -50000,
  "category": "",
  "stock": -10
}
```

Validation:

```text
name     → ❌ @NotBlank
price    → ❌ @Positive
category → ❌ @NotBlank
stock    → ❌ @PositiveOrZero
```

Result:

```text
400 Bad Request
```

---

# 11. Other Common Validation Annotations

## `@NotNull`

Value cannot be `null`.

```java
@NotNull
private Integer quantity;
```

---

## `@Size`

Checks String/collection size.

```java
@Size(
    min = 2,
    max = 100,
    message = "Product name must be between 2 and 100 characters"
)
private String name;
```

Rule:

```text
2 <= length <= 100
```

---

## `@Email`

Validates email format.

```java
@Email(message = "Invalid email format")
private String email;
```

Example:

```text
vijay@gmail.com → Valid

vijaygmail.com  → Invalid
```

---

## `@Min`

Minimum allowed value.

```java
@Min(1)
private int quantity;
```

Means:

```text
quantity >= 1
```

---

## `@Max`

Maximum allowed value.

```java
@Max(100)
private int quantity;
```

Means:

```text
quantity <= 100
```

Can combine them:

```java
@Min(1)
@Max(100)
private int quantity;
```

---

# 12. Validation Exception

When `@Valid` validation fails, Spring can throw:

```text
MethodArgumentNotValidException
```

Flow:

```text
@Valid
   ↓
Validation fails
   ↓
MethodArgumentNotValidException
   ↓
400 Bad Request
```

Without custom exception handling, Spring may return a generic error response.

Example:

```json
{
  "timestamp": "...",
  "status": 400,
  "error": "Bad Request",
  "path": "/api/products"
}
```

Problem:

```text
Client knows request is invalid

BUT

Client doesn't clearly know
which fields are invalid.
```

---

# 13. Handling Validation Errors Globally

We can handle validation errors using:

```java
@RestControllerAdvice
```

and:

```java
@ExceptionHandler
```

Example:

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>>
    handleValidationException(
            MethodArgumentNotValidException exception) {

        Map<String, String> errors = new HashMap<>();

        exception.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        return ResponseEntity
                .badRequest()
                .body(errors);
    }
}
```

---

# 14. `@RestControllerAdvice`

```java
@RestControllerAdvice
```

is used to handle exceptions across REST controllers from a central place.

Instead of writing error-handling logic repeatedly:

```text
ProductController → error handling
OrderController   → error handling
UserController    → error handling
```

we can centralize it:

```text
ProductController ──┐
OrderController   ──┼──→ GlobalExceptionHandler
UserController    ──┘
```

---

# 15. `@ExceptionHandler`

Example:

```java
@ExceptionHandler(MethodArgumentNotValidException.class)
```

means:

```text
If MethodArgumentNotValidException occurs
                ↓
Run this handler method
```

Flow:

```text
Validation fails
      ↓
MethodArgumentNotValidException
      ↓
@ExceptionHandler finds matching handler
      ↓
handleValidationException()
      ↓
Custom response
```

---

# 16. Getting Field Errors

This:

```java
exception
        .getBindingResult()
        .getFieldErrors();
```

provides validation field errors.

For each error:

```java
error.getField()
```

gives field name.

Example:

```text
name
price
category
stock
```

And:

```java
error.getDefaultMessage()
```

gives validation message.

Example:

```text
Product name is required
Price must be greater than 0
Category is required
Stock cannot be Negative
```

---

# 17. Why `Map<String, String>`?

We created:

```java
Map<String, String> errors = new HashMap<>();
```

because we want:

```text
Field → Error Message
```

Example:

```text
name     → Product name is required
price    → Price must be greater than 0
category → Category is required
stock    → Stock cannot be Negative
```

Spring converts this Map into JSON.

---

# 18. Custom Validation Error Response

After Global Exception Handling, invalid request can return:

```json
{
  "price": "Price must be greater than 0",
  "name": "Product name is required",
  "category": "Category is required",
  "stock": "Stock cannot be Negative"
}
```

With:

```text
400 Bad Request
```

This is much more useful for frontend/client applications.

---

# 19. Complete Real Flow

```text
Postman / Frontend
       ↓
POST /api/products
       ↓
JSON
       ↓
@RequestBody
       ↓
ProductRequest
       ↓
@Valid
       ↓
@NotBlank
@Positive
@PositiveOrZero
       ↓
Validation
       ↓
     Invalid
       ↓
MethodArgumentNotValidException
       ↓
GlobalExceptionHandler
       ↓
@ExceptionHandler
       ↓
Field Error Map
       ↓
400 Bad Request
       ↓
Error JSON
```

---

# 20. Package Scanning Important

`GlobalExceptionHandler` should normally be inside a package scanned by the Spring Boot application.

Example:

```text
com.vijay.springbootlearning
│
├── SpringbootLearningApplication.java
├── Controller/
├── dto/
├── service/
└── exception/
      └── GlobalExceptionHandler.java
```

Spring Boot starts component scanning from the package containing:

```java
@SpringBootApplication
```

and its sub-packages by default.

If `GlobalExceptionHandler` is outside the scanned packages, Spring may not detect it.

Symptoms:

```text
@Valid works
400 Bad Request comes

BUT

Custom validation error response
doesn't appear.
```

So when an annotated component is not working, check its package/component scanning.

---

# 21. Validation vs Exception Handling

These are related but different concepts.

### Validation

```text
Is incoming data valid?
```

Examples:

```java
@NotBlank
@Positive
@Email
@Size
```

### Exception Handling

```text
If something fails,
how should the API respond?
```

Examples:

```java
@RestControllerAdvice
@ExceptionHandler
```

Together:

```text
Validation detects problem
          ↓
Exception represents problem
          ↓
Exception Handler creates response
```

---

# 22. Interview Questions

## What is `@Valid`?

`@Valid` triggers validation of an object using its validation constraints.

---

## Difference between `@NotNull` and `@NotBlank`?

```text
@NotNull
→ value cannot be null

@NotBlank
→ String cannot be null, empty or only whitespace
```

---

## What does `@Positive` check?

```text
value > 0
```

---

## What does `@PositiveOrZero` check?

```text
value >= 0
```

---

## What happens when validation fails?

For request-body validation, Spring can raise:

```text
MethodArgumentNotValidException
```

which can result in:

```text
400 Bad Request
```

---

## Why use `@RestControllerAdvice`?

To centralize exception handling across REST controllers.

---

## Why use `@ExceptionHandler`?

To define how a particular exception should be converted into an HTTP response.

---

# 23. Final Mental Model

```text
              REQUEST
                 │
                 ↓
           @RequestBody
                 │
                 ↓
                DTO
                 │
                 ↓
              @Valid
                 │
                 ↓
        Validation Rules
                 │
        ┌────────┴────────┐
        ↓                 ↓
      VALID             INVALID
        │                 │
        ↓                 ↓
 Controller runs       Exception
        │                 │
        ↓                 ↓
 Success        GlobalExceptionHandler
        │                 │
        ↓                 ↓
  200 / 201          400 Response
                          │
                          ↓
                    Error JSON
```

## Quick Revision

```text
@RequestBody
→ JSON → Java Object

@Valid
→ Start validation

@NotBlank
→ String required

@Positive
→ > 0

@PositiveOrZero
→ >= 0

@Size
→ Size/length validation

@Email
→ Email format validation

@Min / @Max
→ Numeric range

MethodArgumentNotValidException
→ Request-body validation failed

@RestControllerAdvice
→ Central/global REST exception handling

@ExceptionHandler
→ Handle a particular exception
```

# Request Validation Completed ✅