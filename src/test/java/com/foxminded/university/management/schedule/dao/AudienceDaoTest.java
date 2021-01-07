package com.foxminded.university.management.schedule.dao;

import com.foxminded.university.management.schedule.models.Audience;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import utils.TestUtils;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class AudienceDaoTest {
    @Container
    private final PostgreSQLContainer<?> POSTGRESQL_CONTAINER = new PostgreSQLContainer<>("postgres:12")
            .withInitScript("init_test_db.sql");

    private AudienceDao audienceDao;
    private TestUtils testUtils;

    @BeforeEach
    void setUp() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(POSTGRESQL_CONTAINER.getJdbcUrl());
        dataSource.setUsername(POSTGRESQL_CONTAINER.getUsername());
        dataSource.setPassword(POSTGRESQL_CONTAINER.getPassword());
        audienceDao = new AudienceDao(dataSource);
        testUtils = new TestUtils(dataSource);
    }

    @Test
    void shouldCreateNewAudience() {
        Audience audience = new Audience(310, 25, 1000L);
        Long audienceId = audienceDao.save(audience).getId();
        assertTrue(testUtils.existsById("audiences", audienceId));

        Map<String, Object> map = testUtils.getEntry("audiences", audienceId);
        Audience actual = new Audience((Integer) map.get("number"), (Integer) map.get("capacity"), (Long) map.get("university_id"));
        assertEquals(audience, actual);
    }

    @Test
    void shouldUpdateAudience() {
        Audience audience = new Audience(1000L, 310, 25, 1000L);
        Long audienceId = audienceDao.save(audience).getId();
        assertTrue(testUtils.existsById("audiences", audienceId));

        Map<String, Object> map = testUtils.getEntry("audiences", audienceId);
        Audience actual = new Audience((Long) map.get("id"), (Integer) map.get("number"), (Integer) map.get("capacity"), (Long) map.get("university_id"));
        assertEquals(audience, actual);
    }

    @Test
    void shouldReturnAudienceWithIdOne() {
        Map<String, Object> map = testUtils.getEntry("audiences", 1000L);
        Audience expected = new Audience((Long) map.get("id"), (Integer) map.get("number"), (Integer) map.get("capacity"), (Long) map.get("university_id"));
        Audience actual = audienceDao.getById(1000L).get();

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnListOfAudiences() {
        List<Audience> expected = List.of(
                new Audience(1000L, 301, 50, 1000L),
                new Audience(1001L, 302, 75, 1000L),
                new Audience(1002L, 303, 100, 1000L),
                new Audience(1003L, 304, 30, 1000L),
                new Audience(1004L, 305, 55, 1000L));
        List<Audience> actual = audienceDao.getAll();

        assertTrue(actual.containsAll(expected));
    }

    @Test
    void shouldDeleteAudience() {
        assertTrue(audienceDao.deleteById(1000L));
        assertFalse(testUtils.existsById("audiences", 1000L));
    }

    @Test
    void shouldSaveListOfAudiences() {
        List<Audience> audiences = List.of(
                new Audience(400, 15, 1000L),
                new Audience(401, 60, 1000L));

        List<Audience> expected = List.of(
                new Audience(1L, 400, 15, 1000L),
                new Audience(2L, 401, 60, 1000L),
                new Audience(1002L, 303, 100, 1000L),
                new Audience(1003L, 304, 30, 1000L),
                new Audience(1004L, 305, 55, 1000L));
        audienceDao.saveAll(audiences);
        List<Audience> actual = audienceDao.getAll();

        assertTrue(actual.containsAll(expected));
    }

    @Test
    void shouldReturnListAudiencesWithUniversityIdOne() {
        List<Audience> expected = List.of(
                new Audience(1000L, 301, 50, 1000L),
                new Audience(1001L, 302, 75, 1000L),
                new Audience(1002L, 303, 100, 1000L),
                new Audience(1003L, 304, 30, 1000L),
                new Audience(1004L, 305, 55, 1000L));
        List<Audience> actual = audienceDao.getAudiencesByUniversityId(1000L);

        assertTrue(actual.containsAll(expected));
    }

    @Test
    void shouldThrowExceptionIfAudienceNotExist() {
        assertThrows(NoSuchElementException.class, () -> audienceDao.getById(21L).get());
    }

    @Test
    void shouldReturnFalseIfAudienceNotExist() {
        assertFalse(() -> audienceDao.deleteById(21L));
    }
}