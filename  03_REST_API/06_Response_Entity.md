# ResponseEntity in Spring Boot

## 1. What is ResponseEntity?

`ResponseEntity` is used when we want more control over an HTTP response.

It can control:

```text
ResponseEntity
    │
    ├── HTTP Status
    ├── Response Body
    └── Response Headers
```

Import:

```java
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
```

---

# 2. Without ResponseEntity

```java
@PostMapping
public OrderRequest createOrder(
        @RequestBody OrderRequest order) {

    return order;
}
```

Spring automatically returns the response.

Typically:

```text
Status → 200 OK
Body   → Order JSON
```

But sometimes we need to explicitly control the HTTP status.

---

# 3. ResponseEntity<T>

Syntax:

```java
ResponseEntity<T>
```

`T` represents the response body type.

Examples:

```java
ResponseEntity<String>
```

```java
ResponseEntity<ProductRequest>
```

```java
ResponseEntity<OrderRequest>
```

```java
ResponseEntity<List<ProductRequest>>
```

For no response body:

```java
ResponseEntity<Void>
```

---

# 4. Returning 200 OK

```java
@GetMapping("/{id}")
public ResponseEntity<String> getOrder(
        @PathVariable int id) {

    return ResponseEntity.ok(
            "Order found: " + id
    );
}
```

Response:

```text
Status → 200 OK
Body   → Order found: 101
```

Shortcut:

```java
ResponseEntity.ok(body)
```

---

# 5. Returning 201 Created

When a new resource is successfully created, `201 Created` is commonly used.

```java
@PostMapping
public ResponseEntity<OrderRequest> createOrder(
        @RequestBody OrderRequest order) {

    return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(order);
}
```

Response:

```text
Status → 201 Created
Body   → Order JSON
```

Flow:

```text
POST /api/orders
       ↓
Order Created
       ↓
201 Created
       +
JSON Body
```

---

# 6. Returning Different Status Codes

`ResponseEntity` allows an endpoint to return different responses depending on the result.

```java
@GetMapping("/{id}")
public ResponseEntity<String> getOrder(
        @PathVariable int id) {

    if (id == 101) {
        return ResponseEntity.ok(
                "Order found: " + id
        );
    }

    return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body("Order not found");
}
```

Results:

```text
GET /api/orders/101

→ 200 OK
→ Order found: 101
```

But:

```text
GET /api/orders/999

→ 404 Not Found
→ Order not found
```

---

# 7. 204 No Content

Sometimes an operation succeeds but no response body is required.

Example:

```java
@DeleteMapping("/{id}")
public ResponseEntity<Void> deleteOrder(
        @PathVariable int id) {

    return ResponseEntity
            .noContent()
            .build();
}
```

Response:

```text
Status → 204 No Content
Body   → Empty
```

Because there is no body:

```java
ResponseEntity<Void>
```

is used.

---

# 8. Response Headers

`ResponseEntity` can also add HTTP response headers.

Example:

```java
@GetMapping("/{id}/details")
public ResponseEntity<String> getOrderDetails(
        @PathVariable int id) {

    return ResponseEntity
            .ok()
            .header(
                "X-App-Name",
                "SpringBootLearning"
            )
            .header(
                "X-Order-Id",
                String.valueOf(id)
            )
            .body("Order found: " + id);
}
```

Response:

```text
HTTP Status
200 OK

Headers
X-App-Name: SpringBootLearning
X-Order-Id: 101

Body
Order found: 101
```

---

# 9. Request Headers vs Response Headers

These are different.

```text
CLIENT
(Postman)
    │
    │ Request Headers
    ↓
Spring Boot
```

Examples:

```text
Authorization
Content-Type
Accept
```

Response headers travel in the opposite direction:

```text
Spring Boot
    │
    │ Response Headers
    ↓
CLIENT
(Postman)
```

Examples:

```text
Content-Type
Location
Cache-Control
X-App-Name
X-Order-Id
```

---

# 10. ResponseEntity Builder Pattern

We commonly build responses like this:

```java
return ResponseEntity
        .status(HttpStatus.CREATED)
        .header("X-App-Name", "MyApp")
        .body(order);
```

Think of it as:

```text
Response
│
├── status = 201
├── header = X-App-Name
└── body   = order
```

---

# 11. Useful ResponseEntity Methods

### 200 OK

```java
ResponseEntity.ok(body);
```

### 201 Created

```java
ResponseEntity
        .status(HttpStatus.CREATED)
        .body(body);
```

### 400 Bad Request

```java
ResponseEntity
        .badRequest()
        .body("Invalid request");
```

### 404 Not Found

```java
ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body("Not found");
```

or when no body is needed:

```java
ResponseEntity
        .notFound()
        .build();
```

### 204 No Content

```java
ResponseEntity
        .noContent()
        .build();
```

---

# 12. Real Project Example

Suppose we have:

```text
POST /api/orders
```

Possible result:

```text
Order created
     ↓
201 Created
```

For:

```text
GET /api/orders/101
```

Possible results:

```text
Order exists
     ↓
200 OK
```

or:

```text
Order doesn't exist
     ↓
404 Not Found
```

For:

```text
DELETE /api/orders/101
```

successful deletion can return:

```text
204 No Content
```

Therefore:

```text
Request
   ↓
Controller
   ↓
Result
   ↓
Choose appropriate status
   ↓
ResponseEntity
   ↓
HTTP Response
```

---

# 13. Why Use ResponseEntity?

Without explicit response control:

```text
Controller
    ↓
Return Object
    ↓
Spring generates response
```

With `ResponseEntity`:

```text
Controller
    ↓
ResponseEntity
    │
    ├── Status
    ├── Headers
    └── Body
```

It is especially useful when the response depends on application results.

---

# 14. Common REST Examples

```text
GET /products/10

Product found
→ 200 OK
```

```text
POST /products

Product created
→ 201 Created
```

```text
GET /products/999

Product not found
→ 404 Not Found
```

```text
POST /products

Invalid request
→ 400 Bad Request
```

```text
DELETE /products/10

Deleted successfully
→ 204 No Content
```

---

# 15. Interview Questions

## What is ResponseEntity?

`ResponseEntity` represents an HTTP response and allows control over the response status, headers, and body.

## What does `ResponseEntity<T>` mean?

`T` represents the response body type.

Example:

```java
ResponseEntity<ProductResponse>
```

means the response body contains a `ProductResponse`.

## Difference between `return product` and `ResponseEntity<Product>`?

```java
return product;
```

returns the object and lets Spring construct the HTTP response.

While:

```java
return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(product);
```

explicitly controls the HTTP response.

## What is `ResponseEntity<Void>`?

It represents a response without a body.

Example:

```java
return ResponseEntity
        .noContent()
        .build();
```

## Can ResponseEntity contain headers?

Yes.

```java
ResponseEntity
        .ok()
        .header("X-App-Name", "MyApp")
        .body(data);
```

---

# 16. Final Mental Model

```text
              Controller
                  │
                  ↓
            ResponseEntity
                  │
       ┌──────────┼──────────┐
       ↓          ↓          ↓
     Status     Headers      Body
       │          │           │
    200 OK    Content-Type   JSON
    201       Location       String
    400       Custom         Object
    404                      List
    204
       │          │           │
       └──────────┼───────────┘
                  ↓
             HTTP Response
                  ↓
                Client
```

## Key Point

Remember:

```text
ResponseEntity
=
Status + Headers + Body
```

Examples:

```java
ResponseEntity.ok(data);
```

```java
ResponseEntity
        .status(HttpStatus.CREATED)
        .body(data);
```

```java
ResponseEntity
        .notFound()
        .build();
```

```java
ResponseEntity
        .noContent()
        .build();
```

---

# ResponseEntity Completed ✅