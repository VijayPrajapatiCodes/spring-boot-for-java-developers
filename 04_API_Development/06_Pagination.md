# 06 - Pagination in Spring Boot

## 1. What is Pagination?

Pagination ka matlab large amount of data ko chhote-chhote pages mein divide karke return karna.

Example:

```text
Total Products = 100

Page 0 → 10 Products
Page 1 → 10 Products
Page 2 → 10 Products
...
```

Instead of:

```text
GET /api/products
→ All 100000 products ❌
```

We can use:

```text
GET /api/products?page=0&size=10
```

---

## 2. Why Pagination?

Agar database mein bahut saare records hain aur hum sabko ek saath return karein:

```text
Database
   ↓
100000 Products
   ↓
Backend
   ↓
JSON
   ↓
Frontend
```

Problems:

- High memory usage
- Slow API
- Large response
- More database load
- More network usage
- Poor frontend performance

Pagination:

```text
Database
   ↓
Required Records Only
   ↓
Backend
   ↓
Client
```

---

## 3. Basic Pagination Parameters

Common parameters:

```text
page
size
```

Example:

```http
GET /api/products?page=0&size=10
```

Meaning:

```text
page = 0
size = 10
```

Spring Data pagination normally uses zero-based page numbering.

```text
page=0 → First Page
page=1 → Second Page
page=2 → Third Page
```

---

## 4. Controller-Level Example

```java
@GetMapping
public String getProducts(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {

    return "Page: " + page + ", Size: " + size;
}
```

Request:

```text
GET /api/products?page=2&size=20
```

Response:

```text
Page: 2, Size: 20
```

---

# 5. Spring Data Pagination

Important interfaces/classes:

```text
Pageable
PageRequest
Page<T>
```

### Pageable

Pagination information represent karta hai.

```java
Pageable pageable;
```

Contains information like:

```text
page number
page size
sorting
```

---

## 6. PageRequest

`PageRequest` `Pageable` object create karne ke liye commonly use hota hai.

```java
Pageable pageable =
        PageRequest.of(page, size);
```

Example:

```java
PageRequest.of(0, 10);
```

Meaning:

```text
First page
10 records
```

---

# 7. Repository

Spring Data JPA repository:

```java
public interface ProductRepository
        extends JpaRepository<Product, Long> {
}
```

`JpaRepository` pagination support provide karta hai.

Example:

```java
productRepository.findAll(pageable);
```

---

# 8. Service Example

```java
public Page<Product> getProducts(
        int page,
        int size) {

    Pageable pageable =
            PageRequest.of(page, size);

    return productRepository.findAll(pageable);
}
```

---

# 9. Controller Example

```java
@GetMapping
public ResponseEntity<Page<Product>> getProducts(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {

    return ResponseEntity.ok(
            productService.getProducts(page, size)
    );
}
```

Request:

```text
GET /api/products?page=0&size=10
```

---

# 10. `Page<T>`

Instead of only returning records:

```java
List<Product>
```

pagination can return:

```java
Page<Product>
```

`Page` provides data + pagination metadata.

Conceptually:

```json
{
  "content": [],
  "totalPages": 10,
  "totalElements": 100,
  "size": 10,
  "number": 0,
  "first": true,
  "last": false
}
```

---

# 11. Important Page Methods

```java
page.getContent();
page.getTotalPages();
page.getTotalElements();
page.getNumber();
page.getSize();
page.isFirst();
page.isLast();
page.hasNext();
page.hasPrevious();
```

---

# 12. Pagination Flow

```text
GET /products?page=0&size=10
             ↓
         Controller
             ↓
       page=0 size=10
             ↓
       PageRequest.of()
             ↓
          Pageable
             ↓
         Repository
             ↓
          Database
             ↓
        Page<Product>
             ↓
           JSON
```

---

# 13. Real-World Examples

E-commerce:

```text
/products?page=0&size=20
```

Orders:

```text
/orders?page=0&size=50
```

Users:

```text
/users?page=2&size=25
```

---

# 14. Interview Questions

### What is pagination?

Large dataset ko smaller pages mein divide karke retrieve karna.

### Why use pagination?

Performance improve karne aur unnecessary data loading avoid karne ke liye.

### What is Pageable?

Pagination information represent karne wala Spring Data interface.

### What is PageRequest?

`Pageable` implementation create karne ka convenient way.

### `List<T>` vs `Page<T>`?

```text
List<T>
→ Mainly records

Page<T>
→ Records + pagination metadata
```

---

# Quick Revision

```text
Pagination
→ Data ko pages mein divide karna

page
→ Current page number

size
→ Records per page

Pageable
→ Pagination information

PageRequest
→ Pageable create karta hai

Page<T>
→ Data + pagination metadata
```