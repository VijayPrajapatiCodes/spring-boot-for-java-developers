# 01 - JPA Introduction

## 1. What is JPA?

JPA stands for:

```text
Jakarta Persistence API
```

JPA Java applications mein relational database persistence ke liye ek **specification/standard** hai.

JPA khud database engine ya ORM implementation nahi hai.

It defines rules/APIs/annotations for mapping Java objects with relational database data.

Example:

```java
@Entity
public class Product {

    @Id
    private Long id;

    private String name;
    private double price;
}
```

Concept:

```text
Java Object                 Database

Product                     products
────────                    ────────
id          ←──────────→    id
name        ←──────────→    name
price       ←──────────→    price
```

---

# 2. What is ORM?

ORM stands for:

```text
Object Relational Mapping
```

ORM ka purpose Java objects/classes aur relational database tables ke beech mapping karna hai.

Example Java class:

```java
public class Product {

    private Long id;
    private String name;
    private double price;
}
```

Database table:

```text
products

+----+--------+---------+
| id | name   | price   |
+----+--------+---------+
| 1  | Laptop | 50000  |
+----+--------+---------+
```

Mapping:

```text
Java                     Database

Product                  products
   │                        │
   ├── id       ←──────→    id
   ├── name     ←──────→    name
   └── price    ←──────→    price
```

---

# 3. Before JPA - JDBC

JDBC stands for:

```text
Java Database Connectivity
```

JDBC Java application ko relational database se communicate karne ki low-level API provide karta hai.

Example:

```java
Connection connection =
        DriverManager.getConnection(
                url,
                username,
                password
        );

PreparedStatement statement =
        connection.prepareStatement(
                "INSERT INTO products(name, price) VALUES (?, ?)"
        );

statement.setString(1, "Laptop");
statement.setDouble(2, 50000);

statement.executeUpdate();
```

Flow:

```text
Java
 ↓
JDBC
 ↓
SQL
 ↓
MySQL
```

JDBC mein developer ko SQL aur database interaction ka kaafi code khud handle karna padta hai.

---

# 4. JDBC Example

Suppose product insert karna hai.

SQL:

```sql
INSERT INTO products(name, price)
VALUES ('Laptop', 50000);
```

JDBC:

```java
PreparedStatement ps =
        connection.prepareStatement(
            "INSERT INTO products(name, price) VALUES (?, ?)"
        );

ps.setString(1, product.getName());
ps.setDouble(2, product.getPrice());

ps.executeUpdate();
```

Large applications mein repeated database operations ke saath boilerplate increase ho sakta hai.

---

# 5. JPA Approach

JPA-based approach mein Java objects/entities ke through persistence model define kar sakte hain.

Example:

```java
@Entity
public class Product {

    @Id
    private Long id;

    private String name;

    private double price;
}
```

Conceptually:

```text
Product Object
      ↓
Persistence Layer
      ↓
Database Row
```

Developer application ko object-oriented model ke through design kar sakta hai.

---

# 6. Is JPA a Framework?

JPA ko ORM implementation samajhna incorrect hai.

```text
JPA
→ Specification / Standard
```

JPA defines:

```text
How entities are represented
How mappings are declared
Persistence APIs
Relationships
Persistence behavior
```

Lekin specification ko implement karne ke liye provider chahiye.

---

# 7. What is Hibernate?

Hibernate ek popular ORM framework hai aur JPA provider/implementation ke roop mein use ho sakta hai.

Mental model:

```text
JPA
 ↓
Rules / Specification
 ↓
Hibernate
 ↓
Implementation
```

Simple statement:

> JPA tells WHAT persistence standard should look like, while a provider such as Hibernate implements that functionality.

---

# 8. JPA vs Hibernate

```text
JPA
→ Specification

Hibernate
→ ORM framework / JPA provider
```

Example annotations:

```java
@Entity
@Id
@Table
@Column
```

JPA annotations ke through entity mapping define ki ja sakti hai.

Hibernate provider in mappings ko use karke actual persistence operations perform kar sakta hai.

---

# 9. Hibernate and JDBC

Hibernate relational database tak ultimately JDBC infrastructure ke through communicate kar sakta hai.

Concept:

```text
Java Application
       ↓
    Hibernate
       ↓
      JDBC
       ↓
     MySQL
```

Hibernate ORM layer provide karta hai, jabki JDBC lower-level database connectivity provide karta hai.

---

# 10. What is Spring Data JPA?

Spring Data JPA Spring ecosystem ka project hai jo JPA-based data access ko easier banata hai.

Major feature:

```text
Repository Abstraction
```

Example:

```java
public interface ProductRepository
        extends JpaRepository<Product, Long> {

}
```

Is interface se common database operations ke liye ready-made repository methods milte hain.

Examples:

```java
save()
findById()
findAll()
deleteById()
existsById()
count()
```

---

# 11. Without Spring Data JPA

Persistence operations manually manage karne ke liye more code required ho sakta hai.

Concept:

```text
Create persistence operation
Write persistence logic
Execute operation
Handle result
```

Spring Data JPA common repository operations ko simplify karta hai.

---

# 12. With Spring Data JPA

Repository:

```java
public interface ProductRepository
        extends JpaRepository<Product, Long> {

}
```

Save:

```java
productRepository.save(product);
```

Find:

```java
productRepository.findById(1L);
```

Find all:

```java
productRepository.findAll();
```

Delete:

```java
productRepository.deleteById(1L);
```

This significantly reduces repetitive repository code.

---

# 13. Complete Architecture

Typical Spring Boot application:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
Spring Data JPA
    ↓
JPA
    ↓
Hibernate
    ↓
JDBC
    ↓
MySQL
```

Important:

This is a useful mental model for understanding responsibilities.

---

# 14. JDBC vs JPA vs Hibernate vs Spring Data JPA

| Technology | Purpose |
|---|---|
| JDBC | Low-level Java database connectivity |
| JPA | Persistence/ORM specification |
| Hibernate | ORM framework and popular JPA provider |
| Spring Data JPA | Simplifies JPA data access using repository abstraction |

---

# 15. Easy Mental Model

```text
JDBC
│
│ Java ↔ Database connectivity
│
▼

JPA
│
│ Persistence standard/specification
│
▼

Hibernate
│
│ Implements ORM/JPA functionality
│
▼

Spring Data JPA
│
│ Makes repository/data-access code easier
│
▼
Developer writes less repetitive code
```

These are not four versions of the same technology.

Each has a different responsibility.

---

# 16. What Do We Use in Spring Boot?

For a typical Spring Boot CRUD/backend project, we generally work primarily with:

```text
Spring Data JPA
```

Example:

```java
public interface ProductRepository
        extends JpaRepository<Product, Long> {
}
```

Then:

```java
productRepository.save(product);
```

Underneath, a JPA provider such as Hibernate can handle ORM/persistence work.

---

# 17. Do We Need to Use Hibernate Directly?

Usually not for a normal Spring Boot CRUD application.

For example, instead of directly working with Hibernate APIs such as:

```java
Session
SessionFactory
```

we can commonly work with:

```java
JpaRepository
```

Example:

```java
productRepository.save(product);
```

So normal project flow:

```text
Our Code
   ↓
Spring Data JPA
   ↓
JPA
   ↓
Hibernate
   ↓
JDBC
   ↓
MySQL
```

---

# 18. When to Use Spring Data JPA?

Spring Data JPA is a strong choice for applications such as:

```text
REST APIs
CRUD applications
E-Commerce backends
Employee Management Systems
Student Management Systems
Order Management Systems
Business applications
```

Especially when application has relational data and Java entities.

Example:

```text
Product
User
Order
OrderItem
Employee
Department
```

---

# 19. When Would Direct Hibernate APIs Be Used?

Direct Hibernate-specific APIs may be useful when:

```text
A project specifically requires Hibernate APIs
Legacy application already uses Hibernate directly
Hibernate-specific functionality/control is required
Custom persistence architecture requires it
```

But for a normal Spring Boot REST backend:

```text
Spring Data JPA
```

is generally the starting choice.

---

# 20. Do We Still Need to Learn Hibernate?

Yes.

Even when Spring Data JPA is used, understanding Hibernate concepts is important.

Important concepts include:

```text
Entity Lifecycle
Persistence Context
Lazy Loading
Eager Loading
Dirty Checking
Cascade
Relationships
N+1 Query Problem
Transactions
```

So:

```text
Use Spring Data JPA
        +
Understand Hibernate
```

is a useful approach.

---

# 21. Spring Data JPA Dependency

Maven:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
```

MySQL driver:

```xml
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <scope>runtime</scope>
</dependency>
```

---

# 22. Database Configuration

Example `application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/springboot_learning
    username: root
    password: YOUR_PASSWORD

  jpa:
    hibernate:
      ddl-auto: update

    show-sql: true
```

Connection:

```text
Spring Boot
    ↓
DataSource
    ↓
JPA / Hibernate
    ↓
JDBC Driver
    ↓
MySQL
```

---

# 23. What is an Entity?

Entity ek Java class hoti hai jo persistence model/database table ko represent karti hai.

Example:

```java
@Entity
public class Product {

    @Id
    private Long id;

    private String name;

    private double price;
}
```

Concept:

```text
Product Java Class
        ↕
products Database Table
```

Entities ko next chapter mein detail mein cover karenge.

---

# 24. What is Repository?

Repository database access layer ko represent karta hai.

Example:

```java
public interface ProductRepository
        extends JpaRepository<Product, Long> {

}
```

Then:

```java
productRepository.save(product);

productRepository.findAll();

productRepository.findById(1L);

productRepository.deleteById(1L);
```

---

# 25. Typical Spring Boot JPA Flow

Suppose client product create karta hai:

```text
POST /api/products
```

Request:

```json
{
  "name": "Laptop",
  "price": 50000
}
```

Application flow:

```text
Client
  ↓
Controller
  ↓
ProductRequest DTO
  ↓
Service
  ↓
Product Entity
  ↓
Repository
  ↓
Spring Data JPA
  ↓
Hibernate
  ↓
MySQL
```

Response:

```text
MySQL
  ↓
Product Entity
  ↓
ProductResponse DTO
  ↓
Controller
  ↓
Client
```

---

# 26. JPA Does Not Mean No SQL

JPA/Hibernate use karne ka matlab ye nahi hai ki SQL knowledge unnecessary ho gayi.

SQL still important hai.

You should understand:

```text
SELECT
INSERT
UPDATE
DELETE
JOIN
INDEX
TRANSACTION
GROUP BY
ORDER BY
Database Design
```

Because ORM eventually relational database ke saath kaam karta hai.

Complex queries aur performance debugging mein SQL knowledge especially important hoti hai.

---

# 27. JPA and DTO Are Different

Do not confuse:

```text
Entity
→ Persistence/database model

DTO
→ Data transfer model
```

Example:

```text
ProductRequest
      ↓
MapStruct
      ↓
Product Entity
      ↓
Repository
      ↓
Database
```

Response:

```text
Database
      ↓
Product Entity
      ↓
MapStruct
      ↓
ProductResponse
```

---

# 28. Our Recommended Project Stack

For a typical Spring Boot backend:

```text
Spring Boot
    ↓
REST Controller
    ↓
Service
    ↓
DTO
    ↓
MapStruct
    ↓
Entity
    ↓
Spring Data JPA
    ↓
Hibernate
    ↓
MySQL
```

Lombok can be used to reduce Java boilerplate where appropriate.

---

# 29. Important Interview Questions

## Q1. What is JPA?

JPA stands for Jakarta Persistence API.

It is a specification for Java persistence and ORM.

---

## Q2. Is JPA a framework?

JPA itself is a specification/API standard, not an ORM implementation.

---

## Q3. What is Hibernate?

Hibernate is an ORM framework and can act as a JPA provider.

---

## Q4. Difference between JPA and Hibernate?

```text
JPA
→ Specification

Hibernate
→ Implementation/provider
```

---

## Q5. What is Spring Data JPA?

Spring Data JPA simplifies JPA-based data access by providing repository abstractions and related features.

---

## Q6. What is JDBC?

JDBC is Java's API for communicating with relational databases.

---

## Q7. Does Hibernate use JDBC?

For relational database access, Hibernate ultimately works through JDBC.

---

## Q8. Why use Spring Data JPA?

Because it reduces repetitive data-access code and provides repository abstractions.

---

## Q9. What is `JpaRepository`?

It is a Spring Data repository interface that provides common persistence operations.

Example:

```java
JpaRepository<Product, Long>
```

---

## Q10. Should we use Hibernate directly or Spring Data JPA?

For normal Spring Boot CRUD/business applications, commonly use:

```text
Spring Data JPA
```

and understand Hibernate as the underlying ORM/JPA provider.

Direct Hibernate APIs are mainly useful when specific requirements demand them.

---

# 30. Quick Revision

```text
JDBC
→ Java ↔ Relational Database connectivity

ORM
→ Object Relational Mapping

JPA
→ Persistence specification

Hibernate
→ ORM framework / JPA provider

Spring Data JPA
→ Repository abstraction over JPA

@Entity
→ Persistent entity class

JpaRepository
→ Common repository operations

MySQL
→ Relational database
```

---

# Most Important Rule

For a normal Spring Boot backend project:

```text
CODE WITH
     ↓
Spring Data JPA

UNDERSTAND
     ↓
JPA + Hibernate

DATABASE
     ↓
MySQL

FOUNDATION
     ↓
SQL + JDBC
```

---

# Final Mental Model

```text
                   CLIENT
                     ↓
                REST API
                     ↓
                Controller
                     ↓
                  Service
                     ↓
                   DTO
                     ↓
                 MapStruct
                     ↓
                  Entity
                     ↓
                Repository
                     ↓
             Spring Data JPA
                     ↓
                    JPA
                     ↓
                 Hibernate
                     ↓
                   JDBC
                     ↓
                   MySQL
```

# JPA Introduction Completed ✅