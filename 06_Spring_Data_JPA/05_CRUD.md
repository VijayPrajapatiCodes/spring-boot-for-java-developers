# CRUD with Spring Data JPA

CRUD:

```text
C → Create
R → Read
U → Update
D → Delete
```

---

# 1. CREATE

```java
Product product = new Product();

product.setName("Laptop");
product.setPrice(50000);

productRepository.save(product);
```

For a new entity, this results in persistence/INSERT behavior.

Conceptually:

```sql
INSERT INTO products ...
```

---

# 2. READ ALL

```java
List<Product> products =
        productRepository.findAll();
```

Conceptually:

```sql
SELECT * FROM products;
```

---

# 3. READ BY ID

```java
Optional<Product> product =
        productRepository.findById(1L);
```

`findById()` returns:

```java
Optional<Product>
```

Example handling:

```java
Product product = productRepository
        .findById(id)
        .orElseThrow(() ->
                new ProductNotFoundException(
                        "Product not found"
                )
        );
```

---

# 4. UPDATE

First existing entity fetch karo:

```java
Product product = productRepository
        .findById(id)
        .orElseThrow();
```

Modify:

```java
product.setName("Gaming Laptop");
product.setPrice(70000);
```

Save:

```java
productRepository.save(product);
```

---

# 5. DELETE

```java
productRepository.deleteById(id);
```

Or:

```java
productRepository.delete(product);
```

---

# 6. existsById()

```java
boolean exists =
        productRepository.existsById(id);
```

Useful:

```java
if (!productRepository.existsById(id)) {
    throw new ProductNotFoundException(
            "Product not found"
    );
}
```

---

# 7. count()

```java
long totalProducts =
        productRepository.count();
```

---

# CRUD Flow

```text
POST
 ↓
save()
 ↓
INSERT


GET
 ↓
findAll() / findById()
 ↓
SELECT


PUT
 ↓
findById()
 ↓
modify
 ↓
save()
 ↓
UPDATE


DELETE
 ↓
deleteById()
 ↓
DELETE
```

# CRUD Completed ✅