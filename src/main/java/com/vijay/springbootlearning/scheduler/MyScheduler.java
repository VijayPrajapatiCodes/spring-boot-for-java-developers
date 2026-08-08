package com.vijay.springbootlearning.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MyScheduler {
//    @Scheduled(fixedRate = 5000)
//    public void runTask(){
//        System.out.println("MyScheduler is running");
//    }
//    @Scheduled(fixedDelay = 5000)
//    public void runTasks() {
//
//        System.out.println("Task Started: " + System.currentTimeMillis());
//
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }
//
//        System.out.println("Task Finished: " + System.currentTimeMillis());
//    }
    @Scheduled(cron = "*/10 * * * * *")
    public void runTask() {

        System.out.println("Cron task running: " + System.currentTimeMillis());

    }
}
