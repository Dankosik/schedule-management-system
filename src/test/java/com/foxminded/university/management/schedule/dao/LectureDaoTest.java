package com.foxminded.university.management.schedule.dao;

import com.foxminded.university.management.schedule.models.Lecture;
import com.foxminded.university.management.schedule.models.Lesson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Date;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@SpringBootTest
class LectureDaoTest extends BaseDaoTest {
    private LectureDao lectureDao;
    @MockBean
    private LessonDao lessonDao;

    @BeforeEach
    void setUp() {
        lectureDao = new LectureDao(jdbcTemplate, lessonDao);
    }

    @Test
    void shouldCreateNewLecture() {
        Lecture expected = new Lecture(1, Date.valueOf(LocalDate.of(2020, 1, 1)), 1000L, 1000L, 1000L, 1000L);
        Lesson lesson = new Lesson(1000L, 1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), 1000L);
        when(lessonDao.getById(1000L)).thenReturn(java.util.Optional.of(lesson));

        Long lectureId = lectureDao.save(expected).getId();
        assertTrue(testUtils.existsById("lectures", lectureId));

        Map<String, Object> map = testUtils.getEntry("lectures", lectureId);
        Lecture actual = new Lecture(lesson.getNumber(), (Date) map.get("date"), (Long) map.get("audience_id"),
                (Long) map.get("group_id"), (Long) map.get("lesson_id"), (Long) map.get("teacher_id"));

        assertEquals(expected, actual);

        verify(lessonDao, times(1)).getById(1000L);
    }

    @Test
    void shouldUpdateLecture() {
        Lecture lecture = new Lecture(1000L, 2, Date.valueOf(LocalDate.of(2020, 1, 1)), 1000L, 1000L, 1000L, 1000L);
        Lesson lesson = new Lesson(1000L, 2, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), 1000L);
        when(lessonDao.getById(1000L)).thenReturn(java.util.Optional.of(lesson));
        Long lectureId = lectureDao.save(lecture).getId();
        assertTrue(testUtils.existsById("lectures", lectureId));

        Map<String, Object> map = testUtils.getEntry("lectures", lectureId);
        Lecture actual = new Lecture((Long) map.get("id"), (Integer) map.get("number"), (Date) map.get("date"),
                (Long) map.get("audience_id"), (Long) map.get("group_id"), (Long) map.get("lesson_id"), (Long) map.get("teacher_id"));

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
                new Lecture(1001L, 2, Date.valueOf(LocalDate.of(2021, 1, 1)), 1001L, 1000L, 1001L, 1001L),
                new Lecture(1002L, 3, Date.valueOf(LocalDate.of(2021, 1, 1)), 1002L, 1000L, 1002L, 1000L),
                new Lecture(1003L, 4, Date.valueOf(LocalDate.of(2021, 2, 1)), 1003L, 1000L, 1003L, 1001L));
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
        Lesson lesson = new Lesson(1000L, 1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), 1000L);
        when(lessonDao.getById(1000L)).thenReturn(java.util.Optional.of(lesson));

        List<Lecture> lectures = List.of(
                new Lecture(222, Date.valueOf(LocalDate.of(2021, 1, 1)), 1000L, 1000L, 1000L, 1000L),
                new Lecture(223, Date.valueOf(LocalDate.of(2021, 1, 2)), 1001L, 1000L, 1000L, 1001L));

        List<Lecture> expected = List.of(
                new Lecture(1000L, 1, Date.valueOf(LocalDate.of(2021, 1, 1)), 1000L, 1000L, 1000L, 1000L),
                new Lecture(1001L, 2, Date.valueOf(LocalDate.of(2021, 1, 1)), 1001L, 1000L, 1001L, 1001L),
                new Lecture(1002L, 3, Date.valueOf(LocalDate.of(2021, 1, 1)), 1002L, 1000L, 1002L, 1000L),
                new Lecture(1003L, 4, Date.valueOf(LocalDate.of(2021, 2, 1)), 1003L, 1000L, 1003L, 1001L),
                new Lecture(1L, 1, Date.valueOf(LocalDate.of(2021, 1, 1)), 1000L, 1000L, 1000L, 1000L),
                new Lecture(2L, 1, Date.valueOf(LocalDate.of(2021, 1, 2)), 1001L, 1000L, 1000L, 1001L));
        lectureDao.saveAll(lectures);
        List<Lecture> actual = lectureDao.getAll();

        assertTrue(actual.containsAll(expected));

        verify(lessonDao, times(2)).getById(1000L);
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
                new Lecture(1002L, 3, Date.valueOf(LocalDate.of(2021, 1, 1)), 1002L, 1000L, 1002L, 1000L));
        List<Lecture> actual = lectureDao.getLecturesByTeacherId(1000L);

        assertTrue(actual.containsAll(expected));
    }

    @Test
    void shouldReturnListOfLecturesWithGroupIdOne() {
        List<Lecture> expected = List.of(
                new Lecture(1000L, 1, Date.valueOf(LocalDate.of(2021, 1, 1)), 1000L, 1000L, 1000L, 1000L),
                new Lecture(1002L, 3, Date.valueOf(LocalDate.of(2021, 1, 1)), 1002L, 1000L, 1002L, 1000L));
        List<Lecture> actual = lectureDao.getLecturesByGroupId(1000L);

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
