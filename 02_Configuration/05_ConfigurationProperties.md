# Spring Boot @ConfigurationProperties

## 1. Introduction

Spring Boot applications mein custom configuration ko Java classes ke saath bind karne ke liye `@ConfigurationProperties` use kiya ja sakta hai.

Example YAML:

```yaml
app:
  name: Spring Boot Learning
  developer: Vijay
  version: 1.0
```

Instead of individually writing:

```java
@Value("${app.name}")
private String name;

@Value("${app.developer}")
private String developer;

@Value("${app.version}")
private String version;
```

we can group these related properties inside one Java class.

```text
application.yml
      ↓
app.*
      ↓
@ConfigurationProperties
      ↓
AppProperties Object
```

---

# 2. Problem with Multiple `@Value`

For a small number of individual properties, `@Value` is convenient.

Example:

```java
@Value("${app.name}")
private String appName;
```

But consider a larger configuration:

```yaml
app:
  name: VijayBaazar
  version: 1.0
  developer: Vijay
  support-email: support@example.com
  frontend-url: http://localhost:3000
```

Using `@Value` for every property would require many separate fields:

```java
@Value("${app.name}")
private String name;

@Value("${app.version}")
private String version;

@Value("${app.developer}")
private String developer;

@Value("${app.support-email}")
private String supportEmail;

@Value("${app.frontend-url}")
private String frontendUrl;
```

For related configuration, `@ConfigurationProperties` provides a cleaner grouped approach.

---

# 3. Basic `@ConfigurationProperties`

YAML:

```yaml
app:
  name: Spring Boot Learning
  developer: Vijay
  version: 1.0
```

Create:

```text
config/
└── AppProperties.java
```

Example:

```java
package com.vijay.springbootlearning.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private String name;
    private String developer;
    private String version;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
```

---

# 4. Understanding `prefix`

The most important part is:

```java
@ConfigurationProperties(prefix = "app")
```

It tells Spring Boot to bind configuration starting with:

```text
app
```

Example:

```yaml
app:
  name: Spring Boot Learning
  developer: Vijay
  version: 1.0
```

Mapping:

```text
app.name
    ↓
name

app.developer
    ↓
developer

app.version
    ↓
version
```

Therefore:

```text
        application.yml

app:
  name: Spring Boot Learning
  developer: Vijay
  version: 1.0

              ↓

@ConfigurationProperties(prefix = "app")

              ↓

        AppProperties

name      = Spring Boot Learning
developer = Vijay
version   = 1.0
```

---

# 5. Why `@Component`?

In our practical we used:

```java
@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {
}
```

`@Component` makes `AppProperties` a Spring-managed Bean.

Concept:

```text
@Component
     ↓
Component Scanning
     ↓
AppProperties Bean
     ↓
@ConfigurationProperties
     ↓
Configuration Binding
```

Now this Bean can also be injected into another Spring Bean.

---

# 6. Using `AppProperties`

Instead of accessing every configuration value separately with `@Value`, we inject the complete configuration object.

Example:

```java
@Component
public class AppPropertyDemo implements CommandLineRunner {

    private final AppProperties appProperties;

    public AppPropertyDemo(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    @Override
    public void run(String... args) {

        System.out.println(
                "Name: " + appProperties.getName()
        );

        System.out.println(
                "Developer: " + appProperties.getDeveloper()
        );

        System.out.println(
                "Version: " + appProperties.getVersion()
        );
    }
}
```

Expected output:

```text
Name: Spring Boot Learning
Developer: Vijay
Version: 1.0
```

---

# 7. Constructor Injection

We injected `AppProperties` using constructor injection:

```java
private final AppProperties appProperties;

public AppPropertyDemo(AppProperties appProperties) {
    this.appProperties = appProperties;
}
```

We did not manually create:

```java
new AppProperties();
```

Instead:

```text
Spring IoC Container
        ↓
Creates AppProperties Bean
        ↓
Configuration values are bound
        ↓
Bean injected into AppPropertyDemo
```

This connects directly with the Dependency Injection concept from Spring Framework.

---

# 8. `@Value` vs `@ConfigurationProperties`

### Using `@Value`

```java
@Value("${app.name}")
private String name;

@Value("${app.developer}")
private String developer;

@Value("${app.version}")
private String version;
```

Each property is injected individually.

### Using `@ConfigurationProperties`

```java
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private String name;
    private String developer;
    private String version;
}
```

The complete related configuration is represented through one object.

Concept:

```text
@Value
   ↓
Individual Property


@ConfigurationProperties
   ↓
Group of Related Properties
   ↓
Java Object
```

---

# 9. Nested Configuration

One major advantage of `@ConfigurationProperties` is structured/nested configuration.

Example:

```yaml
app:
  name: Spring Boot Learning
  developer: Vijay
  version: 1.0

  database:
    host: localhost
    port: 3306
    name: learning_db
```

Here:

```text
app
│
├── name
├── developer
├── version
│
└── database
    ├── host
    ├── port
    └── name
```

The corresponding property paths are:

```text
app.name
app.developer
app.version

app.database.host
app.database.port
app.database.name
```

---

# 10. Nested Java Object

We can represent the nested `database` configuration using a nested Java object.

```java
@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private String name;
    private String developer;
    private String version;

    private Database database;

    public static class Database {

        private String host;
        private int port;
        private String name;

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Database getDatabase() {
        return database;
    }

    public void setDatabase(Database database) {
        this.database = database;
    }
}
```

---

# 11. Nested Property Mapping

Spring Boot can bind:

```yaml
app:
  database:
    host: localhost
    port: 3306
    name: learning_db
```

to:

```java
private Database database;
```

and then:

```java
database.host
database.port
database.name
```

Concept:

```text
application.yml
      ↓

app.database.host
      ↓
database.host

app.database.port
      ↓
database.port

app.database.name
      ↓
database.name
```

So the YAML structure and Java object structure can closely match each other.

---

# 12. Reading Nested Configuration

After injecting `AppProperties`:

```java
private final AppProperties appProperties;

public AppPropertyDemo(AppProperties appProperties) {
    this.appProperties = appProperties;
}
```

we can access nested values:

```java
System.out.println(
        "DB Host: " +
        appProperties.getDatabase().getHost()
);
```

Port:

```java
System.out.println(
        "DB Port: " +
        appProperties.getDatabase().getPort()
);
```

Database name:

```java
System.out.println(
        "DB Name: " +
        appProperties.getDatabase().getName()
);
```

Expected:

```text
App: Spring Boot Learning
Developer: Vijay
Version: 1.0

DB Host: localhost
DB Port: 3306
DB Name: learning_db
```

---

# 13. Complete Flow

```text
                 application.yml
                       ↓

app:
  name: Spring Boot Learning
  developer: Vijay
  version: 1.0

  database:
    host: localhost
    port: 3306
    name: learning_db

                       ↓

        @ConfigurationProperties
              (prefix = "app")

                       ↓

                 AppProperties
                       │
        ┌──────────────┼──────────────┐
        ↓              ↓              ↓
      name         developer       version
                                     
                       │
                   database
                       │
              ┌────────┼────────┐
              ↓        ↓        ↓
            host      port     name

                       ↓

              Spring IoC Container

                       ↓

              Constructor Injection

                       ↓

               AppPropertyDemo
```

---

# 14. Why Not Use `new AppProperties()`?

Avoid manually doing:

```java
AppProperties appProperties =
        new AppProperties();
```

That creates a normal Java object ourselves.

Our configuration object is being managed by Spring:

```text
Spring
  ↓
Creates Bean
  ↓
Binds configuration
  ↓
Injects Bean
```

Therefore we inject it:

```java
public AppPropertyDemo(AppProperties appProperties) {
    this.appProperties = appProperties;
}
```

---

# 15. Real Project Use

Suppose an application has custom payment configuration:

```yaml
payment:
  gateway: cashfree
  currency: INR

  callback:
    success-url: https://example.com/payment/success
    failure-url: https://example.com/payment/failure
```

Instead of many individual `@Value` fields, we could represent related payment configuration through a configuration class.

Concept:

```text
payment.*
    ↓
PaymentProperties
    ↓
Payment Service
```

Similarly configuration groups could represent:

```text
app.*
mail.*
storage.*
payment.*
security.*
```

depending on the application's needs.

---

# 16. When to Use `@Value`

`@Value` can be convenient when only one or a few individual configuration values are required.

Example:

```java
@Value("${app.name}")
private String appName;
```

Simple case:

```text
One Property
     ↓
@Value
```

---

# 17. When to Use `@ConfigurationProperties`

Use it when configuration belongs together as a group.

Example:

```yaml
app:
  name: Spring Boot Learning
  developer: Vijay
  version: 1.0

  database:
    host: localhost
    port: 3306
```

Then:

```text
Related Configuration
        ↓
@ConfigurationProperties
        ↓
Structured Java Object
```

This becomes especially useful with:

- Multiple related properties
- Nested configuration
- Structured application settings

---

# 18. `@Value` vs `@ConfigurationProperties` Summary

| Feature | `@Value` | `@ConfigurationProperties` |
|---|---|---|
| Individual property | Good | Possible |
| Many related properties | Can become repetitive | Good |
| Nested configuration | Less convenient | Good |
| Grouped configuration | Manual fields | Natural mapping |
| Java object structure | No grouped object required | Yes |
| Typical use | Small/simple values | Structured configuration |

---

# 19. Benefits of `@ConfigurationProperties`

Main benefits:

```text
✓ Related configuration stays together
✓ Cleaner than many @Value fields
✓ Supports nested configuration
✓ Maps configuration to Java objects
✓ Works well with dependency injection
✓ Easier to organize large configuration
```

---

# 20. Interview Questions

### Q1. What is `@ConfigurationProperties`?

It is a Spring Boot annotation used to bind external configuration properties to a Java object.

---

### Q2. What does `prefix` mean?

Example:

```java
@ConfigurationProperties(prefix = "app")
```

means configuration starting with:

```text
app
```

will be considered for binding to that class.

---

### Q3. What is the difference between `@Value` and `@ConfigurationProperties`?

`@Value` is convenient for injecting individual values, while `@ConfigurationProperties` is useful for binding groups of related configuration into structured Java objects.

---

### Q4. Can `@ConfigurationProperties` handle nested configuration?

Yes.

Example:

```yaml
app:
  database:
    host: localhost
    port: 3306
```

can be represented using a nested Java object.

---

### Q5. Why did we use `@Component`?

In our implementation, `@Component` registered the configuration class as a Spring-managed Bean so it could be configured and dependency-injected.

---

### Q6. How did we inject `AppProperties`?

Using constructor injection:

```java
public AppPropertyDemo(AppProperties appProperties) {
    this.appProperties = appProperties;
}
```

---

### Q7. Does Spring IoC participate in this process?

Yes.

Conceptually:

```text
Spring IoC Container
        ↓
AppProperties Bean
        ↓
Configuration Binding
        ↓
Dependency Injection
```

---

### Q8. Why is `@ConfigurationProperties` useful in real projects?

Because real applications often have groups of related configuration values. Binding them to dedicated Java objects keeps configuration organized and easier to use.

---

# 21. Quick Revision

```text
              application.yml
                    ↓
                  app.*
                    ↓
     @ConfigurationProperties
          prefix = "app"
                    ↓
              AppProperties
                    ↓
        ┌───────────┴───────────┐
        ↓                       ↓
Simple Properties        Nested Properties
        ↓                       ↓
name                     database
developer                   ↓
version               host / port / name
        │                       │
        └───────────┬───────────┘
                    ↓
              Spring Bean
                    ↓
          Constructor Injection
                    ↓
             Application Code
```

## Remember

> **`@Value` is useful for individual properties, while `@ConfigurationProperties` is a cleaner approach for binding multiple related or nested configuration properties into a structured Java object.**