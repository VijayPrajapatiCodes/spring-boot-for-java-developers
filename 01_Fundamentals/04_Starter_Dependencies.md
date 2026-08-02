# Spring Boot Starter Dependencies

## 1. What are Starter Dependencies?

Spring Boot Starter Dependencies are convenient dependency descriptors that provide commonly required dependencies for a specific type of application or feature.

Instead of adding many related dependencies individually, we can add an appropriate Spring Boot starter.

Example:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webmvc</artifactId>
</dependency>
```

This starter helps provide dependencies required for building Spring MVC web applications.

---

## 2. Why Do We Need Starter Dependencies?

Without starters, developers may need to identify and add several related dependencies individually.

For a web application, these can include libraries related to:

```text
Spring MVC
Tomcat
JSON Processing
Logging
Spring Core
etc.
```

Spring Boot starters simplify dependency setup.

```text
Without Starter
      ↓
Add Dependency 1
Add Dependency 2
Add Dependency 3
Add Dependency 4
Manage Compatibility
      ↓
Application

With Starter
      ↓
Add Appropriate Starter
      ↓
Related Dependencies Resolved
      ↓
Application
```

---

## 3. Web Starter in Our Project

In our Spring Boot learning project, we have:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webmvc</artifactId>
</dependency>
```

This is the direct dependency declared in our `pom.xml`.

At runtime, our project also contained Spring MVC, Tomcat, Jackson and other related libraries. :contentReference[oaicite:0]{index=0}

---

## 4. Direct Dependency

A dependency explicitly declared in our `pom.xml` is a **Direct Dependency**.

Example:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webmvc</artifactId>
</dependency>
```

Here:

```text
spring-boot-starter-webmvc
```

is a direct dependency because we explicitly declared it.

---

## 5. Transitive Dependency

A dependency brought into the project through another dependency is called a **Transitive Dependency**.

Example concept:

```text
Our Application
      ↓
spring-boot-starter-webmvc
      ↓
Related Dependencies
      │
      ├── Spring Web MVC
      ├── Tomcat
      ├── Jackson-related libraries
      ├── Logging-related libraries
      └── Other required libraries
```

We do not necessarily need to declare every transitive dependency individually in our application's `pom.xml`.

Maven resolves the dependency graph for us.

---

## 6. Direct vs Transitive Dependency

### Direct Dependency

Explicitly declared in our project:

```text
pom.xml
   ↓
spring-boot-starter-webmvc
```

### Transitive Dependency

Comes through another dependency:

```text
pom.xml
   ↓
Starter
   ↓
Other Dependencies
```

Simple difference:

```text
Direct Dependency
→ We declare it directly.

Transitive Dependency
→ Another dependency brings it into the project.
```

---

## 7. Starter Dependencies and Maven

Spring Boot starters work together with Maven's dependency resolution.

When Maven sees:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webmvc</artifactId>
</dependency>
```

Maven reads its dependency information and resolves the required dependency graph.

Simplified:

```text
pom.xml
   ↓
Maven
   ↓
Starter Dependency
   ↓
Transitive Dependencies
   ↓
Downloaded to Local Repository
   ↓
Available on Classpath
```

Maven commonly stores downloaded dependencies in the local Maven repository:

```text
.m2/repository
```

Our runtime output showed Spring Boot, Spring Framework, Tomcat, Jackson and other JAR files being loaded from the local Maven repository. :contentReference[oaicite:1]{index=1}

---

## 8. Dependency Version Management

Notice our starter declaration:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webmvc</artifactId>
</dependency>
```

We did not specify:

```xml
<version>...</version>
```

for this dependency.

Spring Boot projects provide dependency management so that compatible versions of supported dependencies can be managed centrally.

This reduces the need to manually specify versions for every managed dependency.

---

## 9. Starter vs Dependency Management

These are related but different concepts.

### Starter

Provides a convenient set of related dependencies.

```text
Starter
   ↓
Required Libraries
```

### Dependency Management

Manages dependency versions.

```text
Dependency Management
        ↓
Compatible Versions
```

Therefore:

```text
Starter
→ What group of dependencies do I commonly need?

Dependency Management
→ Which managed versions should be used?
```

---

## 10. Starter Dependencies vs Auto Configuration

This is an important difference.

### Starter Dependency

Makes required libraries available on the classpath.

```text
Starter
   ↓
Dependencies
   ↓
Classpath
```

### Auto Configuration

Examines the application's environment and conditionally configures suitable components.

```text
Classpath
   +
Beans
   +
Properties
   ↓
Auto Configuration
   ↓
Conditions
   ↓
Configuration
```

They work together:

```text
Starter Dependency
        ↓
Libraries Available
        ↓
Auto Configuration Detects Classes
        ↓
Conditions Match
        ↓
Configuration Applied
```

---

## 11. Practical Example — Tomcat

Our web dependencies resulted in Tomcat-related classes being available on the classpath.

Spring Boot's condition report then showed:

```text
TomcatServletWebServerAutoConfiguration matched
```

because the required Tomcat and Servlet classes were available. :contentReference[oaicite:2]{index=2}

The application subsequently started Tomcat:

```text
Tomcat initialized with port 8080
```

and:

```text
Tomcat started on port 8080
```

:contentReference[oaicite:3]{index=3}

The complete relationship is:

```text
Web Starter / Dependencies
          ↓
Tomcat Libraries Available
          ↓
Classpath
          ↓
Auto Configuration
          ↓
Conditions Match
          ↓
Tomcat Configured
          ↓
Embedded Tomcat Starts
```

---

## 12. Test Starter

Our project also contains a testing dependency:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webmvc-test</artifactId>
    <scope>test</scope>
</dependency>
```

The important part is:

```xml
<scope>test</scope>
```

It indicates that the dependency is intended for the test classpath rather than normal production application code.

Test code is normally located under:

```text
src/test/java
```

---

## 13. Benefits of Starter Dependencies

Starter dependencies provide several benefits:

- Less dependency configuration
- Easier project setup
- Convenient dependency grouping
- Reduced boilerplate in `pom.xml`
- Works with Spring Boot dependency management
- Faster application development
- Easier integration with Spring Boot Auto Configuration

---

## 14. Common Misunderstanding

A starter itself does not perform all the configuration.

For example:

```text
Starter
→ Provides dependencies

Auto Configuration
→ Configures suitable components
```

Therefore, do not confuse:

```text
Starter Dependencies ≠ Auto Configuration
```

Instead:

```text
Starter Dependencies
        +
Auto Configuration
        ↓
Easy Spring Boot Setup
```

---

# 15. Interview Questions

### Q1. What is a Spring Boot Starter?

A Spring Boot Starter is a convenient dependency descriptor that provides commonly required dependencies for a particular type of application or feature.

---

### Q2. Why are Starter Dependencies useful?

They reduce the need to manually identify and add many related dependencies individually.

---

### Q3. What is a Direct Dependency?

A dependency explicitly declared in the project's `pom.xml` is called a direct dependency.

---

### Q4. What is a Transitive Dependency?

A dependency that enters the project through another dependency is called a transitive dependency.

---

### Q5. What is the difference between a Starter and Auto Configuration?

A starter makes required libraries available to the application, while Auto Configuration checks the application environment and conditionally configures suitable components.

---

### Q6. Do we always need to specify versions for Spring Boot managed dependencies?

No.

Spring Boot's dependency management can provide versions for supported managed dependencies.

---

### Q7. What does `<scope>test</scope>` mean?

It indicates that the dependency is intended for testing and is available on the test classpath rather than being a normal runtime dependency of the production application.

---

# 16. Quick Revision

```text
                  Spring Boot Starter
                         ↓
                 Related Dependencies
                         ↓
                       Maven
                         ↓
              Resolve Dependency Graph
                         ↓
                      Classpath
                         ↓
               Auto Configuration
                         ↓
                Conditions Match
                         ↓
                  Configuration
                         ↓
                Application Ready
```

## Remember

> **Starter Dependencies simplify dependency setup, while Auto Configuration uses the available application environment to configure Spring Boot automatically.**