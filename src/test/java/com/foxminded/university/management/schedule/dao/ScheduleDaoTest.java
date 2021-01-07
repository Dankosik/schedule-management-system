package com.foxminded.university.management.schedule.dao;

import com.foxminded.university.management.schedule.models.Schedule;
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
class ScheduleDaoTest {
    @Container
    private final PostgreSQLContainer<?> POSTGRESQL_CONTAINER =
            new PostgreSQLContainer<>("postgres:12")
                    .withInitScript("init_test_db.sql");

    private ScheduleDao scheduleDao;
    private TestUtils testUtils;

    @BeforeEach
    void setUp() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(POSTGRESQL_CONTAINER.getJdbcUrl());
        dataSource.setUsername(POSTGRESQL_CONTAINER.getUsername());
        dataSource.setPassword(POSTGRESQL_CONTAINER.getPassword());
        scheduleDao = new ScheduleDao(dataSource);
        testUtils = new TestUtils(dataSource);
    }

    @Test
    void shouldCreateNewSchedule() {
        Schedule schedule = new Schedule(1000L);
        Long scheduleId = scheduleDao.save(schedule).getId();
        assertTrue(testUtils.existsById("schedule", scheduleId));

        Map<String, Object> map = testUtils.getEntry("schedule", scheduleId);
        Schedule actual = new Schedule((Long) map.get("university_id"));
        assertEquals(schedule, actual);
    }

    @Test
    void shouldUpdateSchedule() {
        Schedule schedule = new Schedule(1000L, 1000L);
        Long scheduleId = scheduleDao.save(schedule).getId();
        assertTrue(testUtils.existsById("schedule", scheduleId));

        Map<String, Object> map = testUtils.getEntry("schedule", scheduleId);
        Schedule actual = new Schedule((Long) map.get("id"), (Long) map.get("university_id"));
        assertEquals(schedule, actual);
    }

    @Test
    void shouldReturnScheduleWithIdOne() {
        Map<String, Object> map = testUtils.getEntry("schedule", 1000L);
        Schedule expected = new Schedule((Long) map.get("id"), (Long) map.get("university_id"));
        Schedule actual = scheduleDao.getById(1000L).get();

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnListOfSchedules() {
        List<Schedule> expected = List.of(
                new Schedule(1000L, 1000L),
                new Schedule(1001L, 1000L));
        List<Schedule> actual = scheduleDao.getAll();

        assertTrue(actual.containsAll(expected));
    }

    @Test
    void shouldDeleteSchedule() {
        assertTrue(scheduleDao.deleteById(1000L));
        assertFalse(testUtils.existsById("schedule", 1000L));
    }

    @Test
    void shouldSaveListOfSchedules() {
        List<Schedule> audiences = List.of(
                new Schedule(1000L),
                new Schedule(1001L));

        List<Schedule> expected = List.of(
                new Schedule(1L, 1000L),
                new Schedule(2L, 1001L));
        scheduleDao.saveAll(audiences);
        List<Schedule> actual = scheduleDao.getAll();

        assertTrue(actual.containsAll(expected));
    }

    @Test
    void shouldReturnListOfSchedulesWithUniversityIdOne() {
        List<Schedule> expected = List.of(
                new Schedule(1000L, 1000L),
                new Schedule(1001L, 1000L));
        List<Schedule> actual = scheduleDao.getSchedulesByUniversityId(1000L);

        assertTrue(actual.containsAll(expected));
    }

    @Test
    void shouldThrowExceptionIfScheduleNotExist() {
        assertThrows(NoSuchElementException.class, () -> scheduleDao.getById(21L).get());
    }

    @Test
    void shouldReturnFalseIfScheduleNotExist() {
        assertFalse(() -> scheduleDao.deleteById(21L));
    }
}