# Spring Boot Asynchronous Processing

## 📌 What is Asynchronous Processing?

Asynchronous processing means executing a task independently in the background without making the main thread wait for that task to complete.

In Spring Boot, asynchronous processing can be implemented using:

```text
@Async
@EnableAsync
CompletableFuture
TaskExecutor
```

---

# Why Do We Need Async Processing?

Suppose a user registers on an application.

After registration, the application needs to:

1. Save user
2. Send welcome email
3. Send notification
4. Generate PDF

Without Async:

```text
Request
   ↓
Save User
   ↓
Send Email
   ↓
Send Notification
   ↓
Generate PDF
   ↓
Response
```

The user has to wait for all tasks to finish.

---

With Async:

```text
Request
   ↓
Save User
   ↓
Start Background Tasks
   ├──→ Email
   ├──→ Notification
   └──→ PDF
   ↓
Response
```

The main request can return without waiting for non-critical background tasks.

---

# Synchronous vs Asynchronous

## Synchronous

Tasks execute one after another.

```text
Task 1
  ↓
Task 2
  ↓
Task 3
  ↓
Response
```

The current execution waits for each task.

---

## Asynchronous

A background task can execute independently.

```text
Main Thread
     │
     ├──────────────→ Response
     │
     └──→ Background Thread
              ↓
           Task
```

---

# Enable Async

Spring Boot asynchronous execution can be enabled using:

```java
@EnableAsync
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

Import:

```java
import org.springframework.scheduling.annotation.EnableAsync;
```

---

# @Async

The `@Async` annotation tells Spring to execute the method asynchronously.

Example:

```java
@Service
public class EmailService {

    @Async
    public void sendEmail() {

        System.out.println("Sending Email...");

    }
}
```

The method is executed using an asynchronous executor.

---

# Simple Example

```java
@Service
public class NotificationService {

    @Async
    public void sendNotification() {

        System.out.println("Notification started...");

        // Long-running task

        System.out.println("Notification completed...");
    }
}
```

Controller:

```java
@RestController
public class UserController {

    private final NotificationService notificationService;

    public UserController(
            NotificationService notificationService) {

        this.notificationService = notificationService;
    }

    @GetMapping("/test")
    public String test() {

        notificationService.sendNotification();

        return "Response Sent";
    }
}
```

The request does not need to wait for the asynchronous task to finish.

---

# Main Thread vs Async Thread

Without Async:

```text
HTTP Request
     ↓
Main Thread
     ↓
Service
     ↓
Long Task
     ↓
Response
```

With Async:

```text
HTTP Request
     ↓
Main Thread
     ├──────────────→ Response
     │
     └──→ Async Thread
              ↓
           Long Task
```

The asynchronous task executes using a different worker thread.

---

# Thread Pool

Applications should not create unlimited threads.

A Thread Pool manages a group of worker threads.

Example:

```text
Thread Pool

┌──────────┐
│ Thread 1 │
│ Thread 2 │
│ Thread 3 │
│ Thread 4 │
└──────────┘
```

Tasks are assigned to available worker threads.

If all workers are busy, additional tasks can wait in a queue.

---

# Custom Thread Pool

Spring provides `ThreadPoolTaskExecutor` for configuring asynchronous execution.

Example:

```java
@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean
    public Executor taskExecutor() {

        ThreadPoolTaskExecutor executor =
                new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("async-");

        executor.initialize();

        return executor;
    }
}
```

---

# Thread Pool Properties

## Core Pool Size

```java
executor.setCorePoolSize(5);
```

Number of worker threads normally maintained by the executor.

---

## Maximum Pool Size

```java
executor.setMaxPoolSize(10);
```

Maximum number of threads that can be created by the executor.

---

## Queue Capacity

```java
executor.setQueueCapacity(100);
```

Number of tasks that can wait in the queue when workers are busy.

---

## Thread Name Prefix

```java
executor.setThreadNamePrefix("async-");
```

Helps identify asynchronous threads in logs.

Example:

```text
async-1
async-2
async-3
```

---

# CompletableFuture

For asynchronous methods that need to return a result, `CompletableFuture` can be used.

Example:

```java
@Async
public CompletableFuture<String> processTask() {

    return CompletableFuture.completedFuture(
        "Task Completed"
    );
}
```

The result can be completed and consumed asynchronously.

---

# Why CompletableFuture?

`CompletableFuture` allows asynchronous tasks to:

- Return results
- Chain operations
- Combine multiple tasks
- Handle errors
- Continue processing after completion

---

# Example Use Case

Suppose an e-commerce application needs:

```text
Product Service
      +
Review Service
      +
Recommendation Service
```

These operations may execute independently.

With asynchronous processing:

```text
             Request
                ↓
        ┌───────┼────────┐
        ↓       ↓        ↓
     Product  Review  Recommendation
        ↓       ↓        ↓
        └───────┼────────┘
                ↓
             Response
```

`CompletableFuture` can be used to combine asynchronous results.

---

# Real-World Use Cases

## Email

```text
User Registration
       ↓
Save User
       ↓
Response
       │
       └──→ Send Welcome Email
```

---

## Notifications

```text
Order Placed
     ↓
Save Order
     ↓
Response
     │
     └──→ Send Notification
```

---

## PDF Generation

```text
Report Request
     ↓
Start PDF Generation
     ↓
Response
     │
     └──→ Generate PDF
```

---

## Image Processing

Large images can be processed in the background.

---

## Data Processing

Large or time-consuming data processing jobs can be executed asynchronously.

---

# Scheduling vs Async

| Scheduling | Async |
|------------|-------|
| Defines when a task should execute | Allows a task to execute asynchronously |
| `@Scheduled` | `@Async` |
| Time/interval based | Background execution |
| Daily report | Email sending |
| Periodic cleanup | PDF generation |

They can also be combined.

Example:

```text
8:00 AM
   ↓
@Scheduled
   ↓
Generate Daily Report
   ↓
@Async
   ↓
Send Report Email
```

---

# Important Rule: Spring Proxy

`@Async` works through Spring's proxy mechanism.

Calling an `@Async` method from another Spring-managed bean works as expected.

Example:

```text
Controller
    ↓
Spring Bean
    ↓
@Async Method
```

Calling an `@Async` method directly from another method within the same class can bypass the Spring proxy.

Example:

```java
public void method1() {

    method2();
}

@Async
public void method2() {

}
```

This should not be relied upon for asynchronous execution.

---

# Exception Handling

Asynchronous tasks should handle failures properly.

For methods returning `void`, exceptions need appropriate handling because the caller does not directly receive the exception.

For more advanced applications, Spring's asynchronous exception handling mechanisms can be configured.

For methods returning `CompletableFuture`, errors can be handled through the future's API.

---

# Advantages

- Faster API response
- Non-blocking background processing
- Better user experience
- Efficient handling of long-running tasks
- Useful for independent operations
- Can improve application throughput

---

# Disadvantages

- More complex debugging
- Thread management required
- Error handling becomes more important
- Race conditions may occur
- Requires careful resource management
- Too many asynchronous tasks can overload the system

---

# When Should We Use Async?

Use asynchronous processing when:

- Task is time-consuming
- Task does not need to block the response
- Task can run independently
- User does not need the result immediately

Examples:

```text
Email
Notification
PDF Generation
Image Processing
Background Data Processing
```

---

# When Should We Avoid Async?

Avoid asynchronous execution when:

- The response depends immediately on the task result.
- The operation must be strictly sequential.
- The task is very small and async adds unnecessary complexity.
- The task requires strict transaction behavior tied to the original request.

---

# Production Considerations

For production applications:

- Configure an appropriate thread pool.
- Monitor thread pool usage.
- Set reasonable queue capacity.
- Handle exceptions.
- Avoid unlimited background tasks.
- Use proper logging.
- Consider message queues for large-scale background processing.
- Monitor failed asynchronous jobs.

For distributed systems, tools such as Kafka, RabbitMQ, or dedicated job-processing systems may be more appropriate for reliable asynchronous workflows.

---

# Interview Questions

### What is asynchronous processing?

Asynchronous processing allows a task to execute independently without making the main execution wait for its completion.

---

### What does @Async do?

`@Async` tells Spring to execute a method asynchronously using an executor.

---

### What does @EnableAsync do?

`@EnableAsync` enables Spring's asynchronous method execution capability.

---

### Does @Async create a new thread every time?

Not necessarily. Spring uses an executor, typically with a thread pool, to manage asynchronous execution.

---

### What is ThreadPoolTaskExecutor?

It is an executor implementation used to manage a pool of worker threads for asynchronous tasks.

---

### What is CompletableFuture?

`CompletableFuture` represents the result of an asynchronous computation and provides APIs for composing, combining, and handling asynchronous operations.

---

### Difference between @Async and @Scheduled?

`@Async` is used for asynchronous/background execution, while `@Scheduled` is used to execute tasks according to a time-based schedule.

---

### Can @Scheduled and @Async be used together?

Yes.

Example:

```text
@Scheduled
     ↓
Start Task
     ↓
@Async
     ↓
Background Processing
```

---

# Summary

```text
@EnableAsync
      ↓
Enable Async Processing
      ↓
@Async
      ↓
Executor
      ↓
Thread Pool
      ↓
Background Task
```

Important concepts:

```text
@Async
@EnableAsync
Thread Pool
ThreadPoolTaskExecutor
CompletableFuture
Async Exception Handling
```

Remember:

```text
@Scheduled → WHEN should the task run?

@Async     → Run the task asynchronously.
``` 