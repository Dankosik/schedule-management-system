package com.foxminded.university.management.schedule.dao;

import com.foxminded.university.management.schedule.models.Lesson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Time;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class LessonDaoTest {
    @Container
    private final PostgreSQLContainer<?> POSTGRESQL_CONTAINER =
            new PostgreSQLContainer<>("postgres:12")
                    .withInitScript("init_test_db.sql");

    private LessonDao lessonDao;

    @BeforeEach
    void setUp() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(POSTGRESQL_CONTAINER.getJdbcUrl());
        dataSource.setUsername(POSTGRESQL_CONTAINER.getUsername());
        dataSource.setPassword(POSTGRESQL_CONTAINER.getPassword());
        lessonDao = new LessonDao(dataSource);
    }

    @Test
    void shouldCreateNewLesson() {
        Lesson lesson = new Lesson(1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), 2L);
        lessonDao.delete(lessonDao.getById(1L).get());
        lessonDao.save(lesson);
        Lesson expected = new Lesson(1L, 1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), 2L);

        assertEquals(expected, lessonDao.getById(1L).get());
    }

    @Test
    void shouldUpdateLesson() {
        Lesson lesson = new Lesson(1L,1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), 2L);
        assertNotEquals(lesson, lessonDao.getById(1L).get());
        lessonDao.save(lesson);

        assertEquals(lesson, lessonDao.getById(1L).get());
    }

    @Test
    void shouldReturnLessonWithIdOne() {
        Lesson expected = new Lesson(1L,1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), 1L);
        Lesson actual = lessonDao.getById(1L).get();

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnListOfLessons() {
        List<Lesson> expected = List.of(
                new Lesson(1L,1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), 1L),
                new Lesson(2L,2, Time.valueOf(LocalTime.of(10, 10, 0)), Duration.ofMinutes(90), 2L),
                new Lesson(3L,3, Time.valueOf(LocalTime.of(11, 50, 0)), Duration.ofMinutes(90), 3L),
                new Lesson(4L,4, Time.valueOf(LocalTime.of(13, 20, 0)), Duration.ofMinutes(90), 3L));
        List<Lesson> actual = lessonDao.getAll();

        assertEquals(expected, actual);
    }

    @Test
    void shouldDeleteLesson() {
        Lesson lesson = new Lesson(1L,1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), 1L);
        List<Lesson> expected = List.of(
                new Lesson(2L,2, Time.valueOf(LocalTime.of(10, 10, 0)), Duration.ofMinutes(90), 2L),
                new Lesson(3L,3, Time.valueOf(LocalTime.of(11, 50, 0)), Duration.ofMinutes(90), 3L),
                new Lesson(4L, 4, Time.valueOf(LocalTime.of(13, 20, 0)), Duration.ofMinutes(90), 3L));
        assertTrue(lessonDao.delete(lesson));
        List<Lesson> actual = lessonDao.getAll();

        assertEquals(expected, actual);
    }

    @Test
    void shouldSaveListOfLessons() {
        List<Lesson> lessons = List.of(
                new Lesson(4, Time.valueOf(LocalTime.of(13, 50, 0)), Duration.ofMinutes(90), 1L),
                new Lesson(5, Time.valueOf(LocalTime.of(15, 30, 0)), Duration.ofMinutes(90), 2L));

        List<Lesson> expected = List.of(
                new Lesson(3L, 3, Time.valueOf(LocalTime.of(11, 50, 0)), Duration.ofMinutes(90), 3L),
                new Lesson(4L, 4, Time.valueOf(LocalTime.of(13, 20, 0)), Duration.ofMinutes(90), 3L),
                new Lesson(1L, 4, Time.valueOf(LocalTime.of(13, 50, 0)), Duration.ofMinutes(90), 1L),
                new Lesson(2L, 5, Time.valueOf(LocalTime.of(15, 30, 0)), Duration.ofMinutes(90), 2L));
        lessonDao.delete(new Lesson(1L, 1, Time.valueOf(LocalTime.of(10, 10, 0)), Duration.ofMinutes(90), 2L));
        lessonDao.delete(new Lesson(2L, 2, Time.valueOf(LocalTime.of(11, 50, 0)), Duration.ofMinutes(90), 3L));
        lessonDao.saveAll(lessons);

        assertEquals(expected, lessonDao.getAll());
    }
}