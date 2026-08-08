# Spring Data JPA Repository

## 1. What is Repository?

Repository layer database ke saath interaction handle karti hai.

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
JPA / Hibernate
    ↓
Database
```

Spring Data JPA repository ki help se hume basic SQL/JPA boilerplate manually likhne ki zarurat nahi padti.

---

# 2. JpaRepository

Example:

```java
public interface ProductRepository
        extends JpaRepository<Product, Long> {

}
```

Here:

```text
Product → Entity
Long    → Primary Key ka type
```

Spring automatically implementation provide karta hai.

Hume normally:

```java
class ProductRepositoryImpl
```

banane ki zarurat nahi hoti.

---

# 3. Repository Example

```java
package com.vijay.springbootlearning.repository;

import com.vijay.springbootlearning.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository
        extends JpaRepository<Product, Long> {
}
```

---

# 4. Built-in Methods

`JpaRepository` se commonly methods milti hain:

```java
save()
findById()
findAll()
deleteById()
existsById()
count()
delete()
saveAll()
```

Example:

```java
productRepository.save(product);
```

Conceptually:

```text
Java Entity
    ↓
Repository
    ↓
Hibernate
    ↓
INSERT / UPDATE
```

---

# 5. Repository Hierarchy

Simplified:

```text
Repository
    ↓
CrudRepository
    ↓
ListCrudRepository
    ↓
JpaRepository
```

`JpaRepository` JPA applications mein commonly use hota hai.

---

# 6. Why Repository Layer?

Repository layer:

- Database operations separate rakhti hai
- Boilerplate code reduce karti hai
- CRUD methods provide karti hai
- Query methods support karti hai
- Pagination/Sorting support karti hai

---

# 7. Service + Repository

Recommended structure:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
Database
```

Example:

```java
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(
            ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
}
```

---

# 8. Important Point

Repository ko directly controller mein use karna technically possible hai.

But larger applications mein:

```text
Controller → Service → Repository
```

separation better architecture provide karta hai.

---

# Quick Revision

```text
JpaRepository<Entity, ID>

ProductRepository
        ↓
JpaRepository<Product, Long>
        ↓
Built-in CRUD operations
```

# Repository Completed ✅