# HTTP Status Codes in Spring Boot REST API

## 1. What are HTTP Status Codes?

HTTP Status Codes client ko batate hain ki request ka result kya hua.

```text
Client
   ↓ Request
Spring Boot Backend
   ↓
Process
   ↓
HTTP Status Code
   ↓
Client
```

Example:

```text
GET /api/products/10
→ 200 OK

POST /api/products
→ 201 Created

GET /api/products/999
→ 404 Not Found
```

---

# 2. Status Code Categories

```text
1xx → Informational
2xx → Success
3xx → Redirection
4xx → Client Error
5xx → Server Error
```

Backend development mein mainly:

```text
2xx → Success
4xx → Client-side/request/auth errors
5xx → Server-side errors
```

---

# 3. 200 OK

Request successfully process hui.

Example:

```http
GET /api/products/10
```

Response:

```text
200 OK
```

Spring:

```java
return ResponseEntity.ok(product);
```

Common use:

```text
GET product
GET user
GET orders
Successful update with response
```

---

# 4. 201 Created

Naya resource successfully create hua.

Example:

```http
POST /api/products
```

Response:

```text
201 Created
```

Spring:

```java
return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(product);
```

Common use:

```text
Create Product
Create Order
Create User
```

---

# 5. 204 No Content

Request successful hai, lekin response body nahi bhejni.

Example:

```http
DELETE /api/products/10
```

Response:

```text
204 No Content
```

Spring:

```java
return ResponseEntity
        .noContent()
        .build();
```

Usually:

```java
ResponseEntity<Void>
```

use kiya ja sakta hai.

---

# 6. 400 Bad Request

Client ki request invalid hai.

Example:

Java expects:

```java
private double price;
```

Client sends:

```json
{
  "price": "abc"
}
```

Flow:

```text
"abc"
   ↓
double conversion
   ↓
Failed
   ↓
400 Bad Request
```

Validation failures bhi commonly `400` return kar sakte hain.

Examples:

```text
Invalid input
Missing required field
Negative quantity
Invalid email format
Invalid JSON
```

---

# 7. 401 Unauthorized

Authentication credentials missing ya invalid hain.

Example:

```text
GET /api/profile
       ↓
JWT missing / invalid
       ↓
401 Unauthorized
```

Remember:

```text
401 → Authentication problem
```

Example:

```text
Token missing
Token invalid
Authentication required
```

---

# 8. 403 Forbidden

User authenticated hai, lekin requested operation ki permission nahi hai.

Example:

```text
Logged-in User
Role = USER
      ↓
DELETE /api/admin/products/10
      ↓
ADMIN permission required
      ↓
403 Forbidden
```

Important difference:

```text
401
↓
Who are you?
Authentication required


403
↓
I know who you are,
but you are not allowed.
```

---

# 9. 404 Not Found

Requested resource nahi mila.

Example:

```http
GET /api/products/999
```

Response:

```text
404 Not Found
```

Spring:

```java
return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body("Product not found");
```

Examples:

```text
Product not found
Order not found
User not found
Endpoint not found
```

---

# 10. 409 Conflict

Request existing resource/state ke saath conflict karti hai.

Example:

```text
POST /api/users
      ↓
Email already exists
      ↓
409 Conflict
```

Spring:

```java
return ResponseEntity
        .status(HttpStatus.CONFLICT)
        .body("Email already exists");
```

Examples:

```text
Duplicate email
Duplicate username
Resource state conflict
```

---

# 11. 500 Internal Server Error

Backend ke andar unexpected error hua.

```text
Request
   ↓
Controller
   ↓
Service
   ↓
Unexpected Exception
   ↓
500 Internal Server Error
```

Remember:

```text
400 → Request/client problem

500 → Server/backend problem
```

Real applications mein internal stack trace client ko expose nahi karna chahiye.

---

# 12. 503 Service Unavailable

Service temporarily available nahi hai.

Example:

```text
Backend
   ↓
Dependent Service
   ↓
Temporarily unavailable
   ↓
503 Service Unavailable
```

It can be used for temporary service availability problems.

---

# 13. Important Status Codes Cheat Sheet

| Code | Meaning | Common Example |
|---|---|---|
| 200 | OK | Product fetched |
| 201 | Created | Product created |
| 204 | No Content | Delete successful |
| 400 | Bad Request | Invalid request |
| 401 | Unauthorized | Authentication required |
| 403 | Forbidden | Permission denied |
| 404 | Not Found | Product not found |
| 409 | Conflict | Duplicate/conflict |
| 500 | Internal Server Error | Backend error |
| 503 | Service Unavailable | Service temporarily unavailable |

---

# 14. Real REST API Examples

## GET

```text
GET /api/products/10

Product exists
→ 200 OK
```

```text
GET /api/products/999

Product doesn't exist
→ 404 Not Found
```

## POST

```text
POST /api/products

Product created
→ 201 Created
```

```text
POST /api/users

Invalid input
→ 400 Bad Request
```

```text
POST /api/users

Email already exists
→ 409 Conflict
```

## DELETE

```text
DELETE /api/products/10

Product deleted
→ 204 No Content
```

## Security

```text
GET /api/profile

Token missing
→ 401 Unauthorized
```

```text
DELETE /api/admin/products/10

Authenticated USER tries ADMIN operation
→ 403 Forbidden
```

---

# 15. Using Status Codes with ResponseEntity

## 200

```java
return ResponseEntity.ok(data);
```

## 201

```java
return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(data);
```

## 204

```java
return ResponseEntity
        .noContent()
        .build();
```

## 400

```java
return ResponseEntity
        .badRequest()
        .body("Invalid request");
```

## 404

```java
return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body("Product not found");
```

## 409

```java
return ResponseEntity
        .status(HttpStatus.CONFLICT)
        .body("Resource already exists");
```

---

# 16. Quick Memory Trick

```text
200 → OK
201 → Created
204 → Success, no body

400 → Bad request
401 → Authentication
403 → Permission
404 → Not found
409 → Conflict

500 → Backend error
503 → Service unavailable
```

---

# 17. Interview Questions

## What does 200 mean?

Request successfully processed.

## Difference between 200 and 201?

```text
200 → Successful request

201 → New resource created
```

## Difference between 401 and 403?

```text
401 → Authentication missing/invalid

403 → Authenticated but not authorized
```

## Difference between 400 and 500?

```text
400 → Client/request problem

500 → Server-side unexpected problem
```

## When should 404 be returned?

When the requested resource cannot be found.

## When can 409 be used?

When the request conflicts with the current resource state, such as a duplicate resource.

## What does 204 mean?

Request successful but response contains no body.

---

# Final Mental Model

```text
HTTP Request
     ↓
Spring Boot
     ↓
Process Result
     ↓
┌─────────────────────────┐
│ Success?                │
│ 200 / 201 / 204         │
├─────────────────────────┤
│ Client/Auth Problem?    │
│ 400 / 401 / 403 / 404   │
│ 409                     │
├─────────────────────────┤
│ Server Problem?         │
│ 500 / 503               │
└─────────────────────────┘
     ↓
HTTP Response
```

# HTTP Status Codes Completed ✅