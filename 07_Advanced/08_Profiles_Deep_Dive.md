# @Profile in Spring Boot

## 📌 What is @Profile?

`@Profile` ka use ye decide karne ke liye hota hai ki Spring application me **kaunsa Bean kis environment/profile ke andar active hoga**.

Common environments:

```text
Development
Testing
Production
```

Example:

```text
Development → Local configuration
Testing     → Test configuration
Production  → Production configuration
```

---

# Why do we need @Profile?

Maan lo application me Payment Service hai.

Development environment me:

```text
Fake Payment
```

use karna hai.

Production environment me:

```text
Real Payment Gateway
```

use karna hai.

Hum dono implementations application me rakh sakte hain aur `@Profile` ke through decide kar sakte hain ki kaunsa Bean active hoga.

---

# Basic Syntax

```java
@Profile("dev")
```

Example:

```java
@Component
@Profile("dev")
public class DevService {

    public DevService() {

        System.out.println(
                "Development Service Started"
        );
    }
}
```

Ye Bean tabhi create hoga jab:

```text
dev
```

profile active ho.

---

# Production Profile

```java
@Component
@Profile("prod")
public class ProductionService {

    public ProductionService() {

        System.out.println(
                "Production Service Started"
        );
    }
}
```

Ye Bean tabhi create hoga jab:

```text
prod
```

profile active ho.

---

# Profile Activation

Profile activate karne ke liye:

```yaml
spring:
  profiles:
    active: dev
```

Ab:

```text
dev
```

profile active hai.

---

# Profile Switching

Development:

```yaml
spring:
  profiles:
    active: dev
```

Production:

```yaml
spring:
  profiles:
    active: prod
```

Profile change karne par different Beans active ho sakte hain.

---

# @Profile with @Service

Example:

```java
@Service
@Profile("dev")
public class DevEmailService {

    public void sendEmail() {

        System.out.println(
                "Development Email Service"
        );
    }
}
```

Production:

```java
@Service
@Profile("prod")
public class ProductionEmailService {

    public void sendEmail() {

        System.out.println(
                "Production Email Service"
        );
    }
}
```

Architecture:

```text
              EmailService
                   ↓
          ┌────────┴────────┐
          ↓                 ↓
        dev               prod
          ↓                 ↓
   DevEmailService   ProductionEmailService
```

---

# @Profile with @Configuration

`@Profile` ko configuration classes ke saath bhi use kar sakte hain.

Example:

```java
@Configuration
@Profile("dev")
public class DevConfig {

    @Bean
    public String environment() {

        return "Development";
    }
}
```

Production:

```java
@Configuration
@Profile("prod")
public class ProdConfig {

    @Bean
    public String environment() {

        return "Production";
    }
}
```

---

# @Profile with @Bean

`@Profile` ko directly Bean definition par bhi use kiya ja sakta hai.

Example:

```java
@Configuration
public class AppConfig {

    @Bean
    @Profile("dev")
    public PaymentService devPaymentService() {

        return new FakePaymentService();
    }

    @Bean
    @Profile("prod")
    public PaymentService prodPaymentService() {

        return new RealPaymentService();
    }
}
```

---

# Multiple Profiles

Ek Bean multiple profiles ke liye active ho sakta hai.

Example:

```java
@Profile({"dev", "test"})
```

Matlab Bean:

```text
dev → ✅
test → ✅
prod → ❌
```

---

# Profile Expression

Profiles ke saath expressions bhi use kiye ja sakte hain.

Example:

```java
@Profile("!prod")
```

Meaning:

```text
prod → ❌
dev  → ✅
test → ✅
```

Matlab Bean `prod` ke alawa active ho sakta hai.

---

# Real-World Example

## Development Payment

```java
@Service
@Profile("dev")
public class FakePaymentService {

    public void pay() {

        System.out.println(
                "Fake payment successful"
        );
    }
}
```

## Production Payment

```java
@Service
@Profile("prod")
public class RealPaymentService {

    public void pay() {

        System.out.println(
                "Real payment gateway called"
        );
    }
}
```

Architecture:

```text
                  Application
                      ↓
                   Profile
                 /          \
               dev          prod
                ↓             ↓
          Fake Payment    Real Payment
```

---

# Important Use Cases

## 1. Payment Gateway

```text
dev  → Fake Payment
prod → Real Payment
```

## 2. Email Service

```text
dev  → Console Email
prod → Real Email
```

## 3. Database

```text
dev  → Local Database
test → Test Database
prod → Production Database
```

## 4. External APIs

```text
dev  → Mock API
prod → Real API
```

---

# @Profile vs application-dev.yml

Ye dono related hain, lekin same cheez nahi hain.

`application-dev.yml`:

```text
Environment-specific configuration
```

`@Profile`:

```text
Environment-specific Beans
```

Example:

```text
application-dev.yml
        ↓
Database URL
Logging
Port
API Configuration
```

while:

```java
@Profile("dev")
```

controls:

```text
Which Bean should be created?
```

---

# Example Architecture

```text
Spring Boot Application
          │
          ↓
     Active Profile
          │
     ┌────┼────┐
     ↓    ↓    ↓
    dev  test  prod
     │    │     │
     ↓    ↓     ↓
   Beans Beans  Beans
```

---

# Important Point

`@Profile` mainly controls **Bean registration based on the active Spring profile**.

Example:

```java
@Component
@Profile("dev")
public class DevService {
}
```

If `dev` is not active, this Bean will not be registered through that profile condition.

---

# Interview Questions

## What is @Profile?

`@Profile` is used to conditionally register Spring Beans based on the currently active Spring profile.

---

## Why is @Profile useful?

It allows different implementations/configurations to be used for different environments.

Example:

```text
Development
Testing
Production
```

---

## Can @Profile be used with @Service?

Yes.

```java
@Service
@Profile("dev")
```

---

## Can @Profile be used with @Configuration?

Yes.

```java
@Configuration
@Profile("dev")
```

---

## Can @Profile be used with @Bean?

Yes.

```java
@Bean
@Profile("dev")
```

---

## Can multiple profiles be specified?

Yes.

```java
@Profile({"dev", "test"})
```

---

## What does @Profile("!prod") mean?

It means the Bean is active when the `prod` profile is not active.

---

# @Profile vs @Conditional

Both can conditionally control Bean creation.

`@Profile` is specifically designed around Spring environments/profiles.

Example:

```java
@Profile("dev")
```

For more complex conditions, Spring provides other conditional mechanisms such as:

```java
@Conditional
@ConditionalOnProperty
@ConditionalOnClass
```

---

# Best Practice

Use profiles to separate environment-specific implementations.

Good:

```text
dev
test
prod
```

Avoid creating unnecessary profiles for every small configuration difference.

For secrets such as:

```text
Database Password
API Keys
SMTP Password
```

use environment variables or a proper secret-management solution instead of hardcoding them.

---

# Summary

```text
@Profile
    ↓
Check Active Profile
    ↓
Conditionally Register Bean
```

Remember:

```text
@Profile("dev")
        ↓
Bean available in dev

@Profile("prod")
        ↓
Bean available in prod

@Profile({"dev", "test"})
        ↓
Bean available in dev + test

@Profile("!prod")
        ↓
Bean available when prod is NOT active
```

---

# Real-World Pattern

```text
                    Spring Boot
                         ↓
                   Active Profile
                         ↓
              ┌──────────┼──────────┐
              ↓          ↓          ↓
             dev        test       prod
              ↓          ↓          ↓
          Dev Beans   Test Beans  Prod Beans
              ↓          ↓          ↓
          Local DB    Test DB    Production DB
```

The main purpose of `@Profile` is:

> **Use different Spring Beans for different environments without changing the main application code.**     