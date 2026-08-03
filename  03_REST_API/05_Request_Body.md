# @RequestBody in Spring Boot

## 1. What is `@RequestBody`?

`@RequestBody` is used to read data from the HTTP request body and convert it into a Java object.

Example request:

```http
POST /api/products
Content-Type: application/json
```

JSON:

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

---

# 2. Basic Flow

```text
Postman / Frontend
       ↓
JSON Request
       ↓
@RequestBody
       ↓
Jackson
       ↓
Java Object
       ↓
Controller Method
```

Example:

```json
{
  "name": "Laptop",
  "price": 50000
}
```

becomes:

```text
ProductRequest

name  = "Laptop"
price = 50000
```

---

# 3. Request DTO

Instead of receiving every value separately, we can create a DTO.

Example:

```java
public class ProductRequest {

    private int id;
    private String name;
    private double price;
    private String category;
    private int stock;

    public ProductRequest() {
    }

    // getters and setters
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

---

# 4. JSON to Java Mapping

JSON property names are mapped to Java properties.

```json
{
  "id": 101,
  "name": "Laptop",
  "price": 50000,
  "category": "Electronics",
  "stock": 10
}
```

Mapping:

```text
JSON                 Java

"id"          →      id
"name"        →      name
"price"       →      price
"category"    →      category
"stock"       →      stock
```

---

# 5. Deserialization

Converting JSON into a Java object is called:

```text
Deserialization
```

Flow:

```text
JSON
 ↓
Jackson
 ↓
ProductRequest
```

Example:

```json
{
  "name": "Laptop",
  "price": 50000
}
```

becomes roughly:

```text
ProductRequest object

name  = Laptop
price = 50000
```

---

# 6. Serialization

When a Java object is returned from a `@RestController`, Spring can convert it into JSON.

This process is called:

```text
Serialization
```

Example:

```java
@PostMapping
public ProductRequest createProduct(
        @RequestBody ProductRequest product) {

    return product;
}
```

Flow:

```text
ProductRequest Java Object
          ↓
        Jackson
          ↓
         JSON
```

Response:

```json
{
  "id": 101,
  "name": "Laptop",
  "price": 50000.0,
  "category": "Electronics",
  "stock": 10
}
```

---

# 7. Complete Request/Response Flow

```text
CLIENT
  │
  │ JSON
  ↓
HTTP Request
  ↓
@RequestBody
  ↓
Jackson Deserialization
  ↓
Java DTO
  ↓
Controller
  ↓
Java Object Returned
  ↓
Jackson Serialization
  ↓
JSON Response
  ↓
CLIENT
```

Remember:

```text
JSON → Java = Deserialization

Java → JSON = Serialization
```

---

# 8. Content-Type

When sending JSON, the request normally contains:

```http
Content-Type: application/json
```

This tells the server that the request body contains JSON data.

In Postman:

```text
Body
 ↓
raw
 ↓
JSON
```

---

# 9. Missing Fields

Suppose Java DTO contains:

```java
private int stock;
```

But request doesn't contain `stock`:

```json
{
  "name": "Laptop",
  "price": 50000
}
```

Because `int` is a primitive:

```text
stock = 0
```

Common Java defaults:

```text
int       → 0
double    → 0.0
boolean   → false

Integer   → null
Double    → null
Boolean   → null

String    → null
Object    → null
```

This is one reason wrapper types can be useful when distinguishing:

```text
value missing
```

from:

```text
value explicitly set to 0
```

---

# 10. Wrong Data Type

Suppose DTO expects:

```java
private double price;
```

But client sends:

```json
{
  "name": "Laptop",
  "price": "abc"
}
```

Jackson cannot convert:

```text
"abc"
  ↓
double
  ↓
❌
```

The request can result in:

```http
400 Bad Request
```

Flow:

```text
JSON
 ↓
@RequestBody
 ↓
Jackson
 ↓
Type conversion fails
 ↓
400 Bad Request
```

---

# 11. Valid Type Does Not Mean Valid Business Data

This JSON has technically compatible data types:

```json
{
  "name": "",
  "price": -50000,
  "category": "",
  "stock": -10
}
```

Jackson can deserialize these values because:

```text
""       → String
-50000   → number
""       → String
-10      → int
```

But logically the data may be invalid.

This requires:

```text
Request Validation
```

which is separate from basic JSON deserialization.

---

# 12. No-Argument Constructor

For our mutable DTO approach, we used a no-argument constructor:

```java
public ProductRequest() {
}
```

along with getters and setters.

Example:

```java
public class ProductRequest {

    private String name;
    private double price;

    public ProductRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
```

If you manually create only a parameterized constructor, Java does not automatically provide the default no-argument constructor.

---

# 13. `@PathVariable` + `@RequestBody`

These are commonly combined for update APIs.

Example:

```http
PUT /api/products/10
```

Body:

```json
{
  "name": "Gaming Laptop",
  "price": 65000,
  "category": "Electronics",
  "stock": 15
}
```

Controller:

```java
@PutMapping("/{id}")
public String updateProduct(
        @PathVariable int id,
        @RequestBody ProductRequest product) {

    return "Updating Product ID: " + id
            + ", Name: " + product.getName()
            + ", Price: " + product.getPrice();
}
```

Responsibilities:

```text
@PathVariable
      ↓
Which product?
      ↓
id = 10


@RequestBody
      ↓
What data?
      ↓
name, price, category, stock
```

---

# 14. Does Update DTO Need ID?

Not necessarily.

Example:

```http
PUT /api/products/10
```

Body:

```json
{
  "name": "Gaming Laptop",
  "price": 65000
}
```

The ID already comes from:

```java
@PathVariable int id
```

Therefore:

```text
URL
 ↓
Resource Identity

Body
 ↓
Resource Data
```

This also avoids ambiguity such as:

```text
URL  → /products/10

Body → "id": 20
```

---

# 15. Returning JSON Instead of Plain Text

Plain text:

```java
@PostMapping
public String createProduct(
        @RequestBody ProductRequest product) {

    return "Product Created";
}
```

Response:

```text
Product Created
```

Returning an object:

```java
@PostMapping
public ProductRequest createProduct(
        @RequestBody ProductRequest product) {

    return product;
}
```

Response:

```json
{
  "id": 101,
  "name": "Laptop",
  "price": 50000.0,
  "category": "Electronics",
  "stock": 10
}
```

For REST APIs, structured JSON responses are commonly more useful than manually concatenated strings.

---

# 16. Nested JSON

JSON objects can contain other objects.

Example:

```json
{
  "id": 102,
  "name": "Laptop",
  "price": 50000,
  "manufacturer": {
    "name": "Dell",
    "country": "India"
  }
}
```

Structure:

```text
Product
 │
 ├── id
 ├── name
 ├── price
 │
 └── manufacturer
       │
       ├── name
       └── country
```

---

# 17. Nested DTO

Create:

```java
public class ManufacturerRequest {

    private String name;
    private String country;

    public ManufacturerRequest() {
    }

    // getters and setters
}
```

Inside `ProductRequest`:

```java
private ManufacturerRequest manufacturer;
```

Getter/setter:

```java
public ManufacturerRequest getManufacturer() {
    return manufacturer;
}

public void setManufacturer(
        ManufacturerRequest manufacturer) {

    this.manufacturer = manufacturer;
}
```

Jackson can map:

```json
"manufacturer": {
  "name": "Dell",
  "country": "India"
}
```

into:

```text
ManufacturerRequest

name    = Dell
country = India
```

---

# 18. Getter/Setter Importance

In our DTO approach, getters and setters are important for property mapping/serialization.

Example:

```java
private ManufacturerRequest manufacturer;
```

Setter:

```java
public void setManufacturer(
        ManufacturerRequest manufacturer) {

    this.manufacturer = manufacturer;
}
```

Getter:

```java
public ManufacturerRequest getManufacturer() {
    return manufacturer;
}
```

In our practical, when the manufacturer getter was missing, the manufacturer was not appearing in the returned JSON.

After adding the getter:

```json
"manufacturer": {
  "country": "India",
  "name": "Dell"
}
```

appeared in the response.

---

# 19. JSON Arrays

JSON can contain arrays:

```json
{
  "customerId": 101,
  "items": [
    {
      "productId": 10,
      "quantity": 2
    },
    {
      "productId": 20,
      "quantity": 1
    }
  ]
}
```

Remember:

```text
{ } → JSON Object

[ ] → JSON Array
```

---

# 20. JSON Array to Java List

Create:

```java
public class OrderItemRequest {

    private int productId;
    private int quantity;

    public OrderItemRequest() {
    }

    // getters and setters
}
```

Then:

```java
public class OrderRequest {

    private int customerId;

    private List<OrderItemRequest> items;

    public OrderRequest() {
    }

    // getters and setters
}
```

Important line:

```java
private List<OrderItemRequest> items;
```

Mapping:

```text
JSON Array
    ↓
Jackson
    ↓
List<OrderItemRequest>
```

---

# 21. Order Request Controller

```java
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @PostMapping
    public OrderRequest createOrder(
            @RequestBody OrderRequest order) {

        return order;
    }
}
```

Request:

```http
POST /api/orders
```

Body:

```json
{
  "customerId": 101,
  "items": [
    {
      "productId": 10,
      "quantity": 2
    },
    {
      "productId": 20,
      "quantity": 1
    },
    {
      "productId": 30,
      "quantity": 3
    }
  ]
}
```

Jackson creates:

```text
OrderRequest
│
├── customerId = 101
│
└── items
     │
     ├── OrderItemRequest
     │    ├── productId = 10
     │    └── quantity = 2
     │
     ├── OrderItemRequest
     │    ├── productId = 20
     │    └── quantity = 1
     │
     └── OrderItemRequest
          ├── productId = 30
          └── quantity = 3
```

---

# 22. Processing the List

Once Jackson converts the JSON array, it is a normal Java `List`.

Example:

```java
@PostMapping
public String createOrder(
        @RequestBody OrderRequest order) {

    System.out.println(
            "Customer ID: " + order.getCustomerId()
    );

    for (OrderItemRequest item : order.getItems()) {

        System.out.println(
                "Product ID: " + item.getProductId()
                + ", Quantity: " + item.getQuantity()
        );
    }

    return "Order received successfully";
}
```

Console:

```text
Customer ID: 101

Product ID: 10, Quantity: 2
Product ID: 20, Quantity: 1
Product ID: 30, Quantity: 3
```

After deserialization, normal Java operations can be used:

```java
order.getItems().size();
```

```java
order.getItems().get(0);
```

```java
for (OrderItemRequest item : order.getItems()) {
}
```

Streams can also be used:

```java
order.getItems()
     .stream()
     .forEach(item ->
         System.out.println(item.getProductId())
     );
```

---

# 23. Real Order Processing Concept

Later, a real application may perform:

```text
POST /api/orders
       ↓
@RequestBody
       ↓
OrderRequest
       ↓
Service
       ↓
Loop through items
       ↓
Find products
       ↓
Check stock
       ↓
Get actual prices
       ↓
Calculate totals
       ↓
Create order
       ↓
Save to database
       ↓
Return response
```

The client should not be trusted to decide sensitive business values such as the final payable amount; the backend can calculate them from authoritative product/order data.

---

# 24. `@RequestParam` vs `@PathVariable` vs `@RequestBody`

### `@PathVariable`

```http
GET /api/products/10
```

```java
@PathVariable int id
```

Used for values in the URL path, commonly resource identity.

---

### `@RequestParam`

```http
GET /api/products?category=electronics
```

```java
@RequestParam String category
```

Commonly used for:

```text
Filtering
Searching
Sorting
Pagination
Options
```

---

### `@RequestBody`

```http
POST /api/products
```

```json
{
  "name": "Laptop",
  "price": 50000
}
```

```java
@RequestBody ProductRequest product
```

Used for structured data sent in the HTTP request body.

---

# 25. Quick Comparison

```text
@PathVariable
      ↓
/products/10
          ↑
         ID


@RequestParam
      ↓
/products?category=electronics
          ↑
        Filter


@RequestBody
      ↓
{
  "name": "Laptop",
  "price": 50000
}
      ↑
Structured Data
```

---

# 26. Important Interview Questions

## Q1. What is `@RequestBody`?

It binds HTTP request-body data to a Java object.

## Q2. What converts JSON to Java objects?

Spring uses HTTP message conversion; for JSON, Jackson is commonly used.

## Q3. What is deserialization?

```text
JSON → Java Object
```

## Q4. What is serialization?

```text
Java Object → JSON
```

## Q5. Can `@RequestBody` handle nested JSON?

Yes.

```text
ProductRequest
     ↓
ManufacturerRequest
```

## Q6. Can it handle arrays?

Yes.

```text
JSON Array
    ↓
List<OrderItemRequest>
```

## Q7. What happens when JSON contains an incompatible datatype?

For example:

```json
"price": "abc"
```

when Java expects:

```java
double price;
```

the request can fail with:

```text
400 Bad Request
```

## Q8. Can `@PathVariable` and `@RequestBody` be used together?

Yes.

```java
@PutMapping("/{id}")
public void update(
        @PathVariable int id,
        @RequestBody ProductRequest request) {
}
```

## Q9. Why use DTOs?

DTOs provide an explicit structure for the data an API receives or returns and help separate the API contract from other application layers.

---

# 27. Final Mental Model

```text
                  HTTP REQUEST
                       │
       ┌───────────────┼────────────────┐
       │               │                │
       ↓               ↓                ↓
@PathVariable    @RequestParam     @RequestBody
       │               │                │
       ↓               ↓                ↓
 Resource ID       Filters         JSON Data
       │               │                │
       └───────────────┼────────────────┘
                       ↓
                   Controller
                       ↓
                     Java
```

For JSON:

```text
Frontend/Postman
      │
      │ JSON
      ↓
@RequestBody
      ↓
Jackson
      ↓
DTO / Java Object
      ↓
Application Logic
      ↓
Java Response Object
      ↓
Jackson
      ↓
JSON
      ↓
Frontend/Postman
```

# Key Takeaways

- `@RequestBody` receives HTTP body data.
- JSON can be converted into Java DTOs.
- JSON → Java is deserialization.
- Java → JSON is serialization.
- Nested JSON can map to nested DTOs. 
- JSON arrays can map to Java `List`.
- After conversion, normal Java Collections operations can be used.
- `@PathVariable` identifies a resource while `@RequestBody` can carry its data.
- Wrong JSON datatypes can produce `400 Bad Request`.
- Real APIs should validate received data before using it.