# 02 - ModelMapper in Spring Boot

## 1. What is ModelMapper?

ModelMapper ek Java object mapping library hai jo ek object ka data dusre object mein map karne mein help karti hai.

Example:

```text
ProductRequest
      ↓
  ModelMapper
      ↓
Product Entity
```

Aur:

```text
Product Entity
      ↓
  ModelMapper
      ↓
ProductResponse
```

---

# 2. Why ModelMapper?

Manual mapping:

```java
Product product = new Product();

product.setName(request.getName());
product.setPrice(request.getPrice());
product.setCategory(request.getCategory());
product.setStock(request.getStock());
```

ModelMapper:

```java
Product product =
        modelMapper.map(request, Product.class);
```

Isse repetitive mapping code reduce hota hai.

---

# 3. Dependency

`pom.xml`:

```xml
<dependency>
    <groupId>org.modelmapper</groupId>
    <artifactId>modelmapper</artifactId>
    <version>3.0.0</version>
</dependency>
```

> Version project ke according newer compatible version bhi use kiya ja sakta hai.

---

# 4. ModelMapper Configuration

Package:

```text
config/
└── ModelMapperConfig.java
```

Configuration:

```java
package com.vijay.springbootlearning.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
```

---

# 5. `@Configuration`

```java
@Configuration
```

Spring ko batata hai ki class configuration define karti hai.

---

# 6. `@Bean`

```java
@Bean
public ModelMapper modelMapper() {
    return new ModelMapper();
}
```

`ModelMapper` object Spring container mein register ho jata hai.

Concept:

```text
new ModelMapper()
       ↓
     @Bean
       ↓
Spring Container
       ↓
Application mein inject
```

---

# 7. Constructor Injection

Controller/service mein ModelMapper inject kar sakte hain:

```java
private final ModelMapper modelMapper;

public ProductController(ModelMapper modelMapper) {
    this.modelMapper = modelMapper;
}
```

Spring registered `ModelMapper` bean provide karega.

---

# 8. Basic Syntax

ModelMapper ka most important syntax:

```java
modelMapper.map(source, Destination.class);
```

Example:

```java
Product product =
        modelMapper.map(
                request,
                Product.class
        );
```

Yahan:

```text
request
→ Source

Product.class
→ Destination Type
```

---

# 9. Request DTO → Entity

Suppose:

```java
ProductRequest request;
```

Mapping:

```java
Product product =
        modelMapper.map(
                request,
                Product.class
        );
```

Flow:

```text
ProductRequest
      ↓
 modelMapper.map()
      ↓
Product
```

Matching properties automatically map ho sakti hain.

Example:

```text
ProductRequest          Product

name        ─────────→ name
price       ─────────→ price
category    ─────────→ category
stock       ─────────→ stock
```

---

# 10. Entity → Response DTO

```java
ProductResponse response =
        modelMapper.map(
                product,
                ProductResponse.class
        );
```

Flow:

```text
Product
   ↓
ModelMapper
   ↓
ProductResponse
```

Example:

```text
Product                 ProductResponse

id          ─────────→ id
name        ─────────→ name
price       ─────────→ price
category    ─────────→ category
```

A field existing only in `Product`, such as:

```text
stock
```

does not need to appear in `ProductResponse`.

---

# 11. Complete Example

```java
@PostMapping("/mapper")
public ProductResponse createProductWithMapper(
        @RequestBody ProductRequest request) {

    // Request DTO → Product
    Product product =
            modelMapper.map(
                    request,
                    Product.class
            );

    // Temporary ID for testing without DB
    product.setId(101L);

    // Product → Response DTO
    ProductResponse response =
            modelMapper.map(
                    product,
                    ProductResponse.class
            );

    return response;
}
```

---

# 12. Complete Request Flow

Request:

```json
{
  "name": "Dell Laptop",
  "price": 55000,
  "category": "Electronics",
  "stock": 15
}
```

Flow:

```text
JSON
 ↓
Jackson
 ↓
ProductRequest
 ↓
ModelMapper
 ↓
Product
 ↓
Business/Database Layer
 ↓
Product
 ↓
ModelMapper
 ↓
ProductResponse
 ↓
Jackson
 ↓
JSON
```

Example response:

```json
{
  "id": 101,
  "name": "Dell Laptop",
  "price": 55000,
  "category": "Electronics"
}
```

---

# 13. Matching Field Names

ModelMapper works most easily when source and destination properties match.

Example:

```text
SOURCE              DESTINATION

name       ───────→ name
price      ───────→ price
category   ───────→ category
stock      ───────→ stock
```

Same property names make conventional mapping straightforward.

---

# 14. Different Fields

Suppose source:

```java
private String productName;
```

Destination:

```java
private String name;
```

Now names different hain:

```text
productName
     ↓
    ???
     ↓
name
```

Aise cases mein custom mapping/configuration ki zarurat pad sakti hai.

Example concept:

```java
modelMapper.typeMap(
        Product.class,
        ProductResponse.class
);
```

Complex/custom mappings ko requirement ke according configure kiya ja sakta hai.

---

# 15. No-Argument Constructor Issue

Practical ke time hume error mila:

```text
Failed to instantiate instance of destination Product
```

Reason:

ModelMapper ko destination object create karna tha.

Conceptually:

```java
new Product();
```

Agar `Product` mein accessible no-argument constructor nahi hai, object creation fail ho sakta hai.

Solution:

```java
public Product() {
}
```

---

# 16. Why Did Constructor Disappear?

Agar class mein koi constructor manually define nahi kiya:

```java
public class Product {
}
```

Java automatically default constructor provide karta hai.

Conceptually:

```java
public Product() {
}
```

Lekin agar parameterized constructor khud define kar diya:

```java
public Product(
        Long id,
        String name) {

    this.id = id;
    this.name = name;
}
```

to compiler automatic no-arg constructor generate nahi karta.

Isliye zarurat hone par explicitly:

```java
public Product() {
}
```

add karna padta hai.

---

# 17. Wrapper vs Primitive Issue

Practical mein ek aur issue mila:

```text
Cannot invoke "java.lang.Long.longValue()"
because "this.id" is null
```

Problem example:

```java
private Long id;

public long getId() {
    return id;
}
```

`id`:

```text
null
```

tha.

Java:

```text
Long
 ↓
Unboxing
 ↓
long
```

karne ki koshish karta hai.

But:

```text
null → primitive long
```

possible nahi hai.

Better consistent getter:

```java
private Long id;

public Long getId() {
    return id;
}

public void setId(Long id) {
    this.id = id;
}
```

---

# 18. ModelMapper With Service Layer

Real project structure commonly:

```text
Controller
    ↓
Service
    ↓
Repository
```

Mapping service layer mein handle ki ja sakti hai.

Example:

```java
@Service
public class ProductService {

    private final ModelMapper modelMapper;

    public ProductService(
            ModelMapper modelMapper) {

        this.modelMapper = modelMapper;
    }

    public ProductResponse create(
            ProductRequest request) {

        Product product =
                modelMapper.map(
                        request,
                        Product.class
                );

        // repository.save(product);

        return modelMapper.map(
                product,
                ProductResponse.class
        );
    }
}
```

---

# 19. ModelMapper With Database

Real application:

```text
CLIENT
  ↓
ProductRequest
  ↓
Controller
  ↓
Service
  ↓
ModelMapper
  ↓
Product Entity
  ↓
Repository
  ↓
DATABASE
```

Response:

```text
DATABASE
  ↓
Repository
  ↓
Product Entity
  ↓
ModelMapper
  ↓
ProductResponse
  ↓
Controller
  ↓
CLIENT
```

---

# 20. Manual Mapping vs ModelMapper

## Manual Mapping

```java
ProductResponse response =
        new ProductResponse();

response.setId(product.getId());
response.setName(product.getName());
response.setPrice(product.getPrice());
response.setCategory(product.getCategory());
```

Advantages:

```text
Explicit
Easy to understand
Full control
```

Disadvantage:

```text
More boilerplate for many fields/DTOs
```

---

## ModelMapper

```java
ProductResponse response =
        modelMapper.map(
                product,
                ProductResponse.class
        );
```

Advantages:

```text
Less boilerplate
Quick mapping
Simple to use
```

But complex mappings may require additional configuration.

---

# 21. ModelMapper vs Jackson

Do not confuse them.

Jackson:

```text
JSON
 ↓
Java Object
```

Example:

```text
JSON → ProductRequest
```

ModelMapper:

```text
Java Object
 ↓
Java Object
```

Example:

```text
ProductRequest → Product
```

Complete:

```text
JSON
 ↓
Jackson
 ↓
ProductRequest
 ↓
ModelMapper
 ↓
Product
```

Response:

```text
Product
 ↓
ModelMapper
 ↓
ProductResponse
 ↓
Jackson
 ↓
JSON
```

---

# 22. Common Errors

### Destination Cannot Be Instantiated

```text
Failed to instantiate destination
```

Check destination constructor.

```java
public Product() {
}
```

---

### Null Unboxing

Wrong combination:

```java
private Long id;

public long getId() {
    return id;
}
```

Prefer:

```java
private Long id;

public Long getId() {
    return id;
}
```

---

### Fields Not Mapping

Check:

```text
Property names
Getter/setter availability
Compatible types
Custom mapping requirements
```

---

# 23. Interview Questions

## Q1. What is ModelMapper?

Java object-to-object mapping library.

---

## Q2. Why use ModelMapper?

DTO and Entity mapping boilerplate reduce karne ke liye.

---

## Q3. Basic ModelMapper syntax?

```java
modelMapper.map(
        source,
        Destination.class
);
```

---

## Q4. DTO to Entity kaise map karenge?

```java
Product product =
        modelMapper.map(
                request,
                Product.class
        );
```

---

## Q5. Entity to DTO?

```java
ProductResponse response =
        modelMapper.map(
                product,
                ProductResponse.class
        );
```

---

## Q6. ModelMapper ko Spring Bean kaise banayenge?

```java
@Bean
public ModelMapper modelMapper() {
    return new ModelMapper();
}
```

---

## Q7. ModelMapper and Jackson same hain?

No.

```text
Jackson
→ JSON ↔ Java

ModelMapper
→ Java Object ↔ Java Object
```

---

## Q8. Manual mapping vs ModelMapper?

```text
Manual
→ More explicit/control
→ More code

ModelMapper
→ Less repetitive code
→ Convention/configuration based
```

---

# 24. Quick Revision

```text
ModelMapper
→ Object mapping library

Source
→ Object jahan se data aa raha hai

Destination
→ Object jisme data map hoga

map()
→ Mapping operation

@Bean
→ ModelMapper Spring container mein register

ProductRequest → Product
→ Request DTO to Entity

Product → ProductResponse
→ Entity to Response DTO

No-arg constructor
→ Destination creation ke liye important

Jackson
→ JSON mapping

ModelMapper
→ Java object mapping
```

---

# Final Mental Model

```text
                    CLIENT
                      ↓
                     JSON
                      ↓
                   Jackson
                      ↓
               ProductRequest
                      ↓
                 ModelMapper
                      ↓
                   Product
                      ↓
                  DATABASE
                      ↓
                   Product
                      ↓
                 ModelMapper
                      ↓
               ProductResponse
                      ↓
                   Jackson
                      ↓
                     JSON
                      ↓
                    CLIENT
```

# ModelMapper Completed ✅