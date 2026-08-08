# Spring Boot CommandLineRunner

## 1. What is CommandLineRunner?

`CommandLineRunner` Spring Boot ka ek interface hai jo application startup complete hone ke baad automatically code execute karne ke liye use hota hai.

```text
Spring Boot Application Start
          ↓
Application Context Created
          ↓
Beans Initialized
          ↓
CommandLineRunner
          ↓
run() Method Executes
```

Hume `run()` method manually call nahi karna padta.

---

# 2. CommandLineRunner Interface

Spring Boot provide karta hai:

```java
public interface CommandLineRunner {

    void run(String... args) throws Exception;

}
```

Use karne ke liye:

```java
@Component
public class StartupRunner
        implements CommandLineRunner {

    @Override
    public void run(String... args) {

        System.out.println(
                "CommandLineRunner executed!"
        );
    }
}
```

Application start hone par output:

```text
CommandLineRunner executed!
```

---

# 3. Why @Component?

Example:

```java
@Component
public class StartupRunner
        implements CommandLineRunner {
}
```

`@Component` class ko Spring-managed Bean banata hai.

Flow:

```text
@Component
     ↓
Spring Bean
     ↓
implements CommandLineRunner
     ↓
Spring Boot Detects Runner
     ↓
run() Executes
```

Agar class Spring Bean hi nahi hai, Spring Boot normally us runner ko manage/execute nahi karega.

---

# 4. run() Method

Main method:

```java
@Override
public void run(String... args) {

}
```

Application startup ke baad jo code execute karna hai wo is method mein likhte hain.

Example:

```java
@Override
public void run(String... args) {

    System.out.println(
            "Application started successfully!"
    );
}
```

---

# 5. Common Use Cases

`CommandLineRunner` useful ho sakta hai:

```text
Initial Data Insert

Default Data Creation

Startup Testing

Configuration Verification

Development Data Seeding

Startup Tasks

Database Initialization Logic
```

---

# 6. CommandLineRunner with Repository

Real application mein Repository inject karke startup par database operation bhi perform kar sakte hain.

Example:

```java
@Component
public class StartupRunner
        implements CommandLineRunner {

    private final ProductRepository productRepository;

    public StartupRunner(
            ProductRepository productRepository) {

        this.productRepository =
                productRepository;
    }

    @Override
    public void run(String... args) {

        // startup logic
    }
}
```

Here constructor injection use hui hai.

---

# 7. ProductRepository

Repository:

```java
public interface ProductRepository
        extends JpaRepository<Product, Long> {

}
```

Spring Data JPA hume methods provide karta hai:

```text
save()

findById()

findAll()

deleteById()

existsById()

count()
```

CommandLineRunner in methods ko startup par use kar sakta hai.

---

# 8. Practical - Insert Product on Startup

Humne startup par Product create kiya:

```java
@Component
public class StartupRunner
        implements CommandLineRunner {

    private final ProductRepository productRepository;

    public StartupRunner(
            ProductRepository productRepository) {

        this.productRepository =
                productRepository;
    }

    @Override
    public void run(String... args) {

        Product product = new Product();

        product.setName("Laptop");
        product.setPrice(55000);
        product.setStock(10);
        product.setStatus(
                ProductStatus.ACTIVE
        );

        productRepository.save(product);

        System.out.println(
                "Default product inserted successfully!"
        );
    }
}
```

---

# 9. What Happened Internally?

Application start hui:

```text
Spring Boot Start
       ↓
Application Context
       ↓
ProductRepository Bean
       ↓
StartupRunner Bean
       ↓
run()
       ↓
Product Object
       ↓
productRepository.save()
       ↓
Spring Data JPA
       ↓
Hibernate
       ↓
SQL INSERT
       ↓
MySQL
```

---

# 10. Database Result

MySQL:

```sql
SELECT * FROM products;
```

Humare practical mein result:

```text
id             → 1

products_name  → Laptop

price          → 55000

stock          → 10

status         → ACTIVE
```

Therefore:

```text
CommandLineRunner
        +
JpaRepository
        +
Hibernate
        +
MySQL

        ↓

Startup Data Inserted Successfully
```

---

# 11. Why category Was NULL?

Humne Product create karte waqt category set nahi ki thi.

Example:

```java
Product product = new Product();

product.setName("Laptop");
product.setPrice(55000);
product.setStock(10);
product.setStatus(ProductStatus.ACTIVE);
```

Category:

```java
product.setCategory(...);
```

set nahi ki.

Therefore database mein:

```text
category = NULL
```

aaya.

---

# 12. CommandLineRunner Runs on Every Startup

Bahut important:

`CommandLineRunner` sirf first startup par execute nahi hota.

Application jab bhi start hoti hai:

```text
Application Start
       ↓
run()
```

dobara execute hota hai.

Example:

```text
First Start

Laptop inserted
```

Again restart:

```text
Second Start

Laptop inserted again
```

Result:

```text
1   Laptop

2   Laptop
```

Duplicate records aa sakte hain.

---

# 13. DevTools + CommandLineRunner

DevTools application restart karta hai.

Aur application restart hone par `CommandLineRunner` bhi dobara execute ho sakta hai.

```text
Code Change
     ↓
Ctrl + F9
     ↓
DevTools Restart
     ↓
Application Starts Again
     ↓
CommandLineRunner
     ↓
run()
     ↓
Data Insert Again
```

Therefore development mein duplicate data ka dhyan rakhna chahiye.

---

# 14. Avoid Duplicate Startup Data

Simple approach:

```java
@Override
public void run(String... args) {

    if (productRepository.count() == 0) {

        Product product = new Product();

        product.setName("Laptop");
        product.setPrice(55000);
        product.setStock(10);
        product.setStatus(
                ProductStatus.ACTIVE
        );

        productRepository.save(product);
    }
}
```

Flow:

```text
Application Start
       ↓
count() == 0 ?
      / \
    YES  NO
     ↓    ↓
 Insert  Skip
```

This is useful for simple learning/demo initialization.

For real applications, initialization/migration strategy should be designed according to the project requirements.

---

# 15. Command-Line Arguments

Method:

```java
run(String... args)
```

`args` application ko diye gaye command-line arguments contain kar sakta hai.

Example application start:

```text
java -jar app.jar hello production
```

Runner:

```java
@Override
public void run(String... args) {

    for (String arg : args) {
        System.out.println(arg);
    }
}
```

Output:

```text
hello
production
```

---

# 16. Multiple CommandLineRunners

Application mein multiple runners ho sakte hain.

Example:

```java
@Component
public class DatabaseRunner
        implements CommandLineRunner {

    @Override
    public void run(String... args) {
        System.out.println("Database Runner");
    }
}
```

Another:

```java
@Component
public class CacheRunner
        implements CommandLineRunner {

    @Override
    public void run(String... args) {
        System.out.println("Cache Runner");
    }
}
```

Dono startup par execute ho sakte hain.

---

# 17. @Order

Agar multiple runners ka execution order control karna ho:

```java
@Component
@Order(1)
public class FirstRunner
        implements CommandLineRunner {

    @Override
    public void run(String... args) {
        System.out.println("First");
    }
}
```

Second:

```java
@Component
@Order(2)
public class SecondRunner
        implements CommandLineRunner {

    @Override
    public void run(String... args) {
        System.out.println("Second");
    }
}
```

Concept:

```text
@Order(1)
    ↓
First Runner

@Order(2)
    ↓
Second Runner
```

Smaller order value gets higher priority.

---

# 18. CommandLineRunner vs main()

Main method:

```java
public static void main(String[] args) {

    SpringApplication.run(
            Application.class,
            args
    );
}
```

`main()` application bootstrap karta hai.

`CommandLineRunner`:

```java
@Override
public void run(String... args) {

}
```

Spring application context initialize hone ke baad startup logic execute karta hai.

```text
main()
   ↓
SpringApplication.run()
   ↓
Spring Context Ready
   ↓
CommandLineRunner.run()
```

---

# 19. CommandLineRunner vs @PostConstruct

Both startup-related code execute kar sakte hain, but purpose/lifecycle different hai.

```text
@PostConstruct
→ Particular Bean initialize hone ke baad

CommandLineRunner
→ Spring Boot startup ke baad startup task
```

Application-level startup tasks ke liye `CommandLineRunner` useful hai.

---

# 20. CommandLineRunner vs ApplicationRunner

Spring Boot ek aur similar interface provide karta hai:

```java
ApplicationRunner
```

Main difference command-line arguments handling ka hai.

### CommandLineRunner

```java
run(String... args)
```

Raw string arguments provide karta hai.

### ApplicationRunner

```java
run(ApplicationArguments args)
```

Arguments ko structured API ke through access karne deta hai.

Simple rule:

```text
Simple raw arguments
        ↓
CommandLineRunner


Structured argument handling
        ↓
ApplicationRunner
```

---

# 21. When Should We Use CommandLineRunner?

Good use cases:

```text
Development seed data

Initial configuration checks

Small startup initialization

Startup diagnostics

Demo/Test data

One-time-per-start initialization logic
```

Avoid heavy/blocking startup work unnecessarily because runner execution application startup readiness ko delay kar sakta hai.

---

# 22. Important Interview Questions

## Q1. What is CommandLineRunner?

`CommandLineRunner` Spring Boot interface hai jo application startup ke baad code execute karne ke liye use hota hai.

---

## Q2. Which method does CommandLineRunner provide?

```java
void run(String... args)
```

---

## Q3. Does run() need to be called manually?

No.

Spring Boot registered `CommandLineRunner` beans ko startup lifecycle mein invoke karta hai.

---

## Q4. Can we access Repository inside CommandLineRunner?

Yes.

Example:

```java
private final ProductRepository productRepository;
```

Constructor injection ke through Repository use kar sakte hain.

---

## Q5. Can CommandLineRunner insert database data?

Yes.

Example:

```java
productRepository.save(product);
```

---

## Q6. Does CommandLineRunner execute after restart?

Yes.

Application restart hone par runner dobara execute hota hai.

Isliye seed logic duplicate data create kar sakti hai.

---

## Q7. What happens with DevTools?

```text
DevTools Restart
       ↓
Application Startup
       ↓
CommandLineRunner Executes Again
```

---

## Q8. CommandLineRunner vs ApplicationRunner?

```text
CommandLineRunner
→ String... args

ApplicationRunner
→ ApplicationArguments
```

---

## Q9. Can we have multiple CommandLineRunners?

Yes.

Execution ordering ke liye:

```java
@Order
```

use kar sakte hain.

---

# 23. Quick Revision

```text
CommandLineRunner
       ↓
Spring Boot Interface
       ↓
run(String... args)
       ↓
Runs during application startup
```

Basic:

```java
@Component
public class StartupRunner
        implements CommandLineRunner {

    @Override
    public void run(String... args) {

        System.out.println(
                "Application Started"
        );
    }
}
```

Database:

```text
CommandLineRunner
       ↓
Repository
       ↓
save()
       ↓
JPA
       ↓
Hibernate
       ↓
MySQL
```

Important:

```text
Every Application Start
        ↓
run() Executes Again
```

Therefore:

```text
Seed Data
   +
DevTools Restart
   ↓
Possible Duplicate Data
```

---

# Final Mental Model

```text
              Spring Boot
                   │
                   ↓
             Application Start
                   │
                   ↓
            Context Initialized
                   │
                   ↓
          CommandLineRunner
                   │
                   ↓
                run()
              /        \
             /          \
      Startup Logic    Repository
                          ↓
                         JPA
                          ↓
                      Hibernate
                          ↓
                        MySQL
```

# CommandLineRunner Completed ✅