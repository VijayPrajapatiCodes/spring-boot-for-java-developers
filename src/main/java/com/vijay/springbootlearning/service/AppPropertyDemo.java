package com.vijay.springbootlearning.service;

import com.vijay.springbootlearning.SpringbootLearningApplication;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class AppPropertyDemo implements CommandLineRunner {
    private  static final Logger logger = LoggerFactory.getLogger(AppPropertyDemo.class);
    @Value("${app.environment}")
    private String environment;

      @Value("${app.name:My Application}")
      private String appName;
      @Value("${app.developer:Unknown}")
      private String developer;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("App Name:"+ appName);
        System.out.println("Developer:" +developer);
        System.out.println("Environment: " + environment);
        logger.trace("TRACE log from Spring Boot");
        logger.info("Application started in environment={}", environment);

        logger.info("Order created successfully, orderId={}, userId={}", 784, 101);

        logger.info("Payment initiated, orderId={}, amount={}", 784, 1499);

        logger.warn("Payment taking longer than expected, orderId={}", 784);

        logger.error("Payment failed, orderId={}", 784);

    }
}
