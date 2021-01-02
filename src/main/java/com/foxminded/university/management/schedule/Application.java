package com.foxminded.university.management.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.sql.DataSource;

@SpringBootApplication
public class Application {
    @Autowired
    static DataSource dataSource;
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}