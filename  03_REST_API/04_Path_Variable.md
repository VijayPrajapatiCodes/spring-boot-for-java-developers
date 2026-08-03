# @PathVariable in Spring Boot

## 1. What is `@PathVariable`?

`@PathVariable` is used to extract a value directly from the URL path.

Example:

```http
GET /api/products/10
```

Here:

```text
/api/products/10
              ↑
         Product ID
```

Spring Boot:

```java
@GetMapping("/{id}")
public String getProductById(
        @PathVariable int id) {

    return "Product ID: " + id;
}
```

Flow:

```text
GET /api/products/10
                  ↓
                 {id}
                  ↓
        @PathVariable int id
                  ↓
                 10
```

---

# 2. Path Variable Placeholder

In:

```java
@GetMapping("/{id}")
```

`{id}` is a placeholder.

Different requests can provide different values:

```text
/api/products/1
              ↓
id = 1

/api/products/50
              ↓
id = 50

/api/products/784
              ↓
id = 784
```

The same controller method can handle all of them.

---

# 3. Basic Example

```java
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @GetMapping("/{id}")
    public String getProductById(
            @PathVariable int id) {

        return "Product ID: " + id;
    }
}
```

Request:

```http
GET /api/products/10
```

Response:

```text
Product ID: 10
```

---

# 4. Real-World Examples

Path variables are commonly used to identify specific resources.

```http
GET /api/products/10
```

Meaning:

```text
Get Product 10
```

```http
GET /api/users/101
```

Meaning:

```text
Get User 101
```

```http
GET /api/orders/784
```

Meaning:

```text
Get Order 784
```

---

# 5. PathVariable with Different HTTP Methods

The same resource URL can be used with different HTTP methods.

```text
GET    /api/products/10
PUT    /api/products/10
DELETE /api/products/10
```

Controller:

```java
@GetMapping("/{id}")
public String getProductById(
        @PathVariable int id) {

    return "Getting Product ID: " + id;
}

@PutMapping("/{id}")
public String updateProduct(
        @PathVariable int id) {

    return "Updating Product ID: " + id;
}

@DeleteMapping("/{id}")
public String deleteProduct(
        @PathVariable int id) {

    return "Deleting Product ID: " + id;
}
```

Mapping:

```text
GET + /api/products/10
        ↓
getProductById()
        ↓
id = 10


PUT + /api/products/10
        ↓
updateProduct()
        ↓
id = 10


DELETE + /api/products/10
        ↓
deleteProduct()
        ↓
id = 10
```

---

# 6. Real Application Flow

For example:

```http
DELETE /api/products/10
```

Later in a real application:

```text
Client
  ↓
DELETE /api/products/10
  ↓
Controller
  ↓
@PathVariable
  ↓
id = 10
  ↓
Service
  ↓
Repository
  ↓
Database
  ↓
Delete Product 10
```

---

# 7. Custom Path Variable Name

The URL placeholder and Java variable name can be different.

Example:

```java
@GetMapping("/{id}")
public String getProduct(
        @PathVariable("id") int productId) {

    return "Product ID: " + productId;
}
```

Here:

```text
{id}
 ↓
@PathVariable("id")
 ↓
productId
```

Request:

```http
GET /api/products/50
```

Result:

```text
productId = 50
```

This can make Java variable names more descriptive.

---

# 8. Multiple Path Variables

An endpoint can contain multiple path variables.

Example:

```http
GET /users/101/orders/784
```

Here:

```text
101 → User ID
784 → Order ID
```

Controller:

```java
@GetMapping("/users/{userId}/orders/{orderId}")
public String getOrder(
        @PathVariable int userId,
        @PathVariable int orderId) {

    return "User ID: " + userId
            + ", Order ID: " + orderId;
}
```

Flow:

```text
/users/{userId}/orders/{orderId}
        ↓               ↓
       101             784
        ↓               ↓
     userId          orderId
```

---

# 9. Nested Resource Examples

Examples of URLs containing multiple resource identifiers:

```text
/users/101/orders/784

/orders/784/items/5

/categories/10/products/50
```

They can represent relationships between resources.

---

# 10. `@PathVariable` vs `@RequestParam`

This is an important difference.

### `@PathVariable`

Example:

```http
GET /api/products/10
```

Here:

```text
10 → Which product?
```

Java:

```java
@PathVariable int id
```

Typically useful when a value identifies a resource in the path.

---

### `@RequestParam`

Example:

```http
GET /api/products?category=electronics
```

Here:

```text
category=electronics
        ↓
Filtering criteria
```

Java:

```java
@RequestParam String category
```

Commonly useful for:

```text
Search
Filter
Sort
Pagination
Optional parameters
```

---

# 11. Easy Rule

```text
@PathVariable
      ↓
Resource identity
      ↓
/products/10
/users/101
/orders/784
```

Whereas:

```text
@RequestParam
      ↓
Search / Filter / Sort / Pagination
      ↓
/products?name=Laptop
/products?category=electronics
/products?page=0&size=10
```

---

# 12. Using Both Together

`@PathVariable` and `@RequestParam` can be used in the same request.

Example:

```http
GET /api/products/10?currency=INR
```

Here:

```text
10
↓
@PathVariable
↓
Which product?


currency=INR
↓
@RequestParam
↓
Additional option
```

Another example:

```http
GET /api/users/101/orders?status=PAID
```

Here:

```text
101
 ↓
@PathVariable
 ↓
User ID


status=PAID
     ↓
@RequestParam
     ↓
Filter
```

Example controller:

```java
@GetMapping("/users/{userId}/orders")
public String getOrders(
        @PathVariable int userId,
        @RequestParam String status) {

    return "User: " + userId
            + ", Status: " + status;
}
```

---

# 13. Product Details Practical

Controller:

```java
@GetMapping("/{id}/details")
public String getProductDetails(
        @PathVariable("id") int productId) {

    return "Getting details for Product ID: "
            + productId;
}
```

Request:

```http
GET /api/products/25/details
```

Response:

```text
Getting details for Product ID: 25
```

Mapping:

```text
/api/products/{id}/details
              ↓
             25
              ↓
@PathVariable("id")
              ↓
productId = 25
```

---

# 14. PathVariable with Update

Later, when updating a product:

```http
PUT /api/products/10
```

The path variable tells the backend:

```text
Which product?
      ↓
Product ID 10
```

The request body can tell the backend what data should be updated:

```json
{
  "name": "Gaming Laptop",
  "price": 65000
}
```

Concept:

```text
PUT /api/products/10
                  ↓
            @PathVariable
                  ↓
             Product 10

Request Body
     ↓
New Product Data
```

We will implement the JSON part while studying `@RequestBody`.

---

# 15. Important Interview Questions

### Q1. What is `@PathVariable`?

`@PathVariable` is used to bind a value from the URL path to a controller method parameter.

### Q2. What does `{id}` mean?

It represents a path variable placeholder.

Example:

```java
@GetMapping("/{id}")
```

For:

```text
/products/10
```

`id` receives `10`.

### Q3. Can we have multiple path variables?

Yes.

```text
/users/{userId}/orders/{orderId}
```

### Q4. Can the path variable name and Java variable name be different?

Yes.

```java
@PathVariable("id") int productId
```

### Q5. Difference between `@PathVariable` and `@RequestParam`?

```text
@PathVariable
→ Value is part of URL path
→ Often identifies a resource

@RequestParam
→ Value is a query parameter
→ Common for filtering/search/sorting/pagination
```

### Q6. Can `@PathVariable` and `@RequestParam` be used together?

Yes.

Example:

```http
GET /products/10?currency=INR
```

### Q7. How would you get product ID 100?

```http
GET /api/products/100
```

Controller:

```java
@GetMapping("/{id}")
public String getProduct(
        @PathVariable int id) {
    // ...
}
```

---

# 16. Quick Revision

```text
                 @PathVariable
                       │
                       ↓
                 URL Path Value
                       │
          ┌────────────┼────────────┐
          ↓            ↓            ↓
      Product ID    User ID      Order ID

/products/10
/users/101
/orders/784
```

Comparison:

```text
/products/10
          ↑
@PathVariable


/products?category=electronics
          ↑
@RequestParam
```

## Remember

```text
@PathVariable → Which resource?

@RequestParam → What filtering/options?
```