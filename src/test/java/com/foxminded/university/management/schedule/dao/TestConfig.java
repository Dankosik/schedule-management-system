package com.foxminded.university.management.schedule.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import utils.TestUtils;

import javax.sql.DataSource;

@TestConfiguration
public class TestConfig {
    @Autowired
    DataSource dataSource;
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Bean
    public TestUtils testUtils() {
        return new TestUtils(dataSource, jdbcTemplate);
    }
}