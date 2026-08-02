# Controller in Spring Boot

## 1. What is a Controller?

Controller Spring Boot application mein incoming HTTP requests ko handle karta hai.

Basic flow:

```text
Client / Postman
       ↓
HTTP Request
       ↓
Tomcat
       ↓
Spring MVC
       ↓
Controller
       ↓
Controller Method
       ↓
HTTP Response
       ↓
Client / Postman
```

Example request:

```http
GET /api/products
```

Spring request mapping ke basis par appropriate controller method execute karta hai.

---

# 2. `@RestController`

REST APIs banane ke liye commonly `@RestController` use hota hai.

Example:

```java
@RestController
public class ProductController {

}
```

`@RestController`:

- Class ko REST controller ke roop mein register karta hai.
- Spring component scanning ke through ise detect kar sakta hai.
- Controller methods ke returned values ko response body mein write karne ki behavior provide karta hai.

Concept:

```text
@RestController
       ↓
Spring detects class
       ↓
Spring-managed Controller Bean
       ↓
Handles HTTP Requests
       ↓
Returns Response Body
```

Controller object normally manually create nahi karte:

```java
new ProductController(); // Not required
```

Spring IoC Container controller bean ko manage karta hai.

---

# 3. `@RequestMapping`

`@RequestMapping` controller ke liye common/base path define kar sakta hai.

Example:

```java
@RestController
@RequestMapping("/api/products")
public class ProductController {

}
```

Here:

```text
Base Path
   ↓
/api/products
```

Ab controller ke methods ke mappings is base path ke saath combine ho sakte hain.

---

# 4. `@GetMapping`

GET requests handle karne ke liye:

```java
@GetMapping
public String getProducts() {
    return "All Products";
}
```

Controller:

```java
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @GetMapping
    public String getProducts() {
        return "All Products";
    }
}
```

Mapping:

```text
@RequestMapping("/api/products")
              +
@GetMapping
              ↓
GET /api/products
```

Postman:

```http
GET http://localhost:8082/api/products
```

Response:

```text
200 OK

All Products
```

---

# 5. Method-Level Mapping

Controller ke andar additional path bhi define kar sakte hain.

Example:

```java
@GetMapping("/featured")
public String getFeaturedProducts() {
    return "Featured Products";
}
```

Class-level path:

```java
@RequestMapping("/api/products")
```

Method-level path:

```java
@GetMapping("/featured")
```

Combined:

```text
/api/products
      +
/featured
      ↓
/api/products/featured
```

Request:

```http
GET /api/products/featured
```

Response:

```text
Featured Products
```

---

# 6. Multiple Endpoints in One Controller

Example:

```java
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @GetMapping
    public String getProducts() {
        return "All Products";
    }

    @GetMapping("/featured")
    public String getFeaturedProducts() {
        return "Featured Products";
    }
}
```

Structure:

```text
ProductController
│
│ Base Path → /api/products
│
├── GET /
│      ↓
│   getProducts()
│
└── GET /featured
       ↓
    getFeaturedProducts()
```

Therefore:

```text
GET /api/products
→ All Products

GET /api/products/featured
→ Featured Products
```

---

# 7. HTTP Method Mappings

Spring provides specific mapping annotations for HTTP methods.

```text
@GetMapping
@PostMapping
@PutMapping
@PatchMapping
@DeleteMapping
```

Example:

```java
@GetMapping
public String getProducts() {
    return "All Products";
}
```

```java
@PostMapping
public String createProduct() {
    return "Product Created";
}
```

```java
@PutMapping
public String updateProduct() {
    return "Product Updated";
}
```

```java
@DeleteMapping
public String deleteProduct() {
    return "Product Deleted";
}
```

---

# 8. Same URL with Different HTTP Methods

An important REST concept is that the same path can have handlers for different HTTP methods.

Example path:

```text
/api/products
```

Mappings:

```text
GET    /api/products
POST   /api/products
PUT    /api/products
DELETE /api/products
```

These are different requests because their HTTP methods are different.

Example controller:

```java
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @GetMapping
    public String getProducts() {
        return "All Products";
    }

    @PostMapping
    public String createProduct() {
        return "Product Created";
    }

    @PutMapping
    public String updateProduct() {
        return "Product Updated";
    }

    @DeleteMapping
    public String deleteProduct() {
        return "Product Deleted";
    }
}
```

Mapping:

```text
GET + /api/products
        ↓
getProducts()


POST + /api/products
        ↓
createProduct()


PUT + /api/products
        ↓
updateProduct()


DELETE + /api/products
        ↓
deleteProduct()
```

So request handling depends on the mapping conditions, including HTTP method and path.

---

# 9. Postman Practical

We tested the same URL using different HTTP methods.

URL:

```text
http://localhost:8082/api/products
```

### GET

```http
GET /api/products
```

Response:

```text
All Products
```

### POST

```http
POST /api/products
```

Response:

```text
Product Created
```

### PUT

```http
PUT /api/products
```

Response:

```text
Product Updated
```

### DELETE

```http
DELETE /api/products
```

Response:

```text
Product Deleted
```

All requests used the same path but executed different controller methods.

---

# 10. `@Controller`

Spring also provides:

```java
@Controller
```

`@Controller` is commonly used in Spring MVC applications where the controller returns a **view name** which is then resolved/rendered as a web page.

Example:

```java
@Controller
public class HomeController {

    @GetMapping("/home")
    public String home() {
        return "home";
    }
}
```

Here:

```java
return "home";
```

can represent a view name.

For example:

```text
templates/
└── home.html
```

Flow:

```text
Browser
   ↓
GET /home
   ↓
@Controller
   ↓
return "home"
   ↓
View Resolver / Template Engine
   ↓
home.html
   ↓
Rendered HTML
   ↓
Browser
```

---

# 11. Example: Employee Dashboard with `@Controller`

Suppose we are creating an employee dashboard using:

```text
Spring Boot
+
Thymeleaf
```

Dashboard displays:

```text
Total Employees : 150
Present         : 132
Absent          : 18
Total Salary    : ₹45,00,000
```

Controller:

```java
@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        model.addAttribute("totalEmployees", 150);
        model.addAttribute("present", 132);
        model.addAttribute("absent", 18);
        model.addAttribute("totalSalary", 4500000);

        return "dashboard";
    }
}
```

Spring can render:

```text
templates/dashboard.html
```

Flow:

```text
Browser
   ↓
GET /dashboard
   ↓
@Controller
   ↓
Get/prepare data
   ↓
Model
   ↓
Thymeleaf
   ↓
dashboard.html
   ↓
Rendered HTML
```

---

# 12. Same Dashboard with React

Suppose UI React mein bana hua hai.

Then Spring Boot ko HTML page render karne ki zarurat nahi.

Spring Boot can expose an API:

```java
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @GetMapping
    public DashboardResponse getDashboard() {
        // return dashboard data
    }
}
```

Response can be JSON:

```json
{
  "totalEmployees": 150,
  "present": 132,
  "absent": 18,
  "totalSalary": 4500000
}
```

React then displays this data.

Flow:

```text
React
  ↓
GET /api/dashboard
  ↓
@RestController
  ↓
JSON
  ↓
React renders dashboard
```

---

# 13. `@Controller` vs `@RestController`

Main difference:

```text
@Controller
      ↓
Commonly used for MVC Views
      ↓
HTML / Template Rendering


@RestController
      ↓
REST API
      ↓
Response Body / JSON
```

Comparison:

| Feature | `@Controller` | `@RestController` |
|---|---|---|
| Common use | MVC applications | REST APIs |
| HTML/View rendering | Yes | Not its primary purpose |
| API response body | Requires appropriate response-body handling | Built in |
| React backend APIs | Less typical | Yes |
| Thymeleaf pages | Common | Usually no |

---

# 14. Important: Data Does Not Decide Controller Type

Suppose data is:

```text
Total Employees
Present Employees
Absent Employees
Total Salary
```

This data itself does NOT decide whether to use:

```java
@Controller
```

or:

```java
@RestController
```

The important question is:

```text
Who is rendering the UI?
```

### Spring Boot renders HTML

```text
Spring Boot
+
Thymeleaf
      ↓
@Controller
```

### React renders UI

```text
Spring Boot
      ↓
JSON
      ↓
React
      ↓
UI
```

Use:

```java
@RestController
```

Therefore:

> Controller choice depends mainly on how the response is being handled, not on whether the data represents employees, products, orders, salary, etc.

---

# 15. Relationship Between `@Controller` and `@RestController`

Important interview concept:

```text
@RestController
       ≈
@Controller
       +
@ResponseBody
```

`@RestController` combines controller behavior with response-body semantics.

Example:

```java
@RestController
public class ProductController {

    @GetMapping("/api/products")
    public String products() {
        return "All Products";
    }
}
```

Here:

```text
All Products
```

is written to the HTTP response body.

---

# 16. Returning Objects from REST Controller

Later our REST controller can return Java objects.

Example concept:

```java
@GetMapping("/api/product")
public Product getProduct() {
    return product;
}
```

The object can be serialized into JSON.

Example:

```json
{
  "id": 1,
  "name": "Laptop",
  "price": 50000
}
```

We will study JSON/request/response handling in upcoming chapters.

---

# 17. Controller Flow

Complete basic flow:

```text
                 Postman / Client
                        │
                        │ HTTP Request
                        ↓
                      Tomcat
                        ↓
                   Spring MVC
                        ↓
                 Request Mapping
                        ↓
               ProductController
                        ↓
                 Handler Method
                        ↓
                   Return Value
                        ↓
                 HTTP Response
                        ↓
                 Postman / Client
```

---

# 18. Important Annotations

Quick revision:

```java
@RestController
```

Used for REST controllers.

```java
@Controller
```

Commonly used for MVC/view controllers.

```java
@RequestMapping("/api/products")
```

Defines a common/base mapping.

```java
@GetMapping
```

Handles GET requests.

```java
@PostMapping
```

Handles POST requests.

```java
@PutMapping
```

Handles PUT requests.

```java
@PatchMapping
```

Handles PATCH requests.

```java
@DeleteMapping
```

Handles DELETE requests.

---

# 19. Interview Questions

### Q1. What is a Controller in Spring Boot?

A controller handles incoming HTTP requests and routes them to appropriate handler methods.

---

### Q2. What is `@RestController`?

`@RestController` is used to create REST controllers whose handler method return values are written to the HTTP response body.

---

### Q3. What is `@RequestMapping`?

It is used to define request mappings. At class level it can define a common/base path for controller endpoints.

Example:

```java
@RequestMapping("/api/products")
```

---

### Q4. What is `@GetMapping`?

It maps HTTP GET requests to a controller method.

---

### Q5. Can the same URL be used for GET and POST?

Yes.

Example:

```text
GET  /api/products
POST /api/products
```

They represent different HTTP requests because the methods are different.

---

### Q6. Difference between `@Controller` and `@RestController`?

`@Controller` is commonly used for MVC/view rendering, while `@RestController` is designed for REST APIs and writes returned values to the response body.

---

### Q7. What is `@RestController` equivalent to conceptually?

```text
@Controller + @ResponseBody
```

---

### Q8. If React is the frontend, which one would normally be used for backend REST APIs?

```java
@RestController
```

Because Spring Boot provides API responses while React renders the UI.

---

### Q9. If Spring Boot + Thymeleaf renders HTML pages, which controller is commonly used?

```java
@Controller
```

---

### Q10. Does the type of business data decide whether to use `@Controller` or `@RestController`?

No.

For example, employee statistics could be rendered through Thymeleaf using `@Controller`, or returned as JSON to React using `@RestController`.

---

# 20. Quick Revision

```text
                  Controller
                      │
          ┌───────────┴───────────┐
          ↓                       ↓
    @Controller             @RestController
          ↓                       ↓
     MVC / Views               REST API
          ↓                       ↓
   Thymeleaf/HTML          Response Body/JSON
```

Request mappings:

```text
@RequestMapping → Common/base mapping

@GetMapping     → GET
@PostMapping    → POST
@PutMapping     → PUT
@PatchMapping   → PATCH
@DeleteMapping  → DELETE
```

Example:

```text
@RequestMapping("/api/products")
              │
              ├── GET    → @GetMapping
              ├── POST   → @PostMapping
              ├── PUT    → @PutMapping
              └── DELETE → @DeleteMapping
```

## Remember

> `@Controller` is commonly used when Spring MVC renders views, while `@RestController` is used when controller methods directly provide REST API response bodies such as JSON.