# Spring Boot Logging

## 1. What is Logging?

Logging is the process of recording events that happen while an application is running.

Examples:

```text
Application started
User logged in
Order created
Payment initiated
Payment failed
Database operation failed
Unexpected exception occurred
```

Basic flow:

```text
Application
    ↓
Runtime Events
    ↓
Logger
    ↓
Logs
    ↓
Console / File / Logging Platform
```

Logs are especially useful for:

- Debugging
- Error investigation
- Production monitoring
- Understanding application flow
- Troubleshooting failed requests

---

# 2. `System.out.println()` vs Logging

During basic development we can print:

```java
System.out.println("Application started");
```

But real backend applications should use a proper logging API for application logs.

Example:

```java
logger.info("Application started");
```

A proper logging system provides features such as:

```text
Timestamp
Log Level
Logger/Class Name
Thread Information
Log Filtering
File Logging
Log Management
```

Example:

```text
2026-08-02T12:15:46 INFO  AppPropertyDemo : Application started
```

---

# 3. Spring Boot Logging

Spring Boot applications can use the SLF4J logging API.

Important imports:

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
```

Create a logger:

```java
private static final Logger logger =
        LoggerFactory.getLogger(AppPropertyDemo.class);
```

Then:

```java
logger.info("Application started");
```

---

# 4. Important Logger Import

Use:

```java
import org.slf4j.Logger;
```

with:

```java
import org.slf4j.LoggerFactory;
```

Do not accidentally mix:

```java
java.util.logging.Logger
```

with:

```java
org.slf4j.LoggerFactory
```

Correct combination:

```text
org.slf4j.Logger
        +
org.slf4j.LoggerFactory
```

---

# 5. Log Levels

Common logging levels are:

```text
TRACE
DEBUG
INFO
WARN
ERROR
```

Simple severity hierarchy:

```text
TRACE
  ↓
DEBUG
  ↓
INFO
  ↓
WARN
  ↓
ERROR
```

---

# 6. TRACE

`TRACE` provides very detailed diagnostic information.

Example:

```java
logger.trace("Entering createOrder() method");
```

It can be useful when extremely detailed execution information is required.

It is usually not enabled for normal production logging because it can generate a very large amount of output.

---

# 7. DEBUG

`DEBUG` is useful during development and debugging.

Example:

```java
logger.debug("Processing userId={}", userId);
```

Other examples:

```text
Cart contains 4 items
User found in database
Calling inventory service
Calculated order total
```

DEBUG logs help developers understand internal application behavior.

---

# 8. INFO

`INFO` represents normal and important application events.

Example:

```java
logger.info("Application started successfully");
```

Real project examples:

```java
logger.info("Order created successfully, orderId={}", orderId);

logger.info("Payment initiated, orderId={}", orderId);

logger.info("User registered successfully, userId={}", userId);
```

INFO should generally represent useful normal application events rather than every tiny implementation detail.

---

# 9. WARN

`WARN` indicates a potentially problematic or unexpected situation where the application may still continue.

Example:

```java
logger.warn("Login attempt failed for userId={}", userId);
```

Other examples:

```text
Invalid coupon supplied
Stock is running low
External service response is slow
Requested resource was not found
```

---

# 10. ERROR

`ERROR` represents an actual failure.

Example:

```java
logger.error("Payment failed for orderId={}", orderId);
```

When an exception is available:

```java
logger.error(
        "Payment failed for orderId={}",
        orderId,
        exception
);
```

Examples:

```text
Database operation failed
Payment gateway failed
Unexpected exception occurred
File processing failed
```

---

# 11. Practical Logging Example

Example:

```java
package com.vijay.springbootlearning.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AppPropertyDemo implements CommandLineRunner {

    private static final Logger logger =
            LoggerFactory.getLogger(AppPropertyDemo.class);

    @Value("${app.environment}")
    private String environment;

    @Override
    public void run(String... args) {

        logger.trace("TRACE log from Spring Boot");

        logger.debug("DEBUG log from Spring Boot");

        logger.info(
                "Application environment: {}",
                environment
        );

        logger.warn("This is a WARN example");

        logger.error("This is an ERROR example");
    }
}
```

---

# 12. Default Logging Behavior

During our practical, we wrote:

```java
logger.trace(...);
logger.debug(...);
logger.info(...);
logger.warn(...);
logger.error(...);
```

But initially we saw:

```text
INFO  ✓
WARN  ✓
ERROR ✓
```

while:

```text
DEBUG ✗
TRACE ✗
```

were not displayed.

This happened because the configured logging threshold was effectively at the INFO level for our logger.

Concept:

```text
TRACE  ✗
DEBUG  ✗
INFO   ✓
WARN   ✓
ERROR  ✓
```

---

# 13. Configuring Logging Level

Logging levels can be configured through Spring Boot configuration.

Example:

```yaml
logging:
  level:
    com.vijay.springbootlearning: DEBUG
```

Now logs for that package can include:

```text
DEBUG
INFO
WARN
ERROR
```

while TRACE remains below the configured threshold.

---

# 14. Enabling TRACE

Configuration:

```yaml
logging:
  level:
    com.vijay.springbootlearning: TRACE
```

Now all these levels can be visible:

```text
TRACE ✓
DEBUG ✓
INFO  ✓
WARN  ✓
ERROR ✓
```

---

# 15. Package-Specific Logging

Instead of enabling detailed logging everywhere, we can configure our own package.

Example:

```yaml
logging:
  level:
    com.vijay.springbootlearning: DEBUG
```

Concept:

```text
Spring Framework
      ↓
Normal logging

Our Application Package
      ↓
DEBUG logging
```

This is useful because enabling extremely detailed logging globally can produce a huge amount of framework output.

---

# 16. Root Logging Level

A root logging level represents the general/default logging threshold.

Example:

```yaml
logging:
  level:
    root: INFO
```

We can combine root and package-specific levels:

```yaml
logging:
  level:
    root: INFO
    com.vijay.springbootlearning: DEBUG
```

Concept:

```text
Entire Application
      ↓
INFO

Our Package
      ↓
DEBUG
```

Package-specific configuration can therefore provide more detailed logging where we need it.

---

# 17. Parameterized Logging

Instead of:

```java
logger.info(
    "Application environment: " + environment
);
```

we can use:

```java
logger.info(
    "Application environment: {}",
    environment
);
```

The `{}` acts as a placeholder.

Another example:

```java
logger.info(
    "Order created, orderId={}, userId={}",
    orderId,
    userId
);
```

Output conceptually:

```text
Order created, orderId=784, userId=101
```

This style is commonly used with SLF4J.

---

# 18. Real Project Example — Order Service

Consider an e-commerce backend.

```java
@Service
public class OrderService {

    private static final Logger logger =
            LoggerFactory.getLogger(OrderService.class);

    public Order createOrder(Long userId) {

        logger.info(
                "Creating order for userId={}",
                userId
        );

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {

                    logger.warn(
                            "Order creation failed: user not found, userId={}",
                            userId
                    );

                    return new RuntimeException(
                            "User not found"
                    );
                });

        logger.debug(
                "User found for order creation, userId={}",
                userId
        );

        try {

            Order savedOrder =
                    orderRepository.save(new Order());

            logger.info(
                    "Order created successfully, orderId={}, userId={}",
                    savedOrder.getId(),
                    userId
            );

            return savedOrder;

        } catch (Exception exception) {

            logger.error(
                    "Failed to create order for userId={}",
                    userId,
                    exception
            );

            throw exception;
        }
    }
}
```

---

# 19. Real Project Example — Payment

Payment initiated:

```java
logger.info(
    "Payment initiated, orderId={}, amount={}",
    orderId,
    amount
);
```

Successful payment:

```java
logger.info(
    "Payment successful, orderId={}",
    orderId
);
```

Failed payment:

```java
logger.error(
    "Payment failed, orderId={}",
    orderId,
    exception
);
```

Now suppose a customer reports:

```text
Payment failed for order 784
```

We can search logs using:

```text
orderId=784
```

and inspect events related to that order.

Example:

```text
INFO  Payment initiated, orderId=784
INFO  Calling payment gateway, orderId=784
ERROR Payment failed, orderId=784
```

This is one of the major benefits of logging in production systems.

---

# 20. Saving Logs to a File

By default, we commonly see Spring Boot logs in the console.

We can also configure a log file.

Example:

```yaml
logging:
  level:
    com.vijay.springbootlearning: INFO

  file:
    name: logs/application.log
```

The project can then contain:

```text
springboot-learning/
│
├── logs/
│   └── application.log
│
├── src/
├── target/
└── pom.xml
```

---

# 21. Log File Practical

We configured:

```yaml
logging:
  level:
    com.vijay.springbootlearning: INFO

  file:
    name: logs/application.log
```

Then generated logs such as:

```java
logger.info(
    "Order created successfully, orderId={}, userId={}",
    784,
    101
);

logger.info(
    "Payment initiated, orderId={}, amount={}",
    784,
    1499
);

logger.warn(
    "Payment taking longer than expected, orderId={}",
    784
);

logger.error(
    "Payment failed, orderId={}",
    784
);
```

The logs were written into:

```text
logs/application.log
```

This proved that application logs can be persisted outside the console.

---

# 22. Searching Logs

If we have:

```text
INFO  Order created, orderId=784
INFO  Payment initiated, orderId=784
ERROR Payment failed, orderId=784

INFO  Order created, orderId=785
```

we can search:

```text
orderId=784
```

to investigate one particular order.

Useful searchable identifiers can include:

```text
orderId
userId
paymentId
requestId
correlationId
```

Be careful that identifiers included in logs are appropriate to log and do not expose sensitive information.

---

# 23. Should Logs Be Stored in the Database?

Application logs are generally handled by logging infrastructure rather than being treated as normal business data.

Conceptually:

```text
Business Data
     ↓
Database

Application Logs
     ↓
Logging System
```

Examples of business data:

```text
Users
Products
Orders
Payments
```

Examples of logs:

```text
Request processing events
Warnings
Errors
Debugging information
Runtime events
```

Therefore, normal application logging does not require inserting every log entry into a MySQL table.

---

# 24. Logs vs Audit Records

Application logs and audit records are different concepts.

### Application Log

Example:

```text
Payment gateway call failed
```

Used mainly for:

```text
Debugging
Monitoring
Troubleshooting
```

### Audit Record

Example:

```text
Admin 42 changed order 784
from PENDING to SHIPPED
at a particular time
```

Audit information may be stored as structured persistent business/security data when required.

So:

```text
Application Logs ≠ Audit Records
```

---

# 25. Logging in Cloud Production

On a developer machine:

```text
Spring Boot
    ↓
Console / application.log
    ↓
Search locally
```

In a larger cloud production environment:

```text
Spring Boot Instance 1 ─┐
                        │
Spring Boot Instance 2 ─┼─→ Central Logging Platform
                        │            ↓
Spring Boot Instance 3 ─┘      Search / Filter
                                     ↓
                               Investigate Errors
```

Centralized logging becomes useful when multiple application instances are running.

---

# 26. Why Centralized Logging?

Imagine three backend instances:

```text
Backend Instance 1
Backend Instance 2
Backend Instance 3
```

A request may be handled by any one of them.

Checking individual log files manually becomes difficult.

Centralized logging can provide:

```text
Multiple Application Instances
            ↓
      Central Logs
            ↓
    Search / Filtering
            ↓
       Investigation
```

Production systems may use cloud-provider logging or dedicated log-management stacks.

---

# 27. Log Rotation

A backend may run continuously and generate many logs.

Without log management:

```text
application.log

Day 1   → grows
Day 10  → grows more
Day 100 → potentially very large
```

This can consume excessive disk space.

Log rotation helps manage this.

Concept:

```text
Current Log
     ↓
application.log
     ↓
Rotation
     ↓
Older Log Files
```

Example conceptually:

```text
application.log
application.2026-08-01.log
application.2026-07-31.log
```

Older logs can then be archived or removed according to a retention policy.

---

# 28. Logging and Production

A common strategy is:

```text
Development
     ↓
More DEBUG information

Production
     ↓
INFO / WARN / ERROR
```

This is not an absolute rule for every system, but production environments usually avoid unnecessary DEBUG/TRACE output because it can:

- Generate huge amounts of logs
- Make useful logs harder to find
- Consume additional storage
- Potentially expose internal implementation details

Profiles can also help maintain environment-specific logging configuration.

---

# 29. Logging with Profiles

Development configuration:

```yaml
# application-dev.yml

logging:
  level:
    com.vijay.springbootlearning: DEBUG
```

Production configuration:

```yaml
# application-prod.yml

logging:
  level:
    com.vijay.springbootlearning: INFO
```

Now:

```text
DEV Profile
    ↓
DEBUG + higher levels

PROD Profile
    ↓
INFO + higher levels
```

This combines two concepts we studied:

```text
Spring Profiles
       +
Logging Configuration
```

---

# 30. Never Log Sensitive Information

Do not put sensitive secrets directly into application logs.

Examples:

```text
❌ Password
❌ OTP
❌ Access Token
❌ Refresh Token
❌ API Secret
❌ Private Key
❌ Full Card Details
```

Bad example:

```java
logger.info(
    "Login password={}",
    password
);
```

Never do this.

Similarly:

```java
logger.info(
    "OTP={}",
    otp
);
```

should be avoided.

Logs often live for a long time and may be accessible to operational systems, so sensitive information must be protected.

---

# 31. Choosing the Correct Log Level

A useful mental model:

```text
TRACE
→ Extremely detailed diagnostic flow

DEBUG
→ Information useful while debugging

INFO
→ Important normal business/application events

WARN
→ Something unexpected or potentially problematic

ERROR
→ Actual operation/application failure
```

Example:

```java
logger.debug(
    "Cart contains {} items",
    cartItems.size()
);

logger.info(
    "Order created, orderId={}",
    orderId
);

logger.warn(
    "Stock low, productId={}",
    productId
);

logger.error(
    "Payment failed, orderId={}",
    orderId,
    exception
);
```

---

# 32. Complete Logging Flow

```text
                    Spring Boot Application
                              ↓
                         SLF4J Logger
                              ↓
                    Logging Configuration
                              ↓
               ┌──────────────┴──────────────┐
               ↓                             ↓
            Console                      Log File
                                             ↓
                                   logs/application.log
                                             ↓
                                     Search / Debug
```

For larger production systems:

```text
Spring Boot Applications
          ↓
        Logs
          ↓
Centralized Logging Platform
          ↓
 Search / Filter / Monitor
          ↓
Investigate Production Issues
```

---

# 33. Interview Questions

### Q1. What is logging?

Logging is the process of recording application runtime events for debugging, monitoring, and troubleshooting.

### Q2. What are common logging levels?

```text
TRACE
DEBUG
INFO
WARN
ERROR
```

### Q3. Which logging API did we use?

SLF4J:

```java
org.slf4j.Logger
org.slf4j.LoggerFactory
```

### Q4. How do you create a logger?

```java
private static final Logger logger =
        LoggerFactory.getLogger(MyClass.class);
```

### Q5. What is parameterized logging?

Instead of string concatenation:

```java
logger.info("Order ID: " + orderId);
```

we can write:

```java
logger.info("Order ID: {}", orderId);
```

### Q6. How can you configure a package logging level?

```yaml
logging:
  level:
    com.vijay.springbootlearning: DEBUG
```

### Q7. How can logs be written to a file?

```yaml
logging:
  file:
    name: logs/application.log
```

### Q8. Why didn't DEBUG appear when INFO was configured?

Because DEBUG has a lower severity than INFO and was filtered out by the configured logging threshold.

### Q9. What is log rotation?

Log rotation manages growing log files by rolling older logs into separate files and applying retention/archive policies.

### Q10. Should application logs normally be stored in a normal business database table?

Application logs are normally handled through logging infrastructure. Structured audit/business records are a separate concern and may be persisted when required.

### Q11. Why are logs important in production?

They help developers and operations teams investigate errors and understand what happened during application execution.

### Q12. What information should never be logged?

Sensitive information such as passwords, OTPs, access tokens, API secrets, private keys, and full payment-card details.

---

# 34. Quick Revision

```text
                         LOGGING
                            ↓
              ┌─────────────┼─────────────┐
              ↓             ↓             ↓
           Levels        Storage       Production
              ↓             ↓             ↓
           TRACE         Console      Central Logs
           DEBUG         File             ↓
           INFO            ↓           Search
           WARN         Rotation           ↓
           ERROR                        Debug
```

Real backend example:

```text
User Request
     ↓
Controller
     ↓
Service
     ↓
INFO  → Order creation started
     ↓
DEBUG → Internal processing details
     ↓
WARN  → Potential problem
     ↓
ERROR → Operation failed
     ↓
Logs
     ↓
Search using orderId / requestId
     ↓
Investigate Issue
```

## Remember

> Logging is not just printing messages. It provides structured runtime information that helps us understand, monitor, and troubleshoot an application, especially after it has been deployed.