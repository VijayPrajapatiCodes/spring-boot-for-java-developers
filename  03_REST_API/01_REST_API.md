# REST API

## 1. What is an API?

**API stands for Application Programming Interface.**

API provides a way for different software applications or components to communicate with each other.

Example:

```text
Frontend
   ↓
  API
   ↓
Backend
```

In a web application:

```text
React Frontend
      ↓
HTTP Request
      ↓
Spring Boot Backend
      ↓
Database
```

The frontend normally does not directly access the backend database.

Instead, the frontend communicates with the backend using APIs.

---

# 2. Example of an API

Suppose a frontend needs all products.

It can send:

```http
GET /api/products
```

Spring Boot can process the request, retrieve the required data and return a response.

Example JSON response:

```json
[
  {
    "id": 1,
    "name": "Laptop",
    "price": 50000
  },
  {
    "id": 2,
    "name": "Mouse",
    "price": 500
  }
]
```

Flow:

```text
Frontend
   ↓
GET /api/products
   ↓
Spring Boot
   ↓
Database
   ↓
Spring Boot
   ↓
JSON Response
   ↓
Frontend
```

---

# 3. What is REST?

**REST stands for Representational State Transfer.**

REST is an architectural style used for designing web APIs.

REST APIs are generally designed around **resources**.

Examples of resources:

```text
Products
Users
Orders
Payments
Cart
```

Resource URLs can look like:

```text
/api/products
/api/users
/api/orders
/api/payments
```

---

# 4. Resource-Based URLs

Suppose our resource is:

```text
Product
```

A collection of products can be represented as:

```http
/api/products
```

A particular product can be represented as:

```http
/api/products/10
```

Similarly:

```text
/api/users
/api/users/5

/api/orders
/api/orders/100
```

REST APIs commonly use nouns/resources in URLs.

---

# 5. HTTP Methods

REST APIs use HTTP methods to represent operations on resources.

The most important methods are:

```text
GET
POST
PUT
PATCH
DELETE
```

Basic CRUD mapping:

```text
CRUD                    HTTP Method

Create    ───────────→  POST
Read      ───────────→  GET
Update    ───────────→  PUT / PATCH
Delete    ───────────→  DELETE
```

---

# 6. GET

`GET` is used to retrieve/read data.

Example:

```http
GET /api/products
```

Meaning:

```text
Get all products
```

For a particular product:

```http
GET /api/products/10
```

Meaning:

```text
Get product with ID 10
```

---

# 7. POST

`POST` is commonly used to create a new resource.

Example:

```http
POST /api/products
```

Request body:

```json
{
  "name": "Laptop",
  "price": 50000
}
```

Meaning:

```text
Create a new product
```

---

# 8. PUT

`PUT` is commonly used to update/replace a resource representation.

Example:

```http
PUT /api/products/10
```

Request body:

```json
{
  "name": "Gaming Laptop",
  "price": 65000
}
```

Meaning:

```text
Update/replace product 10
```

---

# 9. PATCH

`PATCH` is commonly used for a partial update.

Suppose we only want to change the status of a product.

```http
PATCH /api/products/10/status
```

Request:

```json
{
  "status": "OUT_OF_STOCK"
}
```

Instead of sending all product information, we are updating a specific part of the resource.

---

# 10. DELETE

`DELETE` is used to delete a resource.

Example:

```http
DELETE /api/products/10
```

Meaning:

```text
Delete product with ID 10
```

---

# 11. HTTP Request

A client communicates with a REST API by sending an HTTP request.

An HTTP request can contain:

```text
HTTP Method
URL
Headers
Query Parameters
Path Variables
Request Body
```

Example:

```http
POST /api/products
Content-Type: application/json

{
  "name": "Laptop",
  "price": 50000
}
```

We will study these parts separately in upcoming topics.

---

# 12. HTTP Response

After processing the request, the server sends an HTTP response.

A response can contain:

```text
Status Code
Headers
Response Body
```

Example:

```text
Status:
200 OK
```

Response body:

```json
{
  "id": 1,
  "name": "Laptop",
  "price": 50000
}
```

---

# 13. Request-Response Flow

Basic REST API communication:

```text
CLIENT
  │
  │ HTTP Request
  │
  │ GET /api/products
  ↓
SERVER
  │
  │ Process Request
  ↓
DATABASE
  │
  │ Data
  ↑
SERVER
  │
  │ HTTP Response
  │
  │ Status Code + Body
  ↓
CLIENT
```

In our case:

```text
Postman
   ↓
HTTP Request
   ↓
Tomcat
   ↓
Spring Boot
   ↓
Controller
   ↓
HTTP Response
   ↓
Postman
```

---

# 14. REST API in Spring Boot

Spring Boot makes it easy to create REST APIs.

Example:

```java
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/api/hello")
    public String hello() {
        return "Hello Spring Boot REST API";
    }
}
```

Here:

```text
@RestController
      ↓
Class handles REST requests

@GetMapping("/api/hello")
      ↓
Handles GET /api/hello
```

We will study these annotations in detail in the Controller chapter.

---

# 15. Our First REST API Practical

We created:

```java
@RestController
public class HelloController {

    @GetMapping("/api/hello")
    public String hello() {
        return "Hello Spring Boot REST API";
    }
}
```

Our application was running locally.

We sent:

```http
GET http://localhost:8082/api/hello
```

Expected response:

```text
Hello Spring Boot REST API
```

---

# 16. API Testing with Postman

Instead of relying only on a normal browser, we configured **Postman** for API development and testing.

Our development workflow:

```text
IntelliJ IDEA
     ↓
Write Spring Boot API
     ↓
Run Application
     ↓
Postman
     ↓
Send HTTP Request
     ↓
Spring Boot
     ↓
Receive HTTP Response
```

For our first API:

```text
Method:
GET

URL:
http://localhost:8082/api/hello
```

Then:

```text
Send
 ↓
Spring Boot receives request
 ↓
hello() executes
 ↓
Response returned
```

---

# 17. What to Check in Postman

When testing an API, we should not only check the response body.

Important things include:

```text
1. HTTP Status Code
2. Response Body
3. Response Headers
4. Response Time
5. Response Size
```

For our first API:

```text
GET /api/hello

        ↓

Status:
200 OK

Body:
Hello Spring Boot REST API
```

---

# 18. Why Use an API Client?

A browser is enough for very simple GET requests.

But real APIs also require:

```text
POST
PUT
PATCH
DELETE

JSON Request Body
Headers
Authentication
Query Parameters
```

An API client such as Postman makes these requests much easier to create, inspect and test.

For example:

```http
POST /api/products
```

with:

```json
{
  "name": "Laptop",
  "price": 50000
}
```

Later we can also send headers such as:

```text
Content-Type: application/json
Authorization: Bearer <token>
```

---

# 19. Example REST API Design

For a Product resource:

```text
GET    /api/products
GET    /api/products/{id}
POST   /api/products
PUT    /api/products/{id}
PATCH  /api/products/{id}
DELETE /api/products/{id}
```

Meaning:

```text
GET /api/products
→ Get all products

GET /api/products/10
→ Get product 10

POST /api/products
→ Create product

PUT /api/products/10
→ Update/replace product 10

PATCH /api/products/10
→ Partially update product 10

DELETE /api/products/10
→ Delete product 10
```

---

# 20. REST API and JSON

REST APIs commonly exchange JSON data.

Example product:

```json
{
  "id": 10,
  "name": "Laptop",
  "price": 50000
}
```

JSON is convenient for communication between applications such as:

```text
React
   ↕
JSON
   ↕
Spring Boot
```

We will study request-body JSON handling separately.

---

# 21. Real Application Example

Consider an e-commerce application.

Frontend may call:

```http
GET /api/products
```

to display products.

For creating an order:

```http
POST /api/orders
```

For getting an order:

```http
GET /api/orders/784
```

For changing an order status:

```http
PATCH /api/orders/784/status
```

So APIs form the communication layer between clients and backend functionality.

---

# 22. REST API Chapter Connection

This REST API introduction is the base for upcoming topics:

```text
REST API
   │
   ├── Controller
   │
   ├── Request Param
   │
   ├── Path Variable
   │
   ├── Request Body
   │
   ├── ResponseEntity
   │
   └── HTTP Status Codes
```

Each upcoming topic explains one important part of REST API development.

---

# 23. Interview Questions

### Q1. What is API?

API stands for Application Programming Interface. It provides an interface through which software applications or components can communicate.

### Q2. What is REST?

REST stands for Representational State Transfer. It is an architectural style commonly used for designing web APIs.

### Q3. What is a resource in REST?

A resource represents something the API exposes or operates on.

Examples:

```text
Product
User
Order
Payment
```

### Q4. Which HTTP method is used to retrieve data?

```text
GET
```

### Q5. Which HTTP method is commonly used to create a resource?

```text
POST
```

### Q6. Which methods are commonly used for updates?

```text
PUT
PATCH
```

### Q7. What is the difference between PUT and PATCH?

Conceptually:

```text
PUT
→ Full update/replacement semantics

PATCH
→ Partial modification
```

### Q8. Which HTTP method is used to delete a resource?

```text
DELETE
```

### Q9. What format is commonly used for REST API request and response bodies?

```text
JSON
```

### Q10. What can an HTTP response contain?

```text
Status Code
Headers
Response Body
```

### Q11. Why did we use Postman?

To send HTTP requests and inspect/test API responses more conveniently than using only a browser.

---

# 24. Quick Revision

```text
                    REST API
                       │
          ┌────────────┴────────────┐
          ↓                         ↓
       Request                   Response
          │                         │
     HTTP Method                Status Code
     URL                        Headers
     Headers                    Body
     Parameters
     Body
          │
          ↓
     Spring Boot
```

HTTP methods:

```text
POST    → Create
GET     → Read
PUT     → Update/Replace
PATCH   → Partial Update
DELETE  → Delete
```

Our first practical:

```text
Postman
   ↓
GET /api/hello
   ↓
Spring Boot
   ↓
HelloController
   ↓
hello()
   ↓
200 OK
   ↓
Hello Spring Boot REST API
```

## Remember

> REST APIs allow clients and servers to communicate over HTTP using resources, HTTP methods, requests and responses.