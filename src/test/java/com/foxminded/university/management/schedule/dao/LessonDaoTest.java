package com.foxminded.university.management.schedule.dao;

import com.foxminded.university.management.schedule.models.Lesson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.postgresql.util.PGInterval;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Time;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LessonDaoTest extends BaseDaoTest {
    private LessonDao lessonDao;

    @BeforeEach
    void setUp() {
        lessonDao = new LessonDao(jdbcTemplate);
    }

    @Test
    void shouldCreateNewLesson() {
        Lesson lesson = new Lesson(1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), 1000L);
        Long lessonId = lessonDao.save(lesson).getId();
        assertTrue(testUtils.existsById("lessons", lessonId));

        Map<String, Object> map = testUtils.getEntry("lessons", lessonId);
        PGInterval pgInterval = (PGInterval) map.get("duration");
        int minutes = pgInterval.getMinutes();
        int hours = pgInterval.getHours();
        Duration duration = Duration.ofMinutes(hours * 60L + minutes);
        Lesson actual = new Lesson((Integer) map.get("number"), (Time) map.get("start_time"), duration, (Long) map.get("subject_id"));
        assertEquals(lesson, actual);
    }

    @Test
    void shouldUpdateLesson() {
        Lesson lesson = new Lesson(1000L, 1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), 1000L);
        Long lessonId = lessonDao.save(lesson).getId();
        assertTrue(testUtils.existsById("lessons", lessonId));

        Map<String, Object> map = testUtils.getEntry("lessons", lessonId);
        PGInterval pgInterval = (PGInterval) map.get("duration");
        int minutes = pgInterval.getMinutes();
        int hours = pgInterval.getHours();
        Duration duration = Duration.ofMinutes(hours * 60L + minutes);
        Lesson actual = new Lesson((Long) map.get("id"), (Integer) map.get("number"), (Time) map.get("start_time"),
                duration, (Long) map.get("subject_id"));
        assertEquals(lesson, actual);
    }

    @Test
    void shouldReturnLessonWithIdOne() {
        Map<String, Object> map = testUtils.getEntry("lessons", 1000L);
        PGInterval pgInterval = (PGInterval) map.get("duration");
        int minutes = pgInterval.getMinutes();
        int hours = pgInterval.getHours();
        Duration duration = Duration.ofMinutes(hours * 60L + minutes);
        Lesson expected = new Lesson((Long) map.get("id"), (Integer) map.get("number"), (Time) map.get("start_time"),
                duration, (Long) map.get("subject_id"));
        Lesson actual = lessonDao.getById(1000L).get();

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnListOfLessons() {
        List<Lesson> expected = List.of(
                new Lesson(1000L, 1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), 1000L),
                new Lesson(1001L, 2, Time.valueOf(LocalTime.of(10, 10, 0)), Duration.ofMinutes(90), 1001L),
                new Lesson(1002L, 3, Time.valueOf(LocalTime.of(11, 50, 0)), Duration.ofMinutes(90), 1002L),
                new Lesson(1003L, 4, Time.valueOf(LocalTime.of(13, 20, 0)), Duration.ofMinutes(90), 1002L));
        List<Lesson> actual = lessonDao.getAll();

        assertTrue(actual.containsAll(expected));
    }

    @Test
    void shouldDeleteLesson() {
        assertTrue(lessonDao.deleteById(1000L));
        assertFalse(testUtils.existsById("lessons", 1000L));
    }

    @Test
    void shouldSaveListOfLessons() {
        List<Lesson> lessons = List.of(
                new Lesson(4, Time.valueOf(LocalTime.of(13, 50, 0)), Duration.ofMinutes(90), 1000L),
                new Lesson(5, Time.valueOf(LocalTime.of(15, 30, 0)), Duration.ofMinutes(90), 1001L));

        List<Lesson> expected = List.of(
                new Lesson(1000L, 1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), 1000L),
                new Lesson(1001L, 2, Time.valueOf(LocalTime.of(10, 10, 0)), Duration.ofMinutes(90), 1001L),
                new Lesson(1002L, 3, Time.valueOf(LocalTime.of(11, 50, 0)), Duration.ofMinutes(90), 1002L),
                new Lesson(1003L, 4, Time.valueOf(LocalTime.of(13, 20, 0)), Duration.ofMinutes(90), 1002L),
                new Lesson(1L, 4, Time.valueOf(LocalTime.of(13, 50, 0)), Duration.ofMinutes(90), 1000L),
                new Lesson(2L, 5, Time.valueOf(LocalTime.of(15, 30, 0)), Duration.ofMinutes(90), 1001L));
        lessonDao.saveAll(lessons);
        List<Lesson> actual = lessonDao.getAll();

        assertTrue(actual.containsAll(expected));
    }

    @Test
    void shouldReturnListOfLessonsWithSubjectIdOne() {
        List<Lesson> expected = List.of(
                new Lesson(1000L, 1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), 1000L));
        List<Lesson> actual = lessonDao.getLessonsBySubjectId(1000L);

        assertTrue(actual.containsAll(expected));
    }

    @Test
    void shouldNotFindLessonNotExist() {
        assertFalse(lessonDao.getById(21L).isPresent());
    }

    @Test
    void shouldReturnFalseIfLessonNotExist() {
        assertFalse(() -> lessonDao.deleteById(21L));
    }
}