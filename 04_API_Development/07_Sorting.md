# 07 - Sorting in Spring Boot

## 1. What is Sorting?

Sorting ka use records ko particular order mein retrieve karne ke liye hota hai.

Example:

```text
Price Low → High
Price High → Low
Name A → Z
Newest → Oldest
```

---

# 2. Sorting Directions

Main directions:

```text
ASC
DESC
```

ASC:

```text
1
2
3
4
5
```

DESC:

```text
5
4
3
2
1
```

---

# 3. API Example

```text
GET /api/products?sortBy=price&direction=asc
```

Meaning:

```text
sortBy = price
direction = asc
```

---

# 4. Spring Data `Sort`

Spring Data provides:

```java
Sort
```

Import:

```java
import org.springframework.data.domain.Sort;
```

Example:

```java
Sort sort =
        Sort.by("price");
```

---

# 5. Ascending Sorting

```java
Sort sort =
        Sort.by("price").ascending();
```

Result:

```text
100
500
1000
5000
50000
```

---

# 6. Descending Sorting

```java
Sort sort =
        Sort.by("price").descending();
```

Result:

```text
50000
5000
1000
500
100
```

---

# 7. Repository

```java
productRepository.findAll(sort);
```

Example:

```java
public List<Product> getProducts() {

    Sort sort =
            Sort.by("price").ascending();

    return productRepository.findAll(sort);
}
```

---

# 8. Dynamic Sorting

Controller:

```java
@GetMapping
public List<Product> getProducts(
        @RequestParam(defaultValue = "id")
        String sortBy) {

    Sort sort =
            Sort.by(sortBy).ascending();

    return productRepository.findAll(sort);
}
```

Request:

```text
GET /api/products?sortBy=price
```

or:

```text
GET /api/products?sortBy=name
```

---

# 9. Dynamic Direction

```java
Sort sort;

if (direction.equalsIgnoreCase("desc")) {

    sort = Sort.by(sortBy).descending();

} else {

    sort = Sort.by(sortBy).ascending();
}
```

Request:

```text
/products?sortBy=price&direction=desc
```

---

# 10. Pagination + Sorting

Pagination aur sorting ko combine kar sakte hain.

```java
Sort sort =
        Sort.by("price").descending();

Pageable pageable =
        PageRequest.of(
                page,
                size,
                sort
        );
```

Request:

```text
GET /api/products?page=0&size=10&sortBy=price&direction=desc
```

Flow:

```text
Request
   ↓
page = 0
size = 10
sortBy = price
direction = desc
   ↓
Sort
   ↓
PageRequest
   ↓
Repository
   ↓
Database
```

---

# 11. Multiple Field Sorting

Possible:

```java
Sort sort = Sort.by(
        Sort.Order.asc("category"),
        Sort.Order.desc("price")
);
```

Meaning:

```text
First → category ASC

Then
↓
price DESC
```

---

# 12. Real-World Examples

E-commerce:

```text
Price Low → High
Price High → Low
Newest Products
Highest Rated
```

Employee system:

```text
Salary High → Low
Name A → Z
Hire Date Newest → Oldest
```

---

# 13. Important Security/Design Point

Client se directly arbitrary field name lekar sorting karne ke bajay allowed fields define karna better hai.

Example concept:

```text
Allowed:

id
name
price
category

Not Allowed:

random invalid fields
```

---

# 14. Interview Questions

### What is sorting?

Records ko specified order mein retrieve karna.

### Which class is used?

```java
Sort
```

### ASC vs DESC?

```text
ASC → Increasing order
DESC → Decreasing order
```

### Can pagination and sorting work together?

Yes.

```java
PageRequest.of(page, size, sort);
```

---

# Quick Revision

```text
Sort
→ Sorting configuration

ascending()
→ ASC

descending()
→ DESC

Sort.by()
→ Sorting create

PageRequest
→ Pagination + Sorting combine
```