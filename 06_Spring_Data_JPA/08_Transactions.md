# Transactions in Spring Data JPA

## 1. What is Transaction?

Transaction multiple database operations ko ek logical unit mein treat karti hai.

Classic example:

```text
Bank Transfer

Account A
₹1000 debit

Account B
₹1000 credit
```

Dono successful hone chahiye.

Agar second operation fail:

```text
ROLLBACK
```

---

# 2. ACID

Database transactions ke important properties:

```text
A → Atomicity
C → Consistency
I → Isolation
D → Durability
```

---

# 3. Atomicity

```text
All operations succeed
OR
all operations rollback
```

---

# 4. Consistency

Transaction database ko valid state se valid state tak le jati hai while constraints/rules preserve hote hain.

---

# 5. Isolation

Concurrent transactions ek dusre ke effects se defined isolation rules ke according separated rehti hain.

---

# 6. Durability

Committed transaction ke changes durable/persistent hone chahiye.

---

# 7. @Transactional

Spring mein:

```java
@Transactional
public void placeOrder() {

}
```

Method ke database operations ek transaction boundary ke andar execute ho sakte hain.

---

# 8. Example

```java
@Transactional
public void placeOrder() {

    orderRepository.save(order);

    inventoryService.reduceStock();

    paymentService.createPayment();
}
```

Concept:

```text
BEGIN
 ↓
Save Order
 ↓
Update Stock
 ↓
Database operation
 ↓
COMMIT
```

Failure ke case mein applicable transaction changes rollback ho sakte hain.

Important: external payment/network calls ka rollback database transaction jaisa simple nahi hota.

---

# 9. Rollback

Runtime exception example:

```java
@Transactional
public void process() {

    repository.save(entity);

    throw new RuntimeException("Failed");
}
```

Spring transaction management configured hone par database changes rollback ho sakte hain.

---

# 10. readOnly

Read operation ke intent ke liye:

```java
@Transactional(readOnly = true)
public List<Product> getProducts() {
    return productRepository.findAll();
}
```

---

# 11. Where to use @Transactional?

Commonly transaction boundary:

```text
Service Layer
```

Example:

```java
@Service
public class OrderService {

    @Transactional
    public void placeOrder() {

    }
}
```

Because business operation multiple repository operations combine kar sakta hai.

---

# 12. Common Example

```text
Create Order
    ↓
Save Order Items
    ↓
Reduce Inventory
    ↓
Update DB
```

Ye ek business transaction ho sakta hai.

---

# Important

Transaction ka goal:

```text
Either complete business DB operation succeeds
OR
applicable DB changes rollback
```

# Transactions Completed ✅