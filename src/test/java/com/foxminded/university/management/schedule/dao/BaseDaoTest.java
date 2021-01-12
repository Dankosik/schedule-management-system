package com.foxminded.university.management.schedule.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;

@ContextConfiguration(initializers = {BaseDaoTest.Initializer.class})
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:init_test_db.sql")
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:clear_db.sql")
//@Import({TestConfig.class})
class BaseDaoTest {

    private static final PostgreSQLContainer<?> POSTGRESQL_CONTAINER = new PostgreSQLContainer<>("postgres:12")
            .withDatabaseName("integration-tests-db")
            .withUsername("sa")
            .withPassword("sa");

    @Autowired
    protected TestUtils testUtils;
    @Autowired
    protected DataSource dataSource;
    @Autowired
    protected JdbcTemplate jdbcTemplate;


    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            POSTGRESQL_CONTAINER.start();

            TestPropertyValues.of(
                    "spring.datasource.url=" + POSTGRESQL_CONTAINER.getJdbcUrl(),
                    "spring.datasource.username=" + POSTGRESQL_CONTAINER.getUsername(),
                    "spring.datasource.password=" + POSTGRESQL_CONTAINER.getPassword()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }
}
