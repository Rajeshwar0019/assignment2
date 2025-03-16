package com.example.orderservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.Collections;

@SpringBootApplication
public class OrderServiceApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(OrderServiceApplication.class);
        // Set the server port to 7002 (instead of the default 8080)
        app.setDefaultProperties(Collections.singletonMap("server.port", "7002"));
        app.run(args);
    }
}
