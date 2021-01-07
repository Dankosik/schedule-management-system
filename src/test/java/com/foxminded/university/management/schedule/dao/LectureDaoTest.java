package com.foxminded.university.management.schedule.dao;

import com.foxminded.university.management.schedule.models.Lecture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import utils.TestUtils;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class LectureDaoTest {
    @Container
    private final PostgreSQLContainer<?> POSTGRESQL_CONTAINER =
            new PostgreSQLContainer<>("postgres:12")
                    .withInitScript("init_test_db.sql");

    private LectureDao lectureDao;
    private TestUtils testUtils;

    @BeforeEach
    void setUp() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(POSTGRESQL_CONTAINER.getJdbcUrl());
        dataSource.setUsername(POSTGRESQL_CONTAINER.getUsername());
        dataSource.setPassword(POSTGRESQL_CONTAINER.getPassword());
        lectureDao = new LectureDao(dataSource);
        testUtils = new TestUtils(dataSource);
    }

    @Test
    void shouldCreateNewLecture() {
        Lecture lecture = new Lecture(111, Date.valueOf(LocalDate.of(2020, 1, 1)), 1000L, 1000L, 1000L, 1000L);
        Long lectureId = lectureDao.save(lecture).getId();
        assertTrue(testUtils.existsById("lectures", lectureId));

        Map<String, Object> map = testUtils.getEntry("lectures", lectureId);
        Lecture actual = new Lecture((Integer) map.get("number"), (Date) map.get("date"), (Long) map.get("audience_id"),
                (Long) map.get("lesson_id"), (Long) map.get("teacher_id"), (Long) map.get("schedule_id"));
        assertEquals(lecture, actual);
    }

    @Test
    void shouldUpdateLecture() {
        Lecture lecture = new Lecture(1000L, 111, Date.valueOf(LocalDate.of(2020, 1, 1)), 1000L, 1000L, 1000L, 1000L);
        Long lectureId = lectureDao.save(lecture).getId();
        assertTrue(testUtils.existsById("lectures", lectureId));

        Map<String, Object> map = testUtils.getEntry("lectures", lectureId);
        Lecture actual = new Lecture((Long) map.get("id"), (Integer) map.get("number"), (Date) map.get("date"),
                (Long) map.get("audience_id"), (Long) map.get("lesson_id"), (Long) map.get("teacher_id"), (Long) map.get("schedule_id"));
        assertEquals(lecture, actual);
    }

    @Test
    void shouldReturnLectureWithIdOne() {
        Lecture expected = new Lecture(1000L, 1, Date.valueOf(LocalDate.of(2021, 1, 1)), 1000L, 1000L, 1000L, 1000L);
        Lecture actual = lectureDao.getById(1000L).get();

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnListOfLecture() {
        List<Lecture> expected = List.of(
                new Lecture(1000L, 1, Date.valueOf(LocalDate.of(2021, 1, 1)), 1000L, 1000L, 1000L, 1000L),
                new Lecture(1001L, 2, Date.valueOf(LocalDate.of(2021, 1, 1)), 1001L, 1001L, 1001L, 1001L),
                new Lecture(1002L, 3, Date.valueOf(LocalDate.of(2021, 1, 1)), 1002L, 1002L, 1000L, 1000L),
                new Lecture(1003L, 4, Date.valueOf(LocalDate.of(2021, 2, 1)), 1003L, 1003L, 1001L, 1001L));
        List<Lecture> actual = lectureDao.getAll();

        assertTrue(actual.containsAll(expected));
    }

    @Test
    void shouldDeleteLecture() {
        assertTrue(lectureDao.deleteById(1000L));
        assertFalse(testUtils.existsById("lectures", 1000L));
    }

    @Test
    void shouldSaveListOfLectures() {
        List<Lecture> lectures = List.of(
                new Lecture(222, Date.valueOf(LocalDate.of(2021, 1, 1)), 1000L, 1000L, 1000L, 1000L),
                new Lecture(223, Date.valueOf(LocalDate.of(2021, 1, 2)), 1001L, 1001L, 1001L, 1001L));

        List<Lecture> expected = List.of(
                new Lecture(1L, 222, Date.valueOf(LocalDate.of(2021, 1, 1)), 1000L, 1000L, 1000L, 1000L),
                new Lecture(2L, 223, Date.valueOf(LocalDate.of(2021, 1, 2)), 1001L, 1001L, 1001L, 1001L),
                new Lecture(1002L, 3, Date.valueOf(LocalDate.of(2021, 1, 1)), 1002L, 1002L, 1000L, 1000L),
                new Lecture(1003L, 4, Date.valueOf(LocalDate.of(2021, 2, 1)), 1003L, 1003L, 1001L, 1001L));
        lectureDao.saveAll(lectures);
        List<Lecture> actual = lectureDao.getAll();

        assertTrue(actual.containsAll(expected));
    }

    @Test
    void shouldReturnListOfLecturesWithAudienceIdOne() {
        List<Lecture> expected = List.of(
                new Lecture(1000L, 1, Date.valueOf(LocalDate.of(2021, 1, 1)), 1000L, 1000L, 1000L, 1000L));
        List<Lecture> actual = lectureDao.getLecturesByAudienceId(1000L);

        assertTrue(actual.containsAll(expected));
    }

    @Test
    void shouldReturnListOfLecturesWithLessonIdOne() {
        List<Lecture> expected = List.of(
                new Lecture(1000L, 1, Date.valueOf(LocalDate.of(2021, 1, 1)), 1000L, 1000L, 1000L, 1000L));
        List<Lecture> actual = lectureDao.getLecturesByLessonId(1000L);

        assertTrue(actual.containsAll(expected));
    }

    @Test
    void shouldReturnListOfLecturesWithTeacherIdOne() {
        List<Lecture> expected = List.of(
                new Lecture(1000L, 1, Date.valueOf(LocalDate.of(2021, 1, 1)), 1000L, 1000L, 1000L, 1000L),
                new Lecture(1002L, 3, Date.valueOf(LocalDate.of(2021, 1, 1)), 1002L, 1002L, 1000L, 1000L));
        List<Lecture> actual = lectureDao.getLecturesByTeacherId(1000L);

        assertTrue(actual.containsAll(expected));
    }

    @Test
    void shouldReturnListOfLecturesWithScheduleIdOne() {
        List<Lecture> expected = List.of(
                new Lecture(1000L, 1, Date.valueOf(LocalDate.of(2021, 1, 1)), 1000L, 1000L, 1000L, 1000L),
                new Lecture(1002L, 3, Date.valueOf(LocalDate.of(2021, 1, 1)), 1002L, 1002L, 1000L, 1000L));
        List<Lecture> actual = lectureDao.getLecturesByScheduleId(1000L);

        assertTrue(actual.containsAll(expected));
    }

    @Test
    void shouldThrowExceptionIfLectureNotExist() {
        assertThrows(NoSuchElementException.class, () -> lectureDao.getById(21L).get());
    }

    @Test
    void shouldReturnFalseIfLectureNotExist() {
        assertFalse(() -> lectureDao.deleteById(21L));
    }
}