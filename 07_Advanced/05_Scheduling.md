# Spring Boot Scheduling

## 📌 What is Scheduling?

Scheduling means automatically executing a task at a specific time or after a specific interval.

Spring Boot provides scheduling support using:

- `@EnableScheduling`
- `@Scheduled`

Scheduling is useful when a task needs to run automatically without user interaction.

---

# Why Do We Need Scheduling?

Suppose an application needs to:

- Generate daily reports
- Send scheduled emails
- Delete expired data
- Perform database cleanup
- Check application health
- Process files periodically
- Generate backups

Without scheduling, these tasks would need to be triggered manually.

With scheduling, Spring Boot executes them automatically.

---

# Real-World Examples

### E-Commerce

Delete expired offers every night.

```text
12:00 AM
   ↓
Delete Expired Offers
```

### Banking

Generate monthly interest calculations.

```text
Every Month
   ↓
Calculate Interest
```

### Reporting System

Generate daily sales reports.

```text
Every Day
   ↓
Generate Report
```

### Monitoring

Check server health periodically.

```text
Every 1 Minute
   ↓
Health Check
```

---

# Enable Scheduling

Scheduling must first be enabled in the Spring Boot application.

```java
@EnableScheduling
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
import org.springframework.scheduling.annotation.EnableScheduling;
```

`@EnableScheduling` tells Spring Boot to detect and execute scheduled methods.

---

# @Scheduled

The `@Scheduled` annotation is used to define a scheduled task.

Example:

```java
@Component
public class MyScheduler {

    @Scheduled(fixedRate = 5000)
    public void runTask() {

        System.out.println("Task Running...");

    }
}
```

Here:

```text
5000 milliseconds
=
5 seconds
```

The task runs repeatedly according to the configured schedule.

---

# Important Scheduling Types

Spring Boot commonly provides:

1. `fixedRate`
2. `fixedDelay`
3. `cron`

---

# 1. fixedRate

```java
@Scheduled(fixedRate = 5000)
public void task() {

    System.out.println("Running...");

}
```

`fixedRate` schedules executions at a fixed interval.

```text
Start
  ↓
5 seconds
  ↓
Start
  ↓
5 seconds
  ↓
Start
```

The interval is measured between scheduled starts.

### Example

If:

```text
fixedRate = 5000
```

Then:

```text
5000 ms = 5 seconds
```

---

# fixedRate Example

```java
@Scheduled(fixedRate = 10000)
public void healthCheck() {

    System.out.println("Checking application health");

}
```

The method is scheduled every 10 seconds.

---

# 2. fixedDelay

```java
@Scheduled(fixedDelay = 5000)
public void task() {

    System.out.println("Running...");

}
```

`fixedDelay` waits for the previous execution to finish and then waits for the configured delay before starting the next execution.

Flow:

```text
Task Start
   ↓
Task Finish
   ↓
5 Seconds Wait
   ↓
Task Start
```

---

# fixedDelay Example

```java
@Scheduled(fixedDelay = 10000)
public void processFiles() {

    System.out.println("Processing files...");

}
```

After the method finishes, Spring waits 10 seconds before the next execution.

---

# fixedRate vs fixedDelay

| fixedRate | fixedDelay |
|-----------|------------|
| Fixed interval between scheduled starts | Delay after previous execution finishes |
| Based on start time | Based on completion time |
| Useful for periodic tasks | Useful when next execution should wait for previous completion |

---

# 3. Cron Expression

Cron is used when a task needs to run according to a specific time schedule.

Example:

```java
@Scheduled(cron = "0 0 8 * * *")
public void sendMorningReport() {

    System.out.println("Morning Report");

}
```

This represents a scheduled execution at 8:00 AM each day.

---

# Cron Format

Spring's cron expression uses six fields:

```text
second minute hour day-of-month month day-of-week
```

Format:

```text
┌──────── second
│ ┌────── minute
│ │ ┌──── hour
│ │ │ ┌── day of month
│ │ │ │ ┌ month
│ │ │ │ │ ┌ day of week
│ │ │ │ │ │
* * * * * *
```

---

# Common Cron Examples

## Every minute

```java
@Scheduled(cron = "0 * * * * *")
```

---

## Every hour

```java
@Scheduled(cron = "0 0 * * * *")
```

---

## Every day at 8 AM

```java
@Scheduled(cron = "0 0 8 * * *")
```

---

## Every day at midnight

```java
@Scheduled(cron = "0 0 0 * * *")
```

---

## Every Sunday at 10 AM

```java
@Scheduled(cron = "0 0 10 * * SUN")
```

---

# Scheduling Flow

```text
Spring Boot Application
          ↓
   @EnableScheduling
          ↓
      @Scheduled
          ↓
   Scheduler Engine
          ↓
    Execute Method
```

---

# Component Requirement

The class containing a scheduled method should be managed by Spring.

Example:

```java
@Component
public class ReportScheduler {

    @Scheduled(fixedRate = 60000)
    public void generateReport() {

        System.out.println("Generating report...");

    }
}
```

Spring detects the `@Scheduled` method and executes it automatically.

---

# Multiple Scheduled Tasks

An application can contain multiple scheduled methods.

```java
@Component
public class ApplicationScheduler {

    @Scheduled(fixedRate = 5000)
    public void healthCheck() {

        System.out.println("Health Check");

    }

    @Scheduled(fixedRate = 10000)
    public void processData() {

        System.out.println("Processing Data");

    }

    @Scheduled(cron = "0 0 0 * * *")
    public void dailyCleanup() {

        System.out.println("Daily Cleanup");

    }
}
```

Each method has its own schedule.

---

# Practical Use Cases

### 1. Database Cleanup

```text
Every Night
↓
Delete Expired Records
```

### 2. Email Reports

```text
Every Morning
↓
Generate Report
↓
Send Email
```

### 3. File Processing

```text
Every 10 Minutes
↓
Check Upload Folder
↓
Process Files
```

### 4. Monitoring

```text
Every Minute
↓
Check Service Health
```

### 5. Data Synchronization

```text
Every 5 Minutes
↓
Sync External API Data
```

---

# Important Difference

## fixedRate

```text
Start
 ↓
Interval
 ↓
Start
```

## fixedDelay

```text
Start
 ↓
Finish
 ↓
Delay
 ↓
Start
```

## cron

```text
Specific Schedule
 ↓
Execute
```

---

# Advantages

- Automatic task execution
- Reduces manual work
- Easy to implement
- Useful for background jobs
- Useful for periodic maintenance
- Useful for reports and notifications

---

# Limitations

Spring Boot's basic scheduler is not always suitable for complex distributed job processing.

For large distributed systems, specialized solutions may be required, such as:

- Quartz
- Distributed task schedulers
- Message queues
- External job scheduling systems

---

# Best Practices

- Keep scheduled tasks lightweight when possible.
- Avoid long-running tasks in the default scheduler.
- Handle exceptions properly.
- Monitor scheduled jobs.
- Avoid running the same critical job simultaneously across multiple application instances without a coordination strategy.
- Use appropriate scheduling intervals.
- Use cron when an exact calendar-based schedule is required.

---

# Interview Questions

### What is Scheduling?

Scheduling is the automatic execution of a task at a specified time or interval.

---

### What does `@EnableScheduling` do?

It enables Spring's scheduled-task execution support.

---

### What does `@Scheduled` do?

It defines a method that Spring should execute according to a configured schedule.

---

### What is `fixedRate`?

It schedules executions at a fixed interval measured between scheduled starts.

---

### What is `fixedDelay`?

It waits for the previous execution to finish and then waits for the configured delay before starting the next execution.

---

### Difference between fixedRate and fixedDelay?

`fixedRate` is based on scheduled start intervals, while `fixedDelay` is based on completion followed by a delay.

---

### When should we use cron?

Use cron when a task must run according to a specific calendar/time schedule.

Examples:

- Every day at midnight
- Every Monday
- Every month
- Every morning at 8 AM

---

### Can we have multiple scheduled methods?

Yes. A Spring Boot application can contain multiple scheduled methods with different schedules.

---

# Summary

```text
@EnableScheduling
        ↓
Enable Scheduling
        ↓
@Scheduled
        ↓
Define Schedule
        ↓
Execute Method Automatically
```

Main scheduling options:

```text
fixedRate
fixedDelay
cron
```

### Remember

```text
fixedRate  → Start-to-start interval

fixedDelay → Finish + delay + next start

cron       → Specific calendar/time schedule
```