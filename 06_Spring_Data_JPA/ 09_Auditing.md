# Spring Data JPA Auditing

## 1. What is Auditing?

Auditing automatically track karne mein help karti hai:

```text
Record kab create hua?

Record last kab update hua?

Record kis user ne create kiya?

Record kis user ne update kiya?
```

Common fields:

```text
createdAt
updatedAt
createdBy
updatedBy
```

---

# 2. Enable JPA Auditing

Configuration/main configuration class:

```java
@EnableJpaAuditing
```

Example:

```java
@SpringBootApplication
@EnableJpaAuditing
public class SpringbootLearningApplication {

    public static void main(String[] args) {
        SpringApplication.run(
                SpringbootLearningApplication.class,
                args
        );
    }
}
```

---

# 3. Entity Listener

Entity:

```java
@EntityListeners(
        AuditingEntityListener.class
)
```

Example:

```java
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Product {

}
```

---

# 4. @CreatedDate

```java
@CreatedDate
@Column(updatable = false)
private LocalDateTime createdAt;
```

Entity create hone par creation timestamp automatically populate karne ke liye auditing infrastructure use kar sakti hai.

---

# 5. @LastModifiedDate

```java
@LastModifiedDate
private LocalDateTime updatedAt;
```

Entity modify hone par last-modified timestamp update karne ke liye.

---

# 6. Example

```java
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
```

---

# 7. @CreatedBy

Current record kis user ne create kiya:

```java
@CreatedBy
private String createdBy;
```

Iske liye generally `AuditorAware` configuration required hoti hai.

---

# 8. @LastModifiedBy

```java
@LastModifiedBy
private String updatedBy;
```

Last modification kis user ne ki, track karne mein useful.

---

# 9. AuditorAware

Current authenticated user provide karne ke liye:

```java
@Bean
public AuditorAware<String> auditorProvider() {

    return () ->
            Optional.of("Vijay");
}
```

Real applications mein value usually authenticated user/security context se aati hai.

---

# 10. Complete Auditing Fields

```java
@CreatedDate
@Column(updatable = false)
private LocalDateTime createdAt;

@LastModifiedDate
private LocalDateTime updatedAt;

@CreatedBy
private String createdBy;

@LastModifiedBy
private String updatedBy;
```

---

# 11. Why Auditing?

Useful for:

```text
Admin panels
Order systems
Banking applications
Inventory systems
E-commerce
Enterprise applications
Activity tracking
```

---

# 12. @PrePersist vs Auditing

Earlier approach:

```java
@PrePersist
public void onCreate() {
    createdAt = LocalDateTime.now();
}
```

Spring Data auditing:

```java
@CreatedDate
private LocalDateTime createdAt;
```

For applications already using Spring Data JPA, auditing gives a reusable mechanism for common audit metadata.

---

# Final Flow

```text
Entity Save
    ↓
AuditingEntityListener
    ↓
@CreatedDate
    ↓
createdAt


Entity Update
    ↓
AuditingEntityListener
    ↓
@LastModifiedDate
    ↓
updatedAt
```

# JPA Auditing Completed ✅