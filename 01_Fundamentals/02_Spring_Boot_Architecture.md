# Spring Boot Architecture

## 1. Introduction

Spring Boot is built on top of the **Spring Framework**.

Internally, Spring Boot uses Spring concepts such as:

- IoC Container
- ApplicationContext
- Beans
- Dependency Injection
- Component Scanning

Spring Boot simplifies application startup and configuration using features such as:

- Auto Configuration
- Component Scanning
- Embedded Server
- Starter Dependencies

---

## 2. High-Level Spring Boot Architecture

A typical Spring Boot web application can follow this architecture:

```text
Client / Browser
       ↓
Embedded Server (Tomcat)
       ↓
Spring MVC
       ↓
Controller
       ↓
Service
       ↓
Repository
       ↓
Database
```

Each layer has a different responsibility.

### Controller

Handles incoming HTTP requests and returns responses.

### Service

Contains business logic.

### Repository

Handles data-access operations.

### Database

Stores application data.

> Controller, Service, Repository, and Database interaction will be studied in detail in later topics.

---

# 3. Spring Boot Application Startup Flow

A Spring Boot application normally starts from the `main()` method.

Example:

```java
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

Simplified startup flow:

```text
JVM
 ↓
main()
 ↓
SpringApplication.run()
 ↓
ApplicationContext Created
 ↓
Component Scanning
 ↓
Auto Configuration
 ↓
Beans Created and Managed
 ↓
Embedded Server Starts
 ↓
Application Ready
```

---

# 4. `main()` Method

Spring Boot applications are normal Java applications and execution starts from:

```java
public static void main(String[] args)
```

Example:

```java
public static void main(String[] args) {

    SpringApplication.run(
            SpringbootLearningApplication.class,
            args
    );
}
```

The JVM executes the `main()` method first.

---

# 5. `SpringApplication.run()`

`SpringApplication.run()` bootstraps and starts the Spring Boot application.

```java
SpringApplication.run(
        SpringbootLearningApplication.class,
        args
);
```

At a high level, it performs tasks such as:

- Preparing the Spring application
- Creating the appropriate `ApplicationContext`
- Loading application configuration
- Registering Spring Beans
- Triggering auto-configuration
- Starting the embedded web server for a web application

It also returns the application's `ApplicationContext`.

Example:

```java
ApplicationContext context =
        SpringApplication.run(
                SpringbootLearningApplication.class,
                args
        );
```

---

# 6. ApplicationContext

`ApplicationContext` is a Spring **IoC Container**.

Its important responsibilities include:

- Creating Beans
- Configuring Beans
- Managing Beans
- Resolving dependencies
- Injecting dependencies
- Providing Beans when required

```text
ApplicationContext
       ↓
IoC Container
       ↓
Create Beans
       ↓
Configure Beans
       ↓
Manage Beans
       ↓
Inject Dependencies
```

### Important

`ApplicationContext` does not simply store application data.

It primarily manages **Spring Beans and their dependencies**.

---

# 7. `@SpringBootApplication`

The main Spring Boot application class normally contains:

```java
@SpringBootApplication
```

It is a convenience annotation that combines the effect of three important annotations:

```text
@SpringBootApplication
        │
        ├── @SpringBootConfiguration
        │
        ├── @EnableAutoConfiguration
        │
        └── @ComponentScan
```

A simple way to remember them:

```text
C → Configuration
A → Auto Configuration
S → Scanning
```

---

# 8. `@SpringBootConfiguration`

`@SpringBootConfiguration` indicates that the class provides Spring Boot application configuration.

Conceptually:

```text
@SpringBootConfiguration
        ↓
Spring Boot Configuration Class
```

It is based on Spring's configuration mechanism.

---

# 9. `@EnableAutoConfiguration`

`@EnableAutoConfiguration` enables Spring Boot's auto-configuration mechanism.

Spring Boot checks things such as:

- Classes available on the classpath
- Dependencies
- Existing Beans
- Application configuration

It then applies suitable automatic configuration.

Example:

```text
Spring Web dependencies available
           ↓
Spring Boot detects web environment
           ↓
Web-related Auto Configuration
           ↓
Embedded Server Configuration
```

Auto Configuration will be studied deeply in the next topic.

---

# 10. `@ComponentScan`

`@ComponentScan` allows Spring to discover Spring-managed components.

Examples include:

```java
@Component
@Service
@Repository
@Controller
@RestController
```

Suppose our project structure is:

```text
com.vijay.springbootlearning
│
├── SpringbootLearningApplication.java
│
├── controller/
│
├── service/
│
└── repository/
```

Because the main application class is inside:

```text
com.vijay.springbootlearning
```

Spring can scan its subpackages.

```text
com.vijay.springbootlearning
           ↓
      Component Scan
           ↓
 ┌─────────┼─────────┐
 ↓         ↓         ↓
controller service repository
```

---

# 11. Why Main Class Should Be in Root Package

The main application class should generally be placed in the root/base package.

Recommended:

```text
com.vijay.springbootlearning
│
├── SpringbootLearningApplication.java
├── controller/
├── service/
├── repository/
└── config/
```

This allows component scanning to discover classes inside all subpackages.

For example:

```text
com.vijay.springbootlearning.service
```

will be scanned.

But:

```text
com.vijay.payment
```

is outside:

```text
com.vijay.springbootlearning
```

Therefore, it will not normally be discovered by the default component scanning started from the application's package.

---

# 12. Embedded Web Server

When Spring Boot detects that the application is a servlet-based web application, it can start an embedded web server.

In our project, we observed:

```text
Tomcat initialized with port 8080
```

and:

```text
Tomcat started on port 8080
```

Architecture:

```text
Spring Boot Application
        ↓
Web ApplicationContext
        ↓
Embedded Tomcat
        ↓
Port 8080
        ↓
Application Ready
```

---

# 13. Practical — Component Scanning and IoC Container

We created a service:

```java
package com.vijay.springbootlearning.service;

import org.springframework.stereotype.Service;

@Service
public class MessageService {

    public String getMessage() {
        return "Spring Boot Architecture Practical";
    }
}
```

Here:

```java
@Service
```

marks the class as a Spring-managed service component.

---

## Getting Bean from ApplicationContext

We modified the main class:

```java
package com.vijay.springbootlearning;

import com.vijay.springbootlearning.service.MessageService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class SpringbootLearningApplication {

    public static void main(String[] args) {

        ApplicationContext context =
                SpringApplication.run(
                        SpringbootLearningApplication.class,
                        args
                );

        MessageService messageService =
                context.getBean(MessageService.class);

        System.out.println(messageService.getMessage());
    }
}
```

Output:

```text
Spring Boot Architecture Practical
```

---

# 14. What Happened Internally?

The practical demonstrated the following flow:

```text
@SpringBootApplication
        ↓
@ComponentScan
        ↓
MessageService discovered
        ↓
Spring creates MessageService Bean
        ↓
ApplicationContext manages Bean
        ↓
context.getBean(MessageService.class)
        ↓
MessageService Bean returned
        ↓
getMessage()
        ↓
Spring Boot Architecture Practical
```

Notice that we did **not** write:

```java
new MessageService();
```

Instead:

```java
context.getBean(MessageService.class);
```

was used.

The object was created and managed by Spring's IoC Container.

---

# 15. Startup Logs Observed

While running the application, we observed logs similar to:

```text
Starting SpringbootLearningApplication
```

Then:

```text
Root WebApplicationContext: initialization completed
```

Then:

```text
Tomcat initialized with port 8080
```

Then:

```text
Tomcat started on port 8080
```

Finally:

```text
Started SpringbootLearningApplication
```

This practically confirms the Spring Boot startup process.

---

# 16. Request Architecture

Once REST APIs are created, a simplified request flow looks like:

```text
Client
   ↓
HTTP Request
   ↓
Embedded Tomcat
   ↓
DispatcherServlet
   ↓
Controller
   ↓
Service
   ↓
Repository
   ↓
Database
```

The response travels back toward the client.

```text
Database
   ↓
Repository
   ↓
Service
   ↓
Controller
   ↓
HTTP Response
   ↓
Client
```

`DispatcherServlet` and the complete REST request lifecycle will be studied later.

---

# 17. Interview Questions

### Q1. How does a Spring Boot application start?

Execution starts from the Java `main()` method, which calls:

```java
SpringApplication.run();
```

Spring Boot then creates the application context, performs configuration, registers Beans and starts the embedded server when required.

---

### Q2. What is ApplicationContext?

`ApplicationContext` is a Spring IoC Container responsible for creating, configuring, managing, and providing Spring Beans and their dependencies.

---

### Q3. What does `@SpringBootApplication` do?

It combines the functionality of:

```text
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan
```

---

### Q4. What is Component Scanning?

Component scanning allows Spring to automatically discover classes annotated with stereotypes such as:

```text
@Component
@Service
@Repository
@Controller
@RestController
```

and register suitable instances as Spring Beans.

---

### Q5. Why should the main application class be in the root package?

Because default component scanning starts from the package containing the main application class and scans its subpackages.

---

### Q6. What does `SpringApplication.run()` return?

It returns an `ApplicationContext`.

Example:

```java
ApplicationContext context =
        SpringApplication.run(
                SpringbootLearningApplication.class,
                args
        );
```

---

# 18. Quick Revision

```text
Spring Boot Application
        ↓
main()
        ↓
SpringApplication.run()
        ↓
ApplicationContext
        ↓
@ComponentScan
        ↓
Beans Discovered
        ↓
Auto Configuration
        ↓
Embedded Tomcat
        ↓
Application Ready
```

### Remember

> **Spring Boot uses Spring's IoC Container and adds automated startup, configuration, component scanning, and embedded-server support to simplify application development.**