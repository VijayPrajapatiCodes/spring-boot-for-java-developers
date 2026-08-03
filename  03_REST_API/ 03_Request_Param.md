# @RequestParam in Spring Boot

## 1. What is `@RequestParam`?

`@RequestParam` is used to read **query parameters** from an HTTP request.

Example request:

```http
GET /api/products/search?name=Laptop
```

Here:

```text
/api/products/search  → API Path
?                     → Query parameters start
name                  → Parameter name
Laptop                → Parameter value
```

Spring Boot:

```java
@GetMapping("/search")
public String searchProduct(
        @RequestParam String name) {

    return "Searching product: " + name;
}
```

Flow:

```text
GET /api/products/search?name=Laptop
                         │
                         ↓
                   name=Laptop
                         ↓
               @RequestParam
                         ↓
                String name
                         ↓
                     Laptop
```

---

# 2. Basic `@RequestParam` Example

Controller:

```java
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @GetMapping("/search")
    public String searchProduct(
            @RequestParam String name) {

        return "Searching product: " + name;
    }
}
```

Request:

```http
GET /api/products/search?name=Laptop
```

Response:

```text
Searching product: Laptop
```

---

# 3. Testing Query Parameters with Postman

In Postman, query parameters can be entered using the **Params** tab.

Base URL:

```text
http://localhost:8082/api/products/search
```

Params:

```text
KEY      VALUE

name     Laptop
```

Postman generates:

```text
http://localhost:8082/api/products/search?name=Laptop
```

Response:

```text
200 OK

Searching product: Laptop
```

Using the Params tab becomes especially useful when an API contains multiple query parameters.

---

# 4. Multiple Request Parameters

An API can receive multiple query parameters.

Example:

```http
GET /api/products/filter?category=electronics&maxPrice=50000
```

Here:

```text
category = electronics
maxPrice = 50000
```

Controller:

```java
@GetMapping("/filter")
public String filterProducts(
        @RequestParam String category,
        @RequestParam double maxPrice) {

    return "Category: " + category
            + ", Max Price: " + maxPrice;
}
```

Spring binds:

```text
category=electronics
        ↓
String category


maxPrice=50000
        ↓
double maxPrice
```

---

# 5. Multiple Query Parameter Syntax

First parameter starts after:

```text
?
```

Additional parameters are separated using:

```text
&
```

Example:

```text
/api/products
      ?
category=electronics
      &
maxPrice=50000
```

Therefore:

```http
GET /api/products?category=electronics&maxPrice=50000
```

---

# 6. Price Range Example

If we need minimum and maximum prices:

```http
GET /api/products?minPrice=1000&maxPrice=50000
```

Better than:

```text
?maxPrice=1000&maxPrice=50000
```

because `minPrice` and `maxPrice` represent two different filtering criteria.

Concept:

```text
minPrice = 1000
maxPrice = 50000

        ↓

1000 <= Product Price <= 50000
```

---

# 7. Real-World Use of Query Parameters

Query parameters are commonly useful for:

```text
Search
Filtering
Sorting
Pagination
Optional options
```

Examples:

### Search

```http
GET /api/products?name=Laptop
```

### Category Filter

```http
GET /api/products?category=electronics
```

### Price Filter

```http
GET /api/products?minPrice=1000&maxPrice=50000
```

### Pagination

```http
GET /api/products?page=0&size=10
```

### Sorting

```http
GET /api/products?sort=price
```

---

# 8. E-Commerce Example

Consider an e-commerce application.

When the user opens the product page without any filters:

```http
GET /api/products
```

Backend can return the normal product listing.

Concept:

```text
GET /api/products
        ↓
No Filter
        ↓
Product Listing
```

When the user searches:

```http
GET /api/products?name=Laptop
```

Concept:

```text
All Products
      ↓
name = Laptop
      ↓
Search / Filter
      ↓
Matching Products
```

When a category is selected:

```http
GET /api/products?category=electronics
```

Backend can return products matching that category.

Multiple filters can also be combined:

```http
GET /api/products?category=electronics&maxPrice=50000
```

Concept:

```text
Products
   ↓
Category = electronics
   ↓
Maximum Price = 50000
   ↓
Filtered Products
```

---

# 9. Default Behavior of `@RequestParam`

Consider:

```java
@RequestParam String name
```

By default, the request parameter is required.

Conceptually:

```java
@RequestParam(required = true) String name
```

Therefore:

```http
GET /api/products/search?name=Laptop
```

works.

But if we send:

```http
GET /api/products/search
```

without the required `name` parameter, Spring will normally reject the request.

Typically:

```text
400 Bad Request
```

---

# 10. `required = true`

We can explicitly write:

```java
@RequestParam(required = true) String name
```

Example:

```java
@GetMapping("/search")
public String search(
        @RequestParam(required = true) String name) {

    return "Searching: " + name;
}
```

Request:

```http
GET /api/products/search?name=Laptop
```

Works.

But:

```http
GET /api/products/search
```

does not satisfy the required parameter.

---

# 11. Optional Request Parameter

Sometimes a parameter should not be mandatory.

For that:

```java
@RequestParam(required = false)
```

Example:

```java
@GetMapping("/search")
public String searchProduct(
        @RequestParam(required = false) String name) {

    if (name == null) {
        return "Showing all products";
    }

    return "Searching product: " + name;
}
```

Now both requests are allowed.

Without parameter:

```http
GET /api/products/search
```

Response:

```text
Showing all products
```

With parameter:

```http
GET /api/products/search?name=Laptop
```

Response:

```text
Searching product: Laptop
```

---

# 12. `required = false` Flow

```text
GET /api/products/search
        ↓
name parameter missing
        ↓
name = null
        ↓
Showing all products
```

Whereas:

```text
GET /api/products/search?name=Laptop
                         ↓
                    name=Laptop
                         ↓
              Searching product
                         ↓
                      Laptop
```

This is useful when filtering/searching is optional.

---

# 13. `defaultValue`

Instead of receiving `null` when a parameter is missing, we can provide a default value.

Example:

```java
@GetMapping("/page")
public String getProductsByPage(
        @RequestParam(defaultValue = "0") int page) {

    return "Page: " + page;
}
```

Request:

```http
GET /api/products/page
```

Since `page` wasn't provided:

```text
defaultValue = 0
```

Response:

```text
Page: 0
```

If we provide:

```http
GET /api/products/page?page=3
```

then:

```text
page = 3
```

Response:

```text
Page: 3
```

---

# 14. Pagination Example

A common pagination-style setup is:

```java
@GetMapping("/page")
public String getProductsByPage(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {

    return "Page: " + page + ", Size: " + size;
}
```

Here:

```text
page default → 0
size default → 10
```

---

# 15. Pagination Practical

### Case 1 — No Parameters

Request:

```http
GET /api/products/page
```

Spring uses:

```text
page = 0
size = 10
```

Response:

```text
Page: 0, Size: 10
```

---

### Case 2 — Both Parameters

Request:

```http
GET /api/products/page?page=2&size=20
```

Spring uses:

```text
page = 2
size = 20
```

Response:

```text
Page: 2, Size: 20
```

The supplied values override the defaults.

---

### Case 3 — Only One Parameter

Request:

```http
GET /api/products/page?page=5
```

Here:

```text
page supplied
     ↓
5

size missing
     ↓
defaultValue
     ↓
10
```

Response:

```text
Page: 5, Size: 10
```

---

# 16. `required=false` vs `defaultValue`

### Optional parameter

```java
@RequestParam(required = false) String category
```

If missing:

```text
category → null
```

unless we handle it differently.

### Default value

```java
@RequestParam(defaultValue = "10") int size
```

If missing:

```text
size → 10
```

Quick comparison:

```text
@RequestParam String name
        ↓
Required


@RequestParam(required = false) String name
        ↓
Optional


@RequestParam(defaultValue = "10") int size
        ↓
Use 10 when no value is supplied
```

---

# 17. Custom Request Parameter Name

The query parameter name and Java parameter name do not have to be the same.

Request:

```http
GET /api/products/filter?category=electronics
```

Java:

```java
@GetMapping("/filter")
public String filterProducts(
        @RequestParam("category") String productCategory) {

    return "Category: " + productCategory;
}
```

Mapping:

```text
?category=electronics
        ↓
@RequestParam("category")
        ↓
String productCategory
        ↓
electronics
```

Here:

```text
Request parameter → category

Java parameter    → productCategory
```

---

# 18. Another Custom Name Example

Request:

```http
GET /api/products/filter?maxPrice=50000
```

Controller parameter:

```java
@RequestParam("maxPrice") double maximumPrice
```

Mapping:

```text
maxPrice=50000
      ↓
@RequestParam("maxPrice")
      ↓
maximumPrice
      ↓
50000
```

This allows us to keep the public API parameter name different from the internal Java variable name when needed.

---

# 19. When Names Are Same

If request parameter and Java parameter have the same name:

```text
Request → category
Java    → category
```

we can write:

```java
@RequestParam String category
```

Instead of:

```java
@RequestParam("category") String category
```

Both express the same intended binding when the parameter name is resolved as `category`.

---

# 20. Complete Example

```java
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @GetMapping("/search")
    public String searchProduct(
            @RequestParam(required = false) String name) {

        if (name == null) {
            return "Showing all products";
        }

        return "Searching product: " + name;
    }

    @GetMapping("/filter")
    public String filterProducts(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double maxPrice) {

        return "Category: " + category
                + ", Max Price: " + maxPrice;
    }

    @GetMapping("/page")
    public String getProductsByPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return "Page: " + page + ", Size: " + size;
    }
}
```

---

# 21. Real Application Design

Later a real product API could conceptually support:

```http
GET /api/products
```

Normal product listing.

```http
GET /api/products?category=electronics
```

Category filtering.

```http
GET /api/products?minPrice=1000&maxPrice=50000
```

Price filtering.

```http
GET /api/products?page=0&size=20
```

Pagination.

And filters can be combined:

```http
GET /api/products?category=electronics&minPrice=1000&maxPrice=50000&page=0&size=20
```

The controller receives these query parameters and the application can use them to determine what data should be returned.

---

# 22. Important Interview Questions

### Q1. What is `@RequestParam`?

`@RequestParam` is used to bind an HTTP request query parameter to a controller method parameter.

---

### Q2. What is a query parameter?

It is a key-value parameter included in the URL.

Example:

```text
?name=Laptop
```

Here:

```text
name    → key
Laptop  → value
```

---

### Q3. How are multiple query parameters separated?

Using:

```text
&
```

Example:

```http
/api/products?page=0&size=10
```

---

### Q4. Is `@RequestParam` required by default?

Yes, unless configured otherwise.

```java
@RequestParam String name
```

is required by default.

---

### Q5. How do we make a request parameter optional?

```java
@RequestParam(required = false)
```

---

### Q6. What is `defaultValue`?

It specifies a value to use when the request parameter is not supplied.

Example:

```java
@RequestParam(defaultValue = "10") int size
```

---

### Q7. Can the request parameter name and Java parameter name be different?

Yes.

```java
@RequestParam("category") String productCategory
```

---

### Q8. Where are request parameters commonly used?

Examples include:

```text
Search
Filtering
Sorting
Pagination
Optional request options
```

---

### Q9. What happens if a required request parameter is missing?

Spring normally rejects the request, typically with:

```text
400 Bad Request
```

---

### Q10. Difference between `required=false` and `defaultValue`?

```text
required=false
→ Parameter can be absent

defaultValue
→ A predefined value is used when no value is supplied
```

---

# 23. Quick Revision

```text
                  @RequestParam
                       │
          ┌────────────┼────────────┐
          ↓            ↓            ↓
        Search       Filter      Pagination
          │            │            │
      ?name=       ?category=    ?page=&size=
```

Important syntax:

```java
@RequestParam String name
```

Required parameter.

```java
@RequestParam(required = false) String name
```

Optional parameter.

```java
@RequestParam(defaultValue = "10") int size
```

Default value.

```java
@RequestParam("category") String productCategory
```

Custom binding name.

---

# 24. Remember

```text
/api/products?category=electronics&maxPrice=50000
              ↑                    ↑
              Query Parameters
```

`@RequestParam` allows Spring Boot controllers to receive these values.

Typical REST usage:

```text
Search
Filter
Sort
Pagination
        ↓
Query Parameters
        ↓
@RequestParam
```