# 04 - Lombok in Spring Boot

## 1. What is Lombok?

Lombok ek Java library hai jo repetitive boilerplate code ko reduce karti hai.

Without Lombok:

```java
public class Product {

    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
```

With Lombok:

```java
@Getter
@Setter
public class Product {

    private Long id;
    private String name;
}
```

Lombok required methods compile time par generate kar deta hai.

---

# 2. Lombok Dependency

Spring Boot Maven project:

```xml
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
</dependency>
```

Maven project reload karna na bhulein.

---

# 3. `@Getter`

`@Getter` getter methods generate karta hai.

```java
@Getter
public class Product {

    private Long id;
    private String name;
}
```

Conceptually Lombok generates:

```java
public Long getId() {
    return id;
}

public String getName() {
    return name;
}
```

Usage:

```java
product.getId();
product.getName();
```

---

# 4. `@Setter`

`@Setter` setter methods generate karta hai.

```java
@Setter
public class Product {

    private Long id;
    private String name;
}
```

Conceptually:

```java
public void setId(Long id) {
    this.id = id;
}

public void setName(String name) {
    this.name = name;
}
```

Usage:

```java
product.setId(101L);
product.setName("Laptop");
```

---

# 5. `@Getter` + `@Setter`

Most common basic usage:

```java
@Getter
@Setter
public class Product {

    private Long id;
    private String name;
    private double price;
}
```

Instead of manually writing all getters and setters.

---

# 6. `@NoArgsConstructor`

No-argument constructor generate karta hai.

```java
@NoArgsConstructor
public class Product {

    private Long id;
    private String name;
}
```

Conceptually:

```java
public Product() {
}
```

Usage:

```java
Product product = new Product();
```

Ye frameworks/tools ke saath useful ho sakta hai jahan no-arg constructor required hota hai.

---

# 7. `@AllArgsConstructor`

Class ke fields ke liye all-arguments constructor generate karta hai.

```java
@AllArgsConstructor
public class Product {

    private Long id;
    private String name;
    private double price;
}
```

Conceptually:

```java
public Product(
        Long id,
        String name,
        double price) {

    this.id = id;
    this.name = name;
    this.price = price;
}
```

Usage:

```java
Product product =
        new Product(
                101L,
                "Laptop",
                50000
        );
```

---

# 8. Constructor Combination

Common combination:

```java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    private Long id;
    private String name;
    private double price;
    private String category;
    private int stock;
}
```

Now both available:

```java
Product p1 = new Product();
```

and:

```java
Product p2 =
        new Product(
                101L,
                "Laptop",
                50000,
                "Electronics",
                10
        );
```

---

# 9. `@Builder`

`@Builder` Builder Pattern implement karne mein help karta hai.

```java
@Builder
public class Product {

    private Long id;
    private String name;
    private double price;
    private String category;
    private int stock;
}
```

Object:

```java
Product product =
        Product.builder()
                .id(101L)
                .name("Laptop")
                .price(50000)
                .category("Electronics")
                .stock(10)
                .build();
```

---

# 10. Why Builder?

Normal constructor:

```java
new Product(
    101L,
    "Laptop",
    50000,
    "Electronics",
    10
);
```

Isme values ka meaning immediately clear nahi hota.

Builder:

```java
Product.builder()
    .id(101L)
    .name("Laptop")
    .price(50000)
    .category("Electronics")
    .stock(10)
    .build();
```

More readable:

```text
101L          → id
Laptop        → name
50000         → price
Electronics   → category
10            → stock
```

Builder especially useful hai jab object mein bahut fields ho.

---

# 11. `@Data`

Lombok ka convenience annotation:

```java
@Data
public class Product {

    private Long id;
    private String name;
    private double price;
}
```

`@Data` broadly includes functionality such as:

```text
@Getter
@Setter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
```

Isliye simple DTOs mein convenient ho sakta hai.

---

# 12. `@ToString`

Automatically `toString()` generate karta hai.

```java
@ToString
public class Product {

    private Long id;
    private String name;
}
```

Then:

```java
System.out.println(product);
```

Readable object representation mil sakti hai.

---

# 13. `@EqualsAndHashCode`

Automatically:

```java
equals()
hashCode()
```

generate karta hai.

```java
@EqualsAndHashCode
public class Product {

    private Long id;
    private String name;
}
```

Objects compare karne aur hash-based collections ke behavior ke liye useful hai.

---

# 14. `@RequiredArgsConstructor`

`final` fields aur `@NonNull` fields ke liye constructor generate karta hai.

Spring Boot dependency injection mein bahut useful hai.

Example:

```java
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
}
```

Without Lombok:

```java
public ProductService(
        ProductRepository productRepository,
        ProductMapper productMapper) {

    this.productRepository = productRepository;
    this.productMapper = productMapper;
}
```

So:

```text
@RequiredArgsConstructor
        ↓
Constructor Injection
        ↓
Less Boilerplate
```

---

# 15. Lombok With DTO

Request DTO:

```java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {

    private String name;
    private double price;
    private String category;
    private int stock;
}
```

Response DTO:

```java
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {

    private Long id;
    private String name;
    private double price;
    private String category;
}
```

---

# 16. Lombok With Validation

Lombok aur Jakarta Validation ek saath use kar sakte hain.

```java
@Getter
@Setter
public class ProductRequest {

    @NotBlank
    private String name;

    @Positive
    private double price;

    @NotBlank
    private String category;

    @PositiveOrZero
    private int stock;
}
```

Responsibilities different hain:

```text
Lombok
→ Boilerplate code reduce

Validation
→ Input validate
```

---

# 17. Lombok With MapStruct

MapStruct + Lombok real projects mein use kiye ja sakte hain.

Example DTO:

```java
@Getter
@Setter
public class ProductRequest {

    private String name;
    private double price;
}
```

Mapper:

```java
@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product toEntity(ProductRequest request);

    ProductResponse toResponse(Product product);
}
```

Concept:

```text
ProductRequest
      ↓
   MapStruct
      ↓
Product Entity
```

Lombok accessors/boilerplate reduce karta hai, jabki MapStruct objects ke beech mapping handle karta hai.

---

# 18. Lombok + MapStruct Are Different

In dono ko confuse nahi karna.

```text
LOMBOK
   ↓
Java boilerplate reduce
   ↓
Getter / Setter
Constructor
Builder
toString
etc.


MAPSTRUCT
   ↓
Object Mapping
   ↓
DTO ↔ Entity
```

Dono different problems solve karte hain.

---

# 19. Lombok + ModelMapper

ModelMapper ke saath bhi Lombok use kiya ja sakta hai.

Example:

```java
@Getter
@Setter
@NoArgsConstructor
public class Product {

    private Long id;
    private String name;
}
```

Then:

```java
Product product =
        modelMapper.map(
                request,
                Product.class
        );
```

`@NoArgsConstructor` destination object creation requirements ke cases mein useful ho sakta hai.

---

# 20. Practical Product Example

```java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    private Long id;
    private String name;
    private double price;
    private String category;
    private int stock;
}
```

Builder:

```java
Product product =
        Product.builder()
                .id(101L)
                .name("Laptop")
                .price(50000)
                .category("Electronics")
                .stock(10)
                .build();
```

Getter:

```java
product.getName();
```

Setter:

```java
product.setPrice(55000);
```

---

# 21. Useful Lombok Annotations

| Annotation | Purpose |
|---|---|
| `@Getter` | Generates getters |
| `@Setter` | Generates setters |
| `@NoArgsConstructor` | No-argument constructor |
| `@AllArgsConstructor` | All-fields constructor |
| `@RequiredArgsConstructor` | Required/final fields constructor |
| `@Builder` | Builder pattern |
| `@ToString` | Generates `toString()` |
| `@EqualsAndHashCode` | Generates `equals()` and `hashCode()` |
| `@Data` | Convenience bundle of common Lombok features |

---

# 22. Should We Always Use `@Data`?

Not necessarily.

Instead of blindly:

```java
@Data
@Entity
public class Product {
}
```

it can be clearer to explicitly choose what is needed:

```java
@Getter
@Setter
@NoArgsConstructor
public class Product {
}
```

Especially JPA entities mein `equals()`, `hashCode()` aur `toString()` behavior carefully design karna important hota hai.

So:

```text
DTO
→ @Data can be convenient

Entity
→ Prefer deliberate Lombok annotations
```

depending on project requirements.

---

# 23. `@Builder` vs Constructor

Constructor:

```java
new Product(
    101L,
    "Laptop",
    50000,
    "Electronics",
    10
);
```

Builder:

```java
Product.builder()
    .id(101L)
    .name("Laptop")
    .price(50000)
    .category("Electronics")
    .stock(10)
    .build();
```

Builder is usually easier to read when there are many parameters.

---

# 24. Lombok Does Not Replace Business Logic

Lombok mainly boilerplate reduce karta hai.

It does NOT replace:

```text
Service logic
Validation logic
Database logic
Mapping logic
Business rules
```

Example:

```java
@Getter
@Setter
public class Product {
}
```

doesn't mean Lombok automatically:

```text
save product
validate product
map DTO
query database
```

karega.

---

# 25. Interview Questions

## Q1. What is Lombok?

Lombok is a Java library that reduces boilerplate code using annotations.

---

## Q2. What does `@Getter` do?

Getter methods generate karta hai.

---

## Q3. What does `@Setter` do?

Setter methods generate karta hai.

---

## Q4. What is `@NoArgsConstructor`?

No-argument constructor generate karta hai.

```java
public Product() {
}
```

---

## Q5. What is `@AllArgsConstructor`?

All fields ko arguments mein lene wala constructor generate karta hai.

---

## Q6. What does `@Builder` do?

Builder Pattern based object creation API generate karta hai.

Example:

```java
Product.builder()
       .name("Laptop")
       .price(50000)
       .build();
```

---

## Q7. What is `@Data`?

Convenience annotation hai jo common Lombok functionality combine karta hai, including getters, setters, `toString`, `equals/hashCode`, and required-args constructor behavior.

---

## Q8. What is `@RequiredArgsConstructor` useful for?

Spring constructor injection ke boilerplate ko reduce karne mein useful hai.

```java
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;
}
```

---

## Q9. Lombok vs MapStruct?

```text
Lombok
→ Boilerplate generation

MapStruct
→ Object mapping/code generation
```

---

## Q10. Can Lombok and MapStruct be used together?

Yes.

They solve different problems.

---

# 26. Quick Revision

```text
Lombok
→ Boilerplate reduce

@Getter
→ Getter methods

@Setter
→ Setter methods

@NoArgsConstructor
→ Empty constructor

@AllArgsConstructor
→ All-fields constructor

@RequiredArgsConstructor
→ Required/final fields constructor

@Builder
→ Builder Pattern

@Data
→ Common Lombok functionality bundle

@ToString
→ toString()

@EqualsAndHashCode
→ equals() + hashCode()
```

---

# Final Mental Model

```text
                  NORMAL JAVA

                     Class
                       ↓
        ┌──────────────┼──────────────┐
        ↓              ↓              ↓
     Getters        Setters      Constructors
        ↓              ↓              ↓
            Lots of Boilerplate


                    LOMBOK

                     Class
                       ↓
              Lombok Annotations
                       ↓
        ┌──────────────┼──────────────┐
        ↓              ↓              ↓
     @Getter        @Setter       Constructors
                                  @Builder
                       ↓
              Generated Code
```

# Lombok Completed ✅