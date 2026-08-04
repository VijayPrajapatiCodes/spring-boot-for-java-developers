# 08 - Filtering in Spring Boot

## 1. What is Filtering?

Filtering ka use specific conditions ke according required records retrieve karne ke liye hota hai.

Example:

```text
All Products
      ↓
Category = Electronics
      ↓
Only Electronics Products
```

---

# 2. Real-World Example

Suppose database:

```text
Laptop       Electronics    50000
Mouse        Electronics      500
T-Shirt      Fashion          800
Shoes        Fashion         2500
Phone        Electronics    30000
```

Filter:

```text
category = Electronics
```

Result:

```text
Laptop
Mouse
Phone
```

---

# 3. Request Parameters

Filtering APIs commonly query parameters use karti hain.

Example:

```text
GET /api/products?category=Electronics
```

Controller:

```java
@GetMapping
public String getProducts(
        @RequestParam(required = false)
        String category) {

    return "Category: " + category;
}
```

---

# 4. Multiple Filters

Example:

```text
GET /api/products
?category=Electronics
&minPrice=10000
&maxPrice=50000
```

Controller:

```java
@GetMapping
public String getProducts(
        @RequestParam(required = false)
        String category,

        @RequestParam(required = false)
        Double minPrice,

        @RequestParam(required = false)
        Double maxPrice) {

    return "Filtering products";
}
```

---

# 5. Repository Query Methods

Spring Data JPA method names se simple filters create kar sakta hai.

Example:

```java
List<Product> findByCategory(String category);
```

Usage:

```java
productRepository
        .findByCategory("Electronics");
```

---

# 6. Price Filtering

```java
List<Product> findByPriceLessThanEqual(
        Double price);
```

Example:

```text
price <= 50000
```

---

# 7. Price Range

```java
List<Product> findByPriceBetween(
        Double minPrice,
        Double maxPrice);
```

Example:

```text
10000 - 50000
```

---

# 8. Multiple Conditions

Example:

```java
List<Product>
findByCategoryAndPriceBetween(
        String category,
        Double minPrice,
        Double maxPrice
);
```

Concept:

```sql
WHERE category = ?
AND price BETWEEN ? AND ?
```

---

# 9. Optional Filters

Real APIs mein filters optional ho sakte hain.

Example:

```text
/products
```

→ All products

```text
/products?category=Electronics
```

→ Category filter

```text
/products?minPrice=1000&maxPrice=50000
```

→ Price filter

```text
/products?category=Electronics&minPrice=1000
```

→ Multiple filters

Dynamic filtering ke liye larger applications mein Specifications/Criteria based approaches useful ho sakte hain.

---

# 10. Pagination + Sorting + Filtering

Real-world APIs mein teenon combine hote hain.

Example:

```text
GET /api/products
?category=Electronics
&minPrice=10000
&maxPrice=50000
&page=0
&size=10
&sortBy=price
&direction=asc
```

Meaning:

```text
Filter
↓
category = Electronics
price = 10000 - 50000

Pagination
↓
page = 0
size = 10

Sorting
↓
price ASC
```

---

# 11. Real-World Flow

```text
Client Request
      ↓
Filtering
      ↓
Matching Products
      ↓
Sorting
      ↓
Ordered Products
      ↓
Pagination
      ↓
Required Page
      ↓
Response
```

Conceptually:

```text
100000 Products
      ↓
FILTER
      ↓
5000 Electronics Products
      ↓
SORT
      ↓
Price Low → High
      ↓
PAGINATION
      ↓
First 20 Products
```

---

# 12. Search vs Filtering

Search:

```text
/products?search=laptop
```

Typically finds records matching some text.

Filtering:

```text
/products?category=Electronics
```

Restricts records based on specific conditions.

They can also be combined.

---

# 13. Common Filter Types

```text
Exact Match
→ category=Electronics

Range
→ minPrice=1000
→ maxPrice=50000

Boolean
→ available=true

Date
→ startDate
→ endDate

Multiple Values
→ category=Electronics,Fashion

Text/Search
→ keyword=laptop
```

---

# 14. Advanced Dynamic Filtering

For complex APIs, Spring Data JPA provides approaches such as:

```text
Specifications
Criteria API
Custom Repository Queries
```

Example concept:

```java
Specification<Product>
```

This becomes useful when many filters are optional and can be dynamically combined.

Study this practically during a database-backed project.

---

# 15. Real Project Example

E-commerce product API:

```text
GET /api/products
```

Possible parameters:

```text
category
minPrice
maxPrice
brand
inStock
search
page
size
sortBy
direction
```

Example:

```text
/api/products
?category=Laptop
&minPrice=30000
&maxPrice=80000
&page=0
&size=20
&sortBy=price
&direction=asc
```

---

# 16. Interview Questions

### What is filtering?

Specific conditions ke according required records retrieve karna.

### How can simple filters be created with Spring Data JPA?

Repository query methods se.

Example:

```java
findByCategory(...)
```

### How can range filtering be performed?

Example:

```java
findByPriceBetween(...)
```

### Can filtering and pagination work together?

Yes.

### Dynamic filtering ke liye kya use kar sakte hain?

Complex cases mein:

```text
Spring Data JPA Specifications
Criteria API
Custom Queries
```

---

# Quick Revision

```text
Filtering
→ Required records select karna

@RequestParam
→ Filter values receive karna

findBy...
→ Repository query methods

Between
→ Range filtering

Specification
→ Dynamic/complex filtering approach
```

---

# Final Mental Model

```text
                 GET /api/products
                        ↓
             Query Parameters
                        ↓
       ┌────────────────────────────┐
       │ category = Electronics     │
       │ minPrice = 10000           │
       │ maxPrice = 50000           │
       │ sortBy = price             │
       │ direction = asc            │
       │ page = 0                   │
       │ size = 10                  │
       └──────────────┬─────────────┘
                      ↓
                  FILTERING
                      ↓
                   SORTING
                      ↓
                 PAGINATION
                      ↓
                   DATABASE
                      ↓
                 Page<Product>
                      ↓
                 API RESPONSE
```