package com.vijay.springbootlearning;

// import com.vijay.springbootlearning.service.MessageService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

// import org.springframework.context.ApplicationContext;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.RequestParam;
@EnableCaching
@EnableScheduling
@SpringBootApplication
public class SpringbootLearningApplication {

    public static void main(String[] args) {

        SpringApplication.run(
                SpringbootLearningApplication.class,
                args
        );

        /*
        ApplicationContext context =
                SpringApplication.run(
                        SpringbootLearningApplication.class,
                        args
                );

        MessageService messageService =
                context.getBean(MessageService.class);

        System.out.println(messageService.getMessage());
        */
    }

    /*
    @GetMapping("/users/{userId}/orders")
    public String getOrders(
            @PathVariable int userId,
            @RequestParam String status) {

        return "User: " + userId +
                ", Status: " + status;
    }
    */
}