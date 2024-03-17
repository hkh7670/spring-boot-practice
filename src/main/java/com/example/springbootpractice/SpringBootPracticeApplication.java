package com.example.springbootpractice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SpringBootPracticeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootPracticeApplication.class, args);
    }

}
