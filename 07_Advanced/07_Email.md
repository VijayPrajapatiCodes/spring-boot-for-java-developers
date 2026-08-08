# Spring Boot Email

## 📌 What is Email Integration?

Spring Boot application se automatically emails send karna Email Integration kehlata hai.

Common examples:

- Registration Email
- Welcome Email
- Order Confirmation
- Password Reset
- OTP Email
- Invoice Email
- Notification Email
- Daily Reports

---

# Email Architecture

```text
Spring Boot Application
          ↓
     JavaMailSender
          ↓
       SMTP Server
          ↓
       Internet
          ↓
    Recipient Inbox
```

---

# SMTP

SMTP stands for:

```text
Simple Mail Transfer Protocol
```

SMTP is commonly used for sending emails between mail clients and mail servers.

Examples of SMTP servers:

```text
Gmail SMTP
Brevo SMTP
Amazon SES
SendGrid
Mailgun
```

---

# Spring Boot Mail Dependency

Add the following dependency:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-mail</artifactId>
</dependency>
```

This provides Spring's email sending support.

---

# Gmail SMTP Configuration

Example:

```yaml
spring:
  mail:
    host: smtp.gmail.com
    port: 587
    username: ${MAIL_USERNAME}
    password: ${MAIL_PASSWORD}
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
```

---

# Important Security Rule

Never commit real email credentials to GitHub.

Bad:

```yaml
password: my-real-password
```

Better:

```yaml
username: ${MAIL_USERNAME}
password: ${MAIL_PASSWORD}
```

Credentials can be supplied through environment variables or another secure configuration mechanism.

---

# JavaMailSender

Spring Boot provides:

```java
JavaMailSender
```

for sending emails.

Example:

```java
@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
}
```

---

# Simple Text Email

For simple text emails, use:

```java
SimpleMailMessage
```

Example:

```java
public void sendEmail() {

    SimpleMailMessage message =
            new SimpleMailMessage();

    message.setTo("customer@gmail.com");

    message.setSubject(
            "Spring Boot Email Test"
    );

    message.setText(
            "Your order has been successfully placed."
    );

    mailSender.send(message);
}
```

---

# Simple Email Flow

```text
Controller
    ↓
EmailService
    ↓
JavaMailSender
    ↓
SMTP Server
    ↓
Recipient
```

---

# HTML Email

For formatted emails use:

```java
MimeMessage
```

and:

```java
MimeMessageHelper
```

Example:

```java
public void sendHtmlEmail()
        throws MessagingException {

    MimeMessage message =
            mailSender.createMimeMessage();

    MimeMessageHelper helper =
            new MimeMessageHelper(
                    message,
                    true
            );

    helper.setTo("customer@gmail.com");

    helper.setSubject(
            "Order Confirmation"
    );

    String html = """
            <html>
            <body>

                <h1>Order Confirmed ✅</h1>

                <p>
                    Your order has been successfully placed.
                </p>

            </body>
            </html>
            """;

    helper.setText(html, true);

    mailSender.send(message);
}
```

The second parameter:

```java
true
```

indicates that the content should be treated as HTML.

---

# Dynamic Email

Real applications usually contain dynamic information.

Example:

```text
Customer Name
Order ID
Product Name
Amount
```

Instead of hardcoding:

```text
Hello Vijay
Order ID: VBZ-1001
Amount: ₹50,000
```

we can pass values dynamically.

Example:

```java
public void sendOrderEmail(
        String customerName,
        String orderId,
        String productName,
        double amount
) throws MessagingException {

    MimeMessage message =
            mailSender.createMimeMessage();

    MimeMessageHelper helper =
            new MimeMessageHelper(
                    message,
                    true
            );

    helper.setTo("customer@gmail.com");

    helper.setSubject(
            "Order Confirmation - " + orderId
    );

    String html = """
            <h1>Order Confirmed ✅</h1>

            <p>Hello %s</p>

            <p>Order ID: %s</p>

            <p>Product: %s</p>

            <p>Amount: ₹%.2f</p>
            """.formatted(
                    customerName,
                    orderId,
                    productName,
                    amount
            );

    helper.setText(html, true);

    mailSender.send(message);
}
```

---

# Email Attachments

Real applications may need to attach:

- Invoice PDF
- Reports
- Documents
- Images
- CSV files

For attachments, use:

```java
MimeMessageHelper
```

Example:

```java
FileSystemResource file =
        new FileSystemResource(
                new File("invoice.pdf")
        );

helper.addAttachment(
        "invoice.pdf",
        file
);
```

---

# Attachment Flow

```text
Order Created
      ↓
Generate Invoice
      ↓
invoice.pdf
      ↓
Email Service
      ↓
Attach PDF
      ↓
SMTP
      ↓
Customer
```

---

# Email + @Async

Email sending can sometimes take time.

For appropriate use cases, email sending can be performed asynchronously.

Example:

```java
@Async
public void sendOrderEmail() {

    // Send Email
}
```

Flow:

```text
Order API
    ↓
Save Order
    ↓
Response
    │
    └────→ @Async
              ↓
          Send Email
```

This prevents the main request from unnecessarily waiting for the email operation.

---

# Real-World Example

E-commerce order:

```text
Customer Places Order
          ↓
      Save Order
          ↓
   Return Response
          ↓
      @Async
          ↓
   Generate Invoice
          ↓
   Attach Invoice
          ↓
      Send Email
```

---

# Common Email Use Cases

## Registration

```text
User Registration
       ↓
Welcome Email
```

## Password Reset

```text
Forgot Password
       ↓
Reset Link
       ↓
Email
```

## Order Confirmation

```text
Order Placed
       ↓
Confirmation Email
```

## Invoice

```text
Order
 ↓
Generate Invoice
 ↓
PDF Attachment
 ↓
Email
```

## Notification

```text
Important Event
       ↓
Notification Email
```

---

# SimpleMailMessage vs MimeMessage

| SimpleMailMessage | MimeMessage |
|---|---|
| Simple text email | Advanced email |
| Easy to use | More powerful |
| Plain text | HTML |
| No complex attachments | Supports attachments |
| Basic use cases | Production-style emails |

---

# Gmail SMTP

Typical Gmail SMTP configuration:

```text
Host:
smtp.gmail.com

Port:
587

Protocol:
SMTP

TLS:
Enabled
```

Authentication generally requires appropriate Google account authentication, such as an App Password where applicable.

---

# Security Best Practices

Never commit:

```text
Email Password
SMTP Password
API Keys
App Password
Secret Keys
```

Use:

```text
Environment Variables
Secret Manager
Vault
Cloud Secret Management
```

Example:

```yaml
spring:
  mail:
    username: ${MAIL_USERNAME}
    password: ${MAIL_PASSWORD}
```

---

# Error Handling

Email sending can fail because of:

- Invalid credentials
- SMTP server unavailable
- Network problems
- Invalid recipient
- Authentication failure
- Connection timeout
- Provider restrictions

Therefore production applications should handle mail exceptions properly.

Example:

```java
try {

    mailSender.send(message);

} catch (Exception e) {

    // Log error
}
```

For critical systems, failed emails may need retry or queue-based processing.

---

# Email Providers

Common email providers/services include:

```text
Gmail SMTP
Brevo
Amazon SES
SendGrid
Mailgun
```

For learning/testing, Gmail SMTP can be convenient.

For production systems, dedicated transactional email providers are often more appropriate depending on requirements.

---

# Email + Scheduling

Email and scheduling can be combined.

Example:

```text
Every Day at 8 AM
        ↓
@Scheduled
        ↓
Generate Daily Report
        ↓
Send Email
```

---

# Email + Async + Scheduling

All three can work together.

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
Send Email
```

This is a useful pattern for background report delivery.

---

# Interview Questions

### What is SMTP?

SMTP stands for Simple Mail Transfer Protocol and is commonly used for sending emails.

---

### What is JavaMailSender?

`JavaMailSender` is Spring's interface for sending email messages.

---

### What is SimpleMailMessage?

`SimpleMailMessage` is used for simple text-based emails.

---

### What is MimeMessage?

`MimeMessage` supports more advanced email content such as HTML and attachments.

---

### What is MimeMessageHelper?

`MimeMessageHelper` simplifies creating MIME emails, including HTML content and attachments.

---

### How do you send an HTML email?

Use:

```text
MimeMessage
+
MimeMessageHelper
```

and:

```java
helper.setText(html, true);
```

---

### How do you send an attachment?

Use:

```java
helper.addAttachment(
    "invoice.pdf",
    file
);
```

---

### Should email credentials be stored in application.yml?

Credentials should not be committed directly to source control.

Use environment variables or a secret-management solution.

---

### Can email sending be asynchronous?

Yes. Appropriate email operations can be executed using:

```java
@Async
```

---

# Production Architecture

A simple application:

```text
Spring Boot
    ↓
EmailService
    ↓
SMTP Provider
    ↓
Customer
```

Large-scale application:

```text
Spring Boot
    ↓
Message Queue
    ↓
Email Worker
    ↓
Email Provider
    ↓
Customer
```

Message queues can provide better reliability and scalability for large email workloads.

---

# Summary

```text
Spring Boot Email
       ↓
JavaMailSender
       ↓
SMTP
       ↓
Email Provider
       ↓
Recipient
```

Important classes:

```text
JavaMailSender
SimpleMailMessage
MimeMessage
MimeMessageHelper
```

Important concepts:

```text
SMTP
Simple Email
HTML Email
Dynamic Email
Attachments
Async Email
Email Security
Exception Handling
```

Remember:

```text
SimpleMailMessage
        ↓
Simple Text Email

MimeMessage
        ↓
HTML / Advanced Email

MimeMessageHelper
        ↓
HTML + Attachments

@Async
        ↓
Background Email Processing
```

---

# Practical Status

```text
Simple Email          ✅
HTML Email            ✅
Dynamic HTML Email    ✅
Attachment Email      ✅
@Async Concept        ✅
```

Real-world project me in concepts ko combine karke:

```text
Order
 ↓
Database
 ↓
Invoice
 ↓
@Async
 ↓
HTML Email
 ↓
PDF Attachment
```

implement kiya ja sakta hai.