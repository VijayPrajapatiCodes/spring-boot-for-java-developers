package com.vijay.springbootlearning.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail() {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo("vijayprajapati24646@gmail.com");
        message.setSubject("Spring Boot Email Test");
        message.setText(
                "Hello Vijay!\n\n" +
                        "This email was sent from Spring Boot using Gmail SMTP."
        );

        mailSender.send(message);

        System.out.println("Email sent successfully!");
    }
    public void sendHtmlEmail() throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper =
                new MimeMessageHelper(message, true);

        helper.setTo("vijayprajapati24646@gmail.com");
        helper.setSubject("🎉 Order Confirmation");

        String htmlContent = """
            <html>
            <body>
                <h1>Order Confirmed ✅</h1>

                <p>Hello Vijay,</p>

                <p>Your order has been successfully placed.</p>

                <h3>Order Details</h3>

                <ul>
                    <li>Order ID: VBZ-1001</li>
                    <li>Product: Laptop</li>
                    <li>Amount: ₹50,000</li>
                </ul>

                <p>Thank you for shopping with VijayBaazar.</p>
            </body>
            </html>
            """;

        helper.setText(htmlContent, true);

        mailSender.send(message);

        System.out.println("HTML Email sent successfully!");
    }
}