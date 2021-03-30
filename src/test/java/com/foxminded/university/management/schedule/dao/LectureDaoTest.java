package com.foxminded.university.management.schedule.dao;

import com.foxminded.university.management.schedule.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class LectureDaoTest extends BaseDaoTest {
    private LectureDao lectureDao;
    @Autowired
    private EntityManager entityManager;
    private Audience audience;
    private Group group;
    private Lesson lesson;
    private Teacher teacher;

    @BeforeEach
    void setUp() {
        lectureDao = new LectureDao(entityManager);
        audience = entityManager.find(Lecture.class, 1000L).getAudience();
        group = entityManager.find(Lecture.class, 1000L).getGroup();
        lesson = entityManager.find(Lecture.class, 1000L).getLesson();
        teacher = entityManager.find(Lecture.class, 1000L).getTeacher();
    }

    @Test
    void shouldCreateNewLecture() {
        Lecture actual = lectureDao.save(new Lecture(777, Date.valueOf(LocalDate.of(2020, 1, 1)),
                audience, group, lesson, teacher));
        Lecture expected = new Lecture(777, Date.valueOf(LocalDate.of(2020, 1, 1)),
                audience, group, lesson, teacher);

        assertEquals(expected, actual);
    }

    @Test
    void shouldUpdateLecture() {
        Lecture lecture = new Lecture(1000L, 777, Date.valueOf(LocalDate.of(2020, 1, 1)),
                audience, group, lesson, teacher);

        assertNotEquals(lecture, entityManager.find(Lecture.class, lecture.getId()));

        Lecture actual = lectureDao.save(lecture);

        assertEquals(lecture, actual);
    }

    @Test
    void shouldReturnLectureWithIdOne() {
        Lecture expected = new Lecture(1000L, 1, Date.valueOf(LocalDate.of(2021, 1, 1)),
                audience, group, lesson, teacher);
        Lecture actual = lectureDao.getById(1000L).get();

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnListOfLecture() {
        List<Lecture> expected = List.of(
                new Lecture(1000L, 1, Date.valueOf(LocalDate.of(2021, 1, 1)),
                        audience, group, lesson, teacher),
                new Lecture(1001L, 2, Date.valueOf(LocalDate.of(2021, 1, 1)),
                        entityManager.find(Lecture.class, 1001L).getAudience(),
                        entityManager.find(Lecture.class, 1001L).getGroup(),
                        entityManager.find(Lecture.class, 1001L).getLesson(),
                        entityManager.find(Lecture.class, 1001L).getTeacher()),
                new Lecture(1002L, 3, Date.valueOf(LocalDate.of(2021, 1, 1)),
                        entityManager.find(Lecture.class, 1002L).getAudience(),
                        entityManager.find(Lecture.class, 1002L).getGroup(),
                        entityManager.find(Lecture.class, 1002L).getLesson(),
                        entityManager.find(Lecture.class, 1002L).getTeacher()),
                new Lecture(1003L, 4, Date.valueOf(LocalDate.of(2021, 2, 1)),
                        entityManager.find(Lecture.class, 1003L).getAudience(),
                        entityManager.find(Lecture.class, 1003L).getGroup(),
                        entityManager.find(Lecture.class, 1003L).getLesson(),
                        entityManager.find(Lecture.class, 1003L).getTeacher()));
        List<Lecture> actual = lectureDao.getAll();

        assertTrue(actual.containsAll(expected));
    }

    @Test
    void shouldDeleteLecture() {
        assertTrue(lectureDao.deleteById(1000L));
        assertFalse(lectureDao.getById(1000L).isPresent());
    }

    @Test
    void shouldSaveListOfLectures() {
        List<Lecture> lectures = List.of(
                new Lecture(222, Date.valueOf(LocalDate.of(2021, 1, 1)),  audience, group, lesson, teacher),
                new Lecture(223, Date.valueOf(LocalDate.of(2021, 1, 2)),  audience, group, lesson, teacher));

        List<Lecture> expected = List.of(
                new Lecture(1000L, 1, Date.valueOf(LocalDate.of(2021, 1, 1)),  audience, group, lesson, teacher),
                new Lecture(1001L, 2, Date.valueOf(LocalDate.of(2021, 1, 1)),
                        entityManager.find(Lecture.class, 1001L).getAudience(),
                        entityManager.find(Lecture.class, 1001L).getGroup(),
                        entityManager.find(Lecture.class, 1001L).getLesson(),
                        entityManager.find(Lecture.class, 1001L).getTeacher()),
                new Lecture(1002L, 3, Date.valueOf(LocalDate.of(2021, 1, 1)),
                        entityManager.find(Lecture.class, 1002L).getAudience(),
                        entityManager.find(Lecture.class, 1002L).getGroup(),
                        entityManager.find(Lecture.class, 1002L).getLesson(),
                        entityManager.find(Lecture.class, 1002L).getTeacher()),
                new Lecture(1003L, 4, Date.valueOf(LocalDate.of(2021, 2, 1)),
                        entityManager.find(Lecture.class, 1003L).getAudience(),
                        entityManager.find(Lecture.class, 1003L).getGroup(),
                        entityManager.find(Lecture.class, 1003L).getLesson(),
                        entityManager.find(Lecture.class, 1003L).getTeacher()),
                new Lecture(1L, 222, Date.valueOf(LocalDate.of(2021, 1, 1)),  audience, group, lesson, teacher),
                new Lecture(2L, 223, Date.valueOf(LocalDate.of(2021, 1, 2)),  audience, group, lesson, teacher));
        lectureDao.saveAll(lectures);
        List<Lecture> actual = lectureDao.getAll();

        assertTrue(actual.containsAll(expected));
    }

    @Test
    void shouldNotFindLectureNotExist() {
        assertFalse(lectureDao.getById(21L).isPresent());
    }

    @Test
    void shouldReturnFalseIfLectureNotExist() {
        assertFalse(() -> lectureDao.deleteById(21L));
    }
}
