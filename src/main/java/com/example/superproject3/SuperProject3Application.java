package com.example.superproject3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SuperProject3Application {

    public static void main(String[] args) {
        SpringApplication.run(SuperProject3Application.class, args);
    }

}
