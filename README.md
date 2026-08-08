# 🌱 Spring Boot for Java Developers

> A structured Spring Boot learning repository covering concepts, notes, code implementations, and practical examples from fundamentals to advanced Spring Boot features.

This repository documents my learning journey with **Spring Boot and Java backend development**, with each topic organized into dedicated notes and practical implementations.

---

## 🚀 About This Repository

The purpose of this repository is to build a strong and practical understanding of Spring Boot by learning concepts step by step and implementing them in code.

The repository currently covers:

- Spring Boot Fundamentals
- Configuration
- REST APIs
- API Development
- DTO & Mapping
- Spring Data JPA
- Advanced Spring Boot

The focus is on understanding how different parts of a Spring Boot backend application work together.

---

# 🛠️ Technology Stack

| Technology | Usage |
|---|---|
| ☕ Java 17 | Programming Language |
| 🌱 Spring Boot 3.5.6 | Backend Framework |
| 🌐 Spring MVC | Web & REST API Development |
| 🗄️ Spring Data JPA | Database Access |
| ⚙️ Hibernate | ORM |
| 🐬 MySQL | Relational Database |
| 📦 Maven | Build & Dependency Management |
| 🔴 Redis | Caching |
| 📊 Spring Boot Actuator | Application Monitoring |
| 📈 Prometheus | Metrics |
| 📉 Grafana | Monitoring & Visualization |
| 📧 Gmail SMTP | Email Integration |
| 🔧 Git & GitHub | Version Control |

---

# 📚 Learning Roadmap

```text
01 Fundamentals
       ↓
02 Configuration
       ↓
03 REST API
       ↓
04 API Development
       ↓
05 DTO & Mapping
       ↓
06 Spring Data JPA
       ↓
07 Advanced Spring Boot
```

---

# 01 — Spring Boot Fundamentals

Core concepts required to understand Spring Boot applications.

### Topics

- Spring Boot Introduction
- Spring Boot Project Structure
- Spring Boot Starters
- Dependency Injection
- IoC Container
- Spring Beans
- Component Scanning
- `@Component`
- `@Service`
- `@Repository`
- `@Controller`
- `@RestController`

---

# 02 — Configuration

Understanding how Spring Boot applications are configured.

### Topics

- `application.properties`
- `application.yml`
- Custom Properties
- `@Value`
- `@ConfigurationProperties`
- Externalized Configuration
- Environment Configuration
- Configuration Profiles

---

# 03 — REST API

Building RESTful APIs using Spring Boot.

### Topics

- REST Architecture
- HTTP Methods
- GET
- POST
- PUT
- PATCH
- DELETE
- Request Parameters
- Path Variables
- Request Body
- Response Entity
- HTTP Status Codes
- REST API Structure

---

# 04 — API Development

Understanding the layered architecture of a Spring Boot backend.

```text
Client
  │
  ▼
Controller
  │
  ▼
Service
  │
  ▼
Repository
  │
  ▼
Database
```

### Topics

- Controller Layer
- Service Layer
- Repository Layer
- Request Handling
- Response Handling
- Validation
- Exception Handling
- Custom Exceptions
- API Development Practices

---

# 05 — DTO & Mapping

Understanding how DTOs are used to separate API models from database entities.

### Topics

- DTO Concept
- Entity vs DTO
- Request DTO
- Response DTO
- Object Mapping
- ModelMapper
- API Model Separation

---

# 06 — Spring Data JPA

Database integration and ORM using Spring Data JPA.

### Topics

- JPA Introduction
- Entity Mapping
- `@Entity`
- `@Id`
- `@GeneratedValue`
- Entity Relationships
- One-to-One
- One-to-Many
- Many-to-One
- Many-to-Many
- Repository Interfaces
- CRUD Operations
- Query Methods
- Custom Queries
- Transactions
- Auditing

---

# 07 — Advanced Spring Boot

Advanced Spring Boot concepts covered through notes and practical implementations.

---

## 🔧 Spring Boot DevTools

- Spring Boot DevTools
- Automatic Restart
- Development Workflow

---

## ▶️ CommandLineRunner

- Application Startup Tasks
- `CommandLineRunner`
- Startup Logic

---

## 📊 Spring Boot Actuator

- Application Health
- Actuator Endpoints
- Health Details
- Application Metrics
- Prometheus Metrics
- Monitoring Concepts

---

## ⚡ Caching

- What is Cache?
- Why Caching is Required
- Cache vs Database
- Cache Strategies
- `@EnableCaching`
- `@Cacheable`
- `@CachePut`
- `@CacheEvict`
- Redis Caching
- Cache Performance

---

## ⏰ Scheduling

- Scheduled Tasks
- `@Scheduled`
- Fixed Rate
- Fixed Delay
- Cron Expressions
- Background Scheduled Jobs

---

## 🔄 Asynchronous Processing

- Synchronous vs Asynchronous Execution
- `@Async`
- Async Execution
- Background Processing
- Thread-based Execution

---

## 📧 Email Integration

- SMTP
- Gmail SMTP
- SMTP Configuration
- `JavaMailSender`
- Simple Text Email
- HTML Email
- Dynamic Email
- Email Attachments
- Email with `@Async`

---

## 🌎 Spring Profiles

- Development Profile
- Test Profile
- Production Profile
- Profile-specific Configuration
- `@Profile`
- Multiple Profiles
- Profile Negation
- Environment-specific Beans

---

## 🎨 Spring Boot Banner

- Default Banner
- Custom `banner.txt`
- Custom Text Banner
- Dynamic Banner Properties
- Banner Configuration
- Disable Banner

---

# 📊 Application Monitoring

Spring Boot Actuator was also explored with a monitoring stack using:

```text
Spring Boot Application
          │
          ▼
 Spring Boot Actuator
          │
          ▼
      Prometheus
          │
          ▼
       Grafana
```

### Monitoring Concepts

- Application Health
- JVM Metrics
- Memory Metrics
- CPU Metrics
- HTTP Request Metrics
- Database Metrics
- Prometheus Metrics
- Grafana Visualization

---

# 📸 Practical Screenshots

Screenshots from the practical implementations are available in the repository.

## 🌐 REST Controller

![REST Controller](./screenshort/Controller.png)

---

## 📧 Email Service

![Email Service](./screenshort/EmailService.png)

---

## ✅ Email Confirmation

![Email Confirmation](./screenshort/EmailConfirm.png)

---

## 🌎 Spring Profiles

![Spring Profiles](./screenshort/Profile.png)

---

# 🏗️ Project Structure

```text
spring-boot-for-java-developers/
│
├── 01_Fundamentals/
│
├── 02_Configuration/
│
├── 03_REST_API/
│
├── 04_API_Development/
│
├── 05_DTO_and_Mapping/
│
├── 06_Spring_Data_JPA/
│
├── 07_Advanced/
│   │
│   ├── 01_DEVTOOLS.md
│   ├── 02_CommandLineRunner.md
│   ├── 03_Actuator.md
│   ├── 04_Caching.md
│   ├── 05_Scheduling.md
│   ├── 06_Asynchronous.md
│   ├── 07_Email.md
│   ├── 08_Profiles_Deep_Dive.md
│   └── 09_Banner.md
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   └── resources/
│   │
│   └── test/
│
├── screenshort/
│   ├── Controller.png
│   ├── EmailConfirm.png
│   ├── EmailService.png
│   └── Profile.png
│
├── pom.xml
├── mvnw
├── mvnw.cmd
├── .gitignore
└── README.md
```

---

# 💻 Development Approach

The practical implementations follow a layered backend architecture:

```text
                 Client
                   │
                   ▼
              REST Controller
                   │
                   ▼
                Service
                   │
                   ▼
              Repository
                   │
                   ▼
                MySQL
```

Supporting Spring Boot features:

```text
                  Spring Boot
                       │
       ┌───────────────┼────────────────┐
       ▼               ▼                ▼
    Caching         Scheduling        Async
       │               │                │
      Redis         @Scheduled        @Async

                       │
                       ▼
                  Monitoring
                       │
                  ┌────┴────┐
                  ▼         ▼
              Actuator   Prometheus
                             │
                             ▼
                          Grafana
```

---

# 🎯 Learning Focus

The main focus of this repository is developing a practical understanding of Spring Boot backend development.

### Key areas

- Building REST APIs
- Understanding Spring Boot architecture
- Dependency Injection
- Layered application design
- Database integration
- JPA and Hibernate
- DTO-based API design
- Validation
- Exception Handling
- Caching
- Scheduled Tasks
- Asynchronous Processing
- Email Integration
- Application Monitoring
- Environment-specific Configuration
- Spring Boot Developer Tools
- Custom Application Banner

---

# 📖 Documentation

Each major topic contains dedicated notes in Markdown format.

The notes are organized so that concepts can be studied individually and revisited during development.

```text
Concept
   ↓
Notes
   ↓
Implementation
   ↓
Practical Understanding
```

---

# 👨‍💻 About Me

**Vijay Prajapati**

Java Backend Developer

Currently focusing on:

```text
Java
Spring Boot
REST APIs
MySQL
JPA / Hibernate
Redis
Backend Development
```

### GitHub

https://github.com/VijayPrajapatiCodes

### LinkedIn

https://www.linkedin.com/in/thevijayprajapati/

---

# 📌 Repository Status

🚧 **Actively maintained and updated**

This repository represents my ongoing learning and practical implementation of Spring Boot and Java backend development concepts.

---

## ⭐ Support

If you find the notes or implementations useful, feel free to explore the repository and leave a ⭐.
