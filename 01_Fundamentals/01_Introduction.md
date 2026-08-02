# Spring Boot Introduction

## 1. What is Spring Boot?

Spring Boot is built on top of the **Spring Framework**.

It makes it easier and faster to create, configure, and run Spring-based applications.

Spring Boot does **not replace Spring Framework**. It internally uses the Spring Framework and simplifies application development by providing features such as:

- Auto Configuration
- Starter Dependencies
- Embedded Server
- Sensible Defaults
- Production-ready features

---

## 2. Spring Framework and Spring Boot

Spring Framework provides the core features required for building Java applications.

Examples:

- IoC Container
- Dependency Injection
- Bean Management
- Spring MVC
- AOP
- Transaction Management

Spring Boot uses these Spring Framework features and reduces the amount of manual setup and configuration required.

```text
Spring Boot
     ↓
Uses Spring Framework
     ↓
IoC Container
     ↓
Beans
     ↓
Dependency Injection
     ↓
Application
```

### Important

> Spring Boot does not replace Spring Framework.  
> It makes Spring Framework easier and faster to configure and use.

---

## 3. Why Do We Need Spring Boot?

Traditional Spring applications may require developers to manually handle several configuration and setup tasks.

For example:

```text
Configure Dependencies
        ↓
Configure Spring
        ↓
Configure Spring MVC
        ↓
Configure Web Server
        ↓
Deploy Application
        ↓
Run Application
```

Spring Boot simplifies many of these tasks.

```text
Spring Framework
       +
Auto Configuration
       +
Starter Dependencies
       +
Embedded Server
       +
Sensible Defaults
       ↓
Spring Boot Application
```

This allows developers to focus more on **business logic** instead of repetitive configuration.

---

## 4. Main Features of Spring Boot

### 4.1 Auto Configuration

Spring Boot can automatically configure many parts of an application based on:

- Dependencies available in the project
- Existing Beans
- Application configuration

This reduces manual configuration.

> Auto Configuration will be covered in detail separately.

---

### 4.2 Starter Dependencies

Spring Boot provides starter dependencies that group commonly required dependencies together.

Example:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

`spring-boot-starter-web` provides commonly required dependencies for building Spring web applications.

> Starter Dependencies will be covered in detail separately.

---

### 4.3 Embedded Server

Spring Boot supports embedded web servers.

Common examples:

- Tomcat
- Jetty

For a typical Spring Boot web application, we do not need to manually install and configure a separate Tomcat server.

```text
Spring Boot Application
        ↓
Embedded Tomcat
        ↓
Application Starts
```

The default HTTP port commonly used by a Spring Boot web application is:

```text
8080
```

Example:

```text
http://localhost:8080
```

---

### 4.4 Sensible Defaults

Spring Boot provides sensible default configurations for common application requirements.

Developers can override these defaults whenever required.

This approach reduces unnecessary configuration.

---

## 5. Basic Spring Boot Application

A Spring Boot project usually contains a main application class.

```java
package com.vijay.springbootlearning;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringbootLearningApplication {

    public static void main(String[] args) {

        SpringApplication.run(
                SpringbootLearningApplication.class,
                args
        );

    }
}
```

This class is the entry point of our Spring Boot application.

---

## 6. `@SpringBootApplication`

`@SpringBootApplication` is one of the most important annotations in Spring Boot.

```java
@SpringBootApplication
```

At a high level, it tells Spring Boot that this is the main configuration/application class.

It enables important Spring Boot functionality such as:

- Configuration
- Auto Configuration
- Component Scanning

We will study its internal working in later topics.

---

## 7. `SpringApplication.run()`

The following statement starts the Spring Boot application:

```java
SpringApplication.run(SpringbootLearningApplication.class, args);
```

High-level flow:

```text
main()
   ↓
SpringApplication.run()
   ↓
Spring Boot Starts
   ↓
Spring ApplicationContext Created
   ↓
Beans Configured
   ↓
Auto Configuration
   ↓
Embedded Web Server Starts
   ↓
Application Ready
```

---

## 8. Practical Observation

We created the project using **Spring Initializr** and added the `Spring Web` dependency.

After running the application, Spring Boot automatically started the embedded Tomcat server.

We did not manually:

```text
Install Tomcat separately       ❌
Configure Tomcat manually       ❌
Deploy WAR manually             ❌
Configure Spring MVC manually   ❌
```

Still, the web application started successfully.

This demonstrates one of the major advantages of Spring Boot.

---

## 9. Spring Framework vs Spring Boot

| Spring Framework | Spring Boot |
|---|---|
| Core Spring framework | Built on top of Spring Framework |
| Provides IoC and DI | Uses Spring IoC and DI |
| Provides Bean management | Uses Spring Bean management |
| More configuration may be required | Reduces manual configuration |
| Dependencies may need more manual setup | Provides starter dependencies |
| External server deployment can be used | Supports embedded servers |
| Provides core Spring functionality | Simplifies Spring application development |

---

## 10. Advantages of Spring Boot

- Faster application development
- Less manual configuration
- Auto Configuration
- Starter Dependencies
- Embedded Server support
- Easy application setup
- Standalone application support
- Easy integration with the Spring ecosystem
- Production-ready features

---

## 11. Interview Questions

### Q1. What is Spring Boot?

Spring Boot is built on top of the Spring Framework and simplifies the development of Spring applications by providing features such as auto-configuration, starter dependencies, and embedded servers.

### Q2. Does Spring Boot replace Spring Framework?

No.

Spring Boot internally uses the Spring Framework and simplifies its configuration and application setup.

### Q3. Why do we use Spring Boot?

Spring Boot is used to reduce manual configuration and make Spring application development faster and easier.

### Q4. What are the major features of Spring Boot?

Major features include:

- Auto Configuration
- Starter Dependencies
- Embedded Servers
- Sensible Defaults
- Production-ready features

### Q5. What is an embedded server?

An embedded server runs as part of the application itself.

For example, a Spring Boot web application can start an embedded Tomcat server without requiring us to manually install Tomcat separately.

### Q6. What is the default port of a Spring Boot web application?

By default:

```text
8080
```

---

## 12. Quick Revision

```text
                    Spring Boot
                         |
        ┌────────────────┼────────────────┐
        |                |                |
Spring Framework   Easy Configuration   Fast Development
        |
        ├── IoC
        ├── DI
        └── Beans

Spring Boot Features
        |
        ├── Auto Configuration
        ├── Starter Dependencies
        ├── Embedded Server
        └── Sensible Defaults
```

### Remember

> **Spring Framework provides the core functionality, while Spring Boot makes Spring applications easier to configure, develop, and run.**