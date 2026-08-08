# Spring Data JPA Query Methods

## 1. What are Query Methods?

Spring Data JPA method name ko analyze karke query derive kar sakta hai.

Example:

```java
List<Product> findByCategory(String category);
```

Conceptually:

```sql
SELECT *
FROM products
WHERE category = ?;
```

Hume basic cases mein SQL manually nahi likhni padti.

---

# 2. findBy

```java
Product findByName(String name);
```

---

# 3. Multiple Conditions

```java
List<Product> findByCategoryAndPrice(
        String category,
        double price
);
```

Conceptually:

```text
WHERE category = ?
AND price = ?
```

---

# 4. OR

```java
List<Product> findByNameOrCategory(
        String name,
        String category
);
```

---

# 5. GreaterThan

```java
List<Product> findByPriceGreaterThan(
        double price
);
```

Conceptually:

```sql
WHERE price > ?
```

---

# 6. LessThan

```java
List<Product> findByPriceLessThan(
        double price
);
```

---

# 7. Between

```java
List<Product> findByPriceBetween(
        double min,
        double max
);
```

Conceptually:

```sql
WHERE price BETWEEN ? AND ?
```

---

# 8. Like / Containing

```java
List<Product> findByNameContaining(
        String keyword
);
```

Useful for simple search.

---

# 9. IgnoreCase

```java
List<Product> findByNameIgnoreCase(
        String name
);
```

---

# 10. StartingWith / EndingWith

```java
findByNameStartingWith(String prefix);

findByNameEndingWith(String suffix);
```

---

# 11. OrderBy

```java
List<Product>
findByCategoryOrderByPriceAsc(
        String category
);
```

Or:

```java
findByCategoryOrderByPriceDesc(
        String category
);
```

---

# 12. Boolean Queries

```java
boolean existsByName(String name);
```

```java
long countByCategory(String category);
```

```java
void deleteByName(String name);
```

---

# Common Keywords

```text
findBy
And
Or
GreaterThan
LessThan
Between
Containing
Like
IgnoreCase
StartingWith
EndingWith
OrderBy
ExistsBy
CountBy
DeleteBy
```

---

# Important

Simple queries:

```text
Query Methods ✅
```

Complex queries:

```text
@Query / Specifications / Criteria etc.
```

use karna better ho sakta hai.

# Query Methods Completed ✅