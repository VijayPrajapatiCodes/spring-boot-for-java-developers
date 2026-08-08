package com.vijay.springbootlearning.Controller;

import com.vijay.springbootlearning.service.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/send-email")
    public String sendEmail() {

        emailService.sendEmail();

        return "Email sent successfully!";
    }
    @GetMapping("/send-html-email")
    public String sendHtmlEmail() throws MessagingException {

        emailService.sendHtmlEmail();

        return "HTML email sent successfully!";
    }
}