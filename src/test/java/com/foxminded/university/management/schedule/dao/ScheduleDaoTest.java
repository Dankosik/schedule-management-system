package com.foxminded.university.management.schedule.dao;

import com.foxminded.university.management.schedule.models.Schedule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class ScheduleDaoTest {
    @Container
    private final PostgreSQLContainer<?> POSTGRESQL_CONTAINER =
            new PostgreSQLContainer<>("postgres:12")
                    .withInitScript("init_test_db.sql");

    private ScheduleDao scheduleDao;

    @BeforeEach
    void setUp() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(POSTGRESQL_CONTAINER.getJdbcUrl());
        dataSource.setUsername(POSTGRESQL_CONTAINER.getUsername());
        dataSource.setPassword(POSTGRESQL_CONTAINER.getPassword());
        scheduleDao = new ScheduleDao(dataSource);
    }

    @Test
    void shouldCreateNewSchedule() {
        Schedule schedule = new Schedule(1L);
        scheduleDao.delete(scheduleDao.getById(1L).get());
        scheduleDao.save(schedule);
        Schedule expected = new Schedule(1L, 1L);

        assertEquals(expected, scheduleDao.getById(1L).get());
    }

    @Test
    void shouldUpdateSchedule() {
        Schedule schedule = new Schedule(1L, 2L);
        assertNotEquals(schedule, scheduleDao.getById(1L).get());
        scheduleDao.save(schedule);

        assertEquals(schedule, scheduleDao.getById(1L).get());
    }

    @Test
    void shouldReturnScheduleWithIdOne() {
        Schedule expected = new Schedule(1L, 1L);
        Schedule actual = scheduleDao.getById(1L).get();

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnListOfSchedules() {
        List<Schedule> expected = List.of(
                new Schedule(1L, 1L),
                new Schedule(2L, 1L));
        List<Schedule> actual = scheduleDao.getAll();

        assertEquals(expected, actual);
    }

    @Test
    void shouldDeleteSchedule() {
        Schedule schedule = new Schedule(1L, 1L);
        List<Schedule> expected = List.of(
                new Schedule(2L, 1L));
        assertTrue(scheduleDao.delete(schedule));
        List<Schedule> actual = scheduleDao.getAll();

        assertEquals(expected, actual);
    }

    @Test
    void shouldSaveListOfSchedules() {
        List<Schedule> audiences = List.of(
                new Schedule(1L),
                new Schedule(2L));

        List<Schedule> expected = List.of(
                new Schedule(1L, 1L),
                new Schedule(2L, 2L));
        scheduleDao.delete(new Schedule(1L, 1L));
        scheduleDao.delete(new Schedule(2L, 1L));
        scheduleDao.saveAll(audiences);

        assertEquals(expected, scheduleDao.getAll());
    }

    @Test
    void shouldReturnListOfSchedulesWithUniversityIdOne() {
        List<Schedule> expected = List.of(
                new Schedule(1L, 1L),
                new Schedule(2L, 1L));
        List<Schedule> actual = scheduleDao.getSchedulesByUniversityId(1L);

        assertEquals(expected, actual);
    }
}