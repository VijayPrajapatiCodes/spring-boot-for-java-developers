# 03 - MapStruct in Spring Boot

## 1. What is MapStruct?

MapStruct ek Java **object mapping / code generation library** hai.

Iska use commonly:

```text
DTO → Entity
Entity → DTO
```

mapping ke liye hota hai.

Example:

```text
ProductRequest
      ↓
   MapStruct
      ↓
Product Entity
```

Response:

```text
Product Entity
      ↓
   MapStruct
      ↓
ProductResponse
```

---

# 2. Why MapStruct?

Manual mapping:

```java
Product product = new Product();

product.setName(request.getName());
product.setPrice(request.getPrice());
product.setCategory(request.getCategory());
product.setStock(request.getStock());
```

MapStruct:

```java
Product product =
        productMapper.toEntity(request);
```

Entity → DTO:

```java
ProductResponse response =
        productMapper.toResponse(product);
```

Benefits:

```text
Less repetitive mapping code
Compile-time code generation
Good performance
Type-safe mapping
Compile-time error detection
Easy integration with Spring
```

---

# 3. ModelMapper vs MapStruct

Dono ka purpose similar hai:

```text
DTO ↔ Entity Mapping
```

But implementation approach different hai.

| ModelMapper | MapStruct |
|---|---|
| Runtime mapping | Compile-time mapping code generation |
| Convention/reflection based | Generated Java implementation |
| Very easy setup | Annotation processor setup |
| Runtime mapping issues possible | Many mapping issues compile time par detect |
| Generally slower | Generally faster |
| Less generated code visible | Implementation generate hoti hai |

Important:

```text
ModelMapper ≠ MapStruct
```

Dono separate libraries hain.

Ek project mein usually ek mapping approach choose ki jati hai.

---

# 4. MapStruct Dependency

Maven project mein MapStruct dependency add karte hain.

```xml
<dependency>
    <groupId>org.mapstruct</groupId>
    <artifactId>mapstruct</artifactId>
    <version>${org.mapstruct.version}</version>
</dependency>
```

Annotation processor bhi configure karna hota hai.

Example:

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>

    <configuration>

        <annotationProcessorPaths>

            <path>
                <groupId>org.mapstruct</groupId>
                <artifactId>mapstruct-processor</artifactId>
                <version>${org.mapstruct.version}</version>
            </path>

        </annotationProcessorPaths>

    </configuration>
</plugin>
```

MapStruct version ko project ke compatible/current version ke according configure karna chahiye.

---

# 5. Mapper Interface

MapStruct mein generally mapper interface banate hain.

Example structure:

```text
mapper/
└── ProductMapper.java
```

Example:

```java
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product toEntity(ProductRequest request);

    ProductResponse toResponse(Product product);
}
```

---

# 6. `@Mapper`

```java
@Mapper
```

MapStruct ko batata hai:

```text
Ye mapping interface hai.
```

MapStruct compile time par iska implementation generate karta hai.

---

# 7. `componentModel = "spring"`

Spring Boot project mein:

```java
@Mapper(componentModel = "spring")
```

use kar sakte hain.

Isse generated mapper Spring component/bean ban sakta hai.

Then:

```java
private final ProductMapper productMapper;

public ProductService(
        ProductMapper productMapper) {

    this.productMapper = productMapper;
}
```

constructor injection se mapper use kar sakte hain.

---

# 8. Request DTO → Entity

Mapper:

```java
Product toEntity(ProductRequest request);
```

Usage:

```java
Product product =
        productMapper.toEntity(request);
```

Flow:

```text
ProductRequest
      ↓
ProductMapper
      ↓
Product
```

Matching fields:

```text
ProductRequest        Product

name       ────────→ name
price      ────────→ price
category   ────────→ category
stock      ────────→ stock
```

---

# 9. Entity → Response DTO

Mapper:

```java
ProductResponse toResponse(Product product);
```

Usage:

```java
ProductResponse response =
        productMapper.toResponse(product);
```

Flow:

```text
Product
   ↓
ProductMapper
   ↓
ProductResponse
```

---

# 10. Complete Mapper

```java
package com.vijay.springbootlearning.mapper;

import com.vijay.springbootlearning.dto.ProductRequest;
import com.vijay.springbootlearning.dto.ProductResponse;
import com.vijay.springbootlearning.entity.Product;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product toEntity(ProductRequest request);

    ProductResponse toResponse(Product product);
}
```

---

# 11. How MapStruct Works

Hum sirf interface likhte hain:

```java
Product toEntity(ProductRequest request);
```

MapStruct compile time par implementation generate karta hai.

Conceptually generated code kuch aisa ho sakta hai:

```java
public Product toEntity(
        ProductRequest request) {

    if (request == null) {
        return null;
    }

    Product product = new Product();

    product.setName(request.getName());
    product.setPrice(request.getPrice());
    product.setCategory(request.getCategory());
    product.setStock(request.getStock());

    return product;
}
```

Yaani MapStruct ke peeche ultimately normal Java mapping code generate hota hai.

---

# 12. Generated Implementation

Build ke baad generated sources mein implementation mil sakti hai.

Concept:

```text
ProductMapper.java
      ↓
MapStruct Annotation Processor
      ↓
ProductMapperImpl.java
```

Application actual generated implementation use karti hai.

---

# 13. Complete Request Flow

```text
CLIENT
  ↓
JSON
  ↓
Jackson
  ↓
ProductRequest
  ↓
ProductMapper
  ↓
Product
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
Product
  ↓
ProductMapper
  ↓
ProductResponse
  ↓
Jackson
  ↓
JSON
  ↓
CLIENT
```

---

# 14. Same Field Names

Source:

```java
private String name;
private double price;
```

Destination:

```java
private String name;
private double price;
```

MapStruct same-name compatible properties ko map kar sakta hai.

Example:

```text
name  → name
price → price
```

---

# 15. Different Field Names

Suppose Entity:

```java
private String name;
```

DTO:

```java
private String productName;
```

Now:

```text
name ≠ productName
```

Explicit mapping define kar sakte hain.

```java
@Mapping(
    source = "name",
    target = "productName"
)
ProductResponse toResponse(Product product);
```

Import:

```java
import org.mapstruct.Mapping;
```

Meaning:

```text
SOURCE

Product.name
     ↓
TARGET
     ↓
ProductResponse.productName
```

---

# 16. Multiple Custom Mappings

```java
@Mapping(
    source = "name",
    target = "productName"
)
@Mapping(
    source = "price",
    target = "productPrice"
)
ProductResponse toResponse(Product product);
```

Mapping:

```text
name  → productName
price → productPrice
```

---

# 17. Ignore a Field

Kisi target field ko map nahi karna:

```java
@Mapping(
    target = "id",
    ignore = true
)
Product toEntity(ProductRequest request);
```

Meaning:

```text
id
→ Ignore
```

---

# 18. Constant Value

Fixed value set kar sakte hain.

Example:

```java
@Mapping(
    target = "status",
    constant = "ACTIVE"
)
Product toEntity(ProductRequest request);
```

Concept:

```text
status = "ACTIVE"
```

---

# 19. Mapping Expressions

Special cases mein expression use kiya ja sakta hai.

Example concept:

```java
@Mapping(
    target = "createdAt",
    expression =
        "java(java.time.LocalDateTime.now())"
)
```

Use expressions carefully; simple mappings ko simple hi rakhna better hai.

---

# 20. List Mapping

MapStruct collection mapping bhi kar sakta hai.

```java
List<ProductResponse> toResponseList(
        List<Product> products
);
```

Flow:

```text
List<Product>
      ↓
ProductMapper
      ↓
List<ProductResponse>
```

Without manually:

```java
for (...)
```

mapping logic likhne ki need reduce ho sakti hai.

---

# 21. Mapper in Service Layer

Example:

```java
@Service
public class ProductService {

    private final ProductMapper productMapper;

    public ProductService(
            ProductMapper productMapper) {

        this.productMapper = productMapper;
    }

    public ProductResponse createProduct(
            ProductRequest request) {

        Product product =
                productMapper.toEntity(request);

        // Product savedProduct =
        //         productRepository.save(product);

        return productMapper.toResponse(product);
    }
}
```

---

# 22. Controller

Controller ko mapping details se clean rakha ja sakta hai.

```java
@PostMapping
public ResponseEntity<ProductResponse> create(
        @RequestBody ProductRequest request) {

    ProductResponse response =
            productService.createProduct(request);

    return ResponseEntity.ok(response);
}
```

Architecture:

```text
Controller
    ↓
Service
    ↓
Mapper
    ↓
Entity
    ↓
Repository
```

---

# 23. MapStruct is Compile-Time Based

Ye MapStruct ka important concept hai.

```text
Application Compile
       ↓
Annotation Processor
       ↓
Mapper Implementation Generated
       ↓
Java Code Compiled
       ↓
Application Runs
```

Mapping ke waqt runtime reflection-based work par depend karne ke bajay generated Java methods execute hote hain.

---

# 24. Compile-Time Safety

Suppose mapper configuration mein invalid target property de di:

```java
@Mapping(
    source = "name",
    target = "wrongField"
)
```

Aur destination mein:

```text
wrongField
```

exist hi nahi karta.

MapStruct build/compile time par error report kar sakta hai.

Ye bugs ko application run karne se pehle detect karne mein useful hai.

---

# 25. MapStruct and Jackson

Dono ka role different hai.

Jackson:

```text
JSON ↔ Java Object
```

MapStruct:

```text
Java Object ↔ Java Object
```

Complete request:

```text
JSON
 ↓
Jackson
 ↓
ProductRequest
 ↓
MapStruct
 ↓
Product
```

Response:

```text
Product
 ↓
MapStruct
 ↓
ProductResponse
 ↓
Jackson
 ↓
JSON
```

---

# 26. MapStruct vs Manual Mapping

Manual:

```java
ProductResponse response =
        new ProductResponse();

response.setId(product.getId());
response.setName(product.getName());
response.setPrice(product.getPrice());
```

MapStruct:

```java
ProductResponse response =
        productMapper.toResponse(product);
```

MapStruct generated implementation internally similar setter-based Java code produce kar sakta hai.

---

# 27. ModelMapper vs MapStruct Mental Model

## ModelMapper

We write:

```java
modelMapper.map(
    product,
    ProductResponse.class
);
```

Then mapping is handled at runtime.

```text
Source
  ↓
ModelMapper
  ↓
Runtime Mapping
  ↓
Destination
```

---

## MapStruct

We define:

```java
ProductResponse toResponse(
    Product product
);
```

Compile time:

```text
Mapper Interface
      ↓
Annotation Processor
      ↓
Generated Java Code
```

Runtime:

```text
Product
   ↓
Generated Mapper Method
   ↓
ProductResponse
```

---

# 28. Which One Should We Use?

There is no rule that every project must use the same mapper.

### ModelMapper

Useful when:

```text
Quick setup needed
Simple mapping
Prototype/small project
Runtime convention-based mapping acceptable
```

### MapStruct

Useful when:

```text
Compile-time generated mapping preferred
Performance matters
Large DTO/entity mappings
Explicit mappings required
Compile-time checking preferred
```

For many production applications, MapStruct can be a strong choice because mapping code is generated at compile time and remains explicit.

---

# 29. Do We Need Both?

Usually no.

```text
Application
    ↓
Choose Mapping Strategy
    │
    ├── Manual Mapping
    │
    ├── ModelMapper
    │
    └── MapStruct
```

Using ModelMapper and MapStruct together for the same purpose usually adds unnecessary complexity unless there is a specific reason.

---

# 30. Common Errors

## Mapper Bean Not Found

Check:

```java
@Mapper(componentModel = "spring")
```

---

## Implementation Not Generated

Check:

```text
MapStruct processor configured?
Annotation processing working?
Maven build successful?
```

---

## Field Not Mapping

Check:

```text
Source field
Target field
Field names
Getter/setter/accessor compatibility
Types
@Mapping configuration
```

---

## Different Field Names

Use:

```java
@Mapping(
    source = "name",
    target = "productName"
)
```

---

# 31. Interview Questions

## Q1. What is MapStruct?

MapStruct is a Java annotation-processor based object mapping/code generation library.

---

## Q2. Why is MapStruct used?

DTO ↔ Entity mapping boilerplate reduce karne ke liye.

---

## Q3. Does MapStruct generate code?

Yes.

Compile time par mapper implementation generate karta hai.

---

## Q4. How do we integrate mapper with Spring?

```java
@Mapper(componentModel = "spring")
```

---

## Q5. How do we map DTO to Entity?

Define:

```java
Product toEntity(
    ProductRequest request
);
```

Use:

```java
productMapper.toEntity(request);
```

---

## Q6. How do we map Entity to DTO?

```java
ProductResponse toResponse(
    Product product
);
```

---

## Q7. Different field names kaise map karenge?

```java
@Mapping(
    source = "name",
    target = "productName"
)
```

---

## Q8. How do we ignore a field?

```java
@Mapping(
    target = "id",
    ignore = true
)
```

---

## Q9. ModelMapper vs MapStruct?

```text
ModelMapper
→ Runtime/convention-based mapping

MapStruct
→ Compile-time generated mapping code
```

---

## Q10. Does MapStruct use reflection for normal generated mapping?

Its normal approach is generated Java mapping code rather than runtime reflection-based mapping.

---

# 32. Quick Revision

```text
MapStruct
→ Object mapping/code generation tool

@Mapper
→ Mapper interface

componentModel = "spring"
→ Spring integration

@Mapping
→ Custom field mapping

source
→ Source property

target
→ Destination property

ignore = true
→ Field ignore

toEntity()
→ DTO → Entity

toResponse()
→ Entity → DTO

Compile Time
→ Mapper implementation generated
```

---

# Final Mental Model

```text
                 SOURCE CODE
                     ↓
              ProductMapper
                     ↓
                  @Mapper
                     ↓
          MAPSTRUCT PROCESSOR
                     ↓
          ProductMapperImpl
                     ↓
                COMPILE
                     ↓
              APPLICATION


                 REQUEST

                   JSON
                    ↓
                 Jackson
                    ↓
             ProductRequest
                    ↓
              ProductMapper
                    ↓
                 Product
                    ↓
                 Database


                 RESPONSE

                 Database
                    ↓
                 Product
                    ↓
              ProductMapper
                    ↓
             ProductResponse
                    ↓
                 Jackson
                    ↓
                   JSON
```

# MapStruct Completed ✅