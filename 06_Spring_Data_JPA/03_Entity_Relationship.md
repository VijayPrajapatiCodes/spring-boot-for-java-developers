# Entity Relationships in Spring Data JPA

## 1. What is an Entity Relationship?

Real applications mein entities ek dusre se related hoti hain.

Example:

```text
Category → Products

User → Orders

Order → OrderItems

User → Profile
```

Relational database mein relationships commonly Foreign Keys ke through represent hoti hain.

JPA annotations Java entities ke beech relationships map karne mein help karte hain.

---

# 2. Four Main Relationships

```text
@OneToOne

@OneToMany

@ManyToOne

@ManyToMany
```

Easy examples:

```text
One User       → One Profile

One Category   → Many Products

Many Products  → One Category

Many Students  → Many Courses
```

---

# 3. @OneToOne

Example:

```text
User → Profile
```

One user ka one profile.

```java
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;
}
```

Profile:

```java
@Entity
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bio;
}
```

Database concept:

```text
users

id
name
profile_id FK
```

---

# 4. @ManyToOne

Backend applications mein bahut common relationship.

Example:

```text
Laptop   ─┐
Mouse    ─┼──→ Electronics
Keyboard ─┘
```

Many Products belong to One Category.

Product:

```java
@ManyToOne
@JoinColumn(name = "category_id")
private Category category;
```

Category:

```java
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
}
```

Database:

```text
products

id
name
category_id
```

`category_id` Foreign Key hai.

---

# 5. @OneToMany

Same relationship Category side se:

```text
One Category
     ↓
Many Products
```

Category:

```java
@OneToMany(mappedBy = "category")
private List<Product> products;
```

Product:

```java
@ManyToOne
@JoinColumn(name = "category_id")
private Category category;
```

Together:

```text
Category
   │
   │ @OneToMany
   ↓
Products

Products
   │
   │ @ManyToOne
   ↓
Category
```

---

# 6. @JoinColumn

Foreign Key column configure karta hai.

```java
@ManyToOne
@JoinColumn(name = "category_id")
private Category category;
```

Database:

```text
products.category_id
        ↓
categories.id
```

---

# 7. mappedBy

Example:

```java
@OneToMany(mappedBy = "category")
private List<Product> products;
```

Important:

```text
"category"
```

database column nahi hai.

Ye Product entity ke Java field ka naam hai:

```java
private Category category;
```

Therefore:

```java
mappedBy = "category"
```

means relationship Product ke `category` field ke through mapped hai.

---

# 8. Owning Side

Jis side ke paas actual relationship/Foreign Key mapping hoti hai, wo owning side hoti hai.

Example:

```java
@ManyToOne
@JoinColumn(name = "category_id")
private Category category;
```

Here:

```text
Product = Owning Side
```

because `products` table mein:

```text
category_id
```

Foreign Key hai.

Category:

```java
@OneToMany(mappedBy = "category")
private List<Product> products;
```

inverse side hai.

---

# 9. Unidirectional Relationship

Agar sirf Product Category ko know karta hai:

```java
class Product {

    @ManyToOne
    private Category category;
}
```

but Category mein:

```java
List<Product>
```

nahi hai.

Then:

```text
Product → Category
```

Unidirectional relationship.

---

# 10. Bidirectional Relationship

Dono entities ek dusre ko reference karein:

```java
class Product {

    @ManyToOne
    private Category category;
}
```

and:

```java
class Category {

    @OneToMany(mappedBy = "category")
    private List<Product> products;
}
```

Then:

```text
Product ↔ Category
```

Bidirectional relationship.

---

# 11. @ManyToMany

Example:

```text
Student ↔ Course
```

One student multiple courses join kar sakta hai.

One course multiple students ka ho sakta hai.

```java
@ManyToMany
private List<Course> courses;
```

Many-to-Many normally join table use karta hai.

---

# 12. @JoinTable

Example:

```java
@ManyToMany
@JoinTable(
    name = "student_courses",

    joinColumns =
        @JoinColumn(name = "student_id"),

    inverseJoinColumns =
        @JoinColumn(name = "course_id")
)
private List<Course> courses;
```

Database:

```text
students
    ↓
student_courses
    ↑
courses
```

Join table:

```text
student_courses

student_id
course_id
```

---

# 13. Cascade

Kabhi parent operation ko related child entity tak propagate karna hota hai.

Example:

```java
@OneToMany(
    mappedBy = "order",
    cascade = CascadeType.ALL
)
private List<OrderItem> items;
```

Common cascade types:

```text
PERSIST
MERGE
REMOVE
REFRESH
DETACH
ALL
```

---

## CascadeType.PERSIST

Parent persist karne par child persist operation bhi cascade ho sakta hai.

---

## CascadeType.MERGE

Merge operation child tak propagate ho sakta hai.

---

## CascadeType.REMOVE

Parent remove hone par child removal bhi cascade ho sakta hai.

Carefully use karna chahiye.

---

## CascadeType.ALL

All supported cascade operations apply karta hai.

```java
cascade = CascadeType.ALL
```

---

# 14. Fetch Type

Related entities kab fetch honi chahiye, fetch strategy is behavior ko control karti hai.

Main types:

```text
FetchType.LAZY

FetchType.EAGER
```

---

# 15. LAZY

```java
@OneToMany(
    mappedBy = "category",
    fetch = FetchType.LAZY
)
private List<Product> products;
```

Concept:

```text
Category fetch
     ↓
Products association immediately initialize karna required nahi
     ↓
Needed when accessed
```

Large relationships mein useful ho sakta hai.

---

# 16. EAGER

```java
@ManyToOne(fetch = FetchType.EAGER)
private Category category;
```

Association eagerly fetched hoti hai.

Blindly EAGER use karna unnecessary queries/data loading cause kar sakta hai.

---

# 17. Default Fetch Types

JPA defaults:

```text
@OneToOne   → EAGER

@ManyToOne  → EAGER

@OneToMany  → LAZY

@ManyToMany → LAZY
```

---

# 18. orphanRemoval

Example:

```java
@OneToMany(
    mappedBy = "order",
    cascade = CascadeType.ALL,
    orphanRemoval = true
)
private List<OrderItem> items;
```

Suppose:

```text
Order
 ├── Item A
 ├── Item B
 └── Item C
```

Item B association se remove hua.

```java
order.getItems().remove(itemB);
```

`orphanRemoval = true` orphaned child ko delete karne ke behavior mein use hota hai.

---

# 19. Real E-Commerce Relationships

```text
User
 │
 └── OneToMany
        ↓
      Orders
        │
        └── OneToMany
               ↓
           OrderItems
               │
               └── ManyToOne
                      ↓
                   Product
                      │
                      └── ManyToOne
                             ↓
                         Category
```

---

# 20. Product and Category Example

Category:

```java
@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "category")
    private List<Product> products;
}
```

Product:

```java
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
```

Database:

```text
categories
----------------
id
name
```

```text
products
----------------
id
name
category_id FK
```

Relationship:

```text
categories.id
      ↑
      │
products.category_id
```

---

# 21. Circular JSON Problem

Bidirectional relationship:

```text
Category
   ↓
Products
   ↓
Category
   ↓
Products
   ↓
...
```

Entities ko directly REST responses mein expose karne par circular serialization problems aa sakti hain.

Isi liye DTO layer useful hai:

```text
Entity
   ↓
Mapper
   ↓
DTO
   ↓
JSON Response
```

---

# 22. Relationship + DTO Example

Entity relationship:

```text
Product → Category
```

Lekin API response:

```json
{
  "id": 101,
  "name": "Laptop",
  "categoryName": "Electronics"
}
```

Response DTO:

```java
public class ProductResponse {

    private Long id;
    private String name;
    private String categoryName;
}
```

API ko complete entity graph expose karna necessary nahi hai.

---

# 23. Foreign Key Mental Model

Java:

```java
@ManyToOne
@JoinColumn(name = "category_id")
private Category category;
```

Database:

```text
Product.category
      ↓
category_id
      ↓
Foreign Key
      ↓
Category.id
```

---

# 24. Choosing Relationship

Ask:

```text
One record kitne records se related hai?

Foreign Key kis table mein honi chahiye?

Kya relation one-way enough hai?

Kya both directions required hain?

Cascade required hai?

Related data kab load karna hai?
```

Then mapping choose karo.

---

# 25. Relationship Summary

| Annotation | Relationship | Example |
|---|---|---|
| `@OneToOne` | One → One | User → Profile |
| `@OneToMany` | One → Many | Category → Products |
| `@ManyToOne` | Many → One | Products → Category |
| `@ManyToMany` | Many ↔ Many | Students ↔ Courses |

---

# 26. Important Annotations

```text
@OneToOne
→ One-to-One relationship

@OneToMany
→ One-to-Many relationship

@ManyToOne
→ Many-to-One relationship

@ManyToMany
→ Many-to-Many relationship

@JoinColumn
→ Foreign Key column

@JoinTable
→ Join table configuration
```

---

# 27. Interview Questions

## What are the four JPA relationships?

```text
@OneToOne
@OneToMany
@ManyToOne
@ManyToMany
```

## What is @JoinColumn?

Foreign Key column ko configure karta hai.

```java
@JoinColumn(name = "category_id")
```

## What is mappedBy?

Bidirectional relationship ke inverse side par owning-side Java field ko identify karta hai.

```java
mappedBy = "category"
```

## What is owning side?

Relationship ka wo side jo actual relationship mapping/Foreign Key ko manage karta hai.

## LAZY vs EAGER?

```text
LAZY
→ Association loading deferred ho sakti hai.

EAGER
→ Association eagerly fetched hoti hai.
```

## What is Cascade?

Persistence operations ko related entities tak propagate karne ka mechanism.

## What is orphanRemoval?

Association se orphan hue dependent child ko remove/delete karne ke behavior ke liye use hota hai.

## @OneToMany vs @ManyToOne?

Same relationship ko opposite perspectives se describe kar sakte hain:

```text
Category → Products
@OneToMany

Product → Category
@ManyToOne
```

---

# Quick Revision

```text
@OneToOne
→ One ↔ One

@OneToMany
→ One → Many

@ManyToOne
→ Many → One

@ManyToMany
→ Many ↔ Many

@JoinColumn
→ Foreign Key

@JoinTable
→ Intermediate Join Table

mappedBy
→ Inverse-side mapping

Cascade
→ Operations propagate

LAZY
→ Deferred association loading

EAGER
→ Eager association loading

orphanRemoval
→ Orphan child removal
```

---

# Final Flow

```text
Java Entities
      ↓
JPA Relationship Annotations
      ↓
Hibernate
      ↓
Foreign Keys / Join Tables
      ↓
MySQL
```

# Entity Relationships Completed ✅