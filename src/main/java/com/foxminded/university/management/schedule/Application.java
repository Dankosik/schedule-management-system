package com.foxminded.university.management.schedule;

import com.foxminded.university.management.schedule.service.data.generation.DataCombiner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    private static DataCombiner dataCombiner;

    public Application(DataCombiner dataCombiner) {
        Application.dataCombiner = dataCombiner;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        dataCombiner.generateDataIfTablesClear();
    }
}
