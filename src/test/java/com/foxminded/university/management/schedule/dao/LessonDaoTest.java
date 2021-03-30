package com.foxminded.university.management.schedule.dao;

import com.foxminded.university.management.schedule.models.Lecture;
import com.foxminded.university.management.schedule.models.Lesson;
import com.foxminded.university.management.schedule.models.Subject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class LessonDaoTest extends BaseDaoTest {
    private LessonDao lessonDao;
    @Autowired
    private EntityManager entityManager;
    private Subject subject;
    private List<Lecture> lectures;

    @BeforeEach
    void setUp() {
        lessonDao = new LessonDao(entityManager);
        subject = entityManager.find(Lesson.class, 1000L).getSubject();
        lectures = entityManager.find(Lesson.class, 1000L).getLectures();
    }

    @Test
    void shouldCreateNewLesson() {
        Lesson actual = lessonDao.save(new Lesson(1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), subject, lectures));
        Lesson expected = new Lesson(actual.getId(), 1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), subject, lectures);

        assertEquals(expected, actual);
    }

    @Test
    void shouldUpdateLesson() {
        Lesson lesson = new Lesson(1000L, 23, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), subject, lectures);

        assertNotEquals(lesson, entityManager.find(Lesson.class, lesson.getId()));

        Lesson actual = lessonDao.save(lesson);

        assertEquals(lesson, actual);
    }

    @Test
    void shouldReturnLessonWithIdOne() {
        Lesson actual = lessonDao.getById(1000L).get();
        Lesson expected = new Lesson(1000L, 1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), subject, lectures);

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnListOfLessons() {
        List<Lesson> expected = List.of(
                new Lesson(1000L, 1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), subject, lectures),
                new Lesson(1001L, 2, Time.valueOf(LocalTime.of(10, 10, 0)), Duration.ofMinutes(90),
                        entityManager.find(Lesson.class, 1001L).getSubject(), entityManager.find(Lesson.class, 1001L).getLectures()),
                new Lesson(1002L, 3, Time.valueOf(LocalTime.of(11, 50, 0)), Duration.ofMinutes(90),
                        entityManager.find(Lesson.class, 1002L).getSubject(), entityManager.find(Lesson.class, 1002L).getLectures()),
                new Lesson(1003L, 4, Time.valueOf(LocalTime.of(13, 20, 0)), Duration.ofMinutes(90),
                        entityManager.find(Lesson.class, 1003L).getSubject(), entityManager.find(Lesson.class, 1003L).getLectures()));
        List<Lesson> actual = lessonDao.getAll();

        assertTrue(actual.containsAll(expected));
    }

    @Test
    void shouldDeleteLesson() {
        assertTrue(lessonDao.deleteById(1000L));
        assertFalse(lessonDao.getById(1000L).isPresent());
    }

    @Test
    void shouldSaveListOfLessons() {
        List<Lesson> lessons = List.of(
                new Lesson(4, Time.valueOf(LocalTime.of(13, 50, 0)), Duration.ofMinutes(90), subject, null),
                new Lesson(5, Time.valueOf(LocalTime.of(15, 30, 0)), Duration.ofMinutes(90), subject, null));

        List<Lesson> expected = List.of(
                new Lesson(1000L, 1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), subject, lectures),
                new Lesson(1001L, 2, Time.valueOf(LocalTime.of(10, 10, 0)), Duration.ofMinutes(90),
                        entityManager.find(Lesson.class, 1001L).getSubject(), entityManager.find(Lesson.class, 1001L).getLectures()),
                new Lesson(1002L, 3, Time.valueOf(LocalTime.of(11, 50, 0)), Duration.ofMinutes(90),
                        entityManager.find(Lesson.class, 1002L).getSubject(), entityManager.find(Lesson.class, 1002L).getLectures()),
                new Lesson(1003L, 4, Time.valueOf(LocalTime.of(13, 20, 0)), Duration.ofMinutes(90),
                        entityManager.find(Lesson.class, 1003L).getSubject(), entityManager.find(Lesson.class, 1003L).getLectures()),
                new Lesson(1L, 4, Time.valueOf(LocalTime.of(13, 50, 0)), Duration.ofMinutes(90), subject, null),
                new Lesson(2L, 5, Time.valueOf(LocalTime.of(15, 30, 0)), Duration.ofMinutes(90), subject, null));
        lessonDao.saveAll(lessons);
        List<Lesson> actual = lessonDao.getAll();

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
