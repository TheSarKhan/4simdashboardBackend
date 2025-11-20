package com.backend.dashboarddemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class DashboardDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DashboardDemoApplication.class, args);
    }

}
