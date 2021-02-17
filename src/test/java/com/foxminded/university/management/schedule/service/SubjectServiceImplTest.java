package com.foxminded.university.management.schedule.service;

import com.foxminded.university.management.schedule.dao.LessonDao;
import com.foxminded.university.management.schedule.dao.SubjectDao;
import com.foxminded.university.management.schedule.exceptions.ServiceException;
import com.foxminded.university.management.schedule.models.Lecture;
import com.foxminded.university.management.schedule.models.Lesson;
import com.foxminded.university.management.schedule.models.Subject;
import com.foxminded.university.management.schedule.service.impl.LessonServiceImpl;
import com.foxminded.university.management.schedule.service.impl.SubjectServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Date;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest
class SubjectServiceImplTest {
    private final Subject subject = new Subject(1L, "Math");
    private final List<Subject> subjects = List.of(subject,
            new Subject(2L, "Art"),
            new Subject(3L, "Programming"));

    @Autowired
    private SubjectServiceImpl subjectService;
    @MockBean
    private SubjectDao subjectDao;
    @MockBean
    private LessonDao lessonDao;
    @MockBean
    private LessonServiceImpl lessonService;

    @Test
    void shouldSaveSubject() {
        when(subjectDao.save(new Subject("Math"))).thenReturn(subject);

        Subject actual = subjectService.saveSubject(subject);

        assertEquals(subject, actual);

        verify(subjectDao, times(1)).save(subject);
    }

    @Test
    void shouldReturnSubjectWithIdOne() {
        when(subjectDao.getById(1L)).thenReturn(Optional.of(subject));

        Subject actual = subjectService.getSubjectById(1L);

        assertEquals(subject, actual);

        verify(subjectDao, times(2)).getById(1L);
    }

    @Test
    void shouldReturnListOfSubjects() {
        when(subjectDao.getAll()).thenReturn(subjects);

        assertEquals(subjects, subjectService.getAllSubjects());

        verify(subjectDao, times(1)).getAll();
    }

    @Test
    void shouldDeleteStudentWithIdOne() {
        when(subjectDao.getById(1L)).thenReturn(Optional.of(subject));
        when(subjectDao.deleteById(1L)).thenReturn(true);

        subjectService.deleteSubjectById(1L);

        verify(subjectDao, times(1)).deleteById(1L);
        verify(subjectDao, times(2)).getById(1L);
    }

    @Test
    void shouldSaveListOfAudiences() {
        when(subjectDao.save(new Subject("Math")))
                .thenReturn(subject);
        when(subjectDao.save(new Subject("Art")))
                .thenReturn(subjects.get(1));
        when(subjectDao.save(new Subject("Programming")))
                .thenReturn(subjects.get(2));

        List<Subject> actual = subjectService.saveAllSubjects(subjects);

        assertEquals(subjects, actual);

        verify(subjectDao, times(1)).save(subjects.get(0));
        verify(subjectDao, times(1)).save(subjects.get(1));
        verify(subjectDao, times(1)).save(subjects.get(2));
    }

    @Test
    void shouldThrowExceptionIfCreatedSubjectWithInputNameIsAlreadyExist() {
        Subject expected = new Subject("Math");
        when(subjectDao.save(expected)).thenThrow(DuplicateKeyException.class);

        assertThrows(ServiceException.class, () -> subjectService.saveSubject(expected));

        verify(subjectDao, times(1)).save(expected);
    }

    @Test
    void shouldThrowExceptionIfUpdatedSubjectWithInputNameIsAlreadyExist() {
        Subject expected = new Subject(1L, "Math");
        when(subjectDao.save(expected)).thenThrow(DuplicateKeyException.class);

        assertThrows(ServiceException.class, () -> subjectService.saveSubject(expected));

        verify(subjectDao, times(1)).save(expected);
    }

    @Test
    void shouldThrowExceptionIfSubjectWithInputIdNotFound() {
        when(subjectDao.getById(1L)).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> subjectService.getSubjectById(1L));

        verify(subjectDao, times(1)).getById(1L);
        verify(subjectDao, never()).save(subject);
    }

    @Test
    void shouldReturnSubjectNamesForLessons() {
        when(subjectDao.getById(1L)).thenReturn(Optional.of(new Subject(1L, "Math")));
        when(subjectDao.getById(2L)).thenReturn(Optional.of(new Subject(2L, "Art")));

        List<Lesson> lessons = List.of(
                new Lesson(1L, 1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), 1L),
                new Lesson(2L, 2, Time.valueOf(LocalTime.of(10, 10, 0)), Duration.ofMinutes(90), 2L));

        List<String> expected = List.of("Math", "Art");

        assertEquals(expected, subjectService.getSubjectNamesForLessons(lessons));

        verify(subjectDao, times(2)).getById(1L);
        verify(subjectDao, times(2)).getById(2L);
    }

    @Test
    void shouldReturnSubjectNamesForLessonsWithSubjectIdZero() {
        when(subjectDao.getById(1L)).thenReturn(Optional.of(new Subject(1L, "Math")));
        when(subjectDao.getById(2L)).thenReturn(Optional.of(new Subject(2L, "Art")));

        List<Lesson> lessons = List.of(
                new Lesson(1L, 1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), 0L),
                new Lesson(2L, 2, Time.valueOf(LocalTime.of(10, 10, 0)), Duration.ofMinutes(90), 2L));

        List<String> expected = Arrays.asList(null, "Art");

        assertEquals(expected, subjectService.getSubjectNamesForLessons(lessons));

        verify(subjectDao, times(2)).getById(2L);
    }

    @Test
    void shouldReturnSubjectsForLessons() {
        when(subjectDao.getById(1L)).thenReturn(Optional.of(new Subject(1L, "Math")));
        when(subjectDao.getById(2L)).thenReturn(Optional.of(new Subject(2L, "Art")));
        when(lessonDao.getById(1L))
                .thenReturn(Optional.of(new Lesson(1L, 1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), 1L)));
        when(lessonDao.getById(2L))
                .thenReturn(Optional.of(new Lesson(2L, 2, Time.valueOf(LocalTime.of(10, 10, 0)), Duration.ofMinutes(90), 2L)));
        when(lessonService.getLessonById(1L))
                .thenReturn((new Lesson(1L, 1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), 1L)));
        when(lessonService.getLessonById(2L))
                .thenReturn((new Lesson(2L, 2, Time.valueOf(LocalTime.of(10, 10, 0)), Duration.ofMinutes(90), 2L)));


        List<Lesson> lessons = List.of(
                new Lesson(1L, 1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), 1L),
                new Lesson(2L, 2, Time.valueOf(LocalTime.of(10, 10, 0)), Duration.ofMinutes(90), 2L));

        List<Subject> expected = List.of(
                new Subject(1L, "Math"),
                new Subject(2L, "Art"));

        assertEquals(expected, subjectService.getSubjectsForLessons(lessons));

        verify(subjectDao, times(2)).getById(1L);
        verify(subjectDao, times(2)).getById(2L);
        verify(lessonService, times(2)).getLessonById(1L);
        verify(lessonService, times(2)).getLessonById(2L);
    }

    @Test
    void shouldReturnSubjectsForLessonsWithSubjectIdZero() {
        when(subjectDao.getById(1L)).thenReturn(Optional.of(new Subject(1L, "Math")));
        when(subjectDao.getById(2L)).thenReturn(Optional.of(new Subject(2L, "Art")));
        when(lessonDao.getById(1L))
                .thenReturn(Optional.of(new Lesson(1L, 1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), 0L)));
        when(lessonDao.getById(2L))
                .thenReturn(Optional.of(new Lesson(2L, 2, Time.valueOf(LocalTime.of(10, 10, 0)), Duration.ofMinutes(90), 2L)));
        when(lessonService.getLessonById(1L))
                .thenReturn((new Lesson(1L, 1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), 0L)));
        when(lessonService.getLessonById(2L))
                .thenReturn((new Lesson(2L, 2, Time.valueOf(LocalTime.of(10, 10, 0)), Duration.ofMinutes(90), 2L)));


        List<Lesson> lessons = List.of(
                new Lesson(1L, 1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), 0L),
                new Lesson(2L, 2, Time.valueOf(LocalTime.of(10, 10, 0)), Duration.ofMinutes(90), 2L));

        List<Subject> expected = Arrays.asList(null, new Subject(2L, "Art"));

        assertEquals(expected, subjectService.getSubjectsForLessons(lessons));

        verify(subjectDao, times(2)).getById(2L);
        verify(lessonService, times(1)).getLessonById(1L);
        verify(lessonService, times(2)).getLessonById(2L);
    }

    @Test
    void shouldReturnSubjectsForLectures() {
        when(subjectDao.getById(1L)).thenReturn(Optional.of(new Subject(1L, "Math")));
        when(subjectDao.getById(2L)).thenReturn(Optional.of(new Subject(2L, "Art")));
        when(lessonService.getLessonById(1L))
                .thenReturn(new Lesson(1L, 1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), 1L));
        when(lessonService.getLessonById(2L))
                .thenReturn(new Lesson(2L, 1, Time.valueOf(LocalTime.of(10, 10, 0)), Duration.ofMinutes(90), 2L));

        List<Lecture> lectures = List.of(
                new Lecture(1L, 1, Date.valueOf(LocalDate.of(2021, 1, 1)), 1L, 1L, 1L, 1L),
                new Lecture(2L, 2, Date.valueOf(LocalDate.of(2021, 1, 1)), 2L, 1L, 2L, 1L));

        List<Subject> expected = List.of(
                new Subject(1L, "Math"),
                new Subject(2L, "Art"));

        assertEquals(expected, subjectService.getSubjectsForLectures(lectures));

        verify(subjectDao, times(2)).getById(1L);
        verify(subjectDao, times(2)).getById(2L);
        verify(lessonService, times(2)).getLessonById(1L);
        verify(lessonService, times(2)).getLessonById(2L);
    }

    @Test
    void shouldReturnSubjectsForLecturesWithSubjectIdZero() {
        when(subjectDao.getById(1L)).thenReturn(Optional.of(new Subject(1L, "Math")));
        when(subjectDao.getById(2L)).thenReturn(Optional.of(new Subject(2L, "Art")));
        when(lessonService.getLessonById(1L))
                .thenReturn(new Lesson(1L, 1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), 1L));
        when(lessonService.getLessonById(2L))
                .thenReturn(new Lesson(2L, 1, Time.valueOf(LocalTime.of(10, 10, 0)), Duration.ofMinutes(90), 2L));

        List<Lecture> lectures = List.of(
                new Lecture(1L, 1, Date.valueOf(LocalDate.of(2021, 1, 1)), 1L, 1L, 0L, 1L),
                new Lecture(2L, 2, Date.valueOf(LocalDate.of(2021, 1, 1)), 2L, 1L, 2L, 1L));

        List<Subject> expected = Arrays.asList(null, new Subject(2L, "Art"));

        assertEquals(expected, subjectService.getSubjectsForLectures(lectures));

        verify(lessonService, times(2)).getLessonById(2L);
    }
}
