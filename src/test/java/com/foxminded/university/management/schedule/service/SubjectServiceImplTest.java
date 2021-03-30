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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {SubjectServiceImpl.class})
class SubjectServiceImplTest {
    private final Subject subject = new Subject(1L, "Math", null);
    private final List<Subject> subjects = List.of(subject,
            new Subject(2L, "Art", null),
            new Subject(3L, "Programming", null));

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
        when(subjectDao.save(new Subject("Math", null))).thenReturn(subject);

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
        when(subjectDao.save(new Subject("Math", null)))
                .thenReturn(subject);
        when(subjectDao.save(new Subject("Art", null)))
                .thenReturn(subjects.get(1));
        when(subjectDao.save(new Subject("Programming", null)))
                .thenReturn(subjects.get(2));

        List<Subject> actual = subjectService.saveAllSubjects(subjects);

        assertEquals(subjects, actual);

        verify(subjectDao, times(1)).save(subjects.get(0));
        verify(subjectDao, times(1)).save(subjects.get(1));
        verify(subjectDao, times(1)).save(subjects.get(2));
    }

    @Test
    void shouldThrowExceptionIfCreatedSubjectWithInputNameIsAlreadyExist() {
        Subject expected = new Subject("Math", null);
        when(subjectDao.save(expected)).thenThrow(DuplicateKeyException.class);

        assertThrows(ServiceException.class, () -> subjectService.saveSubject(expected));

        verify(subjectDao, times(1)).save(expected);
    }

    @Test
    void shouldThrowExceptionIfUpdatedSubjectWithInputNameIsAlreadyExist() {
        Subject expected = new Subject(1L, "Math", null);
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
        List<Lesson> lessons = List.of(
                new Lesson(1L, 1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), subject, null),
                new Lesson(2L, 2, Time.valueOf(LocalTime.of(10, 10, 0)), Duration.ofMinutes(90), subjects.get(1), null));

        List<String> expected = List.of("Math", "Art");

        assertEquals(expected, subjectService.getSubjectNamesForLessons(lessons));
    }

    @Test
    void shouldReturnSubjectNamesForLessonsWithSubjectIdZero() {
        List<Lesson> lessons = List.of(
                new Lesson(1L, 1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), null, null),
                new Lesson(2L, 2, Time.valueOf(LocalTime.of(10, 10, 0)), Duration.ofMinutes(90), subjects.get(1), null));

        List<String> expected = Arrays.asList(null, "Art");

        assertEquals(expected, subjectService.getSubjectNamesForLessons(lessons));
    }

    @Test
    void shouldReturnSubjectsForLessons() {
        List<Lesson> lessons = List.of(
                new Lesson(1L, 1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), subject, null),
                new Lesson(2L, 2, Time.valueOf(LocalTime.of(10, 10, 0)), Duration.ofMinutes(90), subjects.get(1), null));

        List<Subject> expected = List.of(
                new Subject(1L, "Math", null),
                new Subject(2L, "Art", null));

        assertEquals(expected, subjectService.getSubjectsWithPossibleNullForLessons(lessons));
    }

    @Test
    void shouldReturnSubjectsForLessonsWithSubjectIdZero() {
        List<Lesson> lessons = List.of(
                new Lesson(1L, 1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), null, null),
                new Lesson(2L, 2, Time.valueOf(LocalTime.of(10, 10, 0)), Duration.ofMinutes(90), subjects.get(1), null));

        List<Subject> expected = Arrays.asList(null, new Subject(2L, "Art", null));

        assertEquals(expected, subjectService.getSubjectsWithPossibleNullForLessons(lessons));
    }

    @Test
    void shouldReturnSubjectsForLectures() {
        List<Lecture> lectures = List.of(
                new Lecture(1L, 1, Date.valueOf(LocalDate.of(2021, 1, 1)), null, null,
                        new Lesson(1L, 1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), subject, null), null),
                new Lecture(2L, 2, Date.valueOf(LocalDate.of(2021, 1, 1)), null, null,
                        new Lesson(2L, 1, Time.valueOf(LocalTime.of(10, 10, 0)), Duration.ofMinutes(90),subjects.get(1), null), null));

        List<Subject> expected = List.of(
                new Subject(1L, "Math", null),
                new Subject(2L, "Art", null));

        assertEquals(expected, subjectService.getSubjectsForLectures(lectures));
    }

    @Test
    void shouldReturnSubjectsForLecturesWithSubjectIdZero() {
        List<Lecture> lectures = List.of(
                new Lecture(1L, 1, Date.valueOf(LocalDate.of(2021, 1, 1)), null, null,
                        new Lesson(1L, 1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), null, null), null),
                new Lecture(2L, 2, Date.valueOf(LocalDate.of(2021, 1, 1)), null, null,
                        new Lesson(2L, 1, Time.valueOf(LocalTime.of(10, 10, 0)), Duration.ofMinutes(90), subjects.get(1), null), null));

        List<Subject> expected = Arrays.asList(null, new Subject(2L, "Art", null));

        assertEquals(expected, subjectService.getSubjectsForLectures(lectures));
    }
}
