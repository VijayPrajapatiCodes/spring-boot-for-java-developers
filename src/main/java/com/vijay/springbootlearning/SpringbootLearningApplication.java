package com.vijay.springbootlearning;

import com.vijay.springbootlearning.service.MessageService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class SpringbootLearningApplication {

	public static void main(String[] args) {
        ApplicationContext context=
		   SpringApplication.run(SpringbootLearningApplication.class, args);
        MessageService messageService =context.getBean(MessageService.class);
        System.out.println(messageService.getMessage());
	}

}
