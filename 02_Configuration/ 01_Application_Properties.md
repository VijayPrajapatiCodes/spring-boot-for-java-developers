# Spring Boot Application Properties

## 1. Introduction

Spring Boot provides externalized configuration so that application settings can be kept outside Java source code.

One of the most commonly used configuration files is:

```text
src/main/resources/application.properties
```

It can contain configuration such as:

- Server port
- Application name
- Database configuration
- Logging configuration
- Custom application properties
- File upload settings
- Other Spring Boot settings

---

## 2. Properties File Syntax

`application.properties` uses the `key=value` format.

```properties
key=value
```

Example:

```properties
server.port=9090
```

Here:

```text
server.port → Key / Property
9090        → Value
```

---

## 3. Default Configuration

Spring Boot provides sensible default configuration.

For example, a typical servlet web application uses:

```text
8080
```

as the default server port.

If we don't configure:

```properties
server.port
```

the application can start on the default port.

```text
Property provided?
       ↓
   ┌───┴───┐
  YES      NO
   ↓        ↓
Use given  Use default
value      configuration
```

---

## 4. Overriding Default Configuration

Spring Boot defaults can be changed using application configuration.

Example:

```properties
server.port=9090
```

After restarting the application, embedded Tomcat starts on port:

```text
9090
```

Flow:

```text
Spring Boot Default
server.port = 8080
        ↓
application.properties
server.port = 9090
        ↓
Default Overridden
        ↓
Tomcat Starts on 9090
```

---

## 5. Application Name

Spring Boot provides the property:

```properties
spring.application.name=springboot-learning
```

It specifies the logical name of the application.

Example:

```text
spring.application.name
          ↓
   springboot-learning
```

Application names become especially useful in areas such as:

- Logging
- Monitoring
- Configuration
- Distributed systems
- Microservices

---

# 6. Predefined Properties

Predefined properties are properties whose meaning is already understood by Spring Boot/Spring components.

Examples:

```properties
server.port=9090
spring.application.name=springboot-learning
```

For example:

```properties
server.port=9090
```

is automatically used by Spring Boot to configure the web server port.

```text
Predefined Property
       ↓
Spring Boot understands it
       ↓
Configuration Applied
```

---

# 7. Custom Properties

We can also define our own application-specific properties.

Example:

```properties
app.name=Spring Boot Learning
app.developer=Vijay
```

These properties are defined by our application.

Spring Boot does not assign business meaning to them automatically.

Our Java code needs to read or bind them when required.

```text
application.properties
        ↓
Custom Property
        ↓
Application Code
```

---

# 8. Reading Properties Using `@Value`

Spring provides the `@Value` annotation for injecting property values.

Example:

```java
@Value("${app.name}")
private String appName;
```

If:

```properties
app.name=Spring Boot Learning
```

then:

```text
${app.name}
      ↓
Spring Boot Learning
      ↓
appName
```

Similarly:

```java
@Value("${app.developer}")
private String developer;
```

reads:

```properties
app.developer=Vijay
```

---

# 9. `${...}` Property Placeholder

The syntax:

```text
${property.name}
```

is used to resolve a property.

Example:

```java
@Value("${app.developer}")
private String developer;
```

Flow:

```text
application.properties
        ↓
app.developer=Vijay
        ↓
${app.developer}
        ↓
@Value
        ↓
developer field
        ↓
Vijay
```

---

# 10. Practical — Reading Custom Properties

We created custom properties:

```properties
app.name=Spring Boot Learning
app.developer=Vijay
```

Then created:

```java
package com.vijay.springbootlearning.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AppPropertyDemo implements CommandLineRunner {

    @Value("${app.name}")
    private String appName;

    @Value("${app.developer}")
    private String developer;

    @Override
    public void run(String... args) {

        System.out.println("App Name: " + appName);
        System.out.println("Developer: " + developer);
    }
}
```

Output:

```text
App Name: Spring Boot Learning
Developer: Vijay
```

---

# 11. Why `@Component` Was Required

Initially, if `AppPropertyDemo` is only a normal Java class:

```java
public class AppPropertyDemo {
}
```

Spring does not automatically manage that object as a Bean.

We added:

```java
@Component
```

Now component scanning can discover the class.

```text
@ComponentScan
       ↓
AppPropertyDemo detected
       ↓
Spring Bean created
       ↓
@Value processed
       ↓
Properties injected
```

This is why the class needs to be Spring-managed for this form of `@Value` injection.

---

# 12. Default Values with `@Value`

We can specify a fallback/default value.

Syntax:

```java
@Value("${property:default-value}")
```

Example:

```java
@Value("${app.name:Default Spring Application}")
private String appName;
```

Spring checks whether:

```text
app.name
```

is available.

```text
app.name available?
       ↓
   ┌───┴───┐
  YES      NO
   ↓        ↓
Property   Default
Value      Value
```

If:

```properties
app.name=Spring Boot Learning
```

exists:

```text
Spring Boot Learning
```

is injected.

If the property is missing:

```text
Default Spring Application
```

is injected.

---

# 13. Missing Property Without Default Value

Consider:

```java
@Value("${app.name}")
private String appName;
```

If Spring cannot resolve:

```text
app.name
```

the application can fail during startup because the required placeholder cannot be resolved.

Providing a fallback can prevent this when a default value makes sense:

```java
@Value("${app.name:Default Application}")
private String appName;
```

---

# 14. `@Value` vs Hard Coding

Without external configuration:

```java
private String developer = "Vijay";
```

The value is hard-coded in Java source code.

Using configuration:

```properties
app.developer=Vijay
```

and:

```java
@Value("${app.developer}")
private String developer;
```

separates configuration from application logic.

```text
Java Code
   ↓
Application Logic

application.properties
   ↓
Application Configuration
```

---

# 15. Complete Practical Configuration

Example:

```properties
spring.application.name=springboot-learning

server.port=9090

app.name=Spring Boot Learning
app.developer=Vijay
```

Java:

```java
@Component
public class AppPropertyDemo implements CommandLineRunner {

    @Value("${app.name:Default Application}")
    private String appName;

    @Value("${app.developer:Unknown}")
    private String developer;

    @Override
    public void run(String... args) {

        System.out.println("App Name: " + appName);
        System.out.println("Developer: " + developer);
    }
}
```

---

# 16. `@Value` and `@ConfigurationProperties`

For a small number of individual values, `@Value` can be convenient.

Example:

```java
@Value("${app.name}")
```

For multiple related configuration properties, Spring Boot also provides:

```java
@ConfigurationProperties
```

Conceptually:

```text
Individual Property
      ↓
@Value


Group of Related Properties
      ↓
@ConfigurationProperties
```

We will study `@ConfigurationProperties` separately in detail.

---

# 17. Benefits of Externalized Configuration

Externalized configuration provides benefits such as:

- Less hard-coded configuration
- Easier environment-specific configuration
- Cleaner application code
- Easier maintenance
- Configuration can change independently from business logic
- Spring Boot defaults can be overridden

---

# 18. Interview Questions

### Q1. What is `application.properties`?

It is a commonly used Spring Boot configuration file for defining application settings.

Its standard location is:

```text
src/main/resources/application.properties
```

### Q2. What syntax does a `.properties` file use?

```text
key=value
```

### Q3. How do you change the server port?

```properties
server.port=9090
```

### Q4. What is the default port commonly used by a Spring Boot servlet web application?

```text
8080
```

### Q5. Can we create custom properties?

Yes.

Example:

```properties
app.name=Spring Boot Learning
```

### Q6. How can we read a property using `@Value`?

```java
@Value("${app.name}")
private String appName;
```

### Q7. How do we provide a default value with `@Value`?

```java
@Value("${app.name:Default Application}")
private String appName;
```

### Q8. What is the difference between predefined and custom properties?

Predefined properties have meaning understood by Spring Boot/Spring components, while custom properties are application-specific and must be read or bound by our application.

---

# 19. Quick Revision

```text
            application.properties
                     ↓
            External Configuration
                     ↓
        ┌────────────┴────────────┐
        ↓                         ↓
Predefined Properties      Custom Properties
        ↓                         ↓
server.port                app.name
spring.application.name    app.developer
        ↓                         ↓
Spring Boot uses          @Value / Binding
them directly                    ↓
                               Java
```

## Remember

> **`application.properties` separates application configuration from Java code and allows us to configure Spring Boot defaults as well as define our own custom properties.**