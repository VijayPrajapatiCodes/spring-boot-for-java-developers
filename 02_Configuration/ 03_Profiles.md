# Spring Boot Profiles

## 1. What are Spring Profiles?

Spring Profiles allow us to use different configurations for different environments.

A real application can run in multiple environments:

```text
Development  → Developer's local machine
Testing      → Testing environment
Production   → Live application/server
```

Each environment may require different configuration.

Example:

```text
DEV
Port     → 8081
Database → Local Database

PROD
Port     → 8082
Database → Production Database
```

Instead of changing configuration manually every time, Spring Boot Profiles help us maintain environment-specific configuration.

---

## 2. Why Do We Need Profiles?

Suppose we have:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/mydb
```

This works during local development.

But after deploying the application to the cloud, the database may be completely different.

Therefore:

```text
Same Application
      ↓
Different Environments
      ↓
Different Configuration
```

Profiles solve this problem.

---

## 3. Profile-Specific Configuration Files

Spring Boot supports profile-specific configuration files.

Example:

```text
src/main/resources/
│
├── application.yml
├── application-dev.yml
└── application-prod.yml
```

Purpose:

```text
application.yml
        ↓
Common / Base Configuration

application-dev.yml
        ↓
Development Configuration

application-prod.yml
        ↓
Production Configuration
```

Naming convention:

```text
application-{profile}.yml
```

Examples:

```text
application-dev.yml
application-test.yml
application-prod.yml
```

---

## 4. Base Configuration

Common configuration can be placed inside:

```text
application.yml
```

Example:

```yaml
spring:
  application:
    name: springboot-learning

app:
  name: Spring Boot YAML Learning
  developer: Vijay
```

These properties are not specific to only development or production.

---

## 5. Development Profile

We created:

```text
application-dev.yml
```

Example:

```yaml
server:
  port: 8081

app:
  environment: Development
```

This contains development-specific configuration.

---

## 6. Production Profile

We also created:

```text
application-prod.yml
```

Example:

```yaml
server:
  port: 8082

app:
  environment: Production
```

This contains production-specific configuration.

---

# 7. Activating a Profile

A profile can be activated using:

```text
spring.profiles.active
```

Example:

```yaml
spring:
  profiles:
    active: dev
```

Now:

```text
Active Profile
     ↓
    dev
     ↓
application-dev.yml
     ↓
Development Configuration
```

For production:

```yaml
spring:
  profiles:
    active: prod
```

Flow:

```text
Active Profile
     ↓
    prod
     ↓
application-prod.yml
     ↓
Production Configuration
```

---

# 8. Development Profile Practical

We activated:

```yaml
spring:
  profiles:
    active: dev
```

Our `application-dev.yml` contained:

```yaml
server:
  port: 8081

app:
  environment: Development
```

The application startup showed:

```text
The following 1 profile is active: "dev"
```

Tomcat then used:

```text
8081
```

and our application printed:

```text
Environment: Development
```

Therefore:

```text
dev Active
    ↓
application-dev.yml
    ↓
server.port = 8081
app.environment = Development
```

---

# 9. Production Profile Practical

We changed the active profile to:

```yaml
spring:
  profiles:
    active: prod
```

Our `application-prod.yml` contained:

```yaml
server:
  port: 8082

app:
  environment: Production
```

The application startup showed:

```text
The following 1 profile is active: "prod"
```

Tomcat started on:

```text
8082
```

and our application printed:

```text
Environment: Production
```

Flow:

```text
prod Active
    ↓
application-prod.yml
    ↓
server.port = 8082
app.environment = Production
```

---

# 10. Reading Profile-Specific Custom Properties

We created:

```yaml
app:
  environment: Development
```

or:

```yaml
app:
  environment: Production
```

Java:

```java
@Value("${app.environment}")
private String environment;
```

Then:

```java
System.out.println("Environment: " + environment);
```

With `dev`:

```text
Environment: Development
```

With `prod`:

```text
Environment: Production
```

The Java code remained the same.

Only the active configuration changed.

---

# 11. Common + Profile-Specific Configuration

Spring Boot can combine common configuration with active profile configuration.

Example:

```text
application.yml
       +
application-dev.yml
```

when:

```text
dev
```

is active.

Conceptually:

```text
Common Configuration
        +
Profile-Specific Configuration
        ↓
Effective Application Configuration
```

If a profile-specific configuration supplies a different value for a property, it can override the common value according to Spring Boot's configuration precedence rules.

---

# 12. Real-World Profile Usage

During development:

```text
Developer Laptop
      ↓
dev Profile
      ↓
Local Configuration
```

After deployment:

```text
Cloud / Production Server
      ↓
prod Profile
      ↓
Production Configuration
```

Therefore, we can use the same application with different configurations.

```text
                  Same Backend
                       │
          ┌────────────┴────────────┐
          ↓                         ↓
      Development               Production
          ↓                         ↓
     dev Profile                prod Profile
          ↓                         ↓
application-dev.yml       application-prod.yml
```

---

# 13. Should We Manually Switch Profiles on Cloud?

Normally, we do not want to edit:

```yaml
spring:
  profiles:
    active: dev
```

to:

```yaml
spring:
  profiles:
    active: prod
```

every time we deploy.

Instead, the deployment environment can provide the active profile.

For example:

```text
SPRING_PROFILES_ACTIVE=prod
```

Then:

```text
Cloud Environment
        ↓
SPRING_PROFILES_ACTIVE=prod
        ↓
Spring Boot
        ↓
prod Profile
        ↓
application-prod.yml
```

This allows the environment to decide which profile should run.

---

# 14. Environment Variable

Spring Boot configuration can be supplied through environment variables.

For active profiles:

```text
SPRING_PROFILES_ACTIVE=prod
```

Local environment could use:

```text
SPRING_PROFILES_ACTIVE=dev
```

Production environment could use:

```text
SPRING_PROFILES_ACTIVE=prod
```

Therefore:

```text
Same Code
Same Build
   ↓
Environment Configuration
   ↓
Different Active Profile
```

---

# 15. Command-Line Profile Activation

A profile can also be supplied when starting the application.

Example:

```bash
java -jar application.jar --spring.profiles.active=prod
```

For development:

```bash
java -jar application.jar --spring.profiles.active=dev
```

This means the profile does not need to be permanently hard-coded into the application's YAML file.

---

# 16. IntelliJ Profile Activation

During local development, the active profile can also be supplied through the IDE's run configuration.

For example:

```text
Active Profile → dev
```

or through program arguments:

```text
--spring.profiles.active=dev
```

This allows developers to switch environments without modifying application configuration files every time.

---

# 17. Profiles with Database Configuration

Profiles become especially useful when database configurations differ.

Development:

```yaml
# application-dev.yml

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/mydb
```

Production may use a cloud database.

Example:

```yaml
# application-prod.yml

spring:
  datasource:
    url: ${DB_URL}
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
```

Then the production environment provides:

```text
DB_URL
DB_USERNAME
DB_PASSWORD
```

---

# 18. Do Not Hard-Code Production Secrets

Sensitive values should generally not be committed directly to GitHub.

Avoid:

```yaml
spring:
  datasource:
    username: admin
    password: my-real-production-password
```

Instead:

```yaml
spring:
  datasource:
    url: ${DB_URL}
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
```

Then:

```text
Cloud Environment / Secret Manager
              ↓
         DB_USERNAME
         DB_PASSWORD
         DB_URL
              ↓
        Spring Boot
```

This keeps environment-specific secrets outside source control.

---

# 19. Profiles vs Environment Variables

These concepts work together.

### Profiles

Choose a group of environment-specific configuration.

```text
dev
prod
test
```

### Environment Variables

Can provide individual configuration values or select the active profile.

Example:

```text
SPRING_PROFILES_ACTIVE=prod
DB_URL=...
DB_USERNAME=...
DB_PASSWORD=...
```

Together:

```text
Environment Variables
        ↓
Select prod Profile
        ↓
application-prod.yml
        ↓
Read Database Variables
        ↓
Production Application
```

---

# 20. Complete Real-World Flow

```text
                     Spring Boot Backend
                            │
              Same Source Code / Build
                            │
              ┌─────────────┴─────────────┐
              ↓                           ↓
        Developer Laptop              Cloud Server
              ↓                           ↓
SPRING_PROFILES_ACTIVE=dev   SPRING_PROFILES_ACTIVE=prod
              ↓                           ↓
    application-dev.yml          application-prod.yml
              ↓                           ↓
       Local Database              Cloud Database
              ↓                           ↓
       Development                 Production
```

---

# 21. Benefits of Profiles

Spring Profiles provide:

- Environment-specific configuration
- Cleaner configuration management
- Separation of dev/test/prod settings
- Same application code across environments
- Easy cloud deployment configuration
- Better management of database/server settings
- Integration with environment variables and cloud secrets

---

# 22. Interview Questions

### Q1. What is a Spring Profile?

A Spring Profile allows an application to use different configuration for different environments such as development, testing, and production.

### Q2. How do you create profile-specific configuration?

Using files such as:

```text
application-dev.yml
application-test.yml
application-prod.yml
```

### Q3. How do you activate a profile?

Using:

```text
spring.profiles.active
```

Example:

```yaml
spring:
  profiles:
    active: dev
```

### Q4. How can we activate a profile using an environment variable?

```text
SPRING_PROFILES_ACTIVE=prod
```

### Q5. How can we activate a profile using command-line arguments?

```bash
java -jar application.jar --spring.profiles.active=prod
```

### Q6. Why should we avoid hard-coding the production profile?

Because the deployment environment should ideally decide which configuration/profile to use, allowing the same application build to run in different environments.

### Q7. Should database passwords be stored directly in `application-prod.yml`?

Sensitive production secrets should generally be kept outside source control and supplied through environment variables or a secret-management system.

### Q8. Can the same application run with different databases using Profiles?

Yes.

For example:

```text
dev  → Local MySQL
prod → Cloud Database
```

### Q9. What happens if no profile is active?

Spring Boot uses its default profile and the applicable non-profile-specific configuration.

---

# 23. Quick Revision

```text
                         Spring Profiles
                               ↓
                Environment-Specific Config
                               ↓
              ┌────────────────┼────────────────┐
              ↓                ↓                ↓
             DEV              TEST             PROD
              ↓                ↓                ↓
 application-dev.yml  application-test.yml  application-prod.yml
              ↓                                 ↓
      Local Environment                  Cloud Environment
              ↓                                 ↓
SPRING_PROFILES_ACTIVE=dev      SPRING_PROFILES_ACTIVE=prod
```

## Remember

> **Profiles separate environment-specific configuration. In real deployments, the environment can activate the appropriate profile, while sensitive values such as database passwords should be supplied externally rather than hard-coded in source control.**