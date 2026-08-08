# JPA Entity in Spring Boot

## 1. What is an Entity?

JPA Entity ek Java class hoti hai jo database table ko represent karti hai.

```text
Java Class
    ↓
@Entity
    ↓
Hibernate / JPA
    ↓
Database Table
```

Example:

```java
@Entity
public class Product {
}
```

Hibernate `Product` class ko database entity ke roop mein treat karega.

---

# 2. Entity Example

```java
package com.vijay.springbootlearning.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "products_name",
            nullable = false,
            length = 100
    )
    private String name;

    @Column(nullable = false)
    private double price;

    @Column(length = 50)
    private String category;

    @Column(nullable = false)
    private int stock;
}
```

Database:

```text
products
--------------------------------
id
products_name
price
category
stock
```

---

# 3. @Entity

```java
@Entity
public class Product {
}
```

`@Entity` JPA ko batata hai ki ye class database entity hai.

Without:

```java
@Entity
```

Hibernate class ko database table ke saath map nahi karega.

---

# 4. @Table

Database table ka naam customize karne ke liye:

```java
@Entity
@Table(name = "products")
public class Product {
}
```

Result:

```text
Product.java
    ↓
products table
```

Without `@Table`, JPA/Hibernate default naming rules use karega.

---

# 5. @Id

Har Entity ko identifier chahiye.

```java
@Id
private Long id;
```

Database:

```text
id → PRIMARY KEY
```

Example:

```java
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
```

---

# 6. @GeneratedValue

Primary key automatically generate karne ke liye:

```java
@GeneratedValue(strategy = GenerationType.IDENTITY)
```

MySQL mein typically:

```text
AUTO_INCREMENT
```

Result:

```text
id BIGINT PRIMARY KEY AUTO_INCREMENT
```

---

# 7. Generation Strategies

Common strategies:

```java
GenerationType.IDENTITY
GenerationType.SEQUENCE
GenerationType.TABLE
GenerationType.AUTO
```

## IDENTITY

```java
@GeneratedValue(strategy = GenerationType.IDENTITY)
```

Database identity/auto-increment mechanism use karta hai.

MySQL ke saath commonly use hota hai.

---

## SEQUENCE

```java
@GeneratedValue(strategy = GenerationType.SEQUENCE)
```

Database sequence use karta hai.

Sequence-supporting databases mein useful.

---

## AUTO

```java
@GeneratedValue(strategy = GenerationType.AUTO)
```

Provider/database ke according suitable strategy choose ki ja sakti hai.

---

# 8. @Column

Entity field ki database column mapping customize karne ke liye:

```java
@Column
private String name;
```

Example:

```java
@Column(
    name = "products_name",
    nullable = false,
    length = 100
)
private String name;
```

Result:

```text
products_name VARCHAR(100) NOT NULL
```

---

# 9. Important @Column Properties

## name

```java
@Column(name = "products_name")
private String name;
```

Java:

```text
name
```

Database:

```text
products_name
```

---

## nullable

```java
@Column(nullable = false)
private String name;
```

Database:

```text
NOT NULL
```

---

## unique

```java
@Column(unique = true)
private String sku;
```

Database mein unique constraint create karne ke liye use kiya ja sakta hai.

---

## length

```java
@Column(length = 100)
private String name;
```

String column ki length configure karta hai.

Example:

```text
VARCHAR(100)
```

---

# 10. Java → MySQL Mapping

Humare practical mein:

```java
private Long id;
private String name;
private double price;
private String category;
private int stock;
```

Hibernate ne map kiya:

```text
Long        → BIGINT
String      → VARCHAR
double      → DOUBLE
int         → INT
```

Example database structure:

```text
id             BIGINT
products_name  VARCHAR(100)
price          DOUBLE
category       VARCHAR(50)
stock          INT
```

---

# 11. Primitive vs Wrapper Types

Example:

```java
private double price;
private int stock;
```

Primitive fields Java mein `null` nahi ho sakte.

Whereas:

```java
private Double price;
private Integer stock;
```

wrapper types `null` represent kar sakte hain.

Entities design karte waqt is difference ko samajhna important hai.

---

# 12. @Enumerated

Java Enum ko database mein map karne ke liye:

```java
public enum ProductStatus {

    ACTIVE,
    INACTIVE,
    OUT_OF_STOCK
}
```

Entity:

```java
@Enumerated(EnumType.STRING)
private ProductStatus status;
```

Database mein values:

```text
ACTIVE
INACTIVE
OUT_OF_STOCK
```

---

# 13. EnumType.STRING

Recommended common approach:

```java
@Enumerated(EnumType.STRING)
private ProductStatus status;
```

Concept:

```text
ProductStatus.ACTIVE
        ↓
"ACTIVE"
```

Readable aur enum order changes ke against safer hai.

---

# 14. EnumType.ORDINAL

```java
@Enumerated(EnumType.ORDINAL)
private ProductStatus status;
```

Concept:

```text
ACTIVE        → 0
INACTIVE      → 1
OUT_OF_STOCK  → 2
```

Problem:

Enum order change hone par stored numbers ka meaning change ho sakta hai.

Therefore commonly prefer:

```java
EnumType.STRING
```

---

# 15. LocalDateTime Mapping

Entity:

```java
private LocalDateTime createdAt;

private LocalDateTime updatedAt;
```

Import:

```java
import java.time.LocalDateTime;
```

Hibernate ne humare practical mein map kiya:

```text
createdAt → created_at DATETIME(6)

updatedAt → updated_at DATETIME(6)
```

---

# 16. @PrePersist

Entity first time persist hone se pehle method execute karne ke liye:

```java
@PrePersist
public void onCreate() {

    createdAt = LocalDateTime.now();
    updatedAt = LocalDateTime.now();
}
```

Flow:

```text
New Entity
    ↓
@PrePersist
    ↓
createdAt set
updatedAt set
    ↓
INSERT
```

---

# 17. @PreUpdate

Entity update hone se pehle:

```java
@PreUpdate
public void onUpdate() {

    updatedAt = LocalDateTime.now();
}
```

Flow:

```text
Entity modified
      ↓
@PreUpdate
      ↓
updatedAt changed
      ↓
UPDATE
```

Later Spring Data JPA Auditing ke through timestamps ko aur clean tarike se manage kar sakte hain.

---

# 18. @Transient

Kabhi Entity mein field chahiye hoti hai lekin database mein uska column nahi chahiye.

Use:

```java
@Transient
private double discountedPrice;
```

Hibernate:

```text
discountedPrice
      ↓
Database mein persist nahi karega
```

Example:

```java
@Transient
public double getDiscountedPrice() {
    return price * 0.9;
}
```

Ye application-side calculated value ho sakti hai.

---

# 19. Entity Constructor

JPA entities ke liye no-argument constructor available hona chahiye.

Manual:

```java
public Product() {
}
```

Lombok:

```java
@NoArgsConstructor
```

Example:

```java
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Product {
}
```

---

# 20. Lombok with Entity

Instead of manually writing:

```text
getId()
setId()

getName()
setName()

constructor...
```

Lombok:

```java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
```

boilerplate reduce karta hai.

Entity relationships ke saath generated `toString`, `equals`, `hashCode` etc. ko blindly generate karna avoid karna chahiye.

---

# 21. ddl-auto

Configuration:

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: update
```

Common values:

```text
none
validate
update
create
create-drop
```

---

## update

```yaml
ddl-auto: update
```

Entity changes ke according schema update karne ki koshish karta hai.

Development/learning mein useful.

---

## create

```yaml
ddl-auto: create
```

Startup par schema recreate karta hai.

Existing data loss ho sakta hai.

---

## create-drop

```yaml
ddl-auto: create-drop
```

Application startup par schema create aur shutdown par drop kiya ja sakta hai.

---

## validate

```yaml
ddl-auto: validate
```

Schema ko automatically modify nahi karta.

Entity mapping aur existing schema ko validate karta hai.

---

# 22. Important ddl-auto Lesson

Humare practical mein:

```java
private String name;
```

pehle database mein:

```text
name VARCHAR(255)
```

tha.

Baad mein:

```java
@Column(name = "products_name")
private String name;
```

kiya.

`ddl-auto: update` ne old column ko guaranteed rename nahi kiya.

Result temporarily:

```text
name
products_name
```

dono columns aa gaye.

Therefore:

```text
ddl-auto: update
≠
Database Migration Tool
```

Production schema migrations ke liye Flyway/Liquibase jaise migration tools commonly use kiye jate hain.

---

# 23. @Column vs Validation

JPA:

```java
@Column(nullable = false)
```

Database schema constraint se related hai.

Validation:

```java
@NotBlank
private String name;
```

Application/request validation se related hai.

Concept:

```text
@NotBlank
   ↓
Application Validation

@Column(nullable = false)
   ↓
Database Schema
```

Dono ka purpose related ho sakta hai but same nahi hai.

---

# 24. Complete Product Entity

```java
@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "products_name",
            nullable = false,
            length = 100
    )
    private String name;

    @Column(nullable = false)
    private double price;

    @Column(length = 50)
    private String category;

    @Column(nullable = false)
    private int stock;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Transient
    private double discountedPrice;

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
```

---

# 25. Entity Lifecycle

Simple flow:

```text
Java Object
    ↓
@Entity
    ↓
JPA
    ↓
Hibernate
    ↓
SQL
    ↓
MySQL Table
```

---

# 26. Important Entity Annotations

```text
@Entity
→ Class ko JPA Entity banata hai

@Table
→ Database table configure karta hai

@Id
→ Primary Key

@GeneratedValue
→ ID generation

@Column
→ Column mapping/configuration

@Enumerated
→ Enum mapping

@Transient
→ Field database mein persist nahi hoti

@PrePersist
→ First persist se pehle callback

@PreUpdate
→ Update se pehle callback
```

---

# 27. Interview Questions

## What is an Entity?

Entity ek Java class hai jo JPA ke through persistent database data/table se map hoti hai.

## What is @Id?

Entity ka primary identifier define karta hai.

## What is @GeneratedValue?

Primary key generation strategy configure karta hai.

## What is @Column?

Database column mapping aur constraints customize karta hai.

## What is @Transient?

Field ko JPA persistence se exclude karta hai.

## EnumType.STRING vs ORDINAL?

```text
STRING  → Enum name store karta hai
ORDINAL → Enum position/index store karta hai
```

Generally STRING safer hota hai.

## What is @PrePersist?

Entity first time persist hone se pehle lifecycle callback execute karta hai.

## What is @PreUpdate?

Entity update hone se pehle lifecycle callback execute karta hai.

---

# Quick Revision

```text
@Entity
   ↓
Java Class = Persistent Entity

@Table
   ↓
Table configuration

@Id
   ↓
Primary Key

@GeneratedValue
   ↓
Automatic ID

@Column
   ↓
Column configuration

@Enumerated(EnumType.STRING)
   ↓
Enum value mapping

LocalDateTime
   ↓
Date/time column

@PrePersist
   ↓
Before first persist

@PreUpdate
   ↓
Before update

@Transient
   ↓
Not persisted
```

# Entity Completed ✅