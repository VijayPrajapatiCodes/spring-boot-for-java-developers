# 01 - DTO in Spring Boot

## 1. What is DTO?

DTO stands for:

```text
Data Transfer Object
```

DTO ek Java object/class hota hai jiska main purpose application ki different layers ya client-server ke beech required data transfer karna hota hai.

Example:

```text
Frontend
   ↓
JSON
   ↓
DTO
   ↓
Controller
   ↓
Service
```

DTO ko database Entity se separate rakhna common practice hai.

---

# 2. Why Do We Need DTO?

Suppose hamari `Product` Entity hai:

```java
public class Product {

    private Long id;
    private String name;
    private double price;
    private String category;
    private int stock;

    private double costPrice;
    private String supplierCode;
}
```

Isme kuch fields client ke liye useful hain:

```text
id
name
price
category
stock
```

Aur kuch internal ho sakti hain:

```text
costPrice
supplierCode
```

Agar Entity directly API mein use karenge:

```java
@PostMapping
public Product createProduct(
        @RequestBody Product product) {

    return product;
}
```

to API contract database model ke saath tightly coupled ho jayega.

DTO hume API ke required fields separately define karne deta hai.

---

# 3. Entity vs DTO

```text
ENTITY
   ↓
Database/Persistence Model

DTO
   ↓
Data Transfer Model
```

Example:

```java
@Entity
public class Product {

    private Long id;
    private String name;
    private double price;

    private double costPrice;
}
```

DTO:

```java
public class ProductResponse {

    private Long id;
    private String name;
    private double price;
}
```

Client ko `costPrice` bhejne ki zarurat nahi hai.

---

# 4. Benefits of DTO

DTO use karne ke major benefits:

```text
1. API contract control
2. Entity ko directly expose karne se bachata hai
3. Required fields only transfer
4. Request validation easier
5. Request and response structures separate
6. Database model aur API model ko decouple karta hai
7. API maintenance easier
```

---

# 5. Request DTO

Client se backend ko jo data aata hai uske liye Request DTO bana sakte hain.

Example:

```java
public class ProductRequest {

    private String name;
    private double price;
    private String category;
    private int stock;

    // getters and setters
}
```

Request:

```json
{
  "name": "Laptop",
  "price": 50000,
  "category": "Electronics",
  "stock": 10
}
```

Controller:

```java
@PostMapping
public ProductRequest createProduct(
        @RequestBody ProductRequest product) {

    return product;
}
```

Flow:

```text
Client JSON
     ↓
@RequestBody
     ↓
ProductRequest
     ↓
Controller
```

---

# 6. Response DTO

Backend se client ko return hone wale data ke liye Response DTO bana sakte hain.

Example:

```java
public class ProductResponse {

    private Long id;
    private String name;
    private double price;
    private String category;

    // getters and setters
}
```

Response:

```json
{
  "id": 101,
  "name": "Laptop",
  "price": 50000,
  "category": "Electronics"
}
```

Notice:

```text
Product Entity
----------------
id
name
price
category
stock
costPrice
supplierCode


ProductResponse
----------------
id
name
price
category
```

Har Entity field response mein bhejna necessary nahi hai.

---

# 7. Request DTO vs Response DTO

Request DTO:

```java
ProductRequest
```

Client:

```text
Client → Backend
```

Response DTO:

```java
ProductResponse
```

Direction:

```text
Backend → Client
```

Complete:

```text
CLIENT
   │
   │ JSON
   ↓
ProductRequest
   ↓
Controller
   ↓
Service
   ↓
Product Entity
   ↓
Database


Database
   ↓
Product Entity
   ↓
Service
   ↓
ProductResponse
   ↓
Controller
   ↓
JSON
   ↓
CLIENT
```

---

# 8. DTO Validation

DTO ke saath Jakarta Validation use kar sakte hain.

Example:

```java
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

Controller:

```java
@PostMapping
public ProductRequest createProduct(
        @Valid
        @RequestBody ProductRequest product) {

    return product;
}
```

Flow:

```text
JSON
 ↓
ProductRequest
 ↓
@Valid
 ↓
Validation
 ↓
Controller
```

Invalid data:

```text
Validation Error
```

Valid data:

```text
Controller → Service
```

---

# 9. DTO Mapping

DTO aur Entity different classes hain.

Isliye data ko convert/map karna padta hai.

Example:

```text
ProductRequest
      ↓
   Mapping
      ↓
Product Entity
```

Aur response ke time:

```text
Product Entity
      ↓
   Mapping
      ↓
ProductResponse
```

---

# 10. Manual Request DTO → Entity Mapping

Suppose:

```java
ProductRequest request;
```

Entity create:

```java
Product product = new Product();

product.setName(request.getName());
product.setPrice(request.getPrice());
product.setCategory(request.getCategory());
product.setStock(request.getStock());
```

Flow:

```text
ProductRequest
      ↓
getName()
getPrice()
getCategory()
getStock()
      ↓
Product
```

---

# 11. Manual Entity → Response DTO Mapping

Suppose database se:

```java
Product product;
```

mila.

Response DTO:

```java
ProductResponse response =
        new ProductResponse();

response.setId(product.getId());
response.setName(product.getName());
response.setPrice(product.getPrice());
response.setCategory(product.getCategory());
```

Then:

```text
Product
   ↓
Manual Mapping
   ↓
ProductResponse
   ↓
Client
```

---

# 12. Problem With Manual Mapping

Small DTO:

```text
4 fields
```

to manual mapping manageable hai.

Lekin large project:

```text
20 Entities
30 DTOs
20-30 fields each
```

to bahut repetitive code ho sakta hai.

Example:

```java
response.setId(product.getId());
response.setName(product.getName());
response.setPrice(product.getPrice());
response.setCategory(product.getCategory());
```

Har jagah manually likhna boilerplate increase karta hai.

Isi problem ko reduce karne ke liye mapping libraries/tools use kiye ja sakte hain:

```text
ModelMapper
MapStruct
```

---

# 13. ModelMapper Concept

Manual:

```java
ProductResponse response =
        new ProductResponse();

response.setId(product.getId());
response.setName(product.getName());
response.setPrice(product.getPrice());
```

ModelMapper:

```java
ProductResponse response =
        modelMapper.map(
                product,
                ProductResponse.class
        );
```

Concept:

```text
Product
   ↓
ModelMapper
   ↓
ProductResponse
```

---

# 14. MapStruct Concept

MapStruct mapping code generate karne ke liye use kiya ja sakta hai.

Concept:

```java
ProductResponse toResponse(Product product);
```

Flow:

```text
Product
   ↓
Generated Mapper
   ↓
ProductResponse
```

ModelMapper aur MapStruct ko separate chapters mein padhenge.

---

# 15. DTO Package Structure

Example project:

```text
src/main/java/
└── com.vijay.springbootlearning/
    │
    ├── Controller/
    │
    ├── service/
    │
    ├── repository/
    │
    ├── entity/
    │
    └── dto/
        │
        ├── ProductRequest.java
        └── ProductResponse.java
```

---

# 16. Real E-Commerce Example

Suppose Product Entity:

```text
Product

id
name
description
price
stock
category
costPrice
supplierCode
createdAt
updatedAt
```

Create request:

```text
ProductRequest

name
description
price
stock
category
```

API response:

```text
ProductResponse

id
name
description
price
stock
category
```

Internal fields:

```text
costPrice
supplierCode
```

client ko expose karna necessary nahi hai.

---

# 17. DTO Does Not Mean Database Table

Important:

```text
Entity
→ Database persistence se related

DTO
→ Data transfer se related
```

DTO ke liye:

```java
@Entity
```

lagana required nahi hai.

Usually DTO normal Java class hoti hai.

---

# 18. DTO and JSON

Spring/Jackson request JSON ko DTO object mein convert kar sakta hai.

```text
JSON
 ↓
Jackson
 ↓
ProductRequest
```

Response side:

```text
ProductResponse
 ↓
Jackson
 ↓
JSON
```

So:

```text
REQUEST

JSON → DTO


RESPONSE

DTO → JSON
```

---

# 19. DTO With Multipart

Humne Multipart mein bhi DTO use kiya:

```java
@RequestPart("product")
ProductRequest product
```

Flow:

```text
multipart/form-data
       ↓
product part
       ↓
application/json
       ↓
ProductRequest
```

Isliye DTO sirf `@RequestBody` ke saath limited nahi hai.

---

# 20. Important Design

Common structure:

```text
Controller
    ↓
Request DTO
    ↓
Service
    ↓
Entity
    ↓
Repository
    ↓
Database
```

Response:

```text
Database
    ↓
Repository
    ↓
Entity
    ↓
Service
    ↓
Response DTO
    ↓
Controller
    ↓
Client
```

---

# 21. Common Mistake

Avoid unnecessarily returning persistence Entity directly everywhere:

```java
public Product getProduct() {
    return product;
}
```

Prefer API-specific response model where separation is useful:

```java
public ProductResponse getProduct() {
    return response;
}
```

This keeps:

```text
Database Model
      ≠
API Contract
```

---

# 22. Interview Questions

## Q1. What is DTO?

DTO stands for Data Transfer Object.

It is used to transfer required data between application layers or client/server boundaries.

---

## Q2. Why use DTO instead of Entity directly?

To:

```text
Control API data
Avoid unnecessary field exposure
Separate API and persistence models
Apply request validation
Maintain API contracts
```

---

## Q3. What is Request DTO?

Client se backend ko aane wale data ko represent karta hai.

Example:

```java
ProductRequest
```

---

## Q4. What is Response DTO?

Backend se client ko jaane wale data ko represent karta hai.

Example:

```java
ProductResponse
```

---

## Q5. Is DTO an Entity?

No.

```text
Entity → Persistence/database model
DTO    → Data transfer model
```

---

## Q6. Can DTO contain validation annotations?

Yes.

Example:

```java
@NotBlank
@Positive
```

---

## Q7. How do we convert DTO to Entity?

Options:

```text
Manual Mapping
ModelMapper
MapStruct
```

---

## Q8. Why have separate Request and Response DTOs?

Because input and output requirements can be different.

Example:

```text
ProductRequest

name
price
stock
```

while:

```text
ProductResponse

id
name
price
stock
```

---

## Q9. Can DTO be used with multipart requests?

Yes.

```java
@RequestPart("product")
ProductRequest product
```

---

# 23. Quick Revision

```text
DTO
→ Data Transfer Object

ProductRequest
→ Client → Backend

ProductResponse
→ Backend → Client

Entity
→ Persistence model

@RequestBody
→ JSON → DTO

@Valid
→ DTO validation

Manual Mapping
→ DTO ↔ Entity manually

ModelMapper
→ Automatic/convention-based mapping

MapStruct
→ Generated mapping code
```

---

# Final Mental Model

```text
                    CLIENT
                      │
                     JSON
                      ↓
               ProductRequest
                      │
                  Validation
                      ↓
                  Controller
                      ↓
                   Service
                      ↓
                   Mapping
                      ↓
               Product Entity
                      ↓
                  Repository
                      ↓
                  DATABASE


                  DATABASE
                      ↓
                  Repository
                      ↓
               Product Entity
                      ↓
                   Mapping
                      ↓
               ProductResponse
                      ↓
                  Controller
                      ↓
                     JSON
                      ↓
                    CLIENT
```

# DTO Completed ✅