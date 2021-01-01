package com.foxminded.university.management.schedule;

import com.foxminded.university.management.schedule.dao.AudienceDao;
import com.foxminded.university.management.schedule.models.Audience;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.sql.DataSource;

@SpringBootApplication
public class Application {
    @Autowired
    static DataSource dataSource;
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        Audience audience = new Audience(2, 100, 2L);
        AudienceDao audienceDao = new AudienceDao(dataSource);
        audienceDao.save(audience);
    }
}