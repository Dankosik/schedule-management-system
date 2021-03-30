package com.foxminded.university.management.schedule.service;

import com.foxminded.university.management.schedule.dao.*;
import com.foxminded.university.management.schedule.exceptions.ServiceException;
import com.foxminded.university.management.schedule.models.Audience;
import com.foxminded.university.management.schedule.models.Lecture;
import com.foxminded.university.management.schedule.models.Lesson;
import com.foxminded.university.management.schedule.models.Teacher;
import com.foxminded.university.management.schedule.service.impl.LectureServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Date;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {LectureServiceImpl.class})
class LectureServiceImplTest {
    private final Lesson lesson = new Lesson(1L, 1, Time.valueOf(LocalTime.of(8, 30, 0)),
            Duration.ofMinutes(90), null, null);
    private final Audience audience = new Audience(1L, 202, 45, null);
    private final Teacher teacher = new Teacher(1L, "John", "Jackson", "Jackson", null, null);
    private final Lecture lecture = new Lecture(1, Date.valueOf(LocalDate.of(2020, 1, 1)),
            audience, null, lesson, teacher);
    private final List<Lecture> lectures = List.of(lecture,
            new Lecture(2L, 2, Date.valueOf(LocalDate.of(2020, 1, 1)),
                    audience, null, lesson, teacher),
            new Lecture(3L, 3, Date.valueOf(LocalDate.of(2020, 1, 1)),
                    audience, null, lesson, teacher));
    @Autowired
    private LectureServiceImpl lectureService;
    @MockBean
    private LectureDao lectureDao;
    @MockBean
    private LessonDao lessonDao;
    @MockBean
    private TeacherDao teacherDao;
    @MockBean
    private AudienceDao audienceDao;
    @MockBean
    private GroupDao groupDao;

    @Test
    void shouldSaveLecture() {
        when(lectureDao.save(lecture)).thenReturn(new Lecture(1L, 1, Date.valueOf(LocalDate.of(2020, 1, 1)),
                audience, null, lesson, teacher));
        when(teacherDao.getById(1L))
                .thenReturn(Optional.of(teacher));
        when(audienceDao.getById(1L))
                .thenReturn(Optional.of(audience));
        when(lessonDao.getById(1L))
                .thenReturn(Optional.of(lesson));
        Lecture actual = lectureService.saveLecture(lecture);

        assertEquals(lecture, actual);

        verify(teacherDao, times(1)).getById(1L);
        verify(audienceDao, times(1)).getById(1L);
        verify(lessonDao, times(1)).getById(1L);
        verify(lectureDao, times(1)).save(lecture);
    }

    @Test
    void shouldReturnLectureWithIdOne() {
        when(lectureDao.getById(1L)).thenReturn(Optional.of(lecture));

        Lecture actual = lectureService.getLectureById(1L);

        assertEquals(lecture, actual);

        verify(lectureDao, times(2)).getById(1L);
    }

    @Test
    void shouldReturnListOfLectures() {
        when(lectureDao.getAll()).thenReturn(lectures);

        assertEquals(lectures, lectureService.getAllLectures());

        verify(lectureDao, times(1)).getAll();
    }

    @Test
    void shouldDeleteLectureWithIdOne() {
        when(lectureDao.getById(1L)).thenReturn(Optional.of(new Lecture(1L, 1, Date.valueOf(LocalDate.of(2020, 1, 1)),
                audience, null, lesson, teacher)));
        when(lectureDao.deleteById(1L)).thenReturn(true);

        lectureService.deleteLectureById(1L);

        verify(lectureDao, times(1)).deleteById(1L);
        verify(lectureDao, times(2)).getById(1L);
    }

    @Test
    void shouldSaveListOfLessons() {
        when(lectureDao.save(lecture)).thenReturn(new Lecture(1L, 1, Date.valueOf(LocalDate.of(2020, 1, 1)),
                audience, null, lesson, teacher));
        when(teacherDao.getById(1L))
                .thenReturn(Optional.of(teacher));
        when(audienceDao.getById(1L))
                .thenReturn(Optional.of(audience));
        when(lessonDao.getById(1L))
                .thenReturn(Optional.of(lesson));

        when(lectureDao.save(new Lecture(1, Date.valueOf(LocalDate.of(2020, 1, 1)),
                audience, null, lesson, teacher))).thenReturn(lecture);
        when(lectureDao.save(new Lecture(2, Date.valueOf(LocalDate.of(2020, 1, 1)),
                audience, null, lesson, teacher))).thenReturn(lectures.get(1));
        when(lectureDao.save(new Lecture(3, Date.valueOf(LocalDate.of(2020, 1, 1)),
                audience, null, lesson, teacher))).thenReturn(lectures.get(2));
        when(teacherDao.getById(1L)).thenReturn(Optional.of(teacher));
        when(audienceDao.getById(1L)).thenReturn(Optional.of(audience));
        when(lessonDao.getById(1L)).thenReturn(Optional.of(lesson));

        List<Lecture> actual = lectureService.saveAllLectures(lectures);

        assertEquals(lectures, actual);

        verify(teacherDao, times(3)).getById(1L);
        verify(audienceDao, times(3)).getById(1L);
        verify(lessonDao, times(3)).getById(1L);
        verify(lectureDao, times(1)).save(lectures.get(0));
        verify(lectureDao, times(1)).save(lectures.get(1));
        verify(lectureDao, times(1)).save(lectures.get(2));
    }

    @Test
    void shouldThrowExceptionIfLectureWithInputIdNotFound() {
        when(lectureDao.getById(1L)).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> lectureService.getLectureById(1L));

        verify(lectureDao, times(1)).getById(1L);
        verify(lectureDao, never()).save(lecture);
    }

    @Test
    void shouldThrowExceptionIfLectureTeacherNotFound() {
        Lecture expected = new Lecture(1L, 1, Date.valueOf(LocalDate.of(2020, 1, 1)),
                audience, null, lesson, teacher);

        when(lectureDao.getById(1L)).thenReturn(Optional.of(expected));
        when(teacherDao.getById(1L)).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> lectureService.saveLecture(expected));

        verify(teacherDao, times(1)).getById(1L);
        verify(lectureDao, never()).save(expected);
    }

    @Test
    void shouldThrowExceptionIfLectureLessonNotFound() {
        Lecture expected = new Lecture(1L, 1, Date.valueOf(LocalDate.of(2020, 1, 1)),
                audience, null, lesson, teacher);

        when(lectureDao.getById(1L)).thenReturn(Optional.of(expected));
        when(teacherDao.getById(1L)).thenReturn(Optional.of(teacher));
        when(audienceDao.getById(1L)).thenReturn(Optional.of(audience));
        when(lessonDao.getById(1L)).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> lectureService.saveLecture(expected));

        verify(teacherDao, times(1)).getById(1L);
        verify(audienceDao, times(1)).getById(1L);
        verify(lessonDao, times(1)).getById(1L);
        verify(lectureDao, never()).save(expected);
    }

    @Test
    void shouldThrowExceptionIfLectureAudienceNotFound() {
        Lecture expected = new Lecture(1L, 1, Date.valueOf(LocalDate.of(2020, 1, 1)),
                audience, null, lesson, teacher);

        when(lectureDao.getById(1L)).thenReturn(Optional.of(expected));
        when(teacherDao.getById(1L)).thenReturn(Optional.of(teacher));
        when(audienceDao.getById(1L)).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> lectureService.saveLecture(expected));

        verify(teacherDao, times(1)).getById(1L);
        verify(audienceDao, times(1)).getById(1L);
        verify(lectureDao, never()).save(expected);
    }
}
