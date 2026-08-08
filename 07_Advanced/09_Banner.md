# Spring Boot Banner

## 📌 What is Spring Boot Banner?

Spring Boot application start hone par console me jo text/logo display hota hai, usse **Spring Boot Banner** kehte hain.

By default Spring Boot apna banner show karta hai.

Example:

```text
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
...
```

Hum default banner ko customize karke apna custom text, ASCII art ya application information display kar sakte hain.

---

# Why Use Banner?

Banner ka use mainly application ko identify karne ke liye hota hai.

Common uses:

- Application branding
- Application identification
- Startup information
- Version information
- Environment information
- Multiple applications/services ko identify karna

Example:

```text
========================================
        VIJAY SPRING BOOT
========================================

        Monitoring System
        Java 17
        Spring Boot 3.5.6

========================================
```

---

# Default Banner

Spring Boot by default apna banner console me display karta hai.

```text
Spring Boot
    ↓
Application Startup
    ↓
Default Banner
    ↓
Application Starts
```

---

# Custom Banner

Custom banner create karne ke liye:

```text
src/main/resources/banner.txt
```

file create karo.

Project structure:

```text
src
└── main
    └── resources
        └── banner.txt
```

---

# Creating banner.txt

Example:

```text
========================================

        V I J A Y   P R A J A P A T I

        SPRING BOOT APPLICATION

        Java 17 | Spring Boot

========================================
```

Application start karne par Spring Boot custom banner display karega.

---

# Custom Banner Example

```text
========================================

        VIJAY SPRING BOOT

        Monitoring System
        Java 17
        Spring Boot 3.5.6

========================================
```

Startup console:

```text
========================================

        VIJAY SPRING BOOT

        Monitoring System
        Java 17
        Spring Boot 3.5.6

========================================

Spring Boot Application Started...
```

---

# Dynamic Banner

Banner ke andar Spring properties aur environment properties use ki ja sakti hain.

Example:

```text
========================================

        V I J A Y   P R A J A P A T I

        ${spring.application.name}

        Java Version: ${java.version}
        Spring Boot: ${spring-boot.version}

        Environment: ${app.environment}

========================================
```

---

# application.yml

Example:

```yaml
spring:
  application:
    name: springboot-learning

app:
  environment: Development
```

Ab banner me:

```text
${spring.application.name}
```

application name se resolve ho sakta hai.

Similarly:

```text
${java.version}
```

Java version display kar sakta hai.

And:

```text
${app.environment}
```

application ka environment display kar sakta hai.

---

# Common Banner Properties

Some useful values:

```text
${spring.application.name}
${java.version}
${spring-boot.version}
```

Custom application property:

```text
${app.environment}
```

Example:

```text
Application : ${spring.application.name}
Java        : ${java.version}
Spring Boot : ${spring-boot.version}
Environment : ${app.environment}
```

---

# Disable Banner

Agar application startup par banner nahi chahiye:

```yaml
spring:
  main:
    banner-mode: off
```

Now:

```text
Banner → Disabled
```

---

# Banner Modes

Spring Boot supports different banner modes.

```yaml
spring:
  main:
    banner-mode: console
```

Banner console par display hota hai.

```yaml
spring:
  main:
    banner-mode: log
```

Banner application log me display hota hai.

```yaml
spring:
  main:
    banner-mode: off
```

Banner disable ho jata hai.

---

# Banner Architecture

```text
Spring Boot Application
          ↓
     Application Start
          ↓
      Banner Loader
          ↓
    ┌─────┴─────┐
    ↓           ↓
Default      banner.txt
Banner       Custom Banner
                ↓
          Console / Log
```

---

# Real-World Use Case

Maan lo ek company ke paas multiple microservices hain:

```text
User Service
Order Service
Payment Service
Notification Service
```

Har application ka custom startup banner ho sakta hai.

Example:

```text
================================
          ORDER SERVICE
================================

Version     : 1.0.0
Environment : Production
Java        : 17

================================
```

Application start hote hi developer ko easily pata chal sakta hai ki kaunsi service start hui hai.

---

# Banner in Microservices

Example:

```text
                Microservices
                     │
       ┌─────────────┼─────────────┐
       ↓             ↓             ↓
  User Service   Order Service  Payment Service
       ↓             ↓             ↓
    Banner         Banner        Banner
```

Custom banners service identification me useful ho sakte hain.

---

# ASCII Art Banner

Banner me ASCII art bhi use kar sakte hain.

Example:

```text
██╗   ██╗██╗     ██╗   ██╗
██║   ██║██║     ██║   ██║
██║   ██║██║     ██║   ██║
╚██╗ ██╔╝██║     ██║   ██║
 ╚████╔╝ ███████╗╚██████╔╝
  ╚═══╝  ╚══════╝ ╚═════╝
```

Is tarah application ka custom visual identity create kiya ja sakta hai.

---

# Banner vs Logging

Banner aur logging ka purpose different hai.

### Banner

```text
Application Startup
       ↓
Application Identity
```

### Logging

```text
Application Runtime
       ↓
Application Events
       ↓
Logs
```

Banner primarily startup identification ke liye hota hai, jabki logging application ke runtime events aur problems ko record karti hai.

---

# Best Practices

### 1. Banner ko simple rakho

Too much information avoid karo.

Good:

```text
Application
Version
Environment
Java Version
```

### 2. Secrets mat show karo

Never display:

```text
Database Password
SMTP Password
API Keys
JWT Secret
```

### 3. Production me sensitive information avoid karo

Banner logs/console me visible ho sakta hai.

### 4. Microservices me useful naming rakho

Example:

```text
USER-SERVICE
ORDER-SERVICE
PAYMENT-SERVICE
```

---

# Interview Questions

## What is Spring Boot Banner?

Spring Boot Banner is the text or logo displayed when a Spring Boot application starts.

---

## How do you create a custom Spring Boot banner?

Create:

```text
src/main/resources/banner.txt
```

and add the desired text.

---

## How do you disable Spring Boot Banner?

Use:

```yaml
spring:
  main:
    banner-mode: off
```

---

## Where is banner.txt placed?

```text
src/main/resources/banner.txt
```

---

## Can we use dynamic values in banner?

Yes.

Example:

```text
${spring.application.name}
${java.version}
```

and application properties can also be referenced.

---

## Why use a custom banner?

Common reasons:

```text
Application Identification
Branding
Version Information
Environment Identification
Microservice Identification
```

---

# Summary

```text
Spring Boot Banner
        ↓
Application Startup
        ↓
Default / Custom Banner
        ↓
Console / Log
```

Important file:

```text
src/main/resources/banner.txt
```

Disable:

```yaml
spring:
  main:
    banner-mode: off
```

Custom:

```text
banner.txt
```

Dynamic:

```text
${spring.application.name}
${java.version}
${spring-boot.version}
${app.environment}
```

---

# Practical Status

```text
Default Banner       ✅
Custom banner.txt    ✅
Custom Text          ✅
Startup Banner       ✅
Dynamic Properties   ✅
Disable Banner       ✅
```

---

# Key Point

> **Spring Boot Banner is mainly used to customize and identify an application during startup.**