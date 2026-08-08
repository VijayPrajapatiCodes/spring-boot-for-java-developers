# Custom Queries in Spring Data JPA

Jab derived query methods inconvenient ya insufficient ho jayein, custom queries likh sakte hain.

Main approaches:

```text
JPQL
Native SQL
```

---

# 1. @Query

Example:

```java
@Query("""
       SELECT p
       FROM Product p
       WHERE p.price > :price
       """)
List<Product> findExpensiveProducts(
        @Param("price") double price
);
```

---

# 2. JPQL

JPQL database table ke bajaye entities aur unke fields ke terms mein query karti hai.

Example:

```java
SELECT p
FROM Product p
WHERE p.name = :name
```

Here:

```text
Product → Entity
p.name  → Entity field
```

---

# 3. Named Parameters

```java
@Query("""
       SELECT p
       FROM Product p
       WHERE p.category = :category
       """)
List<Product> getProducts(
        @Param("category") String category
);
```

---

# 4. Native Query

Actual database SQL use karni ho:

```java
@Query(
    value = """
            SELECT *
            FROM products
            WHERE price > :price
            """,
    nativeQuery = true
)
List<Product> findProducts(
        @Param("price") double price
);
```

---

# JPQL vs Native SQL

```text
JPQL
→ Entities and entity fields

Native SQL
→ Actual tables and columns
```

JPQL:

```java
SELECT p FROM Product p
```

SQL:

```sql
SELECT * FROM products;
```

---

# 5. UPDATE/DELETE Custom Query

Data-modifying custom queries ke saath:

```java
@Modifying
```

use hota hai.

Example:

```java
@Modifying
@Query("""
       UPDATE Product p
       SET p.stock = :stock
       WHERE p.id = :id
       """)
int updateStock(
        @Param("id") Long id,
        @Param("stock") int stock
);
```

Such operations generally transaction boundary ke andar execute ki jati hain.

---

# When to use what?

```text
Simple query
    ↓
Query Method

More custom entity-based query
    ↓
JPQL

Database-specific SQL required
    ↓
Native Query
```

# Custom Queries Completed ✅