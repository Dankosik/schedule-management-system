package com.foxminded.university.management.schedule.dao;

import com.foxminded.university.management.schedule.models.Audience;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class AudienceDaoTest {
    @Container
    private final PostgreSQLContainer<?> POSTGRESQL_CONTAINER =
            new PostgreSQLContainer<>("postgres:12")
                    .withInitScript("init_test_db.sql");

    private AudienceDao audienceDao;

    @BeforeEach
    void setUp() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(POSTGRESQL_CONTAINER.getJdbcUrl());
        dataSource.setUsername(POSTGRESQL_CONTAINER.getUsername());
        dataSource.setPassword(POSTGRESQL_CONTAINER.getPassword());
        audienceDao = new AudienceDao(dataSource);
    }

    @Test
    void shouldCreateNewAudience() {
        Audience audience = new Audience(310, 25, 1L);
        audienceDao.delete(audienceDao.getById(1L).get());
        audienceDao.save(audience);
        Audience expected = new Audience(1L, 310, 25, 1L);

        assertEquals(expected, audienceDao.getById(1L).get());
    }

    @Test
    void shouldUpdateAudience() {
        Audience audience = new Audience(1L, 310, 25, 1L);
        assertNotEquals(audience, audienceDao.getById(1L).get());
        audienceDao.save(audience);

        assertEquals(audience, audienceDao.getById(1L).get());
    }

    @Test
    void shouldReturnAudienceWithIdOne() {
        Audience expected = new Audience(1L, 301, 50, 1L);
        Audience actual = audienceDao.getById(1L).get();

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnListOfAudiences() {
        List<Audience> expected = List.of(
                new Audience(1L, 301, 50, 1L),
                new Audience(2L, 302, 75, 1L),
                new Audience(3L, 303, 100, 1L),
                new Audience(4L, 304, 30, 1L),
                new Audience(5L, 305, 55, 1L));
        List<Audience> actual = audienceDao.getAll();

        assertEquals(expected, actual);
    }

    @Test
    void shouldDeleteAudience() {
        Audience audience = new Audience(1L, 301, 50, 1L);
        List<Audience> expected = List.of(
                new Audience(2L, 302, 75, 1L),
                new Audience(3L, 303, 100, 1L),
                new Audience(4L, 304, 30, 1L),
                new Audience(5L, 305, 55, 1L));
        assertTrue(audienceDao.delete(audience));
        List<Audience> actual = audienceDao.getAll();

        assertEquals(expected, actual);
    }

    @Test
    void shouldSaveListOfAudiences() {
        List<Audience> audiences = List.of(
                new Audience(400, 15, 1L),
                new Audience(401, 60, 1L));

        List<Audience> expected = List.of(
                new Audience(3L, 303, 100, 1L),
                new Audience(4L, 304, 30, 1L),
                new Audience(5L, 305, 55, 1L),
                new Audience(1L, 400, 15, 1L),
                new Audience(2L, 401, 60, 1L));
        audienceDao.delete(new Audience(1L, 301, 50, 1L));
        audienceDao.delete(new Audience(2L, 302, 75, 1L));
        audienceDao.saveAll(audiences);

        assertEquals(expected, audienceDao.getAll());
    }

    @Test
    void shouldReturnListAudiencesWithUniversityIdOne() {
        List<Audience> expected = List.of(
                new Audience(1L, 301, 50, 1L),
                new Audience(2L, 302, 75, 1L),
                new Audience(3L, 303, 100, 1L),
                new Audience(4L, 304, 30, 1L),
                new Audience(5L, 305, 55, 1L));
        List<Audience> actual = audienceDao.getAudiencesByUniversityId(1L);

        assertEquals(expected, actual);
    }
}