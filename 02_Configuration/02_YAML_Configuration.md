# Spring Boot YAML Configuration

## 1. Introduction

Spring Boot application configuration ke liye `.properties` ke saath YAML format bhi use kar sakta hai.

Common YAML configuration file:

```text
src/main/resources/application.yml
```

For example, `application.properties`:

```properties
server.port=9091
spring.application.name=springboot-learning
```

Same configuration YAML mein:

```yaml
server:
  port: 9091

spring:
  application:
    name: springboot-learning
```

---

# 2. What is YAML?

YAML is a human-readable configuration format.

YAML hierarchical or nested configuration ko clean structure mein represent kar sakta hai.

Example:

```yaml
app:
  name: Spring Boot Learning
  developer: Vijay
```

Equivalent properties:

```properties
app.name=Spring Boot Learning
app.developer=Vijay
```

---

# 3. YAML Syntax

YAML configuration generally `key: value` syntax use karti hai.

Example:

```yaml
server:
  port: 9091
```

Here:

```text
server
  ↓
Parent Property

port
  ↓
Child Property

9091
  ↓
Value
```

Equivalent:

```properties
server.port=9091
```

---

# 4. Hierarchical Configuration

YAML ka important feature hierarchical structure hai.

Example:

```yaml
spring:
  application:
    name: springboot-learning
```

Structure:

```text
spring
   ↓
application
   ↓
name
   ↓
springboot-learning
```

Equivalent property:

```properties
spring.application.name=springboot-learning
```

---

# 5. Custom Properties in YAML

Hum custom application properties bhi YAML mein define kar sakte hain.

Example:

```yaml
app:
  name: Spring Boot YAML Learning
  developer: Vijay
```

Equivalent:

```properties
app.name=Spring Boot YAML Learning
app.developer=Vijay
```

Java mein same property placeholders use kiye ja sakte hain:

```java
@Value("${app.name}")
private String appName;

@Value("${app.developer}")
private String developer;
```

Flow:

```text
application.yml
       ↓
app:
  name: Spring Boot YAML Learning
       ↓
Spring Configuration Environment
       ↓
app.name
       ↓
@Value("${app.name}")
       ↓
Java Field
```

---

# 6. Practical — YAML Configuration

We created:

```text
src/main/resources/application.yml
```

with:

```yaml
spring:
  application:
    name: springboot-learning

server:
  port: 9091

app:
  name: Spring Boot YAML Learning
  developer: Vijay
```

Our Java class contained:

```java
@Value("${app.name}")
private String appName;

@Value("${app.developer}")
private String developer;
```

After running the application:

```text
Tomcat started on port 9091
```

and our custom properties were also available:

```text
App Name: Spring Boot YAML Learning
Developer: Vijay
```

This demonstrates that YAML can configure both:

```text
Predefined Spring Boot Properties
             +
Custom Application Properties
```

---

# 7. Nested YAML Configuration

YAML is useful for representing nested configuration.

Example:

```yaml
app:
  name: Spring Boot Learning

  developer:
    name: Vijay
    role: Java Developer
```

Equivalent properties:

```properties
app.name=Spring Boot Learning
app.developer.name=Vijay
app.developer.role=Java Developer
```

The nested values can be referenced using their complete property paths:

```java
@Value("${app.developer.name}")
private String developerName;

@Value("${app.developer.role}")
private String developerRole;
```

---

# 8. YAML Lists

YAML can also represent lists.

Example:

```yaml
app:
  technologies:
    - Java
    - Spring
    - Spring Boot
    - MySQL
```

Here:

```text
technologies
     │
     ├── Java
     ├── Spring
     ├── Spring Boot
     └── MySQL
```

The `-` symbol represents a list item.

Lists and complex grouped configuration are especially useful with configuration binding.

We will study this later using:

```java
@ConfigurationProperties
```

---

# 9. Indentation is Important

YAML is indentation-sensitive.

Correct:

```yaml
app:
  name: Spring Boot Learning
  developer: Vijay
```

Both `name` and `developer` belong to:

```text
app
```

But:

```yaml
app:
  name: Spring Boot Learning

developer: Vijay
```

represents a different structure.

Now:

```text
app.name
```

and:

```text
developer
```

are separate properties.

Therefore, indentation must be correct.

---

# 10. Use Spaces for Indentation

For YAML configuration, use spaces consistently for indentation.

Example:

```yaml
spring:
  application:
    name: springboot-learning
```

Avoid mixing tabs with spaces.

A clean convention is:

```text
2 spaces per indentation level
```

Example:

```yaml
app:
  developer:
    name: Vijay
    role: Java Developer
```

---

# 11. Properties vs YAML

## Properties Format

```properties
spring.application.name=springboot-learning
server.port=9091
app.name=Spring Boot Learning
app.developer.name=Vijay
app.developer.role=Java Developer
```

## YAML Format

```yaml
spring:
  application:
    name: springboot-learning

server:
  port: 9091

app:
  name: Spring Boot Learning

  developer:
    name: Vijay
    role: Java Developer
```

Both can represent application configuration.

The main difference is representation.

```text
.properties
     ↓
Flat key=value structure


YAML
     ↓
Hierarchical structure
```

---

# 12. Is YAML Better Than Properties?

Not necessarily.

Both formats are valid for Spring Boot configuration.

### Properties

Simple and straightforward:

```properties
server.port=9091
```

### YAML

Can be easier to read when configuration becomes deeply nested:

```yaml
server:
  port: 9091
```

The choice can depend on:

- Project requirements
- Team convention
- Configuration complexity
- Developer preference

---

# 13. Predefined Properties in YAML

Spring Boot predefined properties can be written in YAML.

Properties:

```properties
server.port=9091
```

YAML:

```yaml
server:
  port: 9091
```

Properties:

```properties
spring.application.name=springboot-learning
```

YAML:

```yaml
spring:
  application:
    name: springboot-learning
```

Spring Boot can use these values to configure the application.

---

# 14. Custom Properties in YAML

Custom properties can also be represented hierarchically.

Example:

```yaml
app:
  developer:
    name: Vijay
    role: Java Developer
```

Equivalent property names:

```text
app.developer.name
app.developer.role
```

These can later be consumed using approaches such as:

```java
@Value
```

or:

```java
@ConfigurationProperties
```

---

# 15. YAML and `@Value`

Changing from properties syntax to YAML does not necessarily require changing the property placeholder used in Java.

For example:

```yaml
app:
  name: Spring Boot Learning
```

can still be accessed as:

```java
@Value("${app.name}")
private String appName;
```

Spring exposes the configuration through its environment using property names.

---

# 16. YAML and `@ConfigurationProperties`

For a small number of individual properties:

```java
@Value("${app.name}")
```

can be convenient.

For grouped configuration such as:

```yaml
app:
  name: Spring Boot Learning

  developer:
    name: Vijay
    role: Java Developer

  technologies:
    - Java
    - Spring
    - Spring Boot
```

using:

```java
@ConfigurationProperties
```

can provide cleaner structured binding.

We will study this separately.

---

# 17. Important YAML Rules

Remember these basic rules:

```text
1. Indentation matters

2. Use spaces consistently

3. Parent-child hierarchy must be correct

4. Use `key: value` syntax

5. Use `-` for list items
```

Example:

```yaml
app:
  name: Spring Boot Learning

  technologies:
    - Java
    - Spring Boot
```

---

# 18. Interview Questions

### Q1. What is YAML?

YAML is a human-readable configuration format that can represent hierarchical and structured configuration.

---

### Q2. Which YAML file is commonly used in Spring Boot?

```text
application.yml
```

Common location:

```text
src/main/resources/application.yml
```

---

### Q3. Convert this property to YAML.

```properties
server.port=9091
```

Answer:

```yaml
server:
  port: 9091
```

---

### Q4. Convert this property to YAML.

```properties
spring.application.name=springboot-learning
```

Answer:

```yaml
spring:
  application:
    name: springboot-learning
```

---

### Q5. Why is indentation important in YAML?

Because indentation defines the hierarchical relationship between properties.

---

### Q6. Can custom properties be defined in YAML?

Yes.

Example:

```yaml
app:
  name: Spring Boot Learning
```

---

### Q7. Can `@Value` read properties defined in YAML?

Yes.

Example:

```yaml
app:
  name: Spring Boot Learning
```

can be accessed using:

```java
@Value("${app.name}")
private String appName;
```

---

### Q8. What does `-` represent in YAML?

It is commonly used to represent an item in a list.

Example:

```yaml
technologies:
  - Java
  - Spring Boot
```

---

### Q9. What is the main difference between `.properties` and YAML?

`.properties` commonly represents configuration in a flat `key=value` format, while YAML provides a hierarchical structure using indentation.

---

### Q10. Is YAML mandatory in Spring Boot?

No.

Spring Boot applications can use `.properties` configuration as well.

---

# 19. Quick Revision

```text
                    Spring Boot Configuration
                              ↓
                  ┌───────────┴───────────┐
                  ↓                       ↓
       application.properties       application.yml
                  ↓                       ↓
            key=value                Hierarchical
                  ↓                       ↓
                  └───────────┬───────────┘
                              ↓
                    Spring Environment
                              ↓
                  ┌───────────┴───────────┐
                  ↓                       ↓
          Spring Boot Properties     Custom Properties
                  ↓                       ↓
          Automatic Configuration    @Value / Binding
```

## Remember

> **YAML provides a clean hierarchical way to represent Spring Boot configuration. Indentation defines the structure, while Spring can expose those values using the same logical property names.**