package com.foxminded.university.management.schedule.dao;

import com.foxminded.university.management.schedule.models.Lecture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class LectureDaoTest {
    @Container
    private final PostgreSQLContainer<?> POSTGRESQL_CONTAINER =
            new PostgreSQLContainer<>("postgres:12")
                    .withInitScript("init_test_db.sql");

    private LectureDao lectureDao;

    @BeforeEach
    void setUp() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(POSTGRESQL_CONTAINER.getJdbcUrl());
        dataSource.setUsername(POSTGRESQL_CONTAINER.getUsername());
        dataSource.setPassword(POSTGRESQL_CONTAINER.getPassword());
        lectureDao = new LectureDao(dataSource);
    }

    @Test
    void shouldCreateNewLecture() {
        Lecture lecture = new Lecture(111, Date.valueOf(LocalDate.of(2020, 1, 1)), 1L, 1L, 1L, 1L);
        lectureDao.delete(lectureDao.getById(1L).get());
        lectureDao.save(lecture);
        Lecture expected = new Lecture(1L, 111, Date.valueOf(LocalDate.of(2020, 1, 1)), 1L, 1L, 1L, 1L);

        assertEquals(expected, lectureDao.getById(1L).get());
    }

    @Test
    void shouldUpdateLecture() {
        Lecture lecture = new Lecture(1L, 210, Date.valueOf(LocalDate.of(2020, 1, 1)), 1L, 1L, 1L, 1L);
        assertNotEquals(lecture, lectureDao.getById(1L).get());
        lectureDao.save(lecture);

        assertEquals(lecture, lectureDao.getById(1L).get());
    }

    @Test
    void shouldReturnLectureWithIdOne() {
        Lecture expected = new Lecture(1L, 1, Date.valueOf(LocalDate.of(2021, 1, 1)), 1L, 1L, 1L, 1L);
        Lecture actual = lectureDao.getById(1L).get();

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnListOfLecture() {
        List<Lecture> expected = List.of(
                new Lecture(1L, 1, Date.valueOf(LocalDate.of(2021, 1, 1)), 1L, 1L, 1L, 1L),
                new Lecture(2L, 2, Date.valueOf(LocalDate.of(2021, 1, 1)), 2L, 2L, 2L, 2L),
                new Lecture(3L, 3, Date.valueOf(LocalDate.of(2021, 1, 1)), 3L, 3L, 1L, 1L),
                new Lecture(4L, 4, Date.valueOf(LocalDate.of(2021, 2, 1)), 4L, 4L, 2L, 2L));
        List<Lecture> actual = lectureDao.getAll();

        assertEquals(expected, actual);
    }

    @Test
    void shouldDeleteLecture() {
        Lecture lecture = new Lecture(1L, 1, Date.valueOf(LocalDate.of(2021, 1, 1)), 1L, 1L, 1L, 1L);
        List<Lecture> expected = List.of(
                new Lecture(2L, 2, Date.valueOf(LocalDate.of(2021, 1, 1)), 2L, 2L, 2L, 2L),
                new Lecture(3L, 3, Date.valueOf(LocalDate.of(2021, 1, 1)), 3L, 3L, 1L, 1L),
                new Lecture(4L, 4, Date.valueOf(LocalDate.of(2021, 2, 1)), 4L, 4L, 2L, 2L));
        assertTrue(lectureDao.delete(lecture));
        List<Lecture> actual = lectureDao.getAll();

        assertEquals(expected, actual);
    }

    @Test
    void shouldSaveListOfLectures() {
        List<Lecture> audiences = List.of(
                new Lecture(222, Date.valueOf(LocalDate.of(2021, 1, 1)), 1L, 1L, 1L, 1L),
                new Lecture(223, Date.valueOf(LocalDate.of(2021, 1, 2)), 2L, 2L, 2L, 2L));

        List<Lecture> expected = List.of(
                new Lecture(3L, 3, Date.valueOf(LocalDate.of(2021, 1, 1)), 3L, 3L, 1L, 1L),
                new Lecture(4L, 4, Date.valueOf(LocalDate.of(2021, 2, 1)), 4L, 4L, 2L, 2L),
                new Lecture(1L, 222, Date.valueOf(LocalDate.of(2021, 1, 1)), 1L, 1L, 1L, 1L),
                new Lecture(2L, 223, Date.valueOf(LocalDate.of(2021, 1, 2)), 2L, 2L, 2L, 2L));
        lectureDao.delete(new Lecture(1L, 1, Date.valueOf(LocalDate.of(2021, 1, 1)), 1L, 1L, 1L, 1L));
        lectureDao.delete(new Lecture(2L, 2, Date.valueOf(LocalDate.of(2021, 1, 1)), 2L, 2L, 2L, 2L));
        lectureDao.saveAll(audiences);

        assertEquals(expected, lectureDao.getAll());
    }

    @Test
    void shouldReturnListOfLecturesWithAudienceIdOne() {
        List<Lecture> expected = List.of(
                new Lecture(1L, 1, Date.valueOf(LocalDate.of(2021, 1, 1)), 1L, 1L, 1L, 1L));
        List<Lecture> actual = lectureDao.getLecturesByAudienceId(1L);

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnListOfLecturesWithLessonIdOne() {
        List<Lecture> expected = List.of(
                new Lecture(1L, 1, Date.valueOf(LocalDate.of(2021, 1, 1)), 1L, 1L, 1L, 1L));
        List<Lecture> actual = lectureDao.getLecturesByLessonId(1L);

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnListOfLecturesWithTeacherIdOne() {
        List<Lecture> expected = List.of(
                new Lecture(1L, 1, Date.valueOf(LocalDate.of(2021, 1, 1)), 1L, 1L, 1L, 1L),
                new Lecture(3L, 3, Date.valueOf(LocalDate.of(2021, 1, 1)), 3L, 3L, 1L, 1L));
        List<Lecture> actual = lectureDao.getLecturesByTeacherId(1L);

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnListOfLecturesWithScheduleIdOne() {
        List<Lecture> expected = List.of(
                new Lecture(1L, 1, Date.valueOf(LocalDate.of(2021, 1, 1)), 1L, 1L, 1L, 1L),
                new Lecture(3L, 3, Date.valueOf(LocalDate.of(2021, 1, 1)), 3L, 3L, 1L, 1L));
        List<Lecture> actual = lectureDao.getLecturesByScheduleId(1L);

        assertEquals(expected, actual);
    }
}