# Spring Boot Auto Configuration

## 1. What is Auto Configuration?

Auto Configuration is one of the core features of Spring Boot.

It automatically configures the application based on factors such as:

- Classes available on the classpath
- Dependencies available in the project
- Existing Spring Beans
- Configuration properties
- Type of application

The main purpose of Auto Configuration is to reduce manual configuration.

```text
Dependencies / Classes
        ↓
Spring Boot
        ↓
Check Conditions
        ↓
Apply Suitable Configuration
```

---

## 2. Why Do We Need Auto Configuration?

Without Auto Configuration, developers may need to manually configure many components required by an application.

For example, a web application may require:

```text
Web Configuration
Tomcat Configuration
DispatcherServlet Configuration
HTTP Message Converters
Error Handling
Multipart Configuration
etc.
```

Spring Boot can automatically provide many common configurations when the required conditions are satisfied.

This allows developers to focus more on application logic.

---

## 3. How Auto Configuration Works

Spring Boot does not blindly configure everything.

It evaluates different **conditions** before applying an Auto Configuration.

Simplified flow:

```text
Application Starts
       ↓
Spring Boot checks classpath
       ↓
Checks available classes
       ↓
Checks existing Beans
       ↓
Checks properties
       ↓
Checks application environment
       ↓
Conditions evaluated
       ↓
 ┌─────┴─────┐
 ↓           ↓
MATCH      NO MATCH
 ↓           ↓
Apply       Skip
Configuration Configuration
```

---

## 4. `@EnableAutoConfiguration`

Auto Configuration is enabled through:

```java
@EnableAutoConfiguration
```

Normally we do not need to add this annotation manually because:

```java
@SpringBootApplication
```

includes Auto Configuration functionality.

Recall:

```text
@SpringBootApplication
        │
        ├── @SpringBootConfiguration
        ├── @EnableAutoConfiguration
        └── @ComponentScan
```

Therefore:

```java
@SpringBootApplication
public class SpringbootLearningApplication {
}
```

automatically enables Auto Configuration.

---

# 5. Conditional Configuration

Spring Boot Auto Configuration is heavily based on **conditions**.

A configuration is applied only when its required conditions are satisfied.

Some important conditional annotations are:

```java
@ConditionalOnClass
@ConditionalOnMissingBean
@ConditionalOnBean
@ConditionalOnProperty
```

---

## 6. `@ConditionalOnClass`

This condition checks whether a particular class is available on the classpath.

Conceptually:

```text
Required Class Available?
        ↓
   ┌────┴────┐
  YES        NO
   ↓          ↓
Match      No Match
```

Example from our application:

```text
DispatcherServletAutoConfiguration matched
```

because Spring Boot found the required:

```text
org.springframework.web.servlet.DispatcherServlet
```

class. :contentReference[oaicite:0]{index=0}

Simplified meaning:

```text
DispatcherServlet class available
          ↓
Condition matched
          ↓
DispatcherServlet Auto Configuration
```

---

## 7. `@ConditionalOnMissingBean`

This condition checks whether a Bean of a particular type is missing.

Conceptually:

```text
Required Bean already exists?
        ↓
   ┌────┴────┐
  YES        NO
   ↓          ↓
Back Off    Create Default
```

This allows Spring Boot to provide a default Bean when the developer has not already provided one.

For example, our report showed conditions where Spring Boot did not find existing Beans and therefore the related auto-configuration matched. :contentReference[oaicite:1]{index=1}

This is an important Spring Boot principle:

> Spring Boot can provide sensible defaults while allowing developers to provide their own configuration where supported.

---

## 8. `@ConditionalOnProperty`

This condition checks application configuration properties.

Conceptually:

```text
Property has expected value?
        ↓
   ┌────┴────┐
  YES        NO
   ↓          ↓
Match      No Match
```

Our Condition Evaluation Report contained property-based conditions such as:

```text
spring.aop.auto=true
```

which matched. :contentReference[oaicite:2]{index=2}

---

# 9. Auto Configuration Example — Embedded Tomcat

Our project contains Spring Web-related dependencies.

When the application started, Spring Boot evaluated Tomcat-related conditions.

The report showed:

```text
TomcatServletWebServerAutoConfiguration matched
```

Required Servlet and Tomcat classes were found on the classpath. :contentReference[oaicite:3]{index=3}

Spring Boot then configured the embedded web server.

The application logs showed:

```text
Tomcat initialized with port 8080
```

and later:

```text
Tomcat started on port 8080
```

:contentReference[oaicite:4]{index=4}

Simplified flow:

```text
Spring Web Dependencies
        ↓
Tomcat Classes Available
        ↓
Web Application Detected
        ↓
Conditions Match
        ↓
Tomcat Auto Configuration
        ↓
Embedded Tomcat Starts
        ↓
Port 8080
```

We did not manually install or configure Tomcat.

---

# 10. Auto Configuration Example — DispatcherServlet

Our application also matched:

```text
DispatcherServletAutoConfiguration
```

Spring Boot found the required `DispatcherServlet` class and detected the web application environment. :contentReference[oaicite:5]{index=5}

Simplified:

```text
DispatcherServlet class found
        ↓
Web Application detected
        ↓
Conditions matched
        ↓
DispatcherServlet configured
```

---

# 11. Auto Configuration Example — Jackson

Our project also matched:

```text
JacksonAutoConfiguration
```

The required Jackson classes were available in the application. :contentReference[oaicite:6]{index=6}

Jackson is commonly used for JSON processing in Spring web applications.

We will understand JSON conversion practically when studying REST APIs and Request/Response Body.

---

# 12. Positive Matches

To inspect Auto Configuration, we added the following property:

```properties
debug=true
```

After restarting the application, Spring Boot printed:

```text
CONDITIONS EVALUATION REPORT
```

The report contained:

```text
Positive matches:
```

:contentReference[oaicite:7]{index=7}

A **Positive Match** means the required conditions for that Auto Configuration were satisfied.

Example:

```text
Required class found
        ↓
Required environment found
        ↓
Condition matched
        ↓
Auto Configuration applied
```

Examples observed in our project included:

```text
DispatcherServletAutoConfiguration
JacksonAutoConfiguration
MultipartAutoConfiguration
TomcatServletWebServerAutoConfiguration
WebMvcAutoConfiguration
```

These appear as positive matches in the report. :contentReference[oaicite:8]{index=8} :contentReference[oaicite:9]{index=9} :contentReference[oaicite:10]{index=10}

---

# 13. Negative Matches

The report also contained:

```text
Negative matches:
```

A Negative Match means one or more required conditions were not satisfied.

This does **not necessarily mean an application error**.

It often simply means:

> This particular Auto Configuration is not required for the current application.

For example, our report showed:

```text
GsonHttpMessageConvertersConfiguration:
    Did not match
```

because the required:

```text
com.google.gson.Gson
```

class was not found. :contentReference[oaicite:11]{index=11}

Flow:

```text
Is Gson available?
        ↓
       NO
        ↓
Condition does not match
        ↓
Gson Auto Configuration skipped
```

---

# 14. Practical — Condition Evaluation Report

To inspect Spring Boot Auto Configuration, we added:

```properties
debug=true
```

inside:

```text
src/main/resources/application.properties
```

After restarting the application, Spring Boot generated:

```text
============================
CONDITIONS EVALUATION REPORT
============================
```

:contentReference[oaicite:12]{index=12}

This report helped us see:

```text
Positive Matches
      ↓
Configurations whose conditions matched

Negative Matches
      ↓
Configurations whose conditions did not match
```

After completing the practical, `debug=true` can be removed to avoid verbose startup logs.

---

# 15. Auto Configuration Is Not Magic

Auto Configuration does not mean:

```text
Spring Boot configures everything blindly ❌
```

Instead:

```text
Spring Boot
    ↓
Checks Conditions
    ↓
Understands Application Environment
    ↓
Applies Suitable Defaults
```

Therefore:

> Auto Configuration is **conditional configuration**.

---

# 16. Auto Configuration vs Component Scanning

These concepts should not be confused.

### Component Scanning

Finds our Spring components.

For example:

```java
@Service
public class UserService {
}
```

```text
@ComponentScan
      ↓
Find UserService
      ↓
Register Bean
```

### Auto Configuration

Configures framework/infrastructure components based on conditions.

```text
Spring Boot
     ↓
Check Classpath + Beans + Properties
     ↓
Conditions Match
     ↓
Apply Auto Configuration
```

So:

```text
Component Scanning
→ Finds our application components

Auto Configuration
→ Configures suitable framework infrastructure/defaults
```

---

# 17. Important Interview Questions

### Q1. What is Spring Boot Auto Configuration?

Auto Configuration is a Spring Boot feature that automatically configures application components based on the classpath, existing Beans, properties, and application environment.

---

### Q2. Which annotation enables Auto Configuration?

```java
@EnableAutoConfiguration
```

It is normally included through:

```java
@SpringBootApplication
```

---

### Q3. Does Spring Boot apply every Auto Configuration?

No.

Spring Boot evaluates conditions and only applies configurations whose required conditions match.

---

### Q4. What is `@ConditionalOnClass`?

It applies a configuration when the required class is available on the classpath.

---

### Q5. What is `@ConditionalOnMissingBean`?

It allows configuration to be applied when a particular Bean is not already available.

---

### Q6. What is `@ConditionalOnProperty`?

It applies configuration based on the presence or value of an application property.

---

### Q7. What are Positive Matches?

Positive Matches are Auto Configurations whose required conditions were satisfied.

---

### Q8. What are Negative Matches?

Negative Matches are configurations whose required conditions were not satisfied, so those configurations were skipped.

A negative match does not automatically indicate an error.

---

# 18. Quick Revision

```text
                Spring Boot
                     ↓
            Auto Configuration
                     ↓
              Check Conditions
                     ↓
       ┌─────────────┼─────────────┐
       ↓             ↓             ↓
   Classpath       Beans        Properties
       ↓             ↓             ↓
       └─────────────┼─────────────┘
                     ↓
              Conditions Match?
                 ↙       ↘
               YES        NO
                ↓          ↓
             Apply        Skip
```

## Remember

> **Spring Boot Auto Configuration checks the application's classpath, Beans, properties, and environment and conditionally provides suitable default configuration.**