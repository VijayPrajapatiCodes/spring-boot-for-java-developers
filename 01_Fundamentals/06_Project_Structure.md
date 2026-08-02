# Spring Boot Project Structure

## 1. Introduction

Spring Initializr generates a standard Spring Boot project structure.

A typical Maven-based Spring Boot project looks like:

```text
springboot-learning/
│
├── .mvn/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   └── resources/
│   │
│   └── test/
│       └── java/
│
├── target/
│
├── .gitignore
├── mvnw
├── mvnw.cmd
└── pom.xml
```

Each directory and file has a specific responsibility.

---

# 2. `src/main/java`

`src/main/java` contains the main Java source code of the application.

Example:

```text
src/main/java/
└── com/vijay/springbootlearning/
    │
    ├── SpringbootLearningApplication.java
    │
    └── service/
        └── MessageService.java
```

As the application grows, we can organize classes into different packages.

Example:

```text
com.vijay.springbootlearning/
│
├── SpringbootLearningApplication.java
│
├── controller/
├── service/
├── repository/
├── dto/
├── exception/
└── config/
```

### Purpose

```text
src/main/java
      ↓
Main Application Source Code
```

---

# 3. Main Application Class

The main Spring Boot application class is normally placed inside the base/root package.

Example:

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

This class is the entry point of the Spring Boot application.

Keeping it in the base package also helps default component scanning discover components in its subpackages.

---

# 4. `src/main/resources`

This directory contains application resources and configuration files.

Typical structure:

```text
src/main/resources/
│
├── application.properties
├── static/
└── templates/
```

---

# 5. `application.properties`

`application.properties` is commonly used to configure a Spring Boot application.

Location:

```text
src/main/resources/application.properties
```

Example:

```properties
server.port=9090
spring.application.name=my-application
```

During our Auto Configuration practical, we temporarily used:

```properties
debug=true
```

to display the Spring Boot Conditions Evaluation Report.

Configuration properties will be studied separately.

---

# 6. `static/`

The `static` directory can contain static web resources.

Examples:

```text
static/
├── css/
├── js/
├── images/
└── ...
```

Typical static resources include:

- CSS
- JavaScript
- Images
- Static HTML/resources

For a REST API-only backend project, this directory may not be heavily used.

---

# 7. `templates/`

The `templates` directory is commonly used with server-side template engines.

For example:

```text
templates/
├── home.html
├── login.html
└── dashboard.html
```

Template engines such as Thymeleaf can use this directory.

For a pure REST API backend, templates may not be required.

---

# 8. `src/test/java`

This directory contains test source code.

Example:

```text
src/test/java/
└── com/vijay/springbootlearning/
    └── SpringbootLearningApplicationTests.java
```

Difference:

```text
src/main/java
      ↓
Production/Application Code

src/test/java
      ↓
Test Code
```

This also connects with Maven test-scoped dependencies:

```xml
<scope>test</scope>
```

Such dependencies are intended for testing rather than normal production application code.

---

# 9. `pom.xml`

`pom.xml` is the Maven project configuration file.

POM stands for:

```text
Project Object Model
```

It contains important build-related project configuration such as:

- Project metadata
- Dependencies
- Java configuration
- Spring Boot configuration
- Build plugins

Example dependency:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webmvc</artifactId>
</dependency>
```

Simplified flow:

```text
pom.xml
   ↓
Maven
   ↓
Resolve Dependencies
   ↓
Compile / Test / Package
```

---

# 10. `.mvn/`

The `.mvn` directory contains files/configuration related to the Maven Wrapper.

```text
.mvn/
```

It works together with:

```text
mvnw
mvnw.cmd
```

to provide Maven Wrapper functionality.

---

# 11. Maven Wrapper

Spring Initializr-generated Maven projects normally contain:

```text
mvnw
mvnw.cmd
```

These are Maven Wrapper scripts.

### `mvnw`

Used on Unix-like systems such as:

```text
Linux
macOS
```

### `mvnw.cmd`

Used on Windows.

Example:

```powershell
.\mvnw.cmd clean package
```

The Maven Wrapper helps build the project without relying solely on a globally installed `mvn` command.

---

# 12. `target/`

The `target` directory contains Maven-generated build output.

```text
target/
```

It may contain:

```text
target/
├── classes/
├── test-classes/
├── generated-sources/
└── packaged JAR
```

Our application runtime also used compiled classes from:

```text
target/classes
```

---

# 13. `src` vs `target`

This difference is very important.

```text
src/
 ↓
Original Source Code
 ↓
Written by Developer


target/
 ↓
Generated Build Output
 ↓
Created by Maven
```

Therefore:

> `src` contains our source code, while `target` contains generated build artifacts.

---

# 14. What Happens If `target/` Is Deleted?

Deleting:

```text
target/
```

does **not** delete our original Java source code.

Our source code remains inside:

```text
src/main/java
```

Flow:

```text
Delete target/
      ↓
Original Source Code Safe
      ↓
Run Maven Build
      ↓
target/ Created Again
```

For example:

```powershell
.\mvnw.cmd clean package
```

can build/package the project again and regenerate build output.

---

# 15. `.gitignore`

`.gitignore` tells Git which files or directories should normally not be tracked.

Example:

```gitignore
target/
```

Because `target/` contains generated build output, it normally does not need to be committed.

---

# 16. What Should Be Pushed to GitHub?

Important project files should be committed.

Examples:

```text
src/             ✅
pom.xml          ✅
mvnw             ✅
mvnw.cmd         ✅
.mvn/            ✅
.gitignore       ✅
```

Generated build output normally should not be committed:

```text
target/          ❌
```

The exact ignore rules can be maintained in `.gitignore`.

---

# 17. Why `pom.xml` Should Be Committed

`pom.xml` contains important project configuration.

For example:

```text
Project Information
Dependencies
Build Configuration
Plugins
Java Configuration
```

If another developer clones the project, Maven can read `pom.xml` and resolve the required dependencies.

```text
GitHub Repository
       ↓
Clone Project
       ↓
pom.xml
       ↓
Maven
       ↓
Resolve Dependencies
       ↓
Build Application
```

Therefore:

```text
pom.xml → Commit ✅
```

---

# 18. Why `target/` Is Usually Not Committed

`target/` is generated from the project source and build configuration.

```text
Source Code
   +
pom.xml
   ↓
Maven Build
   ↓
target/
```

Because it can be regenerated:

```text
target/ → Normally Ignore ❌
```

This keeps the repository cleaner and avoids committing unnecessary generated files.

---

# 19. Recommended Package Structure

As our application grows, a common layered structure can look like:

```text
src/main/java/
└── com/vijay/springbootlearning/
    │
    ├── SpringbootLearningApplication.java
    │
    ├── controller/
    │
    ├── service/
    │
    ├── repository/
    │
    ├── dto/
    │
    ├── model/
    │
    ├── exception/
    │
    └── config/
```

Different packages can have different responsibilities.

```text
controller/
→ Handles HTTP requests

service/
→ Business logic

repository/
→ Data access

dto/
→ Data Transfer Objects

model/
→ Domain/data models

exception/
→ Exception handling

config/
→ Application configuration
```

We will understand these packages practically as we build REST APIs.

---

# 20. Complete Project Flow

```text
Developer
    ↓
Writes Code
    ↓
src/main/java
    +
src/main/resources
    ↓
pom.xml
    ↓
Maven
    ↓
Compile / Test / Package
    ↓
target/
    ↓
Application Build
```

---

# 21. Interview Questions

### Q1. What is `src/main/java`?

It contains the main Java source code of the application.

### Q2. What is `src/main/resources`?

It contains application resources and configuration files such as `application.properties`.

### Q3. What is `src/test/java`?

It contains test source code.

### Q4. What is `pom.xml`?

`pom.xml` is Maven's Project Object Model file containing project and build configuration such as dependencies and plugins.

### Q5. What is the `target` directory?

`target` contains generated Maven build output such as compiled classes and packaged artifacts.

### Q6. Can we delete the `target` directory?

Yes.

It contains generated build output and can be recreated by building the project again.

### Q7. Does deleting `target/` delete our original Java source code?

No.

Original Java source code is stored under:

```text
src/main/java
```

### Q8. Should `target/` be pushed to GitHub?

Normally no, because it contains generated build output.

### Q9. Should `pom.xml` be pushed to GitHub?

Yes.

It contains essential Maven project and build configuration.

### Q10. What are `mvnw` and `mvnw.cmd`?

They are Maven Wrapper scripts.

```text
mvnw      → Linux/macOS
mvnw.cmd  → Windows
```

---

# 22. Quick Revision

```text
Spring Boot Project
│
├── src/
│   │
│   ├── main/
│   │   ├── java/
│   │   │    └── Java Source Code
│   │   │
│   │   └── resources/
│   │        └── Configuration/Resources
│   │
│   └── test/
│        └── Test Code
│
├── target/
│    └── Generated Build Output
│
├── .mvn/
│    └── Maven Wrapper Support
│
├── mvnw / mvnw.cmd
│    └── Maven Wrapper
│
├── .gitignore
│    └── Ignore Rules
│
└── pom.xml
     └── Maven Project Configuration
```

## Remember

> **`src` contains the source we maintain, `pom.xml` defines the Maven project/build, and `target` contains generated build output.**