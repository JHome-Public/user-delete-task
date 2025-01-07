package com.jhome.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UserDeleteTaskApplication {
    public static void main(String[] args) {
        System.exit(SpringApplication.exit(SpringApplication.run(UserDeleteTaskApplication.class, args)));
    }
}